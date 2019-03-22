package com.zhaomiao.core.search;

import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.Thing;

import java.util.List;

/**
 * 面向接口编程
 */
public interface FileSearch {
    /**
     * 根据条件进行数据库的检索
     * @param condition 条件
     * @return Thing
     */
    List<Thing> search(Condition condition);
}
