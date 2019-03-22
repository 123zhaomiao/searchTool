package com.zhaomiao.core.monitor;

import com.zhaomiao.config.MiniEverythingPlusConfig;

public interface FileWatch {
    /**
     * 监听启动
     */
    void start();
    /**
     * 监听目录
     */
    void monitor();
    /**
     * 监听停止
     */
    void stop();
}
