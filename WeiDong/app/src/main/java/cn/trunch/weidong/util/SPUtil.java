package cn.trunch.weidong.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static Context context;

    public static void init(Context context) {
        SPUtil.context = context;
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("md", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("md", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("md", Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static String getString(String key, String defValue) {
        SharedPreferences preferences = context.getSharedPreferences("md", Context.MODE_PRIVATE);
        return preferences.getString(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences preferences = context.getSharedPreferences("md", Context.MODE_PRIVATE);
        return preferences.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences preferences = context.getSharedPreferences("md", Context.MODE_PRIVATE);
        return preferences.getLong(key, defValue);
    }
}
