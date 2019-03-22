package com.zhaomiao.core.index.impl;

import com.zhaomiao.config.MiniEverythingPlusConfig;
import com.zhaomiao.core.index.FileScan;
import com.zhaomiao.core.interceptor.FileInterceptor;

import java.io.File;
import java.util.LinkedList;

public class FileScanImpl implements FileScan {

    private LinkedList<FileInterceptor> interceptors = new LinkedList<>();
    //config为配置类对象 用于设置哪些路径输出哪些路径不输出
    private MiniEverythingPlusConfig config = MiniEverythingPlusConfig.config();


    /**
     * 扫描路径---Thing---输出
     * @param path
     */
    @Override
    public void index(String path){
        File file = new File(path);
        if(file.isFile()){
            if(config.getExcludePath().contains(file.getParent())){
                return;
            }
        }else{
           if(!config.getExcludePath().contains(path)){
               //列举该目录下的文件和目录
               File[] files = file.listFiles();
               if(files!=null){
                  for(File i:files){
                      index(i.getAbsolutePath());
                  }
               }
           }else{
               return;
           }
        }
        for(FileInterceptor interceptor:this.interceptors){
            interceptor.apply(file);
        }
    }
    @Override
    public void interceptor(FileInterceptor interceptor) {

        this.interceptors.add(interceptor);
    }
}
