package cn.isqing.icloud.starter.variable.service.variable;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableActionsDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableListReq;

public interface VariableService {

    Response<PageResDto<VariableDto>> listWithAction(PageReqDto<VariableListReq> dto);

    Response<PageResDto<VariableDto>> listNoAction(PageReqDto<VariableListReq> dto);

    Response<Object> add(VariableDto dto);

    Response<Object> edit(VariableDto dto);

    Response<Object> sw(UpdateStatusDto dto);

    Response<Object> del(Long id);

    Response<Object> associateAction(VariableActionsDto dto);
}
