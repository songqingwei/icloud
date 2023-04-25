package cn.isqing.icloud.starter.drools.service.template;


import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateDto;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;

import java.util.Map;

public interface RuleTemplateService {

    Response<PageResDto<RuleTemplateDto>> list(PageReqDto<RuleTemplateListReq> dto);

    Response<PageResDto<RuleTemplateDto>> baseList(PageReqDto<RuleTemplateListReq> dto);

    Response<RuleTemplateDto> getText(Long id);

    Response<RuleTemplateDto> baseInfo(Long id);

    Response<Map<String,String>> getBusi(Long id);

    Response<Object> add(RuleTemplateDto dto);

    Response<Object> edit(RuleTemplateDto dto);

    Response<Object> sw(UpdateStatusDto dto);

    Response<Object> del(Long id);

}
