package com.zhaomiao.core.dao.impl;

import com.zhaomiao.core.dao.FileIndexDao;
import com.zhaomiao.core.model.Condition;
import com.zhaomiao.core.model.FileType;
import com.zhaomiao.core.model.Thing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 有关数据库增删改查的具体实现
 * 由于增删改查均与数据库有关
 * 所以首先要获得数据源
 */
public class FileIndexDaoImpl implements FileIndexDao {
    //1.获取数据源 为防止数据源被修改 此处定义为final修饰
    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 数据库的插入操作
     * @param thing
     */
    @Override
    public void insert(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {

            //1.获取数据库的连接
            connection = dataSource.getConnection();
            //2.准备命令
            String sql ="insert into file_index(name , path, " +
                    "depath, file_type) values (?,?,?,?)";

            statement = connection.prepareStatement(sql);

            //3.设置参数 参数的下标依次为1,2,3,4
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType()
                    .name());

            //5.执行更新
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //6.关闭连接
            destory(null,statement,connection);
        }
    }

    /**
     * 根据条件检索文件信息
     * @param condition 检索的条件
     * @return 返回文件属性类Thing
     */
    @Override
    public List<Thing> search(Condition condition) {
        List<Thing> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet =null;
        try {
            //1.获取数据库连接
            connection = dataSource.getConnection();
            //2.获取sql语句
            StringBuffer sqlBuffer = new StringBuffer();
            sqlBuffer.append("select name,path,depath,file_type from file_index");
            //2.1 name匹配原则----前后模糊
            sqlBuffer.append(" where ")
                    .append(" name like '%")
                    .append(condition.getName())
                    .append("%' ");
            //2.2 判断用户是否输入了文件类型
            if(condition.getFileType()!=null){
                //将类型转为大写 防止用户输入小写匹配不到的问题
                sqlBuffer.append( " and file_type= '")
                        .append(condition.getFileType().toUpperCase())
                        .append("' ");
            }
            //2.3 结果集是升序还是降序 true 升序  false 降序
            sqlBuffer.append(" order by depath ")
                    .append(condition.getOrdByAsc() ? "asc ":"desc ");
            //将StringBuffer对象变为String对象
            String sql = sqlBuffer.toString();
            //3.准备命令
            statement = connection.prepareStatement(sql);
            //4.执行查询 返回一个结果集
            resultSet = statement.executeQuery();
            //5.处理结果集
            while(resultSet.next()){
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depath"));
                String fileType =resultSet.getString("file_type");
                thing.setFileType(FileType.lookupByName(fileType));
                result.add(thing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           destory(resultSet,statement,connection);
        }
        return result;
    }

    /**
     * 删除文件
     * @param thing
     */
    @Override
    public void delete(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1.获取数据库的连接
            connection = dataSource.getConnection();
            //2.准备命令
            String sql ="delete from file_index  where path like '"+thing.getPath()+"%'";
            statement = connection.prepareStatement(sql);
            //4.设置参数 参数的下标依次为1,2,3,4
            statement.setString(1,thing.getPath());
            //5.执行更新
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            //6.关闭连接
            destory(null,statement,connection);
        }
    }

    /**
     * 关闭资源
     * @param resultSet 结果集
     * @param statement
     * @param connection
     */
    private static void destory(ResultSet resultSet,PreparedStatement statement,
                           Connection connection){
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
