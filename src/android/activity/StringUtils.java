package com.activity;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/21 0021.
 */

public class StringUtils {

    /**
     * 將時間裝換為 *月*日
     * @param duration
     * @return
     */
    public static String parseDurationToMM(long duration){
        SimpleDateFormat format = new SimpleDateFormat("M月d日");
        return format.format(new Date(duration));
    }

    /**
     * 將時間轉化為 **:**
     * @param duration
     * @return
     */
    public static String parseDurationToHH(long duration){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(duration));
    }

}
