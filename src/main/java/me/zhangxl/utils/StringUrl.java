package me.zhangxl.utils;

import javafx.util.Pair;

/**
 * Created by zhangxiaolong on 16/11/8.
 */
public class StringUrl extends Pair<String, String> {

    public StringUrl(String current, String parent) {
        super(current,parent);
    }

    public String getParent() {
        return getValue();
    }

    public String getCurrent() {
        return getKey();
    }

    @Override
    public String toString() {
        return "    [current:" + getCurrent()  + ",     parent:" + getParent() + "]";
    }
}
