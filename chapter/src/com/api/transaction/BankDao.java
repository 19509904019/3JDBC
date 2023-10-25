package com.api.transaction;

import com.api.utils.JDBCUtils2;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * ClassName: BankDao
 * Package: com.api.transaction
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/25 11:18
 * @Version 1.0
 */
public class BankDao {

    /**
     * 存款的数据库操作方法
     *
     * @param account 存款账户
     * @param money   存款金额
     */
    public void add(String account, int money) throws Exception {
        /*
         *    由于事务的原子性，每个动作需要在同一个connection下完成，
         *    所以注册驱动和建立连接在具体的调用方法中编写
         * */

//        // 注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        // 获取连接
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql:///atguigu?user=root&password=123456");

        Connection connection = JDBCUtils2.getConnection();
        // 编写sql语句
        String sql = "UPDATE t_bank SET money = money + ? WHERE account = ?;";

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        preparedStatement.setObject(1, money);
        preparedStatement.setObject(2, account);

        // 发送sql语句
        preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
    }

    /**
     * 取款的数据库操作
     *
     * @param account 取款账户
     * @param money   取款金额
     */
    public void sub(String account, int money) throws Exception {
        /*
         *    由于事务的原子性，每个动作需要在同一个connection下完成，
         *    所以注册驱动和建立连接在具体的调用方法中编写
         * */
//        // 注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        // 获取连接
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql:///atguigu?user=root&password=123456");

        Connection connection = JDBCUtils2.getConnection();

        // 编写sql语句
        String sql = "UPDATE t_bank SET money = money - ? WHERE account = ?;";

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        preparedStatement.setObject(1, money);
        preparedStatement.setObject(2, account);

        // 发送sql语句
        preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
    }
}
