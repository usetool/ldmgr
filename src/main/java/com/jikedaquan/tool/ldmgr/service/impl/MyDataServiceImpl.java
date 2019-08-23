package com.jikedaquan.tool.ldmgr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jikedaquan.tool.ldmgr.mapper.MyDataMapper;
import com.jikedaquan.tool.ldmgr.pojo.MyData;
import com.jikedaquan.tool.ldmgr.query.QueryMyData;
import com.jikedaquan.tool.ldmgr.service.MyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MyDataServiceImpl implements MyDataService {
    @Autowired
    private MyDataMapper myDataMapper;

    @Override
    public void selectMyDataPage(Page<MyData> myDataPage, QueryMyData queryMyData) {
        //获取查询参数
        QueryWrapper<MyData> queryWrapper = getMyDataQueryWrapper(queryMyData);


        queryWrapper.orderByDesc("data_create_time");
        myDataMapper.selectPage(myDataPage, queryWrapper);
    }

    /**
     * 将自定义的查询参数转为 mapper所需的查询参数
     * @param queryMyData
     * @return
     */
    private QueryWrapper<MyData> getMyDataQueryWrapper(QueryMyData queryMyData) {
        Integer dataId = queryMyData.getDataId();
        String dataName = queryMyData.getDataName();
        Integer dataParent = queryMyData.getDataParent();
        Double dataSize = queryMyData.getDataSize();
        Integer dataType = queryMyData.getDataType();
        Date begin = queryMyData.getBegin();
        Date end = queryMyData.getEnd();
        List<String> dataFileTypes = queryMyData.getDataFileType();

        QueryWrapper<MyData> queryWrapper = new QueryWrapper<>();
        if (dataId != null) {
            queryWrapper.eq("data_id", dataId);
        }
        if (!StringUtils.isEmpty(dataName)) {
            queryWrapper.like("data_name", dataName);
        }
        if (dataParent != null) {
            queryWrapper.eq("data_parent", dataParent);
        }
        if (dataType != null) {
            queryWrapper.eq("data_type", dataType);
        }

        //增加文件类型的or条件并放入()中  [and( data_file_type=a or data_file_type=b ……)]
        //这个过程是动态的集合多长就有多少
        if (dataFileTypes!=null&&dataFileTypes.size()>0){
            queryWrapper.and(myDataQueryOrWrapper -> {
                myDataQueryOrWrapper.eq("data_file_type",dataFileTypes.get(0));
                for (int i = 1; i < dataFileTypes.size(); i++) {
                    String dataFileType = dataFileTypes.get(i);
                    if (!StringUtils.isEmpty(dataFileType)){
                        myDataQueryOrWrapper.or().eq("data_file_type",dataFileType);
                    }
                }
                return myDataQueryOrWrapper;
            });
        }
        return queryWrapper;
    }

    @Override
    public int selectCount(QueryMyData queryMyData) {
        Integer count = myDataMapper.selectCount(getMyDataQueryWrapper(queryMyData));
        return count;
    }

    @Override
    public int selectCountByFileType(String fileType) {
        QueryMyData queryMyData = new QueryMyData();
        queryMyData.setDataFileType(Arrays.asList(fileType));
        return selectCount(queryMyData);
    }
}
