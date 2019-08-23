package com.jikedaquan.tool.ldmgr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class DataType {
    @TableId(value = "type_id",type = IdType.AUTO)
    private Integer typeId;
    private String typeName;
}
