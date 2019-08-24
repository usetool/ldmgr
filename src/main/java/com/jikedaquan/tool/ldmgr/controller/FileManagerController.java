package com.jikedaquan.tool.ldmgr.controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

    private ObservableList<DataFile> dataFileObservableList = FXCollections.observableArrayList();

    static List<String> localDisks = new ArrayList<>();
    static boolean lock = false;

    //TODO 以直接访问本地接口方式以优化加载速度
    //静态方式加载，以便快速获取本地磁盘
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock=true;
                long startTime = System.currentTimeMillis();
                File[] files = File.listRoots();
                FileSystemView fileSystemView = FileSystemView.getFileSystemView();

                for (int i = 0; i < files.length; i++) {
                    String systemDisplayName = fileSystemView.getSystemDisplayName(files[i]);
                    localDisks.add(systemDisplayName);
                }
                long endTime = System.currentTimeMillis();
                System.out.println("加载驱动耗时：" + (endTime - startTime));
                lock=false;
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

        tvDisk.setOnMouseClicked(event -> {
            Node node = event.getPickResult().getIntersectedNode();

            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)){
                //得到选中的节点
                ObservableList<TreeItem<FileInfo>> selectedItems = tvDisk.getSelectionModel().getSelectedItems();
                TreeItem<FileInfo> fileInfoTreeItem = selectedItems.get(0);
                if (fileInfoTreeItem.isExpanded()==false&&fileInfoTreeItem.getChildren().isEmpty()) {//没有展开状态并且没有子节点，也就是说再点一次就是展开
                    //加载该节点对应子级目录
                    loadSubDirAndFile(fileInfoTreeItem);
                }
            }
        });

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
    }

    /**
     * 加载该节点对应的子级目录和文件，只加载目录到子节点，没有子目录不加载，将文件加载到表格中
     * @param fileInfoTreeItem
     */
    private void loadSubDirAndFile(TreeItem<FileInfo> fileInfoTreeItem) {
        File file = fileInfoTreeItem.getValue().getFile();

        fileInfoTreeItem.getChildren().clear();
        for (File subFile :
                file.listFiles()) {
            if (subFile.isDirectory()){
                FileInfo fileInfo = new FileInfo(subFile.getName(),subFile);
                TreeItem<FileInfo> treeItem = new TreeItem<>(fileInfo);
                fileInfoTreeItem.getChildren().add(treeItem);
            }else if (file.isFile()){
                //TODO 加载文件到表格中
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
                while (lock){
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
                    FileInfo fileInfo = new FileInfo(showName,file);
                    TreeItem<FileInfo> treeItem = new TreeItem<>(fileInfo);

                    tvDisk.getRoot().getChildren().add(treeItem);
                }
            }
        }).start();
    }

    /**
     * 表格表头复选框点击事件
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
