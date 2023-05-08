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
        int count = 0;
        for(int i=0;i<100;i++){
            try {
                Random random = new Random();
                //int index =random.nextInt(1);
                int index = 1;

                switch (index){
                    case 1 :
                        sqlOp.InsertAndDelete();
                        break;
                    case 2 :
                        count++;
                        //System.out.println(count);
                        sqlOp.InsertAndRead();
                        break;
                    case 3 :
                        count++;
                        sqlOp.InsertAndUpdate();
                        break;
                    case 4 :
                        sqlOp.UpdateAndRead();
                        break;
                    default:
                        if(count==0)
                            break;
                        count--;
                        sqlOp.ReadAndDelete();
                        break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}