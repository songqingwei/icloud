package cn.isqing.icloud.starter.drools.service.template.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dao.MybatisUtils;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.drools.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.drools.common.dto.RuleH5Dto;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.common.enums.VariableType;
import cn.isqing.icloud.starter.drools.common.util.TextSqlUtil;
import cn.isqing.icloud.starter.drools.dao.entity.*;
import cn.isqing.icloud.starter.drools.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateBusiMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateMapper;
import cn.isqing.icloud.starter.drools.service.event.EventPublisher;
import cn.isqing.icloud.starter.drools.service.msg.MsgParserService;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.drools.service.template.RuleTemplateService;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateDto;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.VariableDto;
import cn.isqing.icloud.starter.variable.api.util.VariableUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Validated
@Slf4j
public class RuleTemplateServiceImpl implements RuleTemplateService {

    @Autowired
    private RuleTemplateMapper mapper;
    @Autowired
    private CommonTextMapper textMapper;
    @Autowired
    private MsgParserService msgParserService;
    @Autowired
    private RuleTemplateBusiMapper busiMapper;

    @Autowired
    private EventPublisher eventPublisher;

    @Resource(name = "iDroolsSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private VariableInterface variableInterface;

    private String format = "(%s)";
    private String blank = " ";

    /**
     * 不查询规则大文本
     *
     * @param dto
     * @return
     */
    @Override
    public Response<PageResDto<RuleTemplateDto>> list(@Valid PageReqDto<RuleTemplateListReq> dto) {
        RuleTemplateListReq req = dto.getCondtion();
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<RuleTemplateDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            //组装结果dto
            List<RuleTemplateDto> dtoList = getDtoList(req, pageInfo);
            resDto.setList(dtoList);
        }
        if (pageInfo.isNeedTotal()) {
            Long count = mapper.countWithBusi(req);
            resDto.setTotal(count);
        }
        return Response.success(resDto);
    }

