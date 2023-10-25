package com.api.preparestatement;

import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: PSCURDPart
 * Package: com.api.preparestatement
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/24 21:24
 * @Version 1.0
 */
public class PSCURDPart {
    @Test
    public void testInsert() throws Exception {
        /*
        t_user插入一条数据
        account test
        password test
        nickname yujun
         */
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 创建连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user=root&password=123456");

        // 编写sql语句
        String sql = "INSERT INTO t_user(account, password, nickname) VALUES (?, ?, ?);";

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 设置参数
        preparedStatement.setObject(1, "test");
        preparedStatement.setObject(2, "test");
        preparedStatement.setObject(3, "yujun");

        // 发送数据
        preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
        connection.close();


    }

    @Test
    public void testUpdate() throws Exception {
        /*
        修改 id = 3 的用户nickname = haiyan
         */
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection
                ("jdbc:mysql:///atguigu?user=root&password=123456");
        String sql = "UPDATE t_user SET nickname = ? WHERE id = 3;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setObject(1,"haiyan");

        // 发送数据
        preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
        connection.close();
    }

    @Test
    public void testDelete() throws Exception {
        /*
        删除id=3的用户数据
         */
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql:///atguigu?user=root&password=123456");
        String sql = "DELETE FROM t_user WHERE id = 3;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 发送数据
        preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
        connection.close();

    }

    /*
    * 目标：查询所有用户数据，并且封装到一个List<Map>list集合中
    *
    * 解释：
    *   行  id account password nickname
    *   行  id account password nickname
    *   行  id account password nickname
    *
    * 数据库 -> resultSet -> java -> 一行 -> map(key=列名, value=列的内容) -> List<Map> list
    * */
    @Test
    public void testSelect() throws Exception {
        ArrayList<Map> list = new ArrayList<>();
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user=root&password=123456");

        // 输入语句
        String sql = "SELECT * FROM t_user;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            HashMap<Object, Object> map = new HashMap<>();
            int id = resultSet.getInt(1);
            String account = resultSet.getString(2);
            String password = resultSet.getString(3);
            String nickname = resultSet.getString(4);

            // 把值放入map中
            map.put("id", id);
            map.put("account",account);
            map.put("password",password);
            map.put("nickname",nickname);

            list.add(map);
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

        list.forEach(System.out::println);
    }

    // 智能获取列和行
    @Test
    public void testSelect1() throws Exception {
        ArrayList<Map> list = new ArrayList<>();
        // 注册驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 建立连接
        Connection connection = DriverManager.getConnection("jdbc:mysql:///atguigu?user=root&password=123456");

        // 输入语句
        String sql = "SELECT * FROM t_user;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        // 获取列的信息
        // metaData 获取的是当前列的信息对象(可以根据下角标获取列的信息，可以获取列的数量)
        ResultSetMetaData metaData = resultSet.getMetaData();
        // 有了columnCount可以水平遍历列
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()){
            HashMap<Object, Object> map = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                // 获取列名,相较于getColumnName，getColumnLabel会取字段的别名
                String columnLabel = metaData.getColumnLabel(i);
                map.put(columnLabel,value);
            }

            // 放入list中
            list.add(map);
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();
        connection.close();

        list.forEach(System.out::println);
    }
}
