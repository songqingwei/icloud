package cn.isqing.icloud.common.utils.dao;

import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.utils.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.sql.SqlUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class BaseProvider<T> {

    private String simpleTpl = "`%s`=#{%s%s}";

    private long lockMinutes = 10;

    private static final String ERR_MSG = "解析sql异常";

    public String lock(T t) {
        SQL sql = new SQL();
        sql.UPDATE(SqlUtil.getTableName(t.getClass(), false));
        sql.SET("lock_version = lock_version+1");
        String nowTime = TimeUtil.now().plusMinutes(lockMinutes).format(TimeUtil.dateTimeFormatter);
        sql.SET("lock_time = '" + nowTime + "'");
        sql.SET("lock_status = 1");
        sql.WHERE("id = #{id}");
        sql.WHERE("status = 0");
        sql.WHERE("lock_version = #{lockVersion}");
        return sql.toString();
    }

    public String unlock(T t) {
        SQL sql = new SQL();
        sql.UPDATE(SqlUtil.getTableName(t.getClass(), false));
        sql.SET("lock_version = lock_version+1");
        sql.SET("lock_status = 0");
        sql.WHERE("id = #{id}");
        sql.WHERE("status = 1");
        sql.WHERE("lock_version = #{lockVersion}");
        return sql.toString();
    }

    public String insert(T object) {
        SQL sql = new SQL();
        Class<?> aClass = object.getClass();
        sql.INSERT_INTO(SqlUtil.getTableName(aClass, false));

        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object v;
            try {
                v = field.get(object);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new BaseException(ResCodeEnum.SYSTEM_ERROR.getCode(), ERR_MSG);
            }
            if (v != null) {
                String fieldName = field.getName();
                String[] arr = SqlUtil.getFieldName(fieldName, false);
                sql.VALUES(arr[0], "#{" + fieldName + "}");
            }
        }

        return sql.toString();
    }

    public String del(T object) {
        SQL sql = new SQL();

        sql.DELETE_FROM(SqlUtil.getTableName(object.getClass(), false));
        sql.WHERE(getKV(object, null));

        return sql.toString();
    }

    public String delById(@Param("id") Long id, @Param("c") Class<T> c) {
        SQL sql = new SQL();

        sql.DELETE_FROM(SqlUtil.getTableName(c, false));
        sql.WHERE("id = #{id}");

        return sql.toString();
    }

    public String delByCondition(T object) {
        SQL sql = new SQL();

        sql.DELETE_FROM(SqlUtil.getTableName(object.getClass(), true));
        sql.WHERE(getConditionKV(sql, object));

        return sql.toString();
    }

    public String update(T object) {
        SQL sql = new SQL();
        sql.UPDATE(SqlUtil.getTableName(object.getClass(), false));
        String[] list = getKV(object, null);
        String[] data =
                Arrays.stream(list).filter(v -> (!v.startsWith("id=") && !v.startsWith("id ="))).toArray(String[]::new);
        sql.SET(data);
        sql.WHERE("id = #{id}");

        return sql.toString();
    }


    public String updateByCondition(@Param("t") T t, @Param("c") Object condition) {
        SQL sql = new SQL();
        sql.UPDATE(SqlUtil.getTableName(t.getClass(), false));

        sql.SET(getKV(t, "t"));
        sql.WHERE(getConditionKV(sql, condition, "c",null));

        return sql.toString();
    }

    public String select(@Param("t") T t, @Param("fields") String fields, @Param("order") String order) {
        SQL sql = new SQL();
        if (fields == null) {
            fields = "*";
        }
        sql.SELECT(fields);
        sql.FROM(SqlUtil.getTableName(t.getClass(), false));
        sql.WHERE(getKV(t, "t"));
        if (!StringUtils.isBlank(order)) {
            sql.ORDER_BY(order);
        }
        return sql.toString();
    }

    public String selectByCondition(Object condition) {
        SQL sql = new SQL();
        sql.FROM(SqlUtil.getTableName(condition.getClass(), true));
        sql.SELECT("*");
        sql.WHERE(getConditionKV(sql, condition));
        return sql.toString();
    }


    public String leftJoinSelect(@Param("left") Object left, @Param("right") Object right, @Param("join") String join) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(left.getClass(), true) + " as l");
        sql.LEFT_OUTER_JOIN(SqlUtil.getTableName(right.getClass(), true) + " as r on " + join);
        sql.WHERE(getConditionKV(sql, left, "left","l"));
        sql.WHERE(getConditionKV(sql, right, "right","r"));
        return sql.toString();
    }

    public String leftJoinCount(@Param("left") Object left, @Param("right") Object right, @Param("join") String join) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(SqlUtil.getTableName(left.getClass(), true) + " as l");
        sql.LEFT_OUTER_JOIN(SqlUtil.getTableName(right.getClass(), true) + " as r on " + join);
        sql.WHERE(getConditionKV(sql, left, "left","l",true));
        sql.WHERE(getConditionKV(sql, right, "right","r",true));
        return sql.toString();
    }

    public String innerJoinSelect(@Param("left") Object left, @Param("right") Object right, @Param("join") String join) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(left.getClass(), true) + " as l");
        sql.INNER_JOIN(SqlUtil.getTableName(right.getClass(), true) + " as r on " + join);
        sql.WHERE(getConditionKV(sql, left, "left","l"));
        sql.WHERE(getConditionKV(sql, right, "right","r"));
        return sql.toString();
    }

    public String innerJoinCount(@Param("left") Object left, @Param("right") Object right, @Param("join") String join) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(SqlUtil.getTableName(left.getClass(), true) + " as l");
        sql.INNER_JOIN(SqlUtil.getTableName(right.getClass(), true) + " as r on " + join);
        sql.WHERE(getConditionKV(sql, left, "left","l",true));
        sql.WHERE(getConditionKV(sql, right, "right","r",true));
        return sql.toString();
    }

    public String count(T t) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(SqlUtil.getTableName(t.getClass(), false));
        sql.WHERE(getKV(t, null));
        return sql.toString();
    }

    public String countByCondition(Object c) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(SqlUtil.getTableName(c.getClass(), true));
        sql.WHERE(getConditionKV(sql, c, null,null, true));
        return sql.toString();
    }

    public String countAll(Class<T> c) {
        SQL sql = new SQL();
        sql.SELECT("count(1)");
        sql.FROM(SqlUtil.getTableName(c, false));
        return sql.toString();
    }

    public String getScrollList(@Param("t") T t, @Param("from") Long from, @Param("limit") Integer limit,
                                @Param("order") String order) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(t.getClass(), true));
        if (from != null) {
            sql.WHERE("id>" + from);
        }
        sql.WHERE(getKV(t, "t"));
        if (!StringUtils.isBlank(order)) {
            sql.ORDER_BY(order);
        }
        sql.LIMIT(limit);
        return sql.toString();
    }

    public String getPageList(@Param("t") T t, @Param("limit") Integer limit, @Param("offset") Integer offset,
                              @Param("order") String order) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(t.getClass(), true));
        sql.WHERE(getKV(t, "t"));
        if (!StringUtils.isBlank(order)) {
            sql.ORDER_BY(order);
        }
        sql.LIMIT(limit);
        sql.OFFSET(offset);
        return sql.toString();
    }

    public String getPageListSimple(@Param("c") Class<T> c, @Param("limit") Integer limit,
                                    @Param("offset") Integer offset, @Param("order") String order) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(c, true));
        if (!StringUtils.isBlank(order)) {
            sql.ORDER_BY(order);
        }
        sql.LIMIT(limit);
        sql.OFFSET(offset);
        return sql.toString();
    }

    public String first(@Param("t") T t, @Param("order") String order) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(t.getClass(), true));
        sql.WHERE(getKV(t, "t"));
        if (!StringUtils.isBlank(order)) {
            sql.ORDER_BY(order);
        }
        sql.LIMIT(1);
        return sql.toString();
    }

    public String selectById(@Param("id") Long id, @Param("c") Class<T> c) {
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM(SqlUtil.getTableName(c, true));
        sql.WHERE("id = #{id}");
        sql.LIMIT(1);
        return sql.toString();
    }

    private String[] getKV(T object, String pre) {
        if(pre==null){
            pre="";
        }else {
            pre+=".";
        }
        ArrayList<Object> list = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object v;
            try {
                v = field.get(object);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
                throw new BaseException(ResCodeEnum.SYSTEM_ERROR.getCode(), ERR_MSG);
            }
            if (v == null) {
                continue;
            }
            String fieldName = field.getName();

            String[] arr = SqlUtil.getFieldName(fieldName, false);
            list.add(String.format(simpleTpl, arr[0],pre, fieldName));

        }
        return list.toArray(new String[0]);
    }

    private String[] getConditionKV(SQL sql, Object object) {
        return getConditionKV(sql, object, null, null, false);
    }

    private String[] getConditionKV(SQL sql, Object object, String parameterName, String tableAlais) {
        return getConditionKV(sql, object, parameterName, null, false);
    }

    private String[] getConditionKV(SQL sql, Object object, String parameterName, String tableAlais, boolean isCount) {
        ConditionParser dto = new ConditionParser();
        dto.setCount(isCount);
        dto.setParamName(parameterName);
        dto.setTableAlias(tableAlais);
        dto.setSql(sql);
        dto.setObject(object);
        dto.setList(new ArrayList<>());
        dto.parse();
        return dto.getList().toArray(new String[0]);
    }


}
