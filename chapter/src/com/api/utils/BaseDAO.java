package com.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BaseDAO
 * Package: com.api.utils
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/25 17:13
 * @Version 1.0
 */
public abstract class BaseDAO {

    public int executeUpdate(String sql, Object... args) throws SQLException {
        // 获取连接
        Connection connection = JDBCUtils2.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // 占位符设置
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }

        // 发送语句
        int rows = preparedStatement.executeUpdate();

        preparedStatement.close();

        // 是否回收链接需要考虑是不是事务
        if (connection.getAutoCommit()) {
            JDBCUtils2.freeConnection();
        }
        return rows;
    }

    public <T> List<T> executeQurey(Class<T> clazz, String sql, Object... args) throws Exception {
        // 获取连接
        Connection connection = JDBCUtils2.getConnection();

        // 创建prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 占位符赋值
        if (args != null && args.length != 0) {
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
        }

        // 发送sql语句获取结果
        ResultSet resultSet = preparedStatement.executeQuery();

        // 结果解析
        ArrayList<T> list = new ArrayList<>();

        // 获取列的个数
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            // 实例化对象
            Constructor<T> constructor = clazz.getConstructor();
            constructor.setAccessible(true);
            T t = constructor.newInstance();

            // 自动遍历列
            for (int i = 0; i < columnCount; i++) {
                // 对象的属性值
                Object value = resultSet.getObject(i + 1);

                // 获取指定列下角标的列的名称
                String propertyName = metaData.getColumnLabel(i + 1);

                // 给对象的属性值赋值
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true); // 属性可以设置，打破private修饰
                // 进行赋值
                field.set(t, value);
            }
            // 将map存储到集合中即可
            list.add(t);
        }
        // 关闭资源
        resultSet.close();
        preparedStatement.close();
        // 没有事务可以关闭
        if(connection.getAutoCommit()){
            JDBCUtils2.freeConnection();
        }

        return list;
    }
}
