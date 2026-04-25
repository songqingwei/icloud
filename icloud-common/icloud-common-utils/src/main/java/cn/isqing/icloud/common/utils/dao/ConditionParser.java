package cn.isqing.icloud.common.utils.dao;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.sql.SqlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
@Slf4j
public class ConditionParser {

    private static final String ERR_MSG = "解析sql异常";

    private SQL sql;
    // 原始对象
    private Object object;
    private Object currentObject;
    // 表别名
    private String tableAlias = "";
    private String tableAliasPre = "";
    // 如id=#{dto.id}
    private String nomalTpl = "%s`%s`=#{%s%s}";
    private String inTpl = "%s`%s` in %s";
    private final Map<String, String> specialTplMap = new HashMap<>();

    {
        specialTplMap.put("_min", "%s`%s`>=#{%s%s}");
        specialTplMap.put("_min_open", "%s`%s`>#{%s%s}");
        specialTplMap.put("_max", "%s`%s`<=#{%s%s}");
        specialTplMap.put("_max_open", "%s`%s`<#{%s%s}");
        specialTplMap.put("_like", "%s`%s` like #{%s%s%%}");
        specialTplMap.put("_not_eq", "%s`%s` != #{%s%s}");
        specialTplMap.put("_not_in", "%s`%s` not in %s");
    }

    private final Map<String, Consumer<Object>> baseConditionConsumerMap = new HashMap<>();

    {
        baseConditionConsumerMap.put("groupBy", v -> sql.GROUP_BY((String[]) v));
        baseConditionConsumerMap.put("selectFiled", v -> {
            //移除默认的查询字段*，否则指定支持一个字段 转换Long对象等会拿结果集第一条映射转换，逻辑混乱报错
            sql.REMOVE_SELECT("*");
            sql.SELECT((String[]) v);
        });
        baseConditionConsumerMap.put("orderBy", v -> sql.ORDER_BY((String[]) v));
        baseConditionConsumerMap.put("andConditions", this::dealAndConditions);
        baseConditionConsumerMap.put("orConditions", this::dealOrConditions);
    }

    private List<Object> list;

    private Field currentField;
    private Object currentValue;

    private String paramName = "";
    private String paramNamePre = "";

    private boolean isCount = false;

    public void setTableAlias(String tableAlias) {
        if (StringUtils.isEmpty(tableAlias)) {
            return;
        }
        this.tableAlias = tableAlias;
        this.tableAliasPre = tableAlias + ".";
    }

    public void setParamName(String paramName) {
        if (StringUtils.isEmpty(paramName)) {
            return;
        }
        this.paramName = paramName;
        this.paramNamePre = paramName + ".";
    }

    public void dealFiled() {
        Object v = getV();
        if (v == null) {
            return;
        }
        if (v instanceof List && ((List) v).isEmpty()) {
            return;
        }
        String fieldName = currentField.getName();
        String[] arr = SqlUtil.getFieldName(fieldName, true);
        if (StringUtils.isEmpty(arr[0])) {
            return;
        }
        //特殊条件处理
        if (StringUtils.isNotEmpty(arr[1])) {
            boolean b = dealSpecialSymbolString(arr, fieldName, v);
            if (b) {
                return;
            }
        }
        if (v instanceof List) {
            dealList(arr[0]);
            return;
        }
        //其他类型 todo
        //正常 =
        dealString(arr[0], fieldName);

    }


    private boolean dealSpecialSymbolString(String[] arr, String fieldName, Object v) {
        String s = specialTplMap.get(arr[1]);
        if (s == null) {
            return false;
        }

        if (v instanceof List) {
            list.add(String.format(specialTplMap.get(arr[1]), tableAliasPre, arr[0], getSqlIn()));
        } else {
            list.add(String.format(specialTplMap.get(arr[1]), tableAliasPre, arr[0], paramNamePre, fieldName));
        }
        return true;
    }

    private void dealString(String column, String fieldName) {
        list.add(String.format(nomalTpl, tableAliasPre, column, paramNamePre, fieldName));
    }

    private void dealList(String column) {
        list.add(String.format(inTpl, tableAliasPre, column, getSqlIn()));
    }

    private String getSqlIn() {
        List value1 = (List) currentValue;
        if (value1.isEmpty()) {
            return "()";
        }
        Object o = value1.get(0);
        if (o instanceof Long) {
            return SqlUtil.getLongSqlIn(value1);
        }
        if (o instanceof Integer) {
            return SqlUtil.getIntegerSqlIn(value1);
        }
        return SqlUtil.getSqlIn(value1);
    }

    public void dealBaseCondition() {
        Object v = getV();
        if (v == null) {
            return;
        }
        String name = currentField.getName();
        switch (name) {
            case "limit":
                setLimit();
                break;
            case "offset":
                setOffset();
                break;
            default:
                comsumer(name);
                break;
        }
    }

