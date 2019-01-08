package com.hnzx.hnrb.tools;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by FoAng on 16/12/14 下午4:17；
 */
public class RegularUtil {

    /**
     * 验证手机号
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[0, 1, 6, 7, 8], 18[0-9]
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        boolean isValid;
        Pattern p = Pattern.compile("^[1]([3][0-9]{1}|[4][5-7]|[5][0-9]|[7][01678]|8[0-9])[0-9]{8}$");
        Matcher m  = p.matcher(str);
        isValid = m.matches();
        return isValid;
    }

    /**
     * 判断密码有效 6-18位密码
     * @param passWord
     * @return
     */
    public static boolean isPassWordValid(String passWord) {
        boolean isValid = false;
        if (TextUtils.isEmpty(passWord)) return isValid;
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{7,18}$");
        Matcher matcher = pattern.matcher(passWord);
        return matcher.matches() | isValid;
    }

    public static boolean isHttpUrl(String url){
        if (TextUtils.isEmpty(url)) {
            return false;
        } else {
            if (url.startsWith("http") || url.startsWith("https")) {
                return true;
            } else {
                return false;
            }
        }
    }
}
