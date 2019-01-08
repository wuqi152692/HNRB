package com.hnzx.hnrb.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: mingancai
 * @Time: 2017/4/6 0006.
 */

public class DateUtils {
    public static String patternLong = "yyyy-MM-dd HH:mm";
    public static String patternShort = "MM-dd HH:mm";
    public static String patternMMdd = "MM月dd日";
    public static String patternHHmm = "HH:mm";

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>();

    private static final Object object = new Object();

    /**
     * 获取SimpleDateFormat
     *
     * @param pattern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern)
            throws RuntimeException {
        SimpleDateFormat dateFormat = threadLocal.get();
        if (dateFormat == null) {
            synchronized (object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date stringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        String myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    public static String getFormatTime(long time) {
        if (time < 0)
            return "00:00";
        if (time < 60) {
            return "00:" + (time > 9 ? time : ("0" + time));
        } else if (time > 60 && time < 60 * 60) {
            return time / 60 < 10 ? "0" + time / 60 + ":" + (time % 60 > 9 ? time % 60 : "0" + time % 60) : time / 60 + ":" + (time % 60 > 9 ? time % 60 : "0" + time % 60);
        } else if (time > 60 * 60 && time < 60 * 60 * 24) {
            return time / (60 * 60) < 10 ? "0" + time / (60 * 60) + ":" +
                    (time % (60 * 60) / 60 < 10 ? "0" + (time % (60 * 60) / 60) : (time % (60 * 60) / 60) + ":" + time % 60) :
                    time / (60 * 60) + (time % (60 * 60) / 60) + ":" + time % 60;
        } else {
            return "";
        }
    }

    private String getFormatMinute(long time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return String.valueOf(time);
        }
    }
}
