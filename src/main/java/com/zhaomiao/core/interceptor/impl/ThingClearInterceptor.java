package com.zhaomiao.core.interceptor.impl;

import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.interceptor.ThingInterceptor;
import com.zhaomiao.core.model.Thing;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ThingClearInterceptor implements ThingInterceptor ,Runnable{
    private Queue<Thing> queue = new ArrayBlockingQueue<>(1024);

    private final FileIndexDao fileIndexDao;
    public ThingClearInterceptor(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
    }

    @Override
    public void apply(Thing thing) {
        this.queue.add(thing);
    }
    @Override
    public void run() {
        while(true){
            //移除并返回队列头部的元素
            Thing thing = this.queue.poll();
            fileIndexDao.delete(thing);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
