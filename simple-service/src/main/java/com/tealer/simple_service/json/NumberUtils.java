package com.tealer.simple_service.json;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtils {

    public static boolean isNumeric(String str) {
        if(TextUtils.isEmpty(str)) return false;
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static int convertToInt(String intStr, int defValue) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static long convertToLong(String longStr, long defValue) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static float convertToFloat(String fStr, float defValue) {
        try {
            return Float.parseFloat(fStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static double convertToDouble(String dStr, double defValue) {
        try {
            return Double.parseDouble(dStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defValue;
    }


    public static Integer convertToInteger(String intStr) {
        try {
            return Integer.parseInt(intStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long convertToLong(String longStr) {
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Float convertToFloat(String fStr) {
        try {
            return Float.parseFloat(fStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Double convertToDouble(String dStr) {
        try {
            return Double.parseDouble(dStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}