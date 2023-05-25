package cn.isqing.icloud.starter.drools.service.action;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.service.action.dto.ActionDto;
import cn.isqing.icloud.starter.drools.service.action.dto.ActionListReq;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface ActionService {
    Response<PageResDto<ActionDto>> list(PageReqDto<ActionListReq> dto);

    Response<Object> add(ActionDto dto);

    Response<Object> edit(ActionDto dto);

    Response<Object> sw(UpdateStatusDto dto);

    Response<Object> del(Long id);
}
