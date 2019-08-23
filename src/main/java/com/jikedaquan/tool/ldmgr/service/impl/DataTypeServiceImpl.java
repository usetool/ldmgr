package com.jikedaquan.tool.ldmgr.service.impl;

import com.jikedaquan.tool.ldmgr.mapper.DataTypeMapper;
import com.jikedaquan.tool.ldmgr.pojo.DataType;
import com.jikedaquan.tool.ldmgr.service.DataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataTypeServiceImpl implements DataTypeService {
    @Autowired
    private DataTypeMapper dataTypeMapper;
    @Override
    public List<DataType> findAll() {
        return dataTypeMapper.selectList(null);
    }
}
