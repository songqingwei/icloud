package cn.isqing.icloud.starter.drools.service.component.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.starter.drools.common.constants.ComponentTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.VariableConstants;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.common.util.TextSqlUtil;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import cn.isqing.icloud.starter.drools.dao.entity.ComponentCondition;
import cn.isqing.icloud.starter.drools.dao.entity.ComponentText;
import cn.isqing.icloud.starter.drools.dao.entity.ComponentTextCondition;
import cn.isqing.icloud.starter.drools.dao.mapper.ComponentMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.ComponentTextMapper;
import cn.isqing.icloud.starter.drools.service.component.ComponentService;
import cn.isqing.icloud.starter.drools.service.component.dto.ComponentDto;
import cn.isqing.icloud.starter.drools.service.component.dto.ComponentListReq;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentMapper mapper;

    @Autowired
    private ComponentTextMapper textMapper;

    @Override
    public Response<PageResDto<ComponentDto>> list(PageReqDto<ComponentListReq> dto) {
        ComponentListReq req = dto.getCondition();
        ComponentCondition condition = new ComponentCondition();
        SpringBeanUtils.copyProperties(req, condition);
        condition.setIsDel(YesOrNo.NO.ordinal());
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<ComponentDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            condition.setOffset(pageInfo.getOffset());
            condition.setLimit(pageInfo.getPageSize());

            List<Component> list = mapper.selectByCondition(condition);
            List<ComponentDto> dtoList = list.stream().map(o -> {
                ComponentDto d = new ComponentDto();
                SpringBeanUtils.copyProperties(o, d);
                return d;
            }).collect(Collectors.toList());
            resDto.setList(dtoList);
        }
        if (pageInfo.isNeedTotal()) {
            Long count = mapper.countByCondition(condition);
            resDto.setTotal(count);
        }
        return Response.success(resDto);
    }

    @Override
    public Response<ComponentDto> getText(Long id) {
        ComponentTextCondition condition = new ComponentTextCondition();
        condition.setOrderBy(SqlConstants.ID_ASC);
        condition.setTypeCondition(Arrays.asList(ComponentTextTypeConstants.DEPEND_INPUT_PARAMS,
                ComponentTextTypeConstants.DEPEND_C_RES_NAME, ComponentTextTypeConstants.DIALECT_CONFIG,
                ComponentTextTypeConstants.CONSTANT_PARAMS, ComponentTextTypeConstants.VARIABLE_PARAMS
        ));
        List<ComponentText> texts = textMapper.selectByCondition(condition);
        Map<Integer, String> map = texts.stream().collect(Collectors.groupingBy(ComponentText::getType,
                Collectors.mapping(ComponentText::getText, Collectors.joining())));

        ComponentDto dto = new ComponentDto();
        dto.setDependInputParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_INPUT_PARAMS),
                new TypeReference<Set<String>>() {
                })
        );
        dto.setDependCResName(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_C_RES_NAME),
                new TypeReference<Set<String>>() {
                })
        );
        dto.setDialectConfig(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DIALECT_CONFIG),
                new TypeReference<Map<Integer, Object>>() {
                }));
        dto.setConstantParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.CONSTANT_PARAMS),
                new TypeReference<Map<String, String>>() {
                }));
        dto.setVariableParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.VARIABLE_PARAMS),
                new TypeReference<Map<String, String>>() {
                }));
        dto.setResJudge(JSON.parseArray(map.get(ComponentTextTypeConstants.VARIABLE_PARAMS)).toArray(new String[0]));
        return Response.success(dto);
    }

    @Override
    public Response<Object> add(ComponentDto dto) {
        Component data = new Component();
        SpringBeanUtils.copyProperties(dto, data);
        // 主表入库
        mapper.insert(data);
        // 大字段入库
        insertText(dto, data.getId());
        return Response.SUCCESS;
    }

    private void insertText(ComponentDto dto, Long id) {
        ComponentText text = new ComponentText();
        text.setFid(id);
        Set<Long> dependCid = dto.getDependCResName().stream().map(s -> Long.valueOf(s.substring(1,
                s.indexOf(VariableConstants.DATA_SOURCE_SEPARATOR)))).collect(Collectors.toSet());
        Object[][] arr = {
                {dto.getDependInputParams(), ComponentTextTypeConstants.DEPEND_INPUT_PARAMS},
                {dto.getDependCResName(), ComponentTextTypeConstants.DEPEND_C_RES_NAME},
                {dependCid, ComponentTextTypeConstants.DEPEND_CID},
                {dto.getDialectConfig(), ComponentTextTypeConstants.DIALECT_CONFIG},
                {dto.getConstantParams(), ComponentTextTypeConstants.CONSTANT_PARAMS},
                {dto.getVariableParams(), ComponentTextTypeConstants.VARIABLE_PARAMS},
                {dto.getResJudge(), ComponentTextTypeConstants.RES_JUDGE}
        };
        for (Object[] arr1 : arr) {
            insertText(text,arr1[0],(int)arr1[1]);
        }
    }

    private void insertText(ComponentText text, Object o, int type) {
        TextSqlUtil.insertText(textMapper, text, o, t -> ((ComponentText) t).setType(type), s -> {
            text.setId(null);
            text.setText(s);
        });
    }

    @Override
    public Response<Object> edit(ComponentDto dto) {
        Component data = new Component();
        SpringBeanUtils.copyProperties(dto, data);
        // 主表入库
        mapper.update(data);
        // 大字段更新
        ComponentText commonText = new ComponentText();
        commonText.setFid(dto.getId());
        textMapper.del(commonText);
        insertText(dto, dto.getId());
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> sw(UpdateStatusDto dto) {
        Component data = new Component();
        Component condition = new Component();
        data.setVersion(dto.getVersion() + 1);
        data.setIsActive(dto.getStatus());
        condition.setId(dto.getId());
        condition.setVersion(dto.getVersion());
        // 更新
        int i = mapper.updateByCondition(data, condition);
        if (i == 0) {
            return Response.ERROR;
        }
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> del(Long id) {
        Component data = new Component();
        data.setId(id);
        data.setIsDel(YesOrNo.YES.toValue());
        // 更新
        int i = mapper.update(data);
        if (i == 0) {
            return Response.ERROR;
        }
        return Response.SUCCESS;
    }
}
