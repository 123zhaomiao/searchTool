package com.zhaomiao.core.dao;
/**
 * jdbc编程
 */

import com.alibaba.druid.pool.DruidDataSource;
import com.zhaomiao.config.MiniEverythingPlusConfig;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class DataSourceFactory {
    //1.创建数据源datasource
    private static volatile DruidDataSource dataSource;
    //单例模式 构造方法私有化
    private DataSourceFactory() {
    }
    /**
     * 初始化数据源
     * @return 数据源
     */
    public static DruidDataSource dataSource() {
        if (dataSource == null) {
            synchronized (DataSourceFactory.class) {
                //双重检查  防止多线程下创建多个数据源
                if (dataSource == null) {
                    //实例化druid数据源
                    dataSource = new DruidDataSource();
                    //设置数据库名称
                    dataSource.setDriverClassName("org.h2.Driver");
                    //采用的是h2嵌入式数据库，数据库以本地文件的方式存储
                    //只需要提供url接口不需要提供password以及用户名
                    dataSource.setUrl("jdbc:h2:"+MiniEverythingPlusConfig.config()
                            .getH2IndexPath());
                    dataSource.setTestWhileIdle(false);
                }
            }
        }
        return dataSource;
    }

    /**
     * 初始化数据库
     * 获取数据源----->创建数据库
     */
    public static void initDatabase() {
        Connection connection=null;
        PreparedStatement statement=null;
        //1.获取数据源
        DruidDataSource dataSource =  DataSourceFactory.dataSource();
        try{
            InputStream in = DataSourceFactory.class.getClassLoader().
                    getResourceAsStream("mini_everything_plus.sql");
            //假设mini_everything_plus不存在 则 in==null
            if(in == null){
                throw new RuntimeException("Sql File Not Exist");
            }
            StringBuffer stringBuffer = new StringBuffer();
            //2.获取Sql语句
            Scanner scanner = new Scanner(in);
            while(scanner.hasNext()){
                String str = scanner.nextLine();
                if(!str.startsWith("--")){
                   stringBuffer.append(str);
                }
            }
            //将StringBuffer转为String
            String sql = stringBuffer.toString();

            //3.通过数据库的连接和名称执行Sql
            //3.1获取数据源连接
            connection = dataSource.getConnection();
            //3.2 PreparedStatement用来执行SQL查询语句
            statement = connection.prepareStatement(sql);
            //3.3 执行sql语句
            statement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //无论是否抛出异常都关闭流
           if(statement != null){
               try {
                   statement.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
           if(connection!= null){
               try {
                   connection.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
        }
    }
}