    /**
     * 不查询规则大文本和关联业务
     *
     * @param dto
     * @return
     */
    @Override
    public Response<PageResDto<RuleTemplateDto>> baseList(PageReqDto<RuleTemplateListReq> dto) {
        RuleTemplateListReq req = dto.getCondtion();
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<RuleTemplateDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            //组装结果dto
            List<RuleTemplate> res = mapper.selectWithBusi(req, pageInfo, pageInfo.getOffset());
            List<RuleTemplateDto> dtoList = res.stream().map(t -> {
                RuleTemplateDto dto1 = new RuleTemplateDto();
                SpringBeanUtils.copyProperties(t, dto1);
                return dto1;
            }).collect(Collectors.toList());
            resDto.setList(dtoList);
        }
        if (pageInfo.isNeedTotal()) {
            Long count = mapper.countWithBusi(req);
            resDto.setTotal(count);
        }
        return Response.success(resDto);
    }

    @Override
    public Response<RuleTemplateDto> getText(Long id) {
        CommonTextCondition condition = new CommonTextCondition();
        condition.setFid(id);
        condition.setTypeCondition(Arrays.asList(CommonTextTypeConstants.TARGET_RATIO,
                CommonTextTypeConstants.TARGET_NAME,
                CommonTextTypeConstants.RULE_CONTENT_H5));
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<CommonText> texts = textMapper.selectByCondition(condition);
        Map<Integer, String> map = texts.stream().collect(Collectors.groupingBy(CommonText::getType,
                Collectors.mapping(CommonText::getText, Collectors.joining())));
        RuleTemplateDto dto = new RuleTemplateDto();
        dto.setTargetRatio(map.get(CommonTextTypeConstants.TARGET_RATIO));
        dto.setTargetName(map.get(CommonTextTypeConstants.TARGET_NAME));
        dto.setContent(map.get(CommonTextTypeConstants.RULE_CONTENT_H5));
        return Response.success(dto);
    }

    /**
     * 不查询大文本和关联业务
     *
     * @param id
     * @return
     */
    @Override
    public Response<RuleTemplateDto> baseInfo(Long id) {
        RuleTemplate ruleTemplate = mapper.selectById(id, RuleTemplate.class);
        RuleTemplateDto dto = new RuleTemplateDto();
        SpringBeanUtils.copyProperties(ruleTemplate, dto);
        return Response.success(dto);
    }

    @Override
    public Response<Map<String, String>> getBusi(Long id) {
        RuleTemplateBusiCondition busiCondition = new RuleTemplateBusiCondition();
        busiCondition.setTid(id);
        List<RuleTemplateBusi> resBusi = busiMapper.selectByCondition(busiCondition);

        Map<String, String> busiMap =
                resBusi.stream().collect(Collectors.toMap(RuleTemplateBusi::getBusiCode, b -> b.getBusiName()));
        return Response.success(busiMap);
    }

    private List<RuleTemplateDto> getDtoList(RuleTemplateListReq req, PageReqDto.PageInfo pageInfo) {
        List<RuleTemplate> res = mapper.selectWithBusi(req, pageInfo, pageInfo.getOffset());
        RuleTemplateBusiCondition busiCondition = new RuleTemplateBusiCondition();
        busiCondition.setTidCondtion(res.stream().map(RuleTemplate::getId).collect(Collectors.toList()));
        List<RuleTemplateBusi> resBusi = busiMapper.selectByCondition(busiCondition);

        Map<Long, Map<String, String>> busiMap =
                resBusi.stream().collect(Collectors.groupingBy(RuleTemplateBusi::getTid,
                        Collectors.toMap(RuleTemplateBusi::getBusiCode, b -> b.getBusiName())));

        List<RuleTemplateDto> dtoList = res.stream().map(t -> {
            RuleTemplateDto dto1 = new RuleTemplateDto();
            SpringBeanUtils.copyProperties(t, dto1);
            dto1.setBusiMap(busiMap.get(t.getId()));
            return dto1;
        }).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    @Transactional
    public Response<Object> add(@Validated({AddGroup.class, Default.class}) RuleTemplateDto dto) {
        RuleTemplate template = new RuleTemplate();
        SpringBeanUtils.copyProperties(dto, template);
        // 主表入库
        mapper.insert(template);
        // 大字段入库
        insertText(dto, template.getId());
        // 关联表入库
        Long tid = template.getId();
        List<RuleTemplateBusi> list = new ArrayList<>();
        Map<String, String> busiMap = dto.getBusiMap();
        busiMap.forEach((k, v) -> {
            RuleTemplateBusi busi = new RuleTemplateBusi();
            busi.setTid(tid);
            busi.setBusiCode(k);
            busi.setBusiName(v);
            list.add(busi);
        });
        MybatisUtils.batchSave(sqlSessionFactory, list, busiMapper.getClass(), (busi, mapper) -> {
            mapper.insert(busi);
        });
        tplChangeEvent(dto, null);
        return Response.SUCCESS;
    }


    private void insertText(RuleTemplateDto dto, Long id) {
        CommonText text = new CommonText();
        text.setFid(id);

        insetText(text, dto.getTargetRatio(), CommonTextTypeConstants.TARGET_RATIO);
        insetText(text, dto.getTargetName(), CommonTextTypeConstants.TARGET_NAME);
        insetText(text, dto.getContent(), CommonTextTypeConstants.RULE_CONTENT_H5);

        // 解析h5规则
        RuleH5Dto h5Dto = JSON.parseObject(dto.getContent(), RuleH5Dto.class);
        Map<Long, VariableDto> map = new HashMap<>();
        String content = dealH5Dto(h5Dto, map);
        insetText(text, content, CommonTextTypeConstants.RULE_CONTENT);

        Map<String, VariableDto> map1 =
                map.entrySet().stream().collect(Collectors.toMap(a -> VariableUtil.getUniName(a.getValue()), Map.Entry::getValue));
        insetText(text, map1, CommonTextTypeConstants.RULE_VARIABLE_MAP);
    }

    private void insetText(CommonText text, Object o, int type) {
        TextSqlUtil.insertText(textMapper, text, o, t -> ((CommonText) t).setType(type), s -> {
            text.setId(null);
            text.setText(s);
        });
    }

    private String dealH5Dto(RuleH5Dto h5Dto, Map<Long, VariableDto> map) {
        List<RuleH5Dto.RuleFieldDto> list = h5Dto.getList();
        if (!CollectionUtils.isEmpty(list)) {
            // String.format(format)
            return list.stream().map(d -> {
                // 字符串类型时要求前端加引号{"value1":"\"null\"","value2":"null",}
                // value1是字符串null，value2是真null
                VariableDto variable = map.get(d.getId());
                if (variable == null) {
                    Response<VariableDto> res = variableInterface.getVariableById(d.getId());
                    map.put(d.getId(), res.getData());
                }

                if (variable.getTypePath().equals(VariableType.BIG_DECIMAL.getName())) {
                    if (!"null".equals(d.getValue()) && (!d.getValue().startsWith("\"")) || !d.getValue().endsWith(
                            "\"")) {
                        log.error("异常字符串条件:{}", d);
                        throw new BaseException("异常字符串条件");
                    }
                } else {
                    if (d.getValue().contains("eval")) {
                        log.error("检测到异常入侵条件:{}", d);
                        throw new BaseException("检测到异常入侵条件");
                    }
                }
                if (variable.getTypePath().equals(VariableType.BIG_DECIMAL.getName()) || variable.getTypePath().equals(VariableType.BIG_INTEGER.getName())) {
                    if (!"null".equals(d.getValue())) {
                        d.setValue(d.getValue() + "B");
                    }
                }
                return VariableUtil.getUniName(variable) + blank + d.getOperator() + blank + d.getValue();
            }).collect(Collectors.joining(blank + h5Dto.getRelation() + blank));
        }
        List<RuleH5Dto> h5DtoList = h5Dto.getGrouplist();
        String collect =
                h5DtoList.stream().map(d -> dealH5Dto(d, map)).collect(Collectors.joining(blank + h5Dto.getRelation() + blank));

        return String.format(format, collect);
    }

    @Override
    @Transactional
    public Response<Object> edit(@Validated({EditGroup.class, Default.class}) RuleTemplateDto dto) {
        RuleTemplate template = new RuleTemplate();
        RuleTemplate condition = new RuleTemplate();
        SpringBeanUtils.copyProperties(dto, template);
        condition.setId(template.getId());
        template.setId(null);
        condition.setVersion(template.getVersion());
        template.setVersion(template.getVersion() + 1);
        // 主表入库
        int i = mapper.updateByCondition(template, condition);
        if (i == 0) {
            return Response.ERROR;
        }
        updateText(dto);

        RuleTemplateBusi busiCondition = new RuleTemplateBusi();
        busiCondition.setTid(dto.getId());
        busiMapper.del(busiCondition);
        // 关联业务查询
        RuleTemplateBusiCondition condition1 = new RuleTemplateBusiCondition();
        condition1.setTid(template.getId());
        condition1.setSelectFiled(RuleTemplateBusiFiled.BUSI_CODE);
        List<String> codes = busiMapper.selectStringByCondition(condition1);
        // 关联表入库
        Long tid = template.getId();
        List<RuleTemplateBusi> list = new ArrayList<>();
        Map<String, String> busiMap = dto.getBusiMap();
        busiMap.forEach((k, v) -> {
            RuleTemplateBusi busi = new RuleTemplateBusi();
            busi.setTid(tid);
            busi.setBusiCode(k);
            busi.setBusiName(v);
            busi.setVersion(template.getVersion());
            list.add(busi);
        });
        MybatisUtils.batchSave(sqlSessionFactory, list, busiMapper.getClass(), (busi, mapper) -> mapper.insert(busi));

        tplChangeEvent(dto, codes);
        return Response.SUCCESS;
    }

    private void updateText(RuleTemplateDto dto) {
        CommonText condition = new CommonText();
        condition.setFid(dto.getId());
        textMapper.delByCondition(condition);
        insertText(dto, dto.getId());
    }

    @Override
    @Transactional
    public Response<Object> sw(@Valid UpdateStatusDto dto) {
        RuleTemplate template = new RuleTemplate();
        RuleTemplate condition = new RuleTemplate();
        template.setVersion(dto.getVersion() + 1);
        template.setIsActive(dto.getStatus());
        condition.setId(dto.getId());
        condition.setVersion(dto.getVersion());
        // 更新
        int i = mapper.updateByCondition(template, condition);
        if (i == 0) {
            return Response.ERROR;
        }
        tplChangeEvent(dto.getId());
        return Response.SUCCESS;
    }

    private void tplChangeEvent(Long id) {
        RuleTemplate ruleTemplate = mapper.selectById(id, RuleTemplate.class);
        RuleTemplateBusiCondition busi = new RuleTemplateBusiCondition();
        busi.setTid(id);
        busi.setSelectFiled(RuleTemplateBusiFiled.BUSI_CODE);
        List<String> codes = busiMapper.selectStringByCondition(busi);
        TplChangeMsg msg = new TplChangeMsg();
        msg.setActionId(ruleTemplate.getActionId());
        msg.setDomain(ruleTemplate.getDomain());
        msg.setCreateTime(TimeUtil.now());
        codes.forEach(k -> {
            msg.setBusiCode(k);
            eventPublisher.publishBcEvent(null, EventTypeConstants.TPL_CHANGE,msg);
        });
    }

    private void tplChangeEvent(RuleTemplateDto dto, List<String> oldCodes) {
        TplChangeMsg msg = new TplChangeMsg();
        msg.setActionId(dto.getActionId());
        msg.setDomain(dto.getDomain());
        msg.setCreateTime(TimeUtil.now());
        Map<String, String> busiMap = dto.getBusiMap();
        busiMap.forEach((k, v) -> {
            msg.setBusiCode(k);
            eventPublisher.publishBcEvent(null, EventTypeConstants.TPL_CHANGE,msg);
        });
        Optional.ofNullable(oldCodes).ifPresent(codes->{
            codes.forEach(k->{
                if(busiMap.containsKey(k)){
                    return;
                }
                msg.setBusiCode(k);
                eventPublisher.publishBcEvent(null, EventTypeConstants.TPL_CHANGE,msg);
            });
        });
    }

    @Override
    @Transactional
    public Response<Object> del(Long id) {
        RuleTemplate template = new RuleTemplate();
        template.setId(id);
        template.setIsDel(YesOrNo.YES.toValue());
        // 更新
        int i = mapper.update(template);
        if (i == 0) {
            return Response.ERROR;
        }
        tplChangeEvent(id);
        return Response.SUCCESS;
    }
}
