package com.api.transaction;

import com.api.utils.JDBCUtils2;

import java.sql.Connection;

/**
 * ClassName: BankService
 * Package: com.api.transaction
 * Description:银行业务方法，调用dao方法
 *
 * @Author yujun
 * @Create 2023/10/25 11:20
 * @Version 1.0
 */
public class BankService {

    public void transfer(String addAccount, String subAccount, int money)
            throws Exception {
        BankDao bankDao = new BankDao();

        // 一个事务的最基本要求，必须是同一个连接对象 connection
        // 一个转账方法属于一个事务，包括两个动作，取款和存款

//        // 1. 注册驱动
//        Class.forName("com.mysql.cj.jdbc.Driver");
//
//        // 获取连接
//        Connection connection = DriverManager.getConnection(
//                "jdbc:mysql:///atguigu?user=root&password=123456");
        Connection connection = JDBCUtils2.getConnection();

        try {

            //首先关闭事务自动提交
            connection.setAutoCommit(false);
            // 一个事务的动作
            bankDao.add(addAccount, money);
            bankDao.sub(subAccount, money);
            // 提交事务
            connection.commit();

        } catch (Exception e) {
            // 有一个动作有错就回滚数据
            connection.rollback();
            throw e;
        }finally {
            // 关闭资源
            JDBCUtils2.freeConnection();
        }

    }

    public static void main(String[] args) {
        BankService bankService = new BankService();
        try {
            bankService.transfer("lvdandan", "ergouzi", 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
