package cn.isqing.icloud.common.utils.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础查询条件类
 * 包含查询条件和分页信息
 * 
 * 设计理念：
 * 1. Condition 作为 SQL 查询的完整参数对象
 * 2. 包含 WHERE 条件字段 + LIMIT/OFFSET 分页字段
 * 3. BaseProvider.selectByCondition() 会自动读取所有非空字段生成 SQL
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 */
@Data
public class BaseCondition implements Serializable {

    /**
     * 分组字段
     */
    private Object groupBy;
    
    /**
     * 查询字段
     */
    private Object selectFiled;
    
    /**
     * 排序字段
     */
    private Object orderBy;
    
    /**
     * 限制条数（分页）
     */
    private Integer limit;
    
    /**
     * 偏移量（分页）
     */
    private Long offset;

    /**
     * 自定义 AND 条件列表
     * 每个元素是一个完整的 SQL 条件表达式，会自动追加表别名前缀（如果有）
     * 例如: ["status = 1", "age > 18"] -> WHERE status = 1 AND age > 18
     */
    private java.util.List<String> andConditions;

    /**
     * 自定义 OR 条件列表
     * 每个元素是一个完整的 SQL 条件表达式，会自动追加表别名前缀（如果有）
     * 这些条件会以 OR 方式连接，并整体用括号包裹后与其他条件进行 AND 连接
     * 例如: ["type = 'A'", "type = 'B'"] -> WHERE (type = 'A' OR type = 'B')
     */
    private java.util.List<String> orConditions;

    public void setSelectFiled(Object conditionSelectFiled) {
        this.selectFiled = conditionSelectFiled;
    }

    public void setSelectFiled(String... filed) {
        this.selectFiled = filed;
    }

}
