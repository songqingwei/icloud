package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.starter.drools.dao.entity.RuleTemplate;
import cn.isqing.icloud.starter.drools.dao.entity.RuleTemplateBusi;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleTemplateBusiMapper extends BaseMapper<RuleTemplateBusi> {

    @Select("select  * from rule_template_busi " +
            "b left join rule_template r on b.tid=r.id where " +
            "r.id>#{from} and r.domain=#{t.domain} and r.action_id=#{t.actionId} and r.is_del=0 and r.is_active=1 and b.busi_code=#{t.busi_code} limit ${limit}")
    List<RuleTemplate> getScrollListByTplChangeMsg(@Param("t") TplChangeMsg t, @Param("from") Long from , @Param("limit") Integer limit, @Param("order") String order);



}
