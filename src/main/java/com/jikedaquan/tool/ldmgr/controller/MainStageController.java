package com.jikedaquan.tool.ldmgr.controller;

import com.jikedaquan.tool.ldmgr.view.DataRootView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MainStageController implements Initializable {
    @Autowired
    private DataRootView dataRootView;
    @FXML
    private VBox leftnav;
    @FXML
    private Pane content;

    private List<Hyperlink> hyperlinkList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        openData(new Event(hyperlinkList.get(0), null, EventType.ROOT));
        initData();
    }

    private void initData() {
    }

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

    public void openFilemgr(Event event) {
        Hyperlink source = (Hyperlink) event.getSource();
        changeHyperLinkActive(source);
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
