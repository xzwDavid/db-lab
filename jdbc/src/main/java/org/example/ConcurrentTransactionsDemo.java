package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

public class ConcurrentTransactionsDemo {

    public static insertExcelData operateExcel;

    public static void main(String[] args) {
        int numThreads = 10;
        String url = "jdbc:postgresql://192.168.1.112:5432/jdbc_db";
        String user = "xzw";
        String password = "Xzw@010816";
        insertExcelData.readData();
        // 创建数据库连接池
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        // 创建多个线程执行事务
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
    }
}
