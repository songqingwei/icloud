package cn.isqing.icloud.starter.variable.api.util;

import cn.isqing.icloud.starter.variable.api.dto.VariableDto;
import cn.isqing.icloud.starter.variable.api.dto.VariableSimpleDto;
import com.alibaba.fastjson2.JSONPath;

import java.util.Map;

/**
 * @author songqingwei
 */
public class VariableUtil {

    public static final String uniNameTpl = "v_%d";

    public static String getUniName(VariableSimpleDto v) {
        return String.format(uniNameTpl, v.getId());
    }

    public static String getUniName(VariableDto v) {
        return String.format(uniNameTpl, v.getId());
    }

    /**
     * 获取变量值
     *
     * @param v
     * @param resMap
     * @return
     */
    public static Object getValue(VariableSimpleDto v, Map<Long, String> resMap) {
        String s = resMap.get(v.getCid());
        if (s == null) {
            return null;
        }
        Object value = JSONPath.extract(s, v.getCresPath());
        return value;
    }
}
