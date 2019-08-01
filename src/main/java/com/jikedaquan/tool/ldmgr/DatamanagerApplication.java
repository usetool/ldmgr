package com.jikedaquan.tool.ldmgr;

import com.jikedaquan.tool.ldmgr.view.MainStageView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DatamanagerApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launchApp(DatamanagerApplication.class,MainStageView.class,args);
    }
}
