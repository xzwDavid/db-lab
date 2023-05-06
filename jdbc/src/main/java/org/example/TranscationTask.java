package org.example;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
class TransactionTask implements Runnable {
    public SqlOp sqlOp;
    public TransactionTask(BasicDataSource ds) {
        sqlOp = new SqlOp(ds);
    }

    @Override
    public void run() {

    }
}