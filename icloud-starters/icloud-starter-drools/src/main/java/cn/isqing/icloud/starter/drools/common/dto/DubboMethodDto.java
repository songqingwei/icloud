package cn.isqing.icloud.starter.drools.common.dto;

import lombok.Data;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class DubboMethodDto {
    /** Address */
    private String address;
    /** Interface name */
    private String interfaceName;
    /** Method name */
    private String methodName;
    /** Version */
    private String version;
    /** Group */
    private String group;
    /** Method type */
    private String[] methodType;
    /** Param obj */
    private Object[] paramObj;

}
