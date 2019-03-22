package com.zhaomiao.core.model;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件类型的归类 由于文件类型不同,可以将文件类型枚举
 */
public enum FileType {
    IMG("jpg","png","gif","bmp","pic","tif"),           //图像文件
    DOC("txt","doc","html","ppt","docx","pdf","wps"),   //文档文件
    BIN("exe","com","sh","jar","msi"),                  //二进制文件
    ARCHIVE("rar","zip","gz","z","arj"),                //压缩文件
    LAN("c","java","obj","bas","asm","lib"),            //语言文件
    OTHER;                                              //其他文件

    //每个类型有多个扩展名、将类型作为String对象添加到集合中
    //为什么选用HashSet? 存的是单个元素且元素不重复
    private Set<String> set = new HashSet<>();
    /**
     * 可变参数
     * @param set
     */
    FileType(String... set) {
        //Arrays.asList()该方法是将数组转化为list
        this.set.addAll(Arrays.asList(set));
    }

    /**
     * 根据文件的扩展名获取文件的类型
     * @param str 传入的扩展名
     * @return 返回该文件的类型
     */
    public static FileType lookup(String str){
        //1.如果传入的文件名为空 返回null
        if(str == null){
            return null;
        }else{
            //2.如果传入的文件名不为空 遍历集合set
            //枚举类的values方法理论上是将枚举类转变为一个枚举类型的数组
            for(FileType fileType:FileType.values()){
                if(fileType.set.contains(str)){
                    return fileType;
                }
            }
        }
        //3.遍历若找不到 返回Other
        return OTHER;
    }

    /**
     * 根据名称获取类型
     * @param name 传入的文件名
     * @return 返回该文件的类型
     */
    public static FileType lookupByName(String name){
        //1.如果传入的文件名为空 返回null
        if(name == null){
            return null;
        }else{
            //2.如果传入的文件名不为空 遍历集合set
            for(FileType fileType:FileType.values()){
                if(fileType.name().equals(name)){
                    return fileType;
                }
            }
        }
        //3.遍历若找不到 返回Other
        return OTHER;
    }
}