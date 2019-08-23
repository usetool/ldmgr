package com.jikedaquan.tool.ldmgr.query;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QueryMyData {
    private Integer dataId;
    private String dataName;
    private Double dataSize;//MB
    private Integer dataType;
    private Integer dataParent;
    private List<String> dataFileType = new ArrayList<>();
    private Date begin;
    private Date end;
}
