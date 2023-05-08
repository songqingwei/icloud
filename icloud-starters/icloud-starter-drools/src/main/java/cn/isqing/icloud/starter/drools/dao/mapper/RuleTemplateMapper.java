package cn.isqing.icloud.starter.drools.dao.mapper;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.starter.drools.dao.entity.RuleTemplate;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RuleTemplateMapper extends BaseMapper<RuleTemplate> {

    @Select("<script>\n" +
            "    select \n" +
            "    r.* \n" +
            "    from rule_template r\n" +
            "    <if test=\"busiCodeList !=null\">\n" +
            "    left join rule_template_busi b on r.id=t.tid\n" +
            "    </if>\n" +
            "    <where>\n" +
            "        <if test=\"pageInfo.fromId !=null\">and r.id>#{pageInfo.fromId} </if>\n" +
            "        <if test=\"req.busiCodeList !=null\">\n" +
            "            <foreach collection=\"req.busiCode\" item=\"busiCode\" open=\"and t.busi_code in (\" " +
            "close=\")\" separator=\",\">\n" +
            "                #{busiCode}\n" +
            "            </foreach>\n" +
            "    " +
            "    </if>\n" +
            "        <if test=\"name !=null\">and r.name like #{req.name}</if>\n" +
            "        <if test=\"domain !=null\">and r.domain = #{req.domain}</if>\n" +
            "        <if test=\"createTimeMin !=null\">and r.create_time >= #{req.createTimeMin}</if>\n" +
            "        <if test=\"createTimeMax !=null\">and r.create_time &lt;= " +
            "#{req.createTimeMax}</if>\n" +
            "        and r.is_del = 0\n" +
            "    </where>\n" +
            "    limit #{page.pageSize}\n" +
            "    offset #{offset}\n" +
            "    <if test=\"busiCodeList !=null\">\n" +
            "        group by b.tid\n" +
            "    </if>\n" +
            "</script>")
    List<RuleTemplate> selectWithBusi(@Param("req") RuleTemplateListReq req, @Param("page")PageReqDto.PageInfo pageInfo,@Param("offset") Long offset);

    @Select("<script>\n" +
            "    <if test=\"busiCodeList !=null\">select count(1) from (</if>\n" +
            "        select\n" +
            "        <if test=\"busiCodeList !=null\">r.id</if>\n" +
            "        <if test=\"busiCodeList !=null\">count(1)</if>\n" +
            "        from rule_template r\n" +
            "        <if test=\"busiCodeList !=null\">\n" +
            "            left join rule_template_busi b on r.id=t.tid\n" +
            "        </if>\n" +
            "        <where>\n" +
            "            <if test=\"pageInfo.fromId !=null\">and r.id>#{pageInfo.fromId} </if>\n" +
            "            <if test=\"busiCodeList !=null\">\n" +
            "                <foreach collection=\"busiCode\" item=\"busiCode\" open=\"and t.busi_code in (\" " +
            "close=\")\" separator=\",\">\n" +
            "                    #{busiCode}\n" +
            "                </foreach>\n" +
            "    " +
            "        </if>\n" +
            "            <if test=\"name !=null\">and r.name like #{name}</if>\n" +
            "            <if test=\"domain !=null\">and r.domain = #{domain}</if>\n" +
            "            <if test=\"createTimeMin !=null\">and r.create_time >= #{createTimeMin}</if>\n" +
            "            <if test=\"createTimeMax !=null\">and r.create_time &lt;= " +
            "#{createTimeMax}</if>\n" +
            "            and r.is_del = 0\n" +
            "        </where>\n" +
            "        <if test=\"busiCodeList !=null\">\n" +
            "            group by b.tid\n" +
            "        </if>\n" +
            "    <if test=\"busiCodeList !=null\">) a (</if>\n" +
            "</script>")
    Long countWithBusi(RuleTemplateListReq req);
}
