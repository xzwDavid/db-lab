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
    //插入既读  e.g. 大部分注册功能读APP
    public void InsertAndRead(){
        System.out.println("InsertAndRead");
        try {
            // 获取数据库连接
            insertExcelData.conn = ds.getConnection();
            insertExcelData.conn.setAutoCommit(false);
            // 提交事务

            insertExcelData.insertCourseData();
            insertExcelData.selectCourseData();


            insertExcelData.conn.commit();
            System.out.println("Transaction completed successfully.");
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                insertExcelData.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    //阅后既焚   e.g. shapshot
    public void ReadAndDelete() throws SQLException {
        System.out.println("InsertAndDelete");
        try {
            // 获取数据库连接
            insertExcelData.conn = ds.getConnection();
            insertExcelData.conn.setAutoCommit(false);
            // 提交事务

            insertExcelData.selectStudentData();
            insertExcelData.insertStudentData();


            insertExcelData.conn.commit();
            System.out.println("Transaction completed successfully.");
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                insertExcelData.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    //插入即更。    一些需要加密的场景
    public void InsertAndUpdate(){
        System.out.println("InsertAndUpdate");
        try {
            // 获取数据库连接
            insertExcelData.conn = ds.getConnection();
            insertExcelData.conn.setAutoCommit(false);
            // 提交事务

            insertExcelData.insertStudentData();
            insertExcelData.updateStudenData();

            insertExcelData.conn.commit();
            System.out.println("Transaction completed successfully.");
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                insertExcelData.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    //对及时性要求非常高的场景
    public void UpdateAndRead(){
        System.out.println("UpdateAndRead");
        try {
            // 获取数据库连接
            insertExcelData.conn = ds.getConnection();
            insertExcelData.conn.setAutoCommit(false);
            // 提交事务

            insertExcelData.updateStudenData();
            insertExcelData.selectStudentData();


            insertExcelData.conn.commit();
            System.out.println("Transaction completed successfully.");
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                insertExcelData.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    //数据库科研且没钱的场景 55555555～～～～
    public void InsertAndDelete(){
        System.out.println("InsertAndDelete");
        try {
            // 获取数据库连接
            insertExcelData.conn = ds.getConnection();
            insertExcelData.conn.setAutoCommit(false);
            // 提交事务

            insertExcelData.insertStudentData();
            insertExcelData.deleteStudentData();


            insertExcelData.conn.commit();
            System.out.println("Transaction completed successfully.");
            try {
                if (insertExcelData.conn != null) insertExcelData.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sleep(100);
        } catch (SQLException e) {
            try {
                // 回滚事务
                insertExcelData.conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
