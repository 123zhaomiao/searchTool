package com.zhaomiao.core.common;

import com.zhaomiao.core.model.FileType;
import com.zhaomiao.core.model.Thing;

import java.io.File;

/**
 * 此类用于将文件对象File---转为Thing对象
 */
public final class FileConverThing {
    //构造方法私有化 使得在类外不可以创建对象
    private FileConverThing(){}

    /**
     * 将文件类对象File转为Thing类对象
     * @param file 文件类对象
     * @return Thing类对象
     */
    public static Thing conver(File file){
        //1.创建一个Thing类对象
        Thing thing = new Thing();
        //2.调用Thing类的set方法为Thing类中的属性赋值
        thing.setName(file.getName());
        //3.file.getAbsolutePath() 获取文件的绝对路径
        thing.setPath(file.getAbsolutePath());
        //4.计算路径的深度
        thing.setDepth(computerFileDepath(file));
        //5、判断文件类型
        thing.setFileType(computerFileType(file));
        return thing;
    }

    /**
     * 计算路径深度
     * C：/A/B/C/java笔记.java 深度为4
     * @param file 路径
     * @return 路径深度
     */
    public static int computerFileDepath(File file){
        //1.获取文件的绝对路径
        String path = file.getAbsolutePath();
        //2.将文件的绝对路径以'\'分割开
        String [] str = path.split("\\\\");
        //3.字符\将文件拆分
        return str.length-1;
    }

    /**
     * 判断文件类型
     *  C：/A/B/C/java笔记.java
     *  找到最后一个字符.的位置 取该位置往后的字符串
     * @param file 文件名称
     * @return 文件类型
     */
    public  static FileType computerFileType(File file){
        FileType fileType = null;
        //1.如果该file对象是一个目录 直接返回OTHER
        if(file.isDirectory()){
            return FileType.OTHER;
        }
        //1.取文件的名称
        String name = file.getName();
        //2.找到最后一个字符.的位置
        int  index = name.lastIndexOf(".");
        if(index == -1 || index == name.length()-1){
            //index==-1 说明没有找到字符. index = name.length()-1即证明路径格式为 **.
            //直接返回OTHER
            return FileType.OTHER;
        }else{
            //3.如果找到字符. 取该位置往后的字符串
            String type = name.substring(index+1);
            //4.根据文件扩展名获取文件类型
            fileType = FileType.lookup(type);
        }
        return fileType;
    }
}
