package com.api.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

/**
 * ClassName: StatementUserLoginPart
 * Package: com.api.statement
 * Description: 模拟用户登录
 *
 * @Author yujun
 * @Create 2023/10/24 16:31
 * @Version 1.0
 */
/*
 * TODO
 *   1.键盘输入事件，收集账号和密码信息
 *   2.注册驱动
 *   3.获取连接
 *   4.创建statement
 *   5.发送查询语句
 *   6.结果判断显示登录成功还是失败
 *   7.关闭资源
 * */
public class StatementUserLoginPart {
    public static void main(String[] args) throws Exception {
        // 获取用户信息
        Scanner scan = new Scanner(System.in);
        System.out.print("请输入账号：");
        String acct = scan.nextLine();
        System.out.print("请输入密码:");
        String pwd = scan.nextLine();


        // 1.注册驱动
        /*
         * 方法一：
         *  DriverManager.registerDriver(new Driver());
         *       注意： 8+：com.mysql.cj.jdbc.Driver
         *             5+：com.mysql.jdbc.Driver
         *
         *       问题：注册两次驱动
         *        1.DriverManager.registerDriver() 方法本身会注册依次
         *        2.Driver.static{DriverManager.registerDriver()}静态代码块也会注册一次
         *
         *       解决：只想注册一次驱动
         *               只触发静态代码块即可：Driver
         *
         *       触发静态代码块：
         *            类加载机制：类加载的时刻，会触发静态代码块
         *                 加载[class文件 -> jvm虚拟机的class对象]
         *                 连接[验证 -> 准备 -> 解析]
         *                 初始化(静态属性赋真实值)
         *
         *       触发类加载：
         *           1.new 关键字
         *           2.调用静态方法
         *           3.调用静态属性
         *           4.接口1.8default默认实现
         *           5.反射
         *           6.子类触发父类
         *           7.程序的入口main
         * */
        // 方案1
        //DriverManager.registerDriver(new Driver());

        // 方案2：MySQL新版的驱动 / 换成Oracle / 切换数据库还要改代码
//        new Driver();

        // 反射 字符串的Driver全限定符 可以引导外部的配置文件 -> xx.properties -> oracle -> 配置文件修改
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2.获取数据库连接
        /*
         * TODO
         *   三个参数：
         *   String url:
         *   语法：jdbc:数据库管理软件名称[mysql, oracle]://ip地址:端口号/数据库名？key=value&key=value 可选信息
         *   具体：
         *       jdbc:mysql://127.0.0.1:3306/atguigu
         *       jdbc:mysql://localhost:3306/atguigu
         *       jdbc:mysql:///atguigu (必须是本机且端口号是3306才可以使用)
         *   String user:
         *       数据库账号
         *   String password:
         *       数据库密码
         *
         * TODO
         *   两个参数：
         *       String url: url和三个参数内容都一样
         *       Properties info:
         *           存储账号和密码
         *           Properties 类似于 Map 只不过 key=value 都是字符串形式的
         *           key user:账号信息
         *           key password：密码信息
         *
         * TODO
         *   一个参数：
         *       String url:
         *           jdbc:mysql://localhost:3306/atguigu?user=root&password=123465
         *
         * url的路径属性可选信息：
         *       url?user=账号&password=密码
         *
         *       8.0.27版本驱动，下面都是一些可选属性！
         *           8.0.25以后自动识别时区！ serverTimezone=Asia/Shanghai不用添加！
         *           8.0.25之前需要加上
         *           8版本以后默认使用utf-8格式，useUnicode=true&characterEncoding=utf8&useSSL=true都可以省略
         *       serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=true
         * */
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu",
//                "root", "123456");


        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "123456");
        Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu", properties);

//        Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigu?user=root&password=123456");

        // 3.创建statement
//        Statement statement = connection.createStatement();
        Statement statement1 = connection1.createStatement();
//        Statement statement2 = connection2.createStatement();

        // 4.发送sql语句,返回结果
        String sql = "SELECT * FROM t_user WHERE account ='" + acct + "' AND PASSWORD='" + pwd +"';";
//        ResultSet resultSet = statement.executeQuery(sql);
        ResultSet resultSet1 = statement1.executeQuery(sql);
//        ResultSet resultSet2 = statement2.executeQuery(sql);

        // 5.解析结果
        /*
        * resultSet.get类型(String columnLabel / int columnIndex)
        *   columnLabel:列名，如果有别名写别名
        *   columnIndex：列的下角标获取 从左到有 从1开始
        * */
//        if(resultSet1.next()){
//            System.out.println("登录成功！");
//        }else {
//            System.out.println("登录失败！");
//        }

        while (resultSet1.next()) {
            // 指向当前行
            int id = resultSet1.getInt(1);
            String account = resultSet1.getString("account");
            String nickname = resultSet1.getString("nickname");
            String password = resultSet1.getString("password");

            System.out.println(id + "--" + account + "--" + nickname + "--" + password);
        }

        // 6.关闭资源
        resultSet1.close();
        statement1.close();
        connection1.close();


    }
}
