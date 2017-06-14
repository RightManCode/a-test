package com.wishland.www.wanhaohuimaterialdesign.utils;

import java.util.Date;

/**
 * Created by gerry on 2017/5/2.
 */

public class TimeUtils {
    /**
     * @return  ms转换ss
     */
    public static String getSeconds(){
        long time = new Date(System.currentTimeMillis()).getTime();
        int inttime = (int) (time/1000);
        return inttime+"l";
    }
}
