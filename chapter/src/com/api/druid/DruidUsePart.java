package com.api.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ClassName: DruidUsePart
 * Package: com.api.druid
 * Description: druid连接池使用类
 *
 * @Author yujun
 * @Create 2023/10/25 12:52
 * @Version 1.0
 */
public class DruidUsePart {
    /**
     *
     * 直接使用代码设置连接池参数方式
     *  1.创建一个druid连接池对象
     *  2.设置连接池参数[必须 / 非必须]
     *  3.获取连接[通用方法，所有连接池都一样]
     *  4.回收连接[通用方法，所有连接池都一样]
     */
    public void testHard() throws SQLException {
        // 连接池对象
        DruidDataSource dataSource = new DruidDataSource();

        // 设置参数
        // 必须 连接数据库驱动类的全限定符[注册驱动] / url / user / password
        dataSource.setUrl("jdbc:mysql://localhost:3306/atguigu");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        // 帮助我们进行注册驱动
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 非必须
        dataSource.setInitialSize(5); //初始化连接数量
        dataSource.setMaxActive(10); // 最大连接数量

        // 获取连接
        DruidPooledConnection connection = dataSource.getConnection();

        // 数据库curd

        // 回收连接,不是销毁
        connection.close();
    }


    public void testSoft() throws Exception {
        // 读取配置文件
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("druid.properties");
        // 加载信息
        properties.load(is);

        // 使用连接池的工具类的工程模式，创建连接池
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();

        // 数据库curd

        // 回收连接,不是销毁
        connection.close();
    }
}
