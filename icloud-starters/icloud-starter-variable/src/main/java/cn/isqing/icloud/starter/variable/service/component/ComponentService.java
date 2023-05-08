package cn.isqing.icloud.starter.variable.service.component;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.service.component.dto.ComponentDto;
import cn.isqing.icloud.starter.variable.service.component.dto.ComponentListReq;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface ComponentService {
    Response<PageResDto<ComponentDto>> list(PageReqDto<ComponentListReq> dto);

    Response<ComponentDto> getText(Long id);

    Response<Object> add(ComponentDto dto);

    Response<Object> edit(ComponentDto dto);

    Response<Object> sw(UpdateStatusDto dto);

    Response<Object> del(Long id);
}
