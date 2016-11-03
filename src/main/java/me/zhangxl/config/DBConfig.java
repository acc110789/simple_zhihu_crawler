package me.zhangxl.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
public class DBConfig {
    public final static String dbName;
    public final static String userName;
    public final static String passWord;
    public final static String host;
    public final static String createSql;

    static {
        Properties properties = new Properties();
        try {
            properties.load(DBConfig.class.getClassLoader().getResourceAsStream("mysql_config.properties"));
            dbName = properties.getProperty("database");
            userName = properties.getProperty("username");
            passWord = properties.getProperty("password");
            host = properties.getProperty("host");
            createSql = properties.getProperty("create_table_sql");
        } catch (IOException e) {
            throw new IllegalStateException("parameters is wrong!",e);
        }
    }

    public static void main(String[] args){
        System.out.println(createSql);
    }
}
