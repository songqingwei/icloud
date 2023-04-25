package cn.isqing.icloud.starter.drools.service.util;

import cn.isqing.icloud.starter.drools.common.constants.RunResConstants;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class ComponentUtil {

    public static Map<String, Object> getRunRes(RunLog log) {
       Map<String, Object> map = new HashMap<>();
       map.put(RunResConstants.TARGET_ID,log.getTargetId());
       return map;
    }
}