    private void comsumer(String name) {
        Consumer<Object> consumer = baseConditionConsumerMap.get(name);
        if (consumer == null) {
            return;
        }
        
        // andConditions 和 orConditions 需要特殊处理，直接传递原始值
        if ("andConditions".equals(name) || "orConditions".equals(name)) {
            consumer.accept(currentValue);
            return;
        }
        
        if (currentValue instanceof String) {
            String value = (String) this.currentValue;
            if (!tableAliasPre.equals("")) {
                if (value.indexOf(",") > -1) {
                    value.replace(",", tableAlias + ",");
                }
                value = tableAliasPre + value;
            }
            consumer.accept(new String[]{value});
            return;
        }
        if (currentValue instanceof String[]) {
            String[] arr = (String[]) this.currentValue;
            if (!tableAliasPre.equals("")) {
                for (int i = 0; i < arr.length; i++) {
                    arr[i] = tableAliasPre + arr[i];
                }
            }
            consumer.accept(arr);
            return;
        }
        if (currentValue instanceof List) {
            List<String> list1 = (List<String>) this.currentValue;
            list1 = list1.stream().map(s -> tableAliasPre + s).collect(Collectors.toList());
            consumer.accept(list1.toArray(new String[list1.size()]));
            return;
        }
    }

    private void setOffset() {
        sql.OFFSET((long) currentValue);
    }

    private void setLimit() {
        sql.LIMIT((int) currentValue);
    }

    /**
     * 处理自定义 AND 条件列表
     * 每个条件会自动追加表别名前缀（如果有）
     */
    private void dealAndConditions(Object v) {
        if (!(v instanceof List)) {
            return;
        }
        List<String> conditions = (List<String>) v;
        if (conditions.isEmpty()) {
            return;
        }
        for (String condition : conditions) {
            if (StringUtils.isEmpty(condition)) {
                continue;
            }
            // 自动追加表别名前缀
            String processedCondition = appendTableAlias(condition);
            list.add(processedCondition);
        }
    }

    /**
     * 处理自定义 OR 条件列表
     * 所有条件以 OR 连接，并用括号包裹后作为一个整体条件加入 WHERE
     */
    private void dealOrConditions(Object v) {
        if (!(v instanceof List)) {
            return;
        }
        List<String> conditions = (List<String>) v;
        if (conditions.isEmpty()) {
            return;
        }
        
        // 过滤空条件并处理表别名前缀
        List<String> processedConditions = new ArrayList<>();
        for (String condition : conditions) {
            if (StringUtils.isEmpty(condition)) {
                continue;
            }
            String processedCondition = appendTableAlias(condition);
            processedConditions.add(processedCondition);
        }
        
        if (processedConditions.isEmpty()) {
            return;
        }
        
        // 将所有条件用 OR 连接，并用括号包裹
        String orClause = "(" + String.join(" OR ", processedConditions) + ")";
        list.add(orClause);
    }

    /**
     * 为 SQL 条件表达式自动追加表别名前缀
     * 只在条件中没有包含表名/别名时才追加
     */
    private String appendTableAlias(String condition) {
        if (StringUtils.isEmpty(tableAliasPre)) {
            return condition;
        }
        
        // 如果条件中已经包含了点号（说明已有表名或别名），则不追加
        if (condition.contains(".")) {
            return condition;
        }
        
        // 提取条件中的第一个字段名并追加表别名前缀
        // 例如: "status = 1" -> "l.status = 1"
        int operatorIndex = findOperatorIndex(condition);
        if (operatorIndex > 0) {
            String fieldName = condition.substring(0, operatorIndex).trim();
            String rest = condition.substring(operatorIndex);
            return tableAliasPre + "`" + fieldName + "`" + rest;
        }
        
        return tableAliasPre + condition;
    }

    /**
     * 查找 SQL 操作符的位置（=, >, <, >=, <=, !=, LIKE, IN 等）
     */
    private int findOperatorIndex(String condition) {
        String upperCondition = condition.toUpperCase();
        
        // 检查各种操作符的位置
        int[] positions = {
            condition.indexOf("!="),
            condition.indexOf(">="),
            condition.indexOf("<="),
            condition.indexOf(">"),
            condition.indexOf("<"),
            condition.indexOf("="),
            upperCondition.indexOf(" LIKE "),
            upperCondition.indexOf(" NOT LIKE "),
            upperCondition.indexOf(" IN "),
            upperCondition.indexOf(" NOT IN "),
            upperCondition.indexOf(" IS "),
            upperCondition.indexOf(" IS NOT ")
        };
        
        // 找到最小的有效位置（大于0）
        int minPos = Integer.MAX_VALUE;
        for (int pos : positions) {
            if (pos > 0 && pos < minPos) {
                minPos = pos;
            }
        }
        
        return minPos == Integer.MAX_VALUE ? -1 : minPos;
    }

    private Object getV() {
        Object v;
        try {
            currentField.setAccessible(true);
            v = currentField.get(currentObject);
            currentValue = v;
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResCodeEnum.SYSTEM_ERROR.getCode(), ERR_MSG);
        }
        return v;
    }

    // 解析
    public void parse() {
        // 判断父类
        if (!isCount) {
            Class<?> superclass = object.getClass().getSuperclass();
            if (superclass.equals(BaseCondition.class)) {
                Field[] fields1 = superclass.getDeclaredFields();
                currentObject = object;
                for (Field field : fields1) {
                    currentField = field;
                    dealBaseCondition();
                }
            }
        }
        // 处理普通字段
        Field[] fields = object.getClass().getDeclaredFields();
        currentObject = object;
        for (Field field : fields) {
            currentField = field;
            dealFiled();
        }
    }
}
