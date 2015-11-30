package com.nancy.daycounter.util;

import java.util.Date;

/**
 * Created by nan.zhang on 11/30/15.
 */
public class DateUtil {

    public static int diffInDays(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
    }
}
