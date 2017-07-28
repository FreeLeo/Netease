package com.xiongan.develop.news.util;

import java.text.SimpleDateFormat;

/**
 * Created by admin on 2017/7/28.
 */

public class Time2String {
    public static String getStrTime(long cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long lcc_time = cc_time;
        re_StrTime = sdf.format(lcc_time * 1000L);
        return re_StrTime;
    }
}
