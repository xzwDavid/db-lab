package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.activation.DataSource;
import java.sql.*;
import java.util.Random;

import static java.lang.Thread.sleep;
import static org.example.ConcurrentTransactionsDemo.ds;

class TransactionTask implements Runnable {
    public SqlOp sqlOp;

    public TransactionTask(BasicDataSource ds) {
        sqlOp = new SqlOp(ds);
    }

    @Override
    public void run() {
        int count = 0;
        String url = "jdbc:postgresql://192.168.1.112:5432/jdbc_db";
        String user = "xzw";
        String password = "Xzw@010816";
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (int i = 0; i < 100; i++) {
                Random random = new Random();
                //int index = random.nextInt(20);
                int index = 1;

                switch (index) {
                    case 1:
                        sqlOp.InsertAndDeleteStu(conn);
                        break;
                    default:
                        //sqlOp.InsertAndRead();
                        insertExcelData.insertStudentData();
                        break;
                }
//            switch (index) {
//                case 1:
//                    sqlOp.InsertAndDeleteCou();
//                    break;
//                default:
//                    //sqlOp.InsertAndRead();
//                    insertExcelData.insertCourseData();
//                    break;
//            }
//            switch (index) {
//                case 1:
//                    sqlOp.InsertAndDeleteSC();
//                    break;
//                default:
//                    //sqlOp.InsertAndRead();
//                    insertExcelData.insertSCData();
//                    break;
//            }
            }
            try {
                if (insertExcelData.conn != null) insertExcelData.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}