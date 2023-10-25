package com.api.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: JDBCUtils
 * Package: com.api.utils
 * Description:
 *      利用线程本地变量，存储连接信息。
 *      确保一个线程的多个方法可以获取同一个connection
 *
 *      优势：事务操作的时候service和dao属于同一个线程，不用在传递参数了
 *      大家可以调用getConnection自动获取相同的连接
 * @Author yujun
 * @Create 2023/10/25 14:07
 * @Version 2.0
 */
public class JDBCUtils2 {
    private static DataSource dataSource = null;

    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    static{
        // 通过配置文件初始化连接信息
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().
                getResourceAsStream("druid.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 添加容量池
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 获取连接的方法
    public static Connection getConnection() throws SQLException {
        // 线程本地变量中是否存在
        Connection connection = tl.get();

        // 第一次没有
        if(connection == null){
            connection = dataSource.getConnection();
            tl.set(connection);
        }

        return connection;
    }

    // 关闭连接的方法
    public static void freeConnection() throws SQLException {
        Connection connection = tl.get();

        if(connection != null){ // 清空线程本地变量数据
            tl.remove();
            connection.setAutoCommit(true); // 事务状态回归
            connection.close(); // 回收到连接池即可
        }
    }
}
