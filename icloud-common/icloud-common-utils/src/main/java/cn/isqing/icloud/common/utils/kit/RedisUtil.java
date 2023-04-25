package cn.isqing.icloud.common.utils.kit;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class RedisUtil {

    public static final String SEPARATOR = ":";

    public static String getKey(String... strs) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            buffer.append(strs[i]);
            buffer.append(SEPARATOR);
        }
        buffer.deleteCharAt(buffer.length()-1);
        return buffer.toString();
    }
}
