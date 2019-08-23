package com.jikedaquan.tool.ldmgr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import lombok.Data;

import java.util.Date;

@Data
public class MyData {
    @TableId(value = "data_id",type = IdType.AUTO)
    private Integer dataId;
    private String dataName;
    private Double dataSize;//MB
    private String dataPosition;
    private Integer dataType;
    private Integer dataParent;
    private String dataFileType;
    private Date dataCreateTime;

    @TableField(exist = false)
    private CheckBox checkBox = new CheckBox();//绑定的复选框

    public ObservableValue<CheckBox> getCheckBox() {
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
