package com.zhaomiao.core.model;

import lombok.Data;

/**
 * mini_everything_plus文件的检索条件(文件名称+所属类型的方式查找)
 */
@Data
public class Condition {
    //文件名称
    private String name;
    //文件所属类型
    private String fileType;
    //按照文件所属类型升序或者降序  true 升序 false 降序
    private boolean ordByAsc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean getOrdByAsc() {
        return ordByAsc;
    }

    public void setOrdByAsc(boolean ordByAsc) {
        this.ordByAsc = ordByAsc;
    }
}
