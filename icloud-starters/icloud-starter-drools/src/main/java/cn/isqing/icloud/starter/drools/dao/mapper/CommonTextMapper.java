package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.dao.entity.CommonText;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommonTextMapper extends BaseMapper<CommonText> {


    @Select("select t.text from rule_template r " +
            "left join common_text t on t.fid=r.id and t.type=4 where " +
            "r.id>#{from} and r.domain=#{t.domain} and r.action_id=#{t.actionId} and r.is_del=0 and r.is_active=1 and r.busi_code=#{t.busi_code} limit ${limit}")
    List<String> getRuleVariableSet(@Param("t") RuleKeyDto t, @Param("from") Long from , @Param("limit") Integer limit, @Param("order") String order);
}
