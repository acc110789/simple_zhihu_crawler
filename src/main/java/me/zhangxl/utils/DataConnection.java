package me.zhangxl.utils;

import me.zhangxl.config.DBConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
class DataConnection {
    private static Connection sConnection;
    private static final String PREFIX = "jdbc:mysql://";
    private static final String PORT = ":3306/";

    static Connection getConnection(){
        try {
            if(sConnection == null || sConnection.isClosed()){
                synchronized (DataConnection.class){
                    if(sConnection == null || sConnection.isClosed()){
                        String db = PREFIX + DBConfig.host + PORT + DBConfig.dbName;
                        Class.forName("com.mysql.jdbc.Driver");
                        sConnection = DriverManager.getConnection(db, DBConfig.userName, DBConfig.passWord);
                    }
                }
            }
            return sConnection;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        getConnection();
    }
}
