package com.zhaomiao.config;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * 配置类：(哪些路径扫描 哪些路径不扫描)
 * 一般情况下Windows: C盘的：C:\Windows C:\Program Files  C:\Program Files (x86)
 * C:\ProgramData 不用访问
 * Linux下：/tmp /etc /root一般不访问
 * 为防止用户修改 定义为单例模式
 */

public class MiniEverythingPlusConfig{
    @Override
    public String toString() {
        return "MiniEverythingPlusConfig{" +
                "incoudePath=" + incoudePath +
                ", excludePath=" + excludePath +
                ", deptOrderAsc=" + deptOrderAsc +
                ", h2IndexPath='" + h2IndexPath + '\'' +
                '}';
    }

    private static volatile MiniEverythingPlusConfig config;
    //单例模式构造方法私有化
    private MiniEverythingPlusConfig(){
    };
    //由于路径不会重复 所以可以设置为HashSet
    private Set<String> incoudePath = new HashSet<>();
    private Set<String> excludePath = new HashSet<>();

    public Set<String> getIncoudePath() {
        return incoudePath;
    }

    public Set<String> getExcludePath() {
        return excludePath;
    }

    /**
     * 深度排序的规则：默认为升序
     */
    private Boolean deptOrderAsc = true;

    public Boolean getDeptOrderAsc() {
        return deptOrderAsc;
    }

    public void setDeptOrderAsc(Boolean deptOrderAsc) {
        this.deptOrderAsc = deptOrderAsc;
    }

    //H2数据库文件路径   System.getProperty("user.dir")---获取当前的工作路径
    private String h2IndexPath = System.getProperty("user.dir")+
            File.separator+"mini_everything_plus";

    public String getH2IndexPath() {
        return h2IndexPath;
    }

    private static void initDefaultPath(){
        //2.FileSystems.getDefault() @return  the default file system
        FileSystem fileSystem = FileSystems.getDefault();
        //3.fileSystem.getRootDirectories()
        //返回一个对象，用于遍历根目录的路径。
        Iterable<Path> iterable = fileSystem.getRootDirectories();
        for(Iterable i:iterable){
            config.getIncoudePath().add(i.toString());
        }
        //4.不需要扫描的路径
        //4.1获取操作系统的名称
        String os = System.getProperty("os.name");
        if(os.startsWith("Windows")){
            config.getExcludePath().add("C:\\Windows");
            config.getExcludePath().add("C:\\Program Files");
            config.getExcludePath().add("C:\\Program Files (x86)");
            config.getExcludePath().add("C:\\ProgramData");
        }else{
            config.getExcludePath().add("/tmp");
            config.getExcludePath().add("/etc");
            config.getExcludePath().add("/root");
        }
    }

    public static MiniEverythingPlusConfig config() {
        if (config == null) {
            synchronized (MiniEverythingPlusConfig.class) {
                //单例模式 防止创建多个实例化对象double check
                if (config == null) {
                    //1.创建对象
                    config = new MiniEverythingPlusConfig();
                    initDefaultPath();
                }
            }
        }
        return config;
    }
}
