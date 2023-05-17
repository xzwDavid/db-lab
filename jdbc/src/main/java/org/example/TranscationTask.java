package org.example;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.activation.DataSource;
import java.sql.*;
import java.util.Random;

import static java.lang.Thread.sleep;
import static org.example.ConcurrentTransactionsDemo.*;

class TransactionTask implements Runnable {
    public SqlOp sqlOp;

    public TransactionTask(BasicDataSource ds) {
        sqlOp = new SqlOp(ds);
    }

    @Override
    public void run() {
        int count = 0;
//        String url = "jdbc:postgresql://192.168.1.112:5432/mydb";
//        String user = "xzw";
//        String password = "Xzw@010816";
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (int i = 0; i < 1000; i++) {
                Random random = new Random();
                int index = random.nextInt(20);
//                int index = 1;
                for(int j = 0; j< 5; j++) {
                    switch (index) {
                        case 1:
                            sqlOp.InsertAndDeleteStu(conn);
                            break;
                        default:
                            //sqlOp.InsertAndRead();
                            insertExcelData.insertStudentData(conn);
                            break;
                    }
                }

            switch (index) {
                case 1:
                    sqlOp.InsertAndDeleteCou(conn);
                    break;
                default:
                    //sqlOp.InsertAndRead();
                    insertExcelData.insertCourseData(conn);
                    break;
            }
                for(int j =0 ;j< 210; j++) {
                    switch (index) {
                        case 1:
                            sqlOp.InsertAndDeleteSC(conn);
                            break;
                        default:
                            //sqlOp.InsertAndRead();
                            insertExcelData.insertSCData(conn);
                            break;
                    }
                }
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