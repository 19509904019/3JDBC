package com.api.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ClassName: JDBCCrudPart
 * Package: com.api.utils
 * Description:
 *
 * @Author yujun
 * @Create 2023/10/25 15:58
 * @Version 1.0
 */
public class JDBCCrudPart {
    public static void main(String[] args) throws SQLException {
        // 建立连接
        Connection connection = JDBCUtils.getConnection();

        // 编写sql语句
        String sql = "SELECT * FROM t_user;";

        // 创建preparestatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // 传输sql语句
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            System.out.println("登录成功");
        }else {
            System.out.println("登录失败");
        }

        resultSet.close();
        JDBCUtils.freeConnection(connection);

    }
}
