package com.jikedaquan.tool.ldmgr.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jikedaquan.tool.ldmgr.pojo.DataType;
import com.jikedaquan.tool.ldmgr.pojo.MyData;
import com.jikedaquan.tool.ldmgr.query.QueryMyData;
import com.jikedaquan.tool.ldmgr.service.DataTypeService;
import com.jikedaquan.tool.ldmgr.service.MyDataService;
import com.jikedaquan.tool.ldmgr.util.Constants;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.swing.event.ChangeEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class DataRootController implements Initializable {
    private static final int DEFAULT_PAGESIZE = 20;
    @Autowired
    private DataTypeService dataTypeService;
    @Autowired
    private MyDataService myDataService;


    @FXML
    private TextField txtDataName;
    @FXML
    private CheckBox cbIsDir;
    @FXML
    private CheckBox cbIsAudio;
    @FXML
    private CheckBox cbIsVideo;
    @FXML
    private CheckBox cbIsDoc;


    @FXML
    private ComboBox<DataType> comboBoxDataType;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView tableView;
    @FXML
    private CheckBox column_checkBox_cell;

    @FXML
    private TableColumn<MyData,CheckBox> column_checkBox;
    @FXML
    private TableColumn dataId;
    @FXML
    private TableColumn dataName;
    @FXML
    private TableColumn dataSize;
    @FXML
    private TableColumn dataType;
    @FXML
    private TableColumn dataPosition;

    ObservableList<MyData> myDataObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //绑定列对应的数据，列头加复选框
//        dataId.setCellValueFactory(new PropertyValueFactory<>("dataId"));
        //设定单元格中复选框的选中
        column_checkBox.setCellValueFactory(param -> {
            ObservableValue<CheckBox> checkBox = param.getValue().getCheckBox();
            //复选框的选中事件
            checkBox.getValue().selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    //检测是否应该为全选
                    boolean isSelectAll = true;
                    for (MyData myData : myDataObservableList) {
                        if (!myData.getCheckBox().getValue().isSelected()) {
                            isSelectAll = false;
                            break;
                        }
                    }
                    column_checkBox_cell.setSelected(isSelectAll);
                }
            });
            return checkBox;
        });//初始化这一列为复选框(ObservableValue<CheckBox>)

        tableView.setEditable(true);


        //加载第一页数据
        loadPage(1, DEFAULT_PAGESIZE, new QueryMyData());
        loadType();
//        pagination.setPageFactory(new Callback<Integer, Node>() {
//            @Override
//            public Node call(Integer param) {
//                return null;
//            }
//        });

        //文件夹选中其他不可选中
        cbIsDir.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == true) {
                cbIsVideo.setSelected(false);
                cbIsVideo.disableProperty().set(true);
                cbIsAudio.setSelected(false);
                cbIsAudio.disableProperty().set(true);
                cbIsDoc.setSelected(false);
                cbIsDoc.disableProperty().set(true);
            } else {
                cbIsVideo.disableProperty().set(false);
                cbIsAudio.disableProperty().set(false);
                cbIsDoc.disableProperty().set(false);
            }
        });


        //其他类型选中，文件夹不可选
        ChangeListener<Boolean> cbChangeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    cbIsDir.disableProperty().set(true);
                    cbIsDir.setSelected(false);
                } else {

                    checkFileTypeNoSelect();
                }
            }
        };
        cbIsDoc.selectedProperty().addListener(cbChangeListener);
        cbIsVideo.selectedProperty().addListener(cbChangeListener);
        cbIsAudio.selectedProperty().addListener(cbChangeListener);
        System.out.println(tableView);
    }

    /**
     * 检查除了文件夹以外的复选框没有一个选中
     */
    private void checkFileTypeNoSelect() {
        if (cbIsDoc.isSelected() == false && cbIsAudio.isSelected() == false && cbIsVideo.isSelected() == false) {
            cbIsDir.disableProperty().set(false);//文件夹可用
        } else {
            cbIsDir.disableProperty().set(true);
        }
    }

    /**
     * 加载类别数据到下拉框
     */
    private void loadType() {
        List<DataType> dataTypeList = dataTypeService.findAll();
        DataType dataType = new DataType();
        dataType.setTypeName("不限");
        dataTypeList.add(0, dataType);
        ObservableList<DataType> dataTypes = FXCollections.observableList(dataTypeList);
        comboBoxDataType.setItems(dataTypes);
        comboBoxDataType.setConverter(new StringConverter<DataType>() {
            @Override
            public String toString(DataType object) {
                return object.getTypeName();
            }

            @Override
            public DataType fromString(String string) {
                return null;
            }


        });
    }


    /**
     * 加载表格数据
     *
     * @param pageIndex   当前页，从1开始
     * @param pageSize    每页展示数量
     * @param queryMyData 查询条件
     */
    private void loadPage(int pageIndex, int pageSize, QueryMyData queryMyData) {

        Page<MyData> myDataPage = new Page<>(pageIndex, DEFAULT_PAGESIZE);

        myDataService.selectMyDataPage(myDataPage, queryMyData);

        myDataObservableList.addAll(myDataPage.getRecords());
        tableView.setItems(myDataObservableList);

        pagination.setCurrentPageIndex(pageIndex - 1);
        pagination.setMaxPageIndicatorCount((int) myDataPage.getPages());
        pagination.setPageCount(((int) myDataPage.getPages()));
    }

    /**
     * 查询按钮点击事件
     *
     * @param mouseEvent
     */
    public void search(MouseEvent mouseEvent) {
        //获取查询参数
        QueryMyData queryMyData = new QueryMyData();
        if (!StringUtils.isEmpty(txtDataName.getText())) {
            queryMyData.setDataName(txtDataName.getText());
        }
        System.out.println("log:[comboBoxDataType.getValue()=" + comboBoxDataType.getValue() + "]");
        if (comboBoxDataType.getValue() != null) {
            queryMyData.setDataType(comboBoxDataType.getValue().getTypeId());
        }
        if (cbIsDir.isSelected()) {//是文件夹(没有父元素)
            queryMyData.setDataParent(null);
        }
        if (cbIsAudio.isSelected()) {
            queryMyData.getDataFileType().add(Constants.FILE_TYPE_AUDIO);
        }
        if (cbIsVideo.isSelected()) {
            queryMyData.getDataFileType().add(Constants.FILE_TYPE_VIDEO);
        }
        if (cbIsDoc.isSelected()) {
            queryMyData.getDataFileType().add(Constants.FILE_TYPE_DOC);
        }
        System.out.println("log:[queryMyData=" + queryMyData + "]");


        loadPage(1, DEFAULT_PAGESIZE, queryMyData);
    }

    /**
     * 表头的复选框选中时
     *
     * @param mouseEvent
     */
    public void selectAllRow(MouseEvent mouseEvent) {
        if (column_checkBox_cell.isSelected()) {
            //选中当前页所有行
            myDataObservableList.forEach(myData -> {
                myData.getCheckBox().getValue().setSelected(true);
            });
        } else {
            //取消选中当前页所有行
            myDataObservableList.forEach(myData -> {
                myData.getCheckBox().getValue().setSelected(false);
            });
        }
    }


}
