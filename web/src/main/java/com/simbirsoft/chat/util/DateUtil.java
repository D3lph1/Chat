package com.simbirsoft.chat.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    /**
     * Private constructor to ensure non-instantiation because this class
     * contains only static methods.
     */
    private DateUtil() {
    }

    public static Date addDays(int days) {
        return addDays(new Date(), days);
    }

    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        return calendar.getTime();
    }
}
