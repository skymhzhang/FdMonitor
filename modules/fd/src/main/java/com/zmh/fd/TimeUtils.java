package com.zmh.fd;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author skymhzhang on 2019-11-08.
 */
@SuppressWarnings("WeakerAccess")
public class TimeUtils {

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        Date d = new Date(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(d);
    }
}
