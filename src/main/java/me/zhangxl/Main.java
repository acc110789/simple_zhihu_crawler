package me.zhangxl;

import me.zhangxl.utils.DBUtils;
import me.zhangxl.utils.HttpUtils;
import me.zhangxl.utils.QueueUtils;

/**
 * Created by zhangxiaolong on 16/11/3.
 */
public class Main {

    public static void main(String[] args) {
        init();
        //中间就是爬虫过程
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("shutdown hook begin!");
                store();
            }
        });
        startCrawl();
    }

    /**
     * 开始爬数据
     */
    private static void startCrawl() {
        new Worker().run();
    }

    private static void init(){
        DBUtils.init();
        HttpUtils.init();
        QueueUtils.init();
    }

    private static void store(){
        HttpUtils.store();
        QueueUtils.store();
    }
}
