package com.gly.zhongnan.utils;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author EugeneGe
 * @date 2023-03-02 9:32
 */
public class MyDateUtil {

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parse(String date, String pattern) {
        try {
            return DateUtils.parseDate(date, pattern);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date parseDate(String date) {
        return parse(date, "yyyy-MM-dd");
    }

    /**
     * 日期转周英文
     */
    public static String dayForWeek(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tmpDate = null;
        try {
            tmpDate = sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar cal = Calendar.getInstance();
        String[] weekDays = { "sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday" };
        try {
            cal.setTime(tmpDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 获取指定日期相隔n周的日期集合
     *
     * @param startTime
     * @param nWeek
     * @return
     */
    public static List<String> getNWeekBeforeDaysList(String startTime, int nWeek) {
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        for (int i = nWeek; i > 0; i--) {
            days.add(getNWeekDays(startTime, -i));
        }
        return days;
    }

    /**
     * 获取指定日期相隔n周的日期
     *
     * @param startTime
     * @param nWeek
     * @return
     */
    public static String getNWeekDays(String startTime, int nWeek) {
        // 返回的日期集合
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = sdf.parse(startTime);
            calendar.setTime(date);
            calendar.add(Calendar.WEEK_OF_YEAR, nWeek);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String result = sdf.format(calendar.getTime());
        return result;
    }

    /**
     * 用户可以传入startTime或endTime任意一个或两个，也可以不传入
     * 当传入的时间间隔太长时，默认返回最近的nday
     * plus: StringUtils为org.apache.commons.lang.StringUtils，读者也可以手动判断""和null
     */
    public static List<String> getNDaysList(String startTime, String endTime, int nday) {
        int ndaycurrent = nday;
        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -ndaycurrent);
            Date start = calendar.getTime();
            Date end = new Date();
            if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
                //如果用户只选择了startTime,endTime为null,startTime + 10的日期
                start = dateFormat.parse(startTime);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(start);
                calendar1.add(Calendar.DATE, ndaycurrent);
                end = calendar1.getTime();
            } else if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
                //如果用户只选择了endTime,startTime为null,endTime - 10的日期
                end = dateFormat.parse(endTime);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(end);
                calendar2.add(Calendar.DATE, -ndaycurrent);
                start = calendar2.getTime();
            } else if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
                //如果用户选择了startTime和endTime，判断endTime - startTime两个日期是否超过了ndaycurrent，超过返回最近nday天记录
                Date start1 = dateFormat.parse(startTime);
                Date end1 = dateFormat.parse(endTime);
                int a = (int) ((end1.getTime() - start1.getTime()) / (1000 * 3600 * 24));
                if (a <= ndaycurrent) {
                    //如果小于等于n天
                    start = dateFormat.parse(startTime);
                    end = dateFormat.parse(endTime);
                }
            }
            //如果超过了ndaycurrent天,就是默认的start和end
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, 0);// 日期加1(包含结束)

            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
}
