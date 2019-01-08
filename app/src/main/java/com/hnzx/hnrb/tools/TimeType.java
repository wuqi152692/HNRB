package com.hnzx.hnrb.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeType {

    public static String time(String aa) {
        if (null == aa || aa.length() < 1)
            return "";
        long updatetime = Long.parseLong(date2TimeStamp(aa));

        long temp = System.currentTimeMillis() - updatetime;

        long mins = temp / (1000 * 60);

        long hours = temp / (1000 * 60 * 60);

        long days = temp / (1000 * 60 * 60 * 24);

        long month = days / 30;

        long years = month / 12;

        if (month > 12)
            return years + "年前";
        else if (days > 30)
            return month + "月前";
        else if (hours > 24)
            return days + "天前";
        else if (mins > 60)
            return hours + "小时前";
        else return mins + "分钟前";

    }


    public static String date2TimeStamp(String date_str) {
        String format = "yyyy-MM-dd HH:mm:ss";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getYMD() {
        String format = "yyyy-MM-dd";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = new Date();
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
