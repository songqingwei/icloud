package cn.isqing.icloud.common.utils.sql;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class SqlUtil {

    public static final String TABLE_QUOTES = "`";
    public static final String TABLE_CONDITION_SUF = "_condition";
    public static final String FIELD_CONDITION_SUF = "_condition";
    public static final int FIELD_CONDITION_SUF_LENGTH = 10;


    @Getter
    @Setter
    private static int tableCapacity = 100;
    @Getter
    @Setter
    private static int filedCapacity = 500;


    private static final LRUCache<String, String> TABLE_NAME = CacheUtil.newLRUCache(tableCapacity);
    private static final LRUCache<String, String[]> FILED_NAME = CacheUtil.newLRUCache(filedCapacity);

    public static <T> String getTableName(Class<T> aClass, boolean isCondition) {
        String s = TABLE_NAME.get(aClass.getSimpleName() + isCondition);
        if (s != null) {
            return s;
        }
        String simpleName = aClass.getSimpleName();
        s = snakeCaseToUnderline(simpleName);
        s = TABLE_QUOTES + s + TABLE_QUOTES;
        if (isCondition) {
            int i = s.lastIndexOf(TABLE_CONDITION_SUF);
            if (i != -1) {
                s = s.substring(0, i) + SqlUtil.TABLE_QUOTES;
            }
        }
        TABLE_NAME.put(simpleName + isCondition, s);
        return s;
    }

    public static String[] getFieldName(String fieldName, boolean isCondition) {
        String[] arr = FILED_NAME.get(fieldName + isCondition);
        if (arr != null) {
            return arr;
        }
        String name = snakeCaseToUnderline(fieldName);
        String pre = name;
        String suf = "";
        arr = new String[2];
        if (isCondition) {
            int i = name.lastIndexOf(FIELD_CONDITION_SUF);
            if (i != -1) {
                pre = name.substring(0, i);
                suf = name.substring(i + FIELD_CONDITION_SUF_LENGTH);
            }
        }
        arr[0] = pre;
        arr[1] = suf;
        FILED_NAME.put(fieldName + isCondition, arr);
        return arr;
    }

    /**
     * 驼峰转下划线
     *
     * @param input
     * @return
     */
    public static String snakeCaseToUnderline(String input) {
        int length = input.length();
        StringBuilder result = new StringBuilder(length * 2);
        int resultLength = 0;
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (i > 0 || c != '_') {
                if (Character.isUpperCase(c)) {
                    if (resultLength > 0 && result.charAt(resultLength - 1) != '_') {
                        result.append('_');
                        ++resultLength;
                    }
                    c = Character.toLowerCase(c);
                }
                result.append(c);
                ++resultLength;
            }
        }
        return resultLength > 0 ? result.toString() : input;

    }


    private SqlUtil() {
    }

    public static String escapeSqlValue(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        str = str.replace("\\", "\\\\");
        str = str.replace("\'", "\\\'");
        str = str.replace("\"", "\\\"");
        return str;
    }

    public static String getSqlIn(List<String> list) {
        return "('" + StringUtils.join(list.stream().map(SqlUtil::escapeSqlValue).toArray(), "','") + "')";
    }

    public static String getLongSqlIn(List<Long> list) {
        return "(" + StringUtils.join(list.toArray(), ",") + ")";
    }

    public static String getIntegerSqlIn(List<Integer> list) {
        return "(" + StringUtils.join(list.toArray(), ",") + ")";
    }

}
