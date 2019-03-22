package com.zhaomiao.core.monitor.impl;

import com.zhaomiao.config.MiniEverythingPlusConfig;
import com.zhaomiao.core.common.FileConverThing;
import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.monitor.FileWatch;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;

public class FileWatchImpl implements FileAlterationListener ,FileWatch{
    //监听器
    private FileAlterationMonitor monitor;
    private FileIndexDao fileIndexDao ;

    public FileWatchImpl(FileIndexDao fileIndexDao) {
        this.fileIndexDao = fileIndexDao;
        this.monitor = new FileAlterationMonitor(100);
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {
        fileAlterationObserver.addListener(this);
    }

    @Override
    public void onDirectoryCreate(File file) {

    }
    @Override
    public void onDirectoryChange(File file) {
    }
    @Override
    public void onDirectoryDelete(File file) {

    }

    //文件创建
    @Override
    public void onFileCreate(File file) {
        System.out.println("FileChange"+file);
        this.fileIndexDao.insert(FileConverThing.conver(file));
    }

    @Override
    public void onFileChange(File file) {
        System.out.println("FileChange "+file);
    }

    @Override
    public void onFileDelete(File file) {
        //文件删除
        System.out.println("FileDelete "+file);
        this.fileIndexDao.delete(FileConverThing.conver(file));
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {
       fileAlterationObserver.removeListener(this);
    }


    @Override
    public void monitor() {
        //监控的是includePath集合
        MiniEverythingPlusConfig config = MiniEverythingPlusConfig.config();
        for(String path:config.getIncoudePath()){
            this.monitor.addObserver(new FileAlterationObserver(path
                    , new FileFilter() {
                //如果不传第二个参数 会监控所有的目录
                // 若传第二个参数 可以选择不要监控的目录 false不监控 true 监控
                @Override
                public boolean accept(File pathname) {
                    String path = pathname.getName();
                    for(String excludePath:config.getExcludePath()){
                        if(path.startsWith(excludePath)){
                            return false;
                        }
                    }
                    return true;
                }
            }));
        }
    }

    @Override
    public void start() {
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
