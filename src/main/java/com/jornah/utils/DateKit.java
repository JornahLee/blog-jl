/**
 * Created by IntelliJ IDEA.
 * User: Jornah Lee
 * DateTime: 2018/7/26 15:48
 **/
package com.jornah.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateKit {



    public static String formatDateByUnixTime(long unixTime, String dateFormat) {
        return dateFormat(new Date(unixTime), dateFormat);
    }
    public static String formatDateByUnixTime(Instant unixTime, String dateFormat) {
        return dateFormat(new Date(unixTime.toEpochMilli()), dateFormat);
    }

    public static String dateFormat(Date date, String dateFormat) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            return format.format(date);
        }
        return "";
    }

    public static int getCurrentUnixTime() {
        return getUnixTimeByDate(new Date());
    }

    public static int getUnixTimeByDate(Date date) {
        return (int)(date.getTime() / 1000L);
    }

}
