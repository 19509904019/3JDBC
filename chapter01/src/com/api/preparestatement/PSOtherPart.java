package com.api.preparestatement;

import org.junit.Test;

import java.sql.*;

/**
 * ClassName: PSOtherPart
 * Package: com.api.preparestatement
 * Description:练习PS的特殊使用情况
 *
 * @Author yujun
 * @Create 2023/10/24 22:51
 * @Version 1.0
 */

/**
 * t_user插入一条数据，并且获取数据库自增长的主键
 */
public class PSOtherPart {
    @Test
    public void returnPrimaryKey() throws Exception {
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 创建连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user=root&password=123456");

        // 编写SQL语句
        String sql = "INSERT INTO t_user(account, password, nickname) VALUES(?,?,?);";

        // 创建PS
        // 1.告知携带回数据库自增长的主键(sql,Statement.RETURN_GENERATED_KEYS)
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // 占位符赋值
        preparedStatement.setObject(1,"test");
        preparedStatement.setObject(2,"123456");
        preparedStatement.setObject(3,"yujun");

        // 传输SQL语句
        preparedStatement.executeUpdate();

        // 2.获取自增长的主键
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        int anInt = generatedKeys.getInt(1);
        System.out.println(anInt);

        // 关闭资源
        preparedStatement.close();
        connection.close();
    }
}
