package com.jikedaquan.tool.ldmgr;

import com.jikedaquan.tool.ldmgr.controller.FileManagerController;
import com.jikedaquan.tool.ldmgr.view.MainStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatamanagerApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        System.out.println(FileManagerController.class);//提前静态加载
        launchApp(DatamanagerApplication.class,MainStageView.class,args);
    }
}
