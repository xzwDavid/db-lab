package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.Random;

import static java.lang.Thread.sleep;

class TransactionTask implements Runnable {
    public SqlOp sqlOp;

    public TransactionTask(BasicDataSource ds) {
        sqlOp = new SqlOp(ds);
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            int index =random.nextInt(4);
            switch (index){
                case 1 : sqlOp.InsertAndDelete();break;
                case 2 : sqlOp.InsertAndRead();break;
                case 3 : sqlOp.InsertAndUpdate();break;
                case 4 : sqlOp.UpdateAndRead();break;
                default: sqlOp.ReadAndDelete();break;
            }
            sleep(1000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭数据库连接
            try {
                if (insertExcelData.conn != null) insertExcelData.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}