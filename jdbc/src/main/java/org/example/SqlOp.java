package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.Thread.sleep;

public class SqlOp {

    private final BasicDataSource ds;

    public SqlOp(BasicDataSource ds) {
        this.ds = ds;
    }

    public void InsertAndRead(){
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            // 获取数据库连接
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            // 执行第一个SQL语句
            String sql1 = "UPDATE accounts SET balance = balance + 100 WHERE id = 1";
            stmt1 = conn.prepareStatement(sql1);
            stmt1.executeUpdate();

            // 执行第二个SQL语句
            String sql2 = "UPDATE accounts SET balance = balance - 100 WHERE id = 2";
            stmt2 = conn.prepareStatement(sql2);
            stmt2.executeUpdate();

            // 提交事务
            conn.commit();
            System.out.println("Transaction completed successfully.");
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void ReadAndDelete(){

    }
    public void InsertAndUpdate(){

    }
    public void UpdateAndRead(){

    }
    public void InsertAndDelete(){

    }
}
