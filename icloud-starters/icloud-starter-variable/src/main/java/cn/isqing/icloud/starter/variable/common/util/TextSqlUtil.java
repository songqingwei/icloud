package cn.isqing.icloud.starter.variable.common.util;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.starter.variable.common.constants.TextConstants;
import com.alibaba.fastjson2.JSON;

import java.util.function.Consumer;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class TextSqlUtil {


    public static void insertText(BaseMapper mapper, Object text, Object o, Consumer<Object> pre, Consumer<String> loop) {
        if(o == null){
            return;
        }
        pre.accept(text);
        String o1;
        if(o instanceof String){
            o1 = (String) o;
        }else{
            o1 = JSON.toJSONString(o);
        }
        String[] strings = o1.split(TextConstants.REGEX_5000);
        for (String string : strings) {
            loop.accept(string);
            mapper.insert(text);
        }
    }
}
