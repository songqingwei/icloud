package cn.isqing.icloud.common.utils.log;

import cn.isqing.icloud.common.utils.uuid.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public abstract class MDCUtil {

    private static String TRACE_ID_FIELD = "tid";

    private static String SEPARATOR = "|";

    //设置TRACE_ID
    public static void setTraceIdField(String fieldName) {
        TRACE_ID_FIELD = fieldName;
    }

    public static String getTraceId() {
        try {
            return MDC.get(TRACE_ID_FIELD);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static void appendTraceId() {
        try {
            String pid = MDC.get(TRACE_ID_FIELD);
            String traceId = "";
            if (!StringUtils.isBlank(pid)) {
                traceId = pid + SEPARATOR;
            }
            traceId += UuidUtil.randomNum_6();
            MDC.put(TRACE_ID_FIELD, traceId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void cancelAppendTraceId() {
        try {
            String traceId = MDC.get(TRACE_ID_FIELD);
            if (traceId == null) {
                return;
            }
            int i = traceId.lastIndexOf(SEPARATOR);
            if (i != -1) {
                traceId = traceId.substring(0, i);
                MDC.put(TRACE_ID_FIELD, traceId);
            } else {
                MDC.remove(TRACE_ID_FIELD);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void appendTraceId(String parentTID) {
        try {
            String traceId = parentTID + SEPARATOR + UuidUtil.randomNum_6();
            MDC.put(TRACE_ID_FIELD, traceId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void removeTraceId() {
        try {
            MDC.remove(TRACE_ID_FIELD);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
