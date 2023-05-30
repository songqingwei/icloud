package cn.isqing.icloud.starter.variable.api.util;

import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableSimpleDto;
import cn.isqing.icloud.starter.variable.api.enums.VariableType;
import com.alibaba.fastjson2.JSONPath;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

/**
 * @author songqingwei
 */
public class VariableUtil {

    public static final String uniNameTpl = "v%d";

    public static String getUniName(ApiVariableSimpleDto v) {
        return String.format(uniNameTpl, v.getId());
    }

    public static String getUniName(ApiVariableDto v) {
        return String.format(uniNameTpl, v.getId());
    }

    public static Object getValue(ApiVariableSimpleDto v, Map<Long, String> resMap) {
        String s = resMap.get(v.getCid());
        if (s == null) {
            return null;
        }
        return JSONPath.extract(s, v.getCresPath());
    }

    /**
     * 提取结果，同时做结果类型转换
     *
     * @param v
     * @param resMap
     * @return
     */
    public static Object getValue(ApiVariableDto v, Map<Long, String> resMap) {
        String s = resMap.get(v.getCid());
        if (s == null) {
            return null;
        }
        Object res = JSONPath.extract(s, v.getCresPath());
        VariableType type = VariableType.fromCode(v.getType());
        switch (type) {
            case BIG_INTEGER:
                res = new BigInteger(res.toString());
                break;
            case BIG_DECIMAL:
                res = new BigDecimal(res.toString());
                break;
            default:
                break;
        }
        return res;
    }

    public static Object getValue(Long cid, String cresPath, Map<Long, String> resMap) {
        String s = resMap.get(cid);
        if (s == null) {
            return null;
        }
        return JSONPath.extract(s, cresPath);
    }
}
