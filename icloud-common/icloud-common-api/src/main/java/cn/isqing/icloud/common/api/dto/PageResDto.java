package cn.isqing.icloud.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResDto<T> implements Serializable {

    private Long total = 0L;

    private List<T> list = Collections.emptyList();

    public static final PageResDto EMPTY_DTO = new PageResDto<>();

}
