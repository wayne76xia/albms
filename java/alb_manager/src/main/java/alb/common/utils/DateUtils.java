package alb.common.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * 时间工具类
 *
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * @Date: 2020/6/6 10:35
     * @Description:格式化时间为指定格式 appoint 格式
     */
    public static String formatDateToAppoint(Date date, Integer appoint) {
        StringBuilder resultDate = new StringBuilder();
        if (appoint == 1) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy·MM·dd");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 2) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 3) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 4) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 5) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:00");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 6) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            resultDate.append(dateFormat.format(date));
        }
        if (appoint == 7) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            resultDate.append(dateFormat.format(date));
        }
        return resultDate.toString();
    }

    /**
     * 计算2个日期之间相差的  相差多少年月日
     * 比如：2011-02-02 到  2017-03-02 相差 6年，1个月，0天
     *
     * @param startDate YYYY-MM-DD
     * @param endDate   YYYY-MM-DD
     * @return 年, 月, 日 例如 1,1,1
     */
    public static String dayComparePrecise(Date startDate, Date endDate) {
        String fromDate = formatDateToAppoint(startDate, 7);
        String toDate = formatDateToAppoint(endDate, 7);
        Period period = Period.between(LocalDate.parse(fromDate), LocalDate.parse(toDate));

        StringBuilder sb = new StringBuilder();
        if (period.getYears() > 0) {
            sb.append(period.getYears()).append("年");
        }
        if (period.getMonths() > 0) {
            sb.append(period.getMonths()).append("月");
        }
        if (period.getDays() > 0) {
            sb.append(period.getDays()).append("天");
        }
        return sb.toString();
    }


    /**
     * 获取近一年的月份
     *
     * @return
     */
    public static List<String> getInitMonthMapWithZero() {
        List<String> list = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            int k = c.get(Calendar.YEAR);
            int j = c.get(Calendar.MONTH) + 1 - i;
            String date = "";
            if (j >= 1) {
                date = k + "-" + (j >= 10 ? "" : "0") + j;
            } else {
                int p = 11 - i;//剩余循环次数
                int m = c.get(Calendar.YEAR) - 1;
                int n = c.get(Calendar.MONTH) + 2 + p;
                date = m + "-" + (n >= 10 ? "" : "0") + n;
            }
            list.add(date);
        }
        Collections.reverse(list);
        return list;
    }

    private static  Date getDateAdd(int days){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    /**
     * 获取近N天的日期
     * @param days
     * @return
     */
    public static  List<String> getDaysBetween(int days){ //最近几天日期
        List<String> dayss = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(getDateAdd(days));
        long startTIme = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());
        Long endTime = end.getTimeInMillis();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTIme;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            System.out.println(df.format(d));
            dayss.add(df.format(d));
            time += oneDay;
        }
        return dayss;
    }


    /**
     * 根据返回的weekKey判断是周几
     * @param weekKey
     * @return
     */
    public static String getWeekName(Integer weekKey){
        if (weekKey == 1){
            return "周日";
        }
        if (weekKey == 2){
            return "周一";
        }if (weekKey == 3){
            return "周二";
        }if (weekKey == 4){
            return "周三";
        }if (weekKey == 5){
            return "周四";
        }if (weekKey == 6){
            return "周五";
        }if (weekKey == 7){
            return "周六";
        }
        return null;
    }

    /**
     *  Create By Renbowen
     *  @Date: 2020/8/18 21:20
     *  @Description:转换时间
     */
    public static String convertToTimeString(Date time) {
        Integer weekKey = DateUtil.dayOfWeek(time);
        // 当前时间
        Date nowDate = DateUtil.date();

        StringBuilder result = new StringBuilder();
        // 如果当前时间大于需要转换的时间
        if (nowDate.getTime() > time.getTime()){
            // 今天最大的时间
            Date todayMaxDate = DateUtil.endOfDay(nowDate);
            // 计算两个时间相差多少秒
            long betweenSecond = DateUtil.between(time, nowDate, DateUnit.SECOND);
            // 当前时间距今天剩余秒数
            long maxBetweenSecond = DateUtil.between(time,todayMaxDate,DateUnit.SECOND);
            // 今天剩余秒数
            long todayBetweenSecond = DateUtil.between(nowDate,todayMaxDate,DateUnit.SECOND);
            // 如果秒数小于一年
            if (betweenSecond < 31536000){
                // 如果两个时间差 小于 今天剩余秒数
                if (todayBetweenSecond > maxBetweenSecond){
                    // 如果是一分钟之内
                    if (betweenSecond < 60){
                        result.append("刚刚").append("(").append(getWeekName(weekKey)).append(")");
                    }else if (betweenSecond < 3600){
                        result.append(betweenSecond / 60).append("分钟前").append("(").append(getWeekName(weekKey)).append(")");
                    }else{
                        result.append(betweenSecond/3600).append("小时前").append("(").append(getWeekName(weekKey)).append(")");
                    }
                }else {
                    // 获取两个时间相差多少天  此方法精确到秒 未满86400秒则不算一天
                    long betweenDay = compareDays(time,nowDate);
                    if (betweenDay == 1){
                        result.append("昨天").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
                    }
                    else if (betweenDay == 2){
                        result.append("前天").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
                    }
                    else if (betweenDay == 3){
                        result.append(betweenDay).append("天前").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
                    }else {
                        result.append(DateUtil.format(time,"yyyy-MM-dd HH:mm:ss")).append("(").append(getWeekName(weekKey)).append(")");
                    }
                }
            }else {
                result.append(DateUtil.format(time,"yyyy-MM-dd HH:mm:ss")).append("(").append(getWeekName(weekKey)).append(")");
            }
        }
        // 如果当前时间小于等于需要转换的时间
        else {
            // 获取两个时间相差多少天  此方法精确到秒 未满86400秒则不算一天
            long betweenDay = compareDays(nowDate,time);
            if (betweenDay == 0){
                result.append("今天").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
            }
            else if (betweenDay == 1){
                result.append("明天").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
            }
            else if (betweenDay == 2){
                result.append("后天").append("(").append(getWeekName(weekKey)).append(")").append(DateUtil.format(time,"HH:mm:ss"));
            }else {
                result.append(DateUtil.format(time,"yyyy-MM-dd HH:mm:ss")).append("(").append(getWeekName(weekKey)).append(")");
            }
        }
        return result.toString();
    }


    /**
     *  @Date: 2020/8/18 21:10
     *  @Description: 计算两个日期相差多少天
     */
    public static Integer compareDays(Date date1, Date date2) {
        int day = 0;
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if(year1 > year2) {
            int tempyear = year1;
            int tempday = day1;
            day1 = day2;
            day2 = tempday;
            year1 = year2;
            year2 = tempyear;
        }
        if (year1 == year2) {
            day = (day2-day1);
        } else {
            int DayCount = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    DayCount += 366;
                }else {
                    DayCount += 365;
                }
            }
            day = DayCount+(day2-day1);
        }
        return day;
    }

}
