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
        preparedStatement.setObject(1, "test");
        preparedStatement.setObject(2, "123456");
        preparedStatement.setObject(3, "yujun");

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

    /*
     * 批量插入数据
     * */
    @Test
    public void insertBatch() throws Exception {
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立连接
        /*
        * rewriteBatchedStatements=true:允许批量插入
        *   注意：
        *       1. insert into values(必须写复数，并且语句不能以 ; 结束)
        *       2. 不是执行每条语句，是批量添加addBatch();
        *       3. 统一批量添加
        * */
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql:///atguigu?rewriteBatchedStatements=true","root","123456");

        // 编写sql语句
        String sql = "INSERT INTO t_user(account, password, nickname) VALUES (?,?,?)";

        // 创建preparestatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        long start = System.currentTimeMillis();
        // 占位符赋值,插入10000条数据
        for (int i = 0; i < 10000; i++) {
            preparedStatement.setObject(1, "root" + i);
            preparedStatement.setObject(2, (int) (Math.random() * 1000000 + 1));
            preparedStatement.setObject(3, "ZY" + i);

            // 批量存储语句完后再发送
            preparedStatement.addBatch();
        }
        // 批量发送sql语句
        preparedStatement.executeBatch();

        long end = System.currentTimeMillis();
        System.out.println(end - start);


        //关闭资源
        preparedStatement.close();
        connection.close();
    }
}
