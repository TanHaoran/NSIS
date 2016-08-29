package com.jerry.nsis.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.nfc.FormatException;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /**
     * 取得当前年月日
     *
     * @return yyyy-MM-dd
     */
    public static String getYMD() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    /**
     * 取得当前月日
     *
     * @return MM-dd
     */
    public static String getMD() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    /**
     * 取得当前年月日
     *
     * @return yyyy-MM-dd
     */
    public static String getYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 取得当前年月日
     *
     * @return yyyy年MM月dd日
     */
    public static String getYMDInChinese(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date);
    }

    /**
     * 取得当前年月日时分秒
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYMDHMS(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * 取得当前年月日时分秒
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYMDHMS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * 取得当前年月日时分秒
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getYMDHMSS() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(date);
    }


    /**
     * 获取当前时间
     *
     * @return HH:mm:ss
     */
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }


    /**
     * 获取当前日期
     *
     * @return MM月dd日
     */
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        return sdf.format(date);
    }

    /**
     * 获取当前日期
     *
     * @return MM-dd
     */
    public static String getDateWithDash() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }

    /**
     * 根据生日算出年龄
     *
     * @param birthday
     * @return
     */
    public static int getAge(Date birthday) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (birthday != null) {
            now.setTime(new Date());
            born.setTime(birthday);
            if (born.after(now)) {
                throw new IllegalArgumentException(
                        "Can't be born in the future");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            if (now.get(Calendar.DAY_OF_YEAR) < born.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }
        }
        return age;
    }

    /**
     * 根据生日算出年龄
     *
     * @param birthday
     * @return
     */
    public static int getAge(String birthday) {
        int age = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            age = getAge(format.parse(birthday));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    /**
     * 比较两个日期的大小
     *
     * @param date1
     * @param date2
     * @return 1表示第一个日期在第二个日期之后，-1表示第一个日期在第二个日期之前，0表示相等
     */
    public static int compareDate(String date1, String date2) {

        int result = -2;

        date1 = date1.replace("/", "-");
        date2 = date2.replace("/", "-");

        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df1.parse(date1);
            Date dt2 = df2.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                result = 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                result = -1;
            } else {
                result = 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前是周几
     *
     * @return
     */
    public static String getDay() {
        String[] days = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        L.i("index" + cal.get(Calendar.DAY_OF_WEEK));
        return days[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }

    /**
     * 获取当前是周几
     *
     * @return
     */
    public static String getDay(Date date) {
        String[] days = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        L.i("index" + cal.get(Calendar.DAY_OF_WEEK));
        return days[cal.get(Calendar.DAY_OF_WEEK) - 1];
    }


    /**
     * 获取下一天的日期
     *
     * @param date
     * @return
     */
    public static String getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date resultDate = new Date(calendar.getTimeInMillis());
        return getYMD(resultDate);
    }

    /**
     * 日通过日期获得当前在一周内的序号：周日为1， 周一为2……周六为7.
     */
    public static int getDayOfWeekIndex(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取这周的周一的日期
     */
    public static String getMondayDate(Date date) {
        int index = getDayOfWeekIndex(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (index == 1) {
            c.add(Calendar.DAY_OF_YEAR, -6);
        } else {
            c.add(Calendar.DAY_OF_YEAR, -(index - 2));
        }
        Date result = new Date(c.getTimeInMillis());
        return getYMDInChinese(result);
    }

    /**
     * 获取这周的周日的日期
     */
    public static String getSundayDate(Date date) {
        int index = getDayOfWeekIndex(date);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (index != 1) {
            c.add(Calendar.DAY_OF_YEAR, (8 - index));
        }
        Date result = new Date(c.getTimeInMillis());
        return getYMDInChinese(result);
    }


    /**
     * 获取前一天的日期
     *
     * @param date
     * @return
     */
    public static String getPreviousDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date resultDate = new Date(calendar.getTimeInMillis());
        return getYMD(resultDate);
    }


    /**
     * 通过字符串获得Date对象
     *
     * @return
     */
    public static Date getDateByString(String string) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = f.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
