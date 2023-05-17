package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

public class ConcurrentTransactionsDemo {

    public static insertExcelData operateExcel;
    public static  String password;
    public static String user;

    public static String studentFile;
    public static String courseFile;
    public static String  scFile;

    public static BasicDataSource ds;
    public static String url;
    public static void main(String[] args) throws SQLException {
        int numThreads = 5;

        /*------------------------------Your Configuration------------------------------------------*/
        url = "jdbc:postgresql://192.168.1.112:5432/mydb";
        user = "xzw";
        password = "Xzw@010816";


        studentFile = "/Users/xuzhongwei/code/db-lab/dataset/student.xlsx";
        courseFile = "/Users/xuzhongwei/code/db-lab/dataset/class.xlsx";
        scFile = "/Users/xuzhongwei/code/db-lab/dataset/sc2.xlsx";
        // 创建数据库连接池
        ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        insertExcelData.readData();
         //创建多个线程执行事务
        insertExcelData.conn = ds.getConnection();
        try {
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new TransactionTask(ds));
            threads[i].start();
        }

        // 等待所有线程执行完成
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            if (insertExcelData.conn != null) insertExcelData.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }catch (RuntimeException e) {
            System.out.println("Caught RuntimeException: " + e.getMessage());
        }

    }
}
