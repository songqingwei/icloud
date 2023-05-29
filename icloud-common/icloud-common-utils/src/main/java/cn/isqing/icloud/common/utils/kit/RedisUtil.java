package cn.isqing.icloud.common.utils.kit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class RedisUtil {

    public static final String SEPARATOR = ":";

    public static String getKey(String... strs) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            buffer.append(strs[i]);
            buffer.append(SEPARATOR);
        }
        buffer.deleteCharAt(buffer.length()-1);
        String key = buffer.toString();
        log.info("key:{}",key);
        return key;
    }
}
