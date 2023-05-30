package cn.isqing.icloud.starter.drools.service.template.impl;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.constants.StrConstants;
import cn.isqing.icloud.common.utils.dao.MybatisUtils;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.drools.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.TableJoinConstants;
import cn.isqing.icloud.starter.drools.common.dto.RuleH5Dto;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.common.enums.OperatorType;
import cn.isqing.icloud.starter.drools.common.util.TextSqlUtil;
import cn.isqing.icloud.starter.drools.dao.entity.*;
import cn.isqing.icloud.starter.drools.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateBusiMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleTemplateMapper;
import cn.isqing.icloud.starter.drools.service.event.EventPublisher;
import cn.isqing.icloud.starter.drools.service.event.impl.RuleTemplateChangeFlow;
import cn.isqing.icloud.starter.drools.service.msg.MsgParserService;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.drools.service.template.RuleTemplateService;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateDto;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import cn.isqing.icloud.starter.variable.api.enums.VariableType;
import cn.isqing.icloud.starter.variable.api.util.VariableUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
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
    private RuleTemplateBusiMapper busiMapper;

    @Autowired
    private EventPublisher eventPublisher;

    @Resource(name = "iDroolsSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Reference(group = "${i.variable.dubbo.group:iVariable}", timeout = -1, retries = -1, version = "1.0.0")
    @Lazy
    private VariableInterface variableInterface;

    @Autowired
    private MsgParserService msgParserService;

    @Autowired
    private RuleTemplateChangeFlow changeFlow;

    private String format = "(%s)";
    private String blank = " ";

    /**
     * 不查询规则大文本
     *
     * @param dto
     * @return
     */
    @Override
    public Response<PageResDto<RuleTemplateDto>> list(PageReqDto<RuleTemplateListReq> dto) {
        Response<PageResDto<RuleTemplateDto>> res = baseList(dto);
        if (!res.isSuccess()) {
            return Response.withData(res, null);
        }
        if (dto.getPageInfo().isNeedList()) {
            setBusiMap(res.getData().getList());
        }
        return res;
    }

    /**
     * 不查询规则大文本和关联业务
     *
     * @param dto
     * @return
     */
    @Override
    public Response<PageResDto<RuleTemplateDto>> baseList(PageReqDto<RuleTemplateListReq> dto) {
        RuleTemplateListReq req = dto.getCondition();
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();

        RuleTemplateCondition left = new RuleTemplateCondition();
        SpringBeanUtils.copyProperties(req, left);
        left.setIsDel(YesOrNo.NO.ordinal());
        left.setOrderBy(SqlConstants.ID_ASC);
        left.setSelectFiled(SqlConstants.ALL_FIELD);
        left.setGroupBy(RuleTemplateFiled.ID);
        Optional.ofNullable(pageInfo.getFromId()).ifPresent(left::setIdConditionMin);

        RuleTemplateBusiCondition right = new RuleTemplateBusiCondition();
        right.setBusiCode(req.getBusiCode());


        PageResDto<RuleTemplateDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            //组装结果dto
            left.setLimit(pageInfo.getPageSize());
            left.setOffset(pageInfo.getOffset());
            List<RuleTemplateDto> dtoList = JsonUtil.toList(mapper.leftJoinSelect(left, right, TableJoinConstants.RTPL_BUSI), RuleTemplateDto.class);
            resDto.setList(dtoList);
        }
        if (pageInfo.isNeedTotal()) {
            Long count = mapper.leftJoinCount(left, right, TableJoinConstants.RTPL_BUSI);
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
        dto.setTargetRatio(JSON.parseObject(map.getOrDefault(CommonTextTypeConstants.TARGET_RATIO, StrConstants.EMPTY_JSON_OBJ), new TypeReference<Map<Long, String>>() {
        }));
        dto.setTargetName(JSON.parseObject(map.getOrDefault(CommonTextTypeConstants.TARGET_NAME, StrConstants.EMPTY_JSON_OBJ), new TypeReference<Map<Long, String>>() {
        }));
        dto.setContent(JSON.parseObject(map.getOrDefault(CommonTextTypeConstants.RULE_CONTENT_H5, StrConstants.EMPTY_JSON_OBJ), RuleH5Dto.class));
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

    private void setBusiMap(List<RuleTemplateDto> res) {
        RuleTemplateBusiCondition busiCondition = new RuleTemplateBusiCondition();
        busiCondition.setTidCondition(res.stream().map(RuleTemplateDto::getId).collect(Collectors.toList()));
        List<RuleTemplateBusi> resBusi = busiMapper.selectByCondition(busiCondition);

        Map<Long, Map<String, String>> busiMap =
                resBusi.stream().collect(Collectors.groupingBy(RuleTemplateBusi::getTid,
                        Collectors.toMap(RuleTemplateBusi::getBusiCode, b -> b.getBusiName())));

        res.forEach(d -> d.setBusiMap(busiMap.get(d.getId())));

    }

    @Override
    @Transactional(transactionManager = "iDroolsTransactionManager")
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
        MybatisUtils.batchSave(sqlSessionFactory, list, RuleTemplateBusiMapper.class, (busi, mapper) -> {
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
        RuleH5Dto h5Dto = dto.getContent();
        Map<Long, ApiVariableDto> map = new HashMap<>();
        String content = dealH5Dto(h5Dto, map);
        insetText(text, content, CommonTextTypeConstants.RULE_CONTENT);

        Map<String, ApiVariableDto> map1 =
                map.entrySet().stream().collect(Collectors.toMap(a -> VariableUtil.getUniName(a.getValue()), Map.Entry::getValue));
        insetText(text, map1, CommonTextTypeConstants.RULE_VARIABLE_MAP);
    }

    private void insetText(CommonText text, Object o, int type) {
        TextSqlUtil.insertText(textMapper, text, o, t -> ((CommonText) t).setType(type), s -> {
            text.setId(null);
            text.setText(s);
        });
    }

    private String dealH5Dto(RuleH5Dto h5Dto, Map<Long, ApiVariableDto> map) {
        List<RuleH5Dto.RuleFieldDto> list = h5Dto.getList();
        if (!CollectionUtils.isEmpty(list)) {
            // String.format(format)
            return list.stream().map(d -> {
                if (d.getValue().contains("eval")) {
                    log.error("检测到异常入侵条件:{}", d);
                    throw new BaseException("检测到异常入侵条件");
                }
                // 字符串类型时要求前端加引号{"value1":"\"null\"","value2":"null",}
                // value1是字符串null，value2是真null
                ApiVariableDto variable = map.get(d.getId());
                if (variable == null) {
                    Response<ApiVariableDto> res = variableInterface.getVariableById(d.getId());
                    if (!res.isSuccess()) {
                        log.error("获取变量{}异常:{}", d.getId(), res);
                        throw new BaseException("获取变量异常");
                    }
                    map.put(d.getId(), res.getData());
                    variable = res.getData();
                }

                String left = VariableUtil.getUniName(variable);
                VariableType variableType = VariableType.fromCode(variable.getType());
                String operator = OperatorType.getEnum(d.getOperator().intValue()).getValue();
                switch (variableType){
                    case BIG_DECIMAL:
                        if(!d.getValue().equals("\"null\"")){
                            return left+".compareTo(BigDecimal.valueOf(\""+d.getValue()+"\")) "+operator+" 0";
                        }
                        break;
                    case BIG_INTEGER:
                        if(!d.getValue().equals("\"null\"")){
                            return left+".compareTo(new BigInteger(\""+d.getValue()+"\")) "+operator+" 0";
                        }
                        break;
                    default:
                        break;
                }
                return left + blank + operator + blank + d.getValue();

            }).collect(Collectors.joining(blank + h5Dto.getRelation() + blank));
        }
        List<RuleH5Dto> h5DtoList = h5Dto.getGrouplist();
        String collect =
                h5DtoList.stream().map(d -> dealH5Dto(d, map)).collect(Collectors.joining(blank + h5Dto.getRelation() + blank));

        return String.format(format, collect);
    }

    @Override
    @Transactional(transactionManager = "iDroolsTransactionManager")
    public Response<Object> edit(@Validated({EditGroup.class, Default.class}) RuleTemplateDto dto) {
        RuleTemplate template = new RuleTemplate();
        RuleTemplate condition = new RuleTemplate();
        SpringBeanUtils.copyProperties(dto, template);
        condition.setId(dto.getId());
        template.setId(null);
        condition.setVersion(template.getVersion());
        template.setVersion(template.getVersion() + 1);
        // 主表入库
        int i = mapper.updateByCondition(template, condition);
        if (i == 0) {
            return Response.ERROR;
        }
        updateText(dto);

        // 关联业务查询
        RuleTemplateBusiCondition condition1 = new RuleTemplateBusiCondition();
        condition1.setTid(dto.getId());
        condition1.setSelectFiled(RuleTemplateBusiFiled.BUSI_CODE);
        List<String> codes = busiMapper.selectStringByCondition(condition1);

        // 关联表入库
        RuleTemplateBusi busiCondition = new RuleTemplateBusi();
        busiCondition.setTid(dto.getId());
        busiMapper.del(busiCondition);
        Long tid = dto.getId();
        List<RuleTemplateBusi> list = new ArrayList<>();
        Map<String, String> busiMap = dto.getBusiMap();
        busiMap.forEach((k, v) -> {
            RuleTemplateBusi busi = new RuleTemplateBusi();
            busi.setTid(tid);
            busi.setBusiCode(k);
            codes.add(k);
            busi.setBusiName(v);
            busi.setVersion(template.getVersion());
            list.add(busi);
        });
        MybatisUtils.batchSave(sqlSessionFactory, list, RuleTemplateBusiMapper.class, (busi, mapper) -> mapper.insert(busi));

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
    @Transactional(transactionManager = "iDroolsTransactionManager")
    public Response<Object> sw(UpdateStatusDto dto) {
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
            eventPublisher.publishBcEvent(null, EventTypeConstants.TPL_CHANGE, msg);
        });
    }

    private void tplChangeEvent(RuleTemplateDto dto, List<String> codes) {
        codes = codes.stream().distinct().collect(Collectors.toList());
        TplChangeMsg msg = new TplChangeMsg();
        msg.setActionId(dto.getActionId());
        msg.setDomain(dto.getDomain());
        msg.setCreateTime(TimeUtil.now());
        Optional.ofNullable(codes).ifPresent(c -> {
            c.forEach(k -> {
                msg.setBusiCode(k);
                eventPublisher.publishBcEvent(null, EventTypeConstants.TPL_CHANGE, msg);
            });
        });
    }

    @Override
    @Transactional(transactionManager = "iDroolsTransactionManager")
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
