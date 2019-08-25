package com.jikedaquan.tool.ldmgr.controller;

import com.jikedaquan.tool.ldmgr.query.QueryMyData;
import com.jikedaquan.tool.ldmgr.service.MyDataService;
import com.jikedaquan.tool.ldmgr.util.Constants;
import com.jikedaquan.tool.ldmgr.view.DataRootView;
import com.jikedaquan.tool.ldmgr.view.FileManagerView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@FXMLController
public class MainStageController implements Initializable {
    @Autowired
    private DataRootView dataRootView;
    @Autowired
    private FileManagerView fileManagerView;
    @Autowired
    private MyDataService myDataService;

    @FXML
    private VBox leftnav;
    @FXML
    private Pane content;
    @FXML
    private Label lblDataTotalCount;
    @FXML
    private Label lblDocTotalCount;
    @FXML
    private Label lblVideoTotalCount;
    @FXML
    private Label lblAudioTotalCount;

    private List<Hyperlink> hyperlinkList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();

        loadPreviewCount();

        hyperlinkList = new ArrayList<>();
        leftnav.getChildren().forEach(node -> {
            if (node instanceof VBox) {
                ((VBox) node).getChildren().forEach(node1 -> {
                    if (node1 instanceof Hyperlink) {
                        hyperlinkList.add((Hyperlink) node1);
                    }
                });
            }
        });
        hyperlinkList.get(0).setVisited(true);
        //点击第一个菜单
        openData(new Event(hyperlinkList.get(0), null, EventType.ROOT));
    }

    /**
     * 加载预览的各种类型文件数量
     */
    private void loadPreviewCount() {
        int dataTotalCount =  myDataService.selectCount(new QueryMyData());
        int docTotalCount =  myDataService.selectCountByFileType(Constants.FILE_TYPE_DOC);
        int videoTotalCount =  myDataService.selectCountByFileType(Constants.FILE_TYPE_VIDEO);
        int audioTotalCount =  myDataService.selectCountByFileType(Constants.FILE_TYPE_AUDIO);
        lblDataTotalCount.setText(String.valueOf(dataTotalCount));
        lblDocTotalCount.setText(String.valueOf(docTotalCount));
        lblAudioTotalCount.setText(String.valueOf(audioTotalCount));
        lblVideoTotalCount.setText(String.valueOf(videoTotalCount));
    }

    private void initData() {

    }

    /**
     * 打开首页资料数据
     * @param event
     */
    public void openData(Event event) {
        Hyperlink source = (Hyperlink) event.getSource();
        changeHyperLinkActive(source);
        content.getChildren().clear();

        Parent view = dataRootView.getView();
        content.getChildren().add(view);
    }

    public void openTree(Event event) {
        Hyperlink source = (Hyperlink) event.getSource();
        changeHyperLinkActive(source);
    }

    /**
     * 打开文件管理器
     * @param event
     */
    public void openFilemgr(Event event) {
        Hyperlink source = (Hyperlink) event.getSource();
        changeHyperLinkActive(source);

        content.getChildren().clear();

        Parent view = fileManagerView.getView();
        content.getChildren().add(view);

    }

    /**
     * 移除除了当前以外控件的效果
     *
     * @param hyperlink
     */
    private void changeHyperLinkActive(Hyperlink hyperlink) {
        for (Hyperlink h :
                hyperlinkList) {
            if (h != hyperlink) {
                h.setVisited(false);
            }
        }
    }
}
