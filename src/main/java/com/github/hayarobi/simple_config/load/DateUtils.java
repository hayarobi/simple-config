package com.github.hayarobi.simple_config.load;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String UTC_TIMEZONE = "Z";
    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.SSSXXX";
    public static final String FORMAT_TZ = "yyyy-MM-dd HH:mm:ssX";
    public static final String FORMAT_MILLY = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_NOTHING  = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date) {
        return new SimpleDateFormat(FORMAT_FULL).format(date);
    }
}
