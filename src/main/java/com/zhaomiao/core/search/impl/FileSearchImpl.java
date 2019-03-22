package com.zhaomiao.core.search.impl;

import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.Thing;
import com.zhaomiao.core.search.FileSearch;

import java.util.List;

/**
 * 文件的检索与数据库有关 所有定义一个数据源属性
 */
public class FileSearchImpl implements FileSearch {
    private final FileIndexDao fileIndexDao;
    public FileSearchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public List<Thing> search(Condition condition) {
       if(condition != null){
           return this.fileIndexDao.search(condition);
       }else{
           return null;
       }
    }
}
