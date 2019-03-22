package com.zhaomiao.cmd;

import com.zhaomiao.config.MiniEverythingPlusConfig;
import com.zhaomiao.core.MiniEverythingPlusManager;
import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.Thing;

import java.util.List;
import java.util.Scanner;

public class MiniEverythingPlusCmd {
    public static void main(String[] args) {
        //给用户提供一个可以设置 排除路径、升序/降序的机会
        //手机在程序开始之前解析用户参数
        parseParams(args);
        MiniEverythingPlusManager manage =
                MiniEverythingPlusManager.getInstance();

        System.out.println("欢迎使用everything工具");
        System.out.println("如需帮助请输入help");
        //启动后台清理程序
        manage.startBackgroundClearThread();
        //启动监听
        manage.startFileSystemMonitor();
        //执行程序
        execute(manage);
    }

    /**
     * 解析用户参数
     * 规定格式：
     * ----deptOrderByAsc=true/false 结果升序/降序
     * --includePath=包含路径
     * --excludePath=排除路径(用分号隔开)
     * 参数之间用空格分开
     * @param args
     */
    private static void parseParams(String[] args){
        //定义一个配置类的对象
        MiniEverythingPlusConfig config = MiniEverythingPlusConfig.config();
        //如果用户指定的参数不对 指定默认值即可
        for(String param:args){

            String deptOrderByAscParam = "--deptOrderByAsc=";
            if(param.startsWith(deptOrderByAscParam)){
                //找出 = 的位置
                int index = param.indexOf("=");
                String deptOrderByAscStr = param.substring(index+1);
                config.setDeptOrderAsc(
                        Boolean.parseBoolean(deptOrderByAscStr)
                );
            }

            String includePathParam = "--includePath=";
            if(param.startsWith(includePathParam)){
                int index = param.indexOf("=");
                String includePathStr = param.substring(index+1);
                String[] includePaths=includePathStr.split(";");
                if(includePaths.length > 0){
                    //清理之前默认设置的includePath
                    config.getIncoudePath().clear();
                    for(String p :includePaths){
                        config.getIncoudePath().add(p);
                    }
                }
            }

            String excludePathParam = "--excludePath=";
            if(param.startsWith(excludePathParam)){
                int index = param.indexOf("=");
                String excludePathStr = param.substring(index+1);
                String[] excludePaths=excludePathStr.split(";");
                //清理之前默认设置的excludePath  默认遍历所有路径
                config.getExcludePath().clear();
                if(excludePaths.length > 0 ){
                    //添加自己设置的排除路径
                    for(String p :excludePaths){
                        config.getExcludePath().add(p);
                    }
                }
            }
        }
    }

    /**
     * 命令行交互
     * @param manage
     */
    public static void execute(MiniEverythingPlusManager manage){
        boolean repeat =true;
        while(repeat){
            System.out.print("请输入你想要的操作：");
            Scanner scanner = new Scanner(System.in);
            // 读取一行
            String input = scanner.nextLine();
            if(input !=null){
                String []inputs = input.split(" ");
                String str = inputs[0];
                switch(str){
                    case "help":help();break;
                    case "quit":repeat=false;break;
                    case "search":search(inputs,manage);break;
                    case "index":index(manage);break;
                    default:
                        System.out.println("输入异常，如需帮助请输入help");;break;
                }
            }

        }
    }
    private static void help(){
        System.out.println("帮助手册");
        System.out.println("退出: quit");
        System.out.println("索引：index");
        System.out.println("查找：search <name> [<file_type> | doc | bin " +
                "| archive | lan | other ]");
    }
    private static void search(String [] str,MiniEverythingPlusManager manage){
        if(str.length ==1 || str.length >3){
            System.out.println("输入异常，请重新输入");
            help();
        }else {
            Condition condition = new Condition();
            condition.setName(str[1]);
            condition.setOrdByAsc(MiniEverythingPlusConfig.config().getDeptOrderAsc());
            if(str.length == 3){
                condition.setFileType(str[2].toUpperCase());
            }
            List<Thing> list=manage.search(condition);
            if(list.size() != 0){
                for(Thing thing:list){
                    System.out.println(thing.getPath());
                }
            }else{
                System.out.println("对不起，您要查找的文件不存在!");
            }

        }
    }

    /**
     * 索引操作
     * @param manage
     */
    private static void index(MiniEverythingPlusManager manage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                manage.buildIndex();
            }
        }).start();
    }
}
