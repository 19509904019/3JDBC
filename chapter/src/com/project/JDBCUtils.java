package com.project;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: JDBCUtils
 * Package: com.project
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/25 19:37
 * @Version 1.0
 */
public class JDBCUtils {
    // 创建容量池
    private static DataSource dataSource = null;

    // 通过线程获取同一个connection
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    // 加载配置文件获取用户名、密码、url等信息
    static {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("druid.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 配置数据源对象
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建连接的方法
    public static Connection getConnection() throws SQLException {
        // 先从线程获取连接确认是否存在
        Connection connection = tl.get();

        // 不存在则创建连接
        if (connection == null) {
            connection = dataSource.getConnection();
            tl.set(connection);
        }

        // 存在则返回已有的连接
        return connection;
    }

    // 创建释放连接的方法
    public static void freeConnection() throws SQLException {
        // 先看当前是否有连接
        Connection connection = tl.get();
        // 如果有连接则关闭
        if (connection != null) {
            tl.remove();  // 将连接移除
            connection.setAutoCommit(true);
            connection.close();
        }
    }
}
