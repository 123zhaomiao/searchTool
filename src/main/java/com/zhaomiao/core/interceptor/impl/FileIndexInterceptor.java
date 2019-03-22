package com.zhaomiao.core.interceptor.impl;

import com.zhaomiao.core.common.FileConverThing;
import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.interceptor.FileInterceptor;
import com.zhaomiao.core.model.Thing;

import java.io.File;

/**
 * FileInterceptor有两个子类
 * FileIndexInterceptor用于将对象转为Thing输出
 */
public class FileIndexInterceptor implements FileInterceptor {
    //1.增删改查均在此接口中实现
    private final FileIndexDao fileIndexDao;
    public FileIndexInterceptor(FileIndexDao fileIndexDao) {

        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(File file) {
        //1.将文件类型转为Thing对象
        Thing thing = FileConverThing.conver(file);
        fileIndexDao.insert(thing);
    }
}
