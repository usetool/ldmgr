package com.jikedaquan.tool.ldmgr.vo;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 供资料文件管理-查询数据的表格使用
 */
@Data
public class DataFile {
    private String fileName;//文件名
    private String updateDateTime;//修改日期(精确到分)
    private String fileType;//文件类型
    private String fileSize;//文件大小
    //表格中前面的复选框

    private CheckBox checkBox = new CheckBox();

    //不让用
    private void setCheckBox(CheckBox checkBox){

    }
    public ObservableValue<CheckBox> getCheckBox(){
        ObservableValue<CheckBox> observableValue = new ObservableValue<CheckBox>() {
            @Override
            public void addListener(ChangeListener<? super CheckBox> listener) {

            }

            @Override
            public void removeListener(ChangeListener<? super CheckBox> listener) {

            }

            @Override
            public CheckBox getValue() {
                return checkBox;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
        return observableValue;
    }
}
