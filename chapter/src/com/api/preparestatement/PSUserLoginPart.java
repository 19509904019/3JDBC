package com.api.preparestatement;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Scanner;

/**
 * ClassName: PSUserLoginPart
 * Package: com.api.preparestatement
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/24 20:57
 * @Version 1.0
 */
public class PSUserLoginPart {
    public static void main(String[] args) throws Exception {
        // 获取用户信息
        Scanner scan = new Scanner(System.in);
        System.out.print("请输入账号：");
        String acct = scan.nextLine();
        System.out.print("请输入密码:");
        String pwd = scan.nextLine();

        // 1.注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.创建连接
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "123456");
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu", properties);

        // 创建PrepareStatement
        /*
         * 1. 编写SQL语句结果  不包含动态值部分的语句，动态值部分使用占位符 ? 替代， 注意：?只能替代动态值
         * 2. 创建prepareStatement，并且传入动态值
         * 3. 动态值 占位符 赋值 ? 单独赋值即可
         * 4. 发送SQL语句即可，并获取返回结果
         * */

        // 3.编写SQL语句结果
        String sql = "select * from t_user where account = ? and password = ?;";

        // 4.创建预编译statement并且设置SQL语句结果
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 5.单独的占位符进行赋值
        /*
         * 参数1： index 占位符的位置 从左向右 从1开始  例如：账号 ? 1
         * 参数2： object 占位符的值 可以设置任何类型的数据
         * */
        preparedStatement.setObject(1, acct);
        preparedStatement.setObject(2, pwd);

        // 6.发送SQL语句，并获取返回结果
        ResultSet resultSet = preparedStatement.executeQuery();

        // 7. 结果集解析
        if (resultSet.next())
            System.out.println("登录成功");
        else
            System.out.println("登录失败");

        // 8.关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
