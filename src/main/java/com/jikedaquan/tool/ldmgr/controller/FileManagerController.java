package com.jikedaquan.tool.ldmgr.controller;

import com.jikedaquan.tool.ldmgr.util.ByteUtil;
import com.jikedaquan.tool.ldmgr.vo.DataFile;
import com.jikedaquan.tool.ldmgr.vo.FileInfo;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swt.FXCanvas;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@FXMLController
public class FileManagerController implements Initializable {

    @FXML
    private TableColumn<DataFile, CheckBox> colCheckList;
    @FXML
    private CheckBox cbCheckList;
    @FXML
    private TreeView<FileInfo> tvDisk;//显示磁盘和文件夹
    @FXML
    private ListView lvDisk;//显示磁盘
    @FXML
    private ContextMenu cmFileTable;//展示文件表格的右键菜单
    @FXML
    private ContextMenu cmDisk;//显示磁盘和文件夹的右键菜单
    @FXML
    private TreeItem rootTreeItem;
    @FXML
    private TableView<DataFile> tableDataFile;
    @FXML
    private TableColumn<DataFile, CheckBox> colFileSize;

    //自定义整数比较器，用于比较自定的字节大小文字版
    public static final Comparator BYTETEXT_COMPARATOR = (obj1, obj2) -> {
        if (obj1 == null && obj2 == null) return 0;
        if (obj1 == null) return -1;
        if (obj2 == null) return 1;
        //将字符串转数字
        obj1 = String.format("%d",Integer.valueOf(obj1.toString().replaceAll(" KB","").replace(",","")));
        obj2 = String.format("%d",Integer.valueOf(obj2.toString().replaceAll(" KB","").replace(",","")));
        return Integer.compare(Integer.parseInt(obj1.toString()), Integer.parseInt(obj2.toString()));
    };

    private ObservableList<DataFile> dataFileObservableList = FXCollections.observableArrayList();

    static List<String> localDisks = new ArrayList<>();
    static boolean lock = false;

    //TODO 以直接访问本地接口方式以优化加载速度
    //静态方式加载，以便快速获取本地磁盘
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock = true;
                long startTime = System.currentTimeMillis();
                File[] files = File.listRoots();
                FileSystemView fileSystemView = FileSystemView.getFileSystemView();

                for (int i = 0; i < files.length; i++) {
                    String systemDisplayName = fileSystemView.getSystemDisplayName(files[i]);
                    localDisks.add(systemDisplayName);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("加载驱动耗时：" + (endTime - startTime));
                lock = false;
            }
        }).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        rootTreeItem.setExpanded(true);

//        tvDisk.setRoot(rootTreeItem); //root is treeItem
        TreeCell<FileInfo> treeCell = new TreeCell<>();
        treeCell.updateTreeItem(rootTreeItem);
        tvDisk.setRoot(rootTreeItem);
        tvDisk.setShowRoot(false);
        tvDisk.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tableDataFile.setItems(dataFileObservableList);


        loadDisk();
        colCheckList.setCellValueFactory(param -> {
            ObservableValue<CheckBox> observableValue = param.getValue().getCheckBox();
            //绑定复选框事件
            observableValue.getValue().selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    //检测是否应该为全选
                    boolean isSelectAll = true;
                    for (DataFile dataFile : dataFileObservableList) {
                        if (!dataFile.getCheckBox().getValue().isSelected()) {
                            isSelectAll = false;
                            break;
                        }
                    }
                    cbCheckList.setSelected(isSelectAll);
                }
            });
            return observableValue;
        });
        tvDisk.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode();

            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                //得到选中的节点
                ObservableList<TreeItem<FileInfo>> selectedItems = tvDisk.getSelectionModel().getSelectedItems();
                TreeItem<FileInfo> fileInfoTreeItem = selectedItems.get(0);
                if (fileInfoTreeItem.isExpanded() == false && fileInfoTreeItem.getChildren().isEmpty()) {//没有展开状态并且没有子节点，也就是说再点一次就是展开
                    //加载该节点对应子级目录
                    loadSubDirAndFile(fileInfoTreeItem);
                }
            }
        });
    }

    /**
     * 加载该节点对应的子级目录和文件，只加载目录到子节点，没有子目录不加载，将文件加载到表格中
     *
     * @param fileInfoTreeItem
     */
    private void loadSubDirAndFile(TreeItem<FileInfo> fileInfoTreeItem) {
        File currentFile = fileInfoTreeItem.getValue().getFile();

        //清空
        fileInfoTreeItem.getChildren().clear();
        dataFileObservableList.clear();

        if (currentFile.listFiles() == null) {
            return;
        }

        for (File subFile :
                currentFile.listFiles()) {
            if (subFile.isDirectory() && (!subFile.isHidden())) {

                FileInfo fileInfo = new FileInfo(subFile.getName(), subFile);
                TreeItem<FileInfo> treeItem = new TreeItem<>(fileInfo);
                fileInfoTreeItem.getChildren().add(treeItem);
            } else if (subFile.isFile() && (!subFile.isHidden())) {
                DataFile dataFile = new DataFile();
                dataFile.setFileName(subFile.getName());
                //向上取整，模仿微软资源管理器  wtm 按纯字符串排序，还得改成按值排序
                dataFile.setFileSize(String.format("%,d",(int)Math.ceil(Math.ceil(subFile.length()/ (double)ByteUtil.SIZE_KB))) + " KB");
                colFileSize.setComparator(BYTETEXT_COMPARATOR);
                dataFile.setFileType("随便");
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/d HH:mm");
                    Date date = new Date(subFile.lastModified());
                    dataFile.setUpdateDateTime(simpleDateFormat.format(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dataFileObservableList.add(dataFile);
            }
        }
        fileInfoTreeItem.setExpanded(true);

    }

    /**
     * 加载磁盘
     */
    private void loadDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (lock) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tvDisk.getRoot().getChildren().clear();

                File[] files = File.listRoots();
                for (int i = 0; i < localDisks.size(); i++) {
                    String showName = localDisks.get(i);
                    File file = files[i];
                    FileInfo fileInfo = new FileInfo(showName, file);
                    TreeItem<FileInfo> treeItem = new TreeItem<>(fileInfo);

                    tvDisk.getRoot().getChildren().add(treeItem);
                }
            }
        }).start();
    }

    /**
     * 表格表头复选框点击事件
     *
     * @param mouseEvent
     */
    public void cbCheckListClick(MouseEvent mouseEvent) {
        if (cbCheckList.isSelected()) {
            //选中当前页所有行
            dataFileObservableList.forEach(dataFile -> {
                dataFile.getCheckBox().getValue().setSelected(true);
            });
        } else {
            //取消选中当前页所有行
            dataFileObservableList.forEach(dataFile -> {
                dataFile.getCheckBox().getValue().setSelected(false);
            });
        }
    }
}
