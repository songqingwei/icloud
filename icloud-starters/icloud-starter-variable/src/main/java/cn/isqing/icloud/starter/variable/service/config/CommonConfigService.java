package cn.isqing.icloud.starter.variable.service.config;

import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.variable.service.config.dto.CommonConfigDto;
import cn.isqing.icloud.starter.variable.service.config.dto.CommonConfigListReq;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface CommonConfigService {
    Response<PageResDto<CommonConfigDto>> list(PageReqDto<CommonConfigListReq> dto);

    Response<Object> add(CommonConfigDto dto);

    Response<Object> edit(CommonConfigDto dto);

    Response<Object> del(Long id);
}
