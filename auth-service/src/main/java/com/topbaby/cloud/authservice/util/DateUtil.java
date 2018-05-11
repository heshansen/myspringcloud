package com.topbaby.cloud.authservice.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p>日期工具类</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
public class DateUtil {
    /**
     * 获取今天剩余的毫秒数(截至到次日00:00:00)
     *
     * @return
     */
    public static long getTodayRemainingMillisecond() {
        Calendar todayDate = Calendar.getInstance();
        Calendar tomorrowDate = new GregorianCalendar(todayDate.get(Calendar.YEAR), todayDate.get(Calendar.MONTH),
                                          todayDate.get(Calendar.DATE) + 1, 0, 0, 0);

        return (tomorrowDate.getTimeInMillis() - todayDate.getTimeInMillis());
    }
}
