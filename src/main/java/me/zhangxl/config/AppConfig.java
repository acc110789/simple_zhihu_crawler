package me.zhangxl.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
public class AppConfig {
    public static final int URL_QUEUE_SIZE;
    public static final String COOKIE_STORE_PATH;
    public static final String URL_FILE;
    public static final String CAPTCHA_FILE;

    static {
        Properties properties = new Properties();
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("app_config.properties"));
            URL_QUEUE_SIZE = Integer.valueOf(properties.getProperty("url_queue_size"));
            COOKIE_STORE_PATH = properties.getProperty("cookie_store_path");
            URL_FILE = properties.getProperty("url_file");
            CAPTCHA_FILE = properties.getProperty("captcha_file");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
