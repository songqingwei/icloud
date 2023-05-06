package cn.isqing.icloud.common.api.dto;

import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class PageReqDto<T> implements Serializable {

    private PageInfo pageInfo;

    @Valid
    private T condition;

    @Data
    public static class PageInfo implements Serializable {

        private Integer pageNum = 1;
        private Integer pageSize = 20;
        private boolean needTotal = true;
        private boolean needList = true;

        private Long fromId;

        public Long getOffset(){
            return (long)(pageNum-1)*pageSize;
        }
    }
}
