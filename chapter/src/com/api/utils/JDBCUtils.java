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
 * 内部包含一个连接池对象，并且对外提供获取连接和回收连接的方法
 * <p>
 * 实现：
 * 属性 连接池对象 [实例化一次]
 * 单例模式
 * static{
 * 全局调用一次
 * }
 * 方法
 * 对外提供连接的方法
 * 回收外部传入连接方法
 *
 * @Author yujun
 * @Create 2023/10/25 14:07
 * @Version 1.0
 */
public class JDBCUtils {
    private static DataSource instance = null;

    static {
        // 初始化连接池对象
        Properties properties = new Properties();
        InputStream is = JDBCUtils.class.getClassLoader().
                getResourceAsStream("druid.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            instance = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = instance.getConnection();
        return connection;
    }

    public static void freeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
