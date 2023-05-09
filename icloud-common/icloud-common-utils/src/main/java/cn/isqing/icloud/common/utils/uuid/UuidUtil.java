package cn.isqing.icloud.common.utils.uuid;

import java.util.UUID;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class UuidUtil {

    public static String randomNum_6(){
        return (int)((Math.random()*9+1)*100000)+"";
    }

    public static String uuid(boolean... isUpperCase){
        String s = UUID.randomUUID().toString();
        s.replace("-","");
        if(isUpperCase.length==0){
            return s;
        }
        return isUpperCase[0]?s.toUpperCase():s.toLowerCase();
    }

}
