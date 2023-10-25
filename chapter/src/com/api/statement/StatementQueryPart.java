package com.api.statement;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

/**
 * ClassName: StatementQueryPart
 * Package: com.api.statement
 * Description: 使用statement查询t_user表下的全部数据
 *
 * @Author yujun
 * @Create 2023/10/24 15:19
 * @Version 1.0
 */
public class StatementQueryPart {
    public static void main(String[] args) throws SQLException {
        //1. 注册驱动
        /*
         * TODO:
         *   注册驱动
         *   依赖： 驱动版本8+ com.mysql.cj.jdbc.Driver
         *         驱动版本5+ com.mysql.jdbc.Driver
         * */
        DriverManager.registerDriver(new Driver());

        //2. 获取连接
        /*
         *   TODO
         *      java程序要和数据库创建连接
         *      java程序连接数据库，调用某个方法填入连接数据库的基本信息：
         *           数据库ip地址  127.0.0.1
         *           端口号 3306
         *           账号
         *           密码
         *           连接数据库的名称
         * */
        // 第一个参数包括ip地址、端口号、数据库名称
        // "jdbc:mysql://127.0.0.1:3306/atguigu
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/atguigu",
                "root", "123456");


        //3. 创建statement
        Statement statement = connection.createStatement();

        //4. 发送sql语句，并且返回结果
        String sql = "select * from t_user;";
        ResultSet resultSet = statement.executeQuery(sql);
        //5. 进行结果解析
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String account = resultSet.getString("account");
            String nickname = resultSet.getString("nickname");
            String password = resultSet.getString("password");
            System.out.println(id + "--" + account + "--" + password + "--" + nickname);
        }

        //6. 关闭资源
        resultSet.close();
        statement.close();
        connection.close();
    }
}
