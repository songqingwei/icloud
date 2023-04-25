package cn.isqing.icloud.common.utils.kit;

import cn.isqing.icloud.common.utils.constants.StrConstants;

import java.util.regex.Pattern;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class StrUtil {
    private StrUtil() {
    }

    public static String assembleKey(String... strs){
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append(str);
            builder.append(StrConstants.SEPARATOR);
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    public static String[] splitKey(String key){
        return key.split(StrConstants.SEPARATOR,-1);
    }


    public static boolean isNumber(String str){
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^-?\\d+(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }
}
