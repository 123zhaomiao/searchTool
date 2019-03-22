package com.zhaomiao.core.model;

import lombok.Data;

/**
 * 一个文件的属性有很多种：名称、路径、最后一次修改时间、类型、大小、建立时间....等等
 * 由于文件属性众多我们不可能全部存入数据库中，所以我们只存一部分属性
 *  Thing类表示文件属性索引之后的信息
 */
@Data
public class Thing {
    //文件名称
    private String name;
    //文件路径
    private String path;
    //文件深度
    private Integer depth;
    //文件类型、枚举类型
    private FileType fileType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
}
