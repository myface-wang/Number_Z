package cn.bmob.otaku.number_z.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/12/24.
 */
public class Regular {

    public static boolean isPassword(String strPassword) {
        String check = "^[0-9_a-zA-Z]{6,15}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(strPassword);
        return matcher.matches();
    }


    public static boolean isEmail(String strEmail) {
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(strEmail);
        return matcher.matches();
    }


    /**
     * 不允许输入空格或特殊字符
     * @param strNotnull
     * @return
     */
    public static boolean isNotnull(String strNotnull) {
        String check = "^\\w+";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(strNotnull);
        return matcher.matches();
    }

    /**
     * 匹配首尾空白字符的正则表达式
     * @param strNull
     * @return
     */
    public static boolean isnull(String strNull){
        String check = "^\\s*|\\s*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(strNull);
        return matcher.matches();
    }

}
