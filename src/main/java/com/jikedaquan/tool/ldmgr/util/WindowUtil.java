package com.jikedaquan.tool.ldmgr.util;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import de.felixroske.jfxsupport.GUIState;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class WindowUtil {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取指定视图的控制器，同时会打开该视图对应的场景，可以通过获取到的控制器实现自己想要的操作
     * @param viewClass 视图类的类型
     * @param controller 控制器的类型
     * @param applicationModal 场景的模态，Modality.APPLICATION_MODAL
     * @param title 场景(窗体)的标题
     * @param <T> 返回一个对应的控制器
     * @return 返回一个对应的控制器
     */
    public <T> T getController(Class<? extends AbstractFxmlView> viewClass, Class<T> controller, Modality applicationModal,String title){

        if (controller.getDeclaredAnnotation(FXMLController.class)==null){
            return null;
        }
        final AbstractFxmlView abstractFxmlView = applicationContext.getBean(viewClass);
        T bean = applicationContext.getBean(controller);

        Stage newStage = new Stage();

        Scene newScene;
        if (abstractFxmlView.getView().getScene() != null) {
            newScene = abstractFxmlView.getView().getScene();
        } else {
            newScene = new Scene(abstractFxmlView.getView());
        }
        newStage.setTitle(title);
        newStage.setScene(newScene);
        newStage.initModality(applicationModal);
        newStage.initOwner(GUIState.getStage());
        newStage.setResizable(false);
        newStage.showAndWait();
        return bean;
    }

    public <T> T getController(ControllerParamProperty<T> property){

        if (property.controller.getDeclaredAnnotation(FXMLController.class)==null){
            return null;
        }
        final AbstractFxmlView abstractFxmlView = applicationContext.getBean(property.viewClass);
        T bean = applicationContext.getBean(property.controller);

        Stage newStage = new Stage();

        Scene newScene;
        if (abstractFxmlView.getView().getScene() != null) {
            newScene = abstractFxmlView.getView().getScene();
        } else {
            newScene = new Scene(abstractFxmlView.getView());
        }
        newStage.setTitle(property.title);
        newStage.setScene(newScene);
        newStage.initModality(property.applicationModal);
        newStage.initOwner(GUIState.getStage());
        newStage.setResizable(false);
        newStage.showAndWait();
        return bean;
    }

    /**
     * 为了更为方便定义获取到的控制器及控制打开的窗体参数，将所需参数封到该类中
     */
    @Data
    @Accessors(chain = true)
    public class ControllerParamProperty<T>{
        private Class<? extends AbstractFxmlView> viewClass;
        private Class<T> controller;
        private Modality applicationModal;
        private String title;
        private boolean resizable;

    }
}
