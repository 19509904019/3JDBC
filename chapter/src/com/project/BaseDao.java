package com.project;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BaseDAO
 * Package: com.project
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/25 20:00
 * @Version 1.0
 */
public class BaseDao {
    /**
     * 对数据库的表进行查询
     *
     * @param clazz 反射调用构造器、属性等，确定列表中的类型
     * @param sql   输入的DML语句
     * @param args  DML语句中占位符的个数
     * @param <T>   返回列表的参数类型
     * @return 返回一个存有数据库表数据的列表
     * @throws SQLException 异常抛出
     */
    public static <T> List<T> executeQurey(Class<T> clazz, String sql, Object... args) throws Exception {
        // 创建连接
        Connection connection = JDBCUtils.getConnection();

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        }

        // 传输sql语句获取结果
        ResultSet resultSet = preparedStatement.executeQuery();

        // 获取结果集的列以及获取总列数
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        ArrayList<T> list = new ArrayList<>(); // 存放数据的列表

        // 解析结果
        while (resultSet.next()) {
            // 实例化对象，一条记录相当于一个对象,对构造的一个JavaBean创建实例
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            T t = constructor.newInstance();

            // 遍历结果集
            for (int i = 0; i < columnCount; i++) {
                // 获取对象的属性值，即具体数据，从1开始
                Object value = resultSet.getObject(i + 1);
                // 获取属性值所对应的字段名，从1开始
                String columnName = metaData.getColumnLabel(i + 1);

                // 给对象属性赋值
                Field field = clazz.getDeclaredField(columnName);
                field.setAccessible(true);
                field.set(t, value);
            }

            // 将当前对象，即记录添加到list中
            list.add(t);

        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();
        //
        if (connection.getAutoCommit()) {
            JDBCUtils.freeConnection();
        }
        return list;
    }

    /**
     * 对数据更新操作
     * @param sql 更新数据库的DML语句
     * @param args 占位符个数
     * @return 返回更新数据的数目
     * @throws Exception 抛出异常
     */
    public static int executeUpdate(String sql, Object... args) throws Exception {
        // 创建连接
        Connection connection = JDBCUtils.getConnection();

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 给占位符赋值
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        }

        // 更新数据
        int rows = preparedStatement.executeUpdate();

        // 关闭资源
        preparedStatement.close();
        if(connection.getAutoCommit()){
            JDBCUtils.freeConnection();
        }
        return rows;
    }
}
