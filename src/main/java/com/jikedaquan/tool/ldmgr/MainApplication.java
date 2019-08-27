package com.jikedaquan.tool.ldmgr;

import com.jikedaquan.tool.ldmgr.controller.FileManagerController;
import com.jikedaquan.tool.ldmgr.view.MainStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        System.out.println(FileManagerController.class);//提前静态加载
        launchApp(MainApplication.class,MainStageView.class,args);
    }
}
