package me.zhangxl.utils;

import me.zhangxl.config.AppConfig;

import java.io.*;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by zhangxiaolong on 16/11/6.
 */
public class QueueUtils {
    private static FixedSizeQueue<String> urlStrings = new FixedSizeQueue<>(100);

    public static void init() {
        populateUrlQueue();
    }

    private static void populateUrlQueue() {
        try {
            FileInputStream inputStream = new FileInputStream(AppConfig.URL_FILE);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                urlStrings.offer(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean full() {
        return urlStrings.full();
    }

    public static boolean isEmpty(){
        return urlStrings.isEmpty();
    }

    public static boolean offer(String url) {
        return urlStrings.offer(url);
    }

    public static boolean addAll(Collection<String> target) {
        return urlStrings.offerAll(target);
    }

    /**
     * @return 返回一个url，但是当没有东西的时候，会使得程序自动退出
     */
    public static String poll() {
        return urlStrings.poll();
    }

    public static String peek(){
        return urlStrings.peek();
    }

    public static void store() {
        storeUrlStringsToFile();
    }

    private static void storeUrlStringsToFile() {
        try {
            FileOutputStream inputStream = new FileOutputStream(AppConfig.URL_FILE);
            PrintStream stream = new PrintStream(inputStream);
            while (urlStrings.size() > 0) {
                stream.println(urlStrings.poll());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init();
        System.out.println("result:");
        System.out.println(urlStrings);
    }
}
