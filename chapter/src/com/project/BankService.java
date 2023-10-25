package com.project;

import java.sql.Connection;

/**
 * ClassName: BankService
 * Package: com.project
 * Description: 进行存款和取款操作
 *
 * @Author yujun
 * @Create 2023/10/25 20:55
 * @Version 1.0
 */
public class BankService {

    public static void main(String[] args) throws Exception {
        /*
         * 转账
         * */
        // 创建连接
        Connection connection = JDBCUtils.getConnection();
        try {
            // 关闭事务自动提交
            connection.setAutoCommit(false);
            // 收款
            String sql1 = "UPDATE t_bank SET money = money + ? WHERE account = ?";
            BaseDao.executeUpdate(sql1, 500, "ergouzi");
            // 付款
            String sql2 = "UPDATE t_bank SET money = money - ? WHERE account = ?";
            BaseDao.executeUpdate(sql2, 500, "lvdandan");
        } catch (Exception e) {
            // 事务中有一个出错则回滚数据
            connection.rollback();
            throw e;
        }finally {
            // 关闭资源
            JDBCUtils.freeConnection();
        }

    }

}
