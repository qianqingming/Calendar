package com.example.calendar.utils;

import android.view.View;

import java.util.Calendar;

/**
 * 日期工具类
 */
public class CalendarUtils {

    public static String last_choosed_day;
    public static View v;

    public static Calendar CALENDAR = null;
    public static String YEAR = null;
    public static String MONTH = null;
    public static String DAY = null;
    public static String YEARANDMONTH = null;

    static {
        CALENDAR = Calendar.getInstance();
        YEAR = String.valueOf(CALENDAR.get(Calendar.YEAR));
        MONTH = String.valueOf(CALENDAR.get(Calendar.MONTH) + 1);
        DAY = String.valueOf(CALENDAR.get(Calendar.DAY_OF_MONTH));
        YEARANDMONTH = YEAR + "年" + MONTH + "月";
    }



    /**
     * 获取一月中的第一天是星期几
     * 1-7 星期日-星期六
     * @param cal
     * @return
     */
    public static int weekOfTheFirstDay(Calendar cal) {
        cal.set(Calendar.DAY_OF_MONTH,1);//设置为当月第一天
        int week = cal.get(Calendar.DAY_OF_WEEK);//当天是周几
        return week;
        /*int day = cal.get(Calendar.DAY_OF_MONTH);//4日
        int week = cal.get(Calendar.DAY_OF_WEEK);// 2 -- 星期一
        int count = day - 1;
        int firstDay = 0;
        if(count == 0) {
            return week;
        }else {
            firstDay = week-count<0 ? (week-count+49)%7 : week-count;
        }
        return firstDay;*/
    }

    /**
     * 获取一个月有多少天
     * @param year
     * @param month
     * @return
     */
    public static int daysOfMonth(int year,int month) {
        boolean isLeapYear = isLeapYear(year);
        int days = 0;
        switch(month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = isLeapYear ? 29 : 28;
                break;
        }
        return days;
    }

    /**
     * 	判断是否是闰年
     * @param year
     * @return
     */
    private static boolean isLeapYear(int year) {
        if((year%4==0 && year%100!=0) || year%400==0) {
            return true;
        }
        return false;
    }
}
