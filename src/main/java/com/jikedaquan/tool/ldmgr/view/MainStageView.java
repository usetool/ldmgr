package com.jikedaquan.tool.ldmgr.view;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@FXMLView(value = "/view/MainStage.fxml")
public class MainStageView extends AbstractFxmlView {

//    public MainStageView(){
//        System.out.println("fda");
//        if (getClass().getResourceAsStream("/org/kordamp/bootstrapfx/bootstrapfx.css") == null) {
//            System.out.println("文件没有找到");
//        }else {
//            System.out.println("文件查到");
//        }
//    }

//    @Override
//    public Parent getView() {
////        return super.getView();
//        System.out.println("getview");
//        Pane pane = new Pane();
//        Button button = new Button("A Button");
//
//        pane.getChildren().add(button);
//        return pane;
//    }
}
