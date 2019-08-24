package com.jikedaquan.tool.ldmgr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * 供展示磁盘目录的树状菜单使用
 */
@Data
public class FileInfo {
    private String showName;
    private String path;
    private File file;
    public FileInfo(String showName,File file){
        if (StringUtils.isEmpty(showName)||file==null){
            throw new RuntimeException("showName is Empty or file is null,dont do this!");
        }
        this.showName = showName;
        this.file = file;
    }
    public String getPath(){
        return file.getAbsolutePath();
    }

    @Override
    public String toString(){
        return showName;
    }
}
