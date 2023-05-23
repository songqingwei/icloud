package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.starter.drools.dao.entity.RuleTemplate;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RuleTemplateMapper extends BaseMapper<RuleTemplate> {

}
