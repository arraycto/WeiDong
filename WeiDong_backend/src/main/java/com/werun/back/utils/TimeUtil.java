package com.werun.back.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TimeUtil
 * @Author HWG
 * @Time 2019/4/26 15:36
 */

public class TimeUtil {

    public static Date getDateByYMD(String p){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df=(DateFormat)sdf;
        Date parse = null;
        try {
            parse = df.parse(p);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    };

    public static Date getDateByYMDHMS(String p){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df=(DateFormat)sdf;
        Date parse = null;
        try {
            parse = df.parse(p);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    };

    public static String getFormatyMd(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(new Date());
        return format;
    }

    public static String getFormatyMd(Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(date);
        return format;
    }

    public static String getFormatyMdHms(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getFormatyMdHms(Date d){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
}
