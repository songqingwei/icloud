package cn.isqing.icloud.common.utils.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class BaseCondition implements Serializable {

    private Object groupBy;
    private Object selectFiled;
    private Object orderBy;
    private Integer limit;
    private Long offset;

    public void setSelectFiled(Object conditionSelectFiled) {
        this.selectFiled = conditionSelectFiled;
    }

    public void setSelectFiled(String... filed) {
        this.selectFiled = filed;
    }

}
