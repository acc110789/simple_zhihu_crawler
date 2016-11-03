package me.zhangxl.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
public class ZhihuConfig {
    public static final String username;
    public static final String password;

    static {
        Properties properties = new Properties();
        try {
            properties.load(ZhihuConfig.class.getClassLoader().getResourceAsStream("zhihu_account.properties"));
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args){
        System.out.println(username + " :   " + password);
    }
}
