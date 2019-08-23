package com.jikedaquan.tool.ldmgr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jikedaquan.tool.ldmgr.pojo.MyData;
import com.jikedaquan.tool.ldmgr.query.QueryMyData;

public interface MyDataService {
    void selectMyDataPage(Page<MyData> myDataPage, QueryMyData queryMyData);

    int selectCount(QueryMyData queryMyData);

    int selectCountByFileType(String fileType);
}
