package com.zhaomiao.core.dao;

import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.Thing;

import java.util.List;

/**
 * 业务层访问数据库的增删改查
 */
public interface FileIndexDao {
    /**
     * 数据库的插入操作
     * @param thing
     */
    void insert(Thing thing);
    /**
     * 数据库的检索操作
     * @param condition
     * @return  返回一个结果集
     */
    List<Thing> search(Condition condition);
    /**
     * 数据库的删除操作
     * @param thing
     */
    void delete (Thing thing);
}
