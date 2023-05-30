package cn.isqing.icloud.starter.variable.service.component.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.starter.variable.common.constants.ComponentTextTypeConstants;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.common.util.TextSqlUtil;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentCondition;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentText;
import cn.isqing.icloud.starter.variable.dao.entity.ComponentTextCondition;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentTextMapper;
import cn.isqing.icloud.starter.variable.service.component.ComponentService;
import cn.isqing.icloud.starter.variable.service.component.dto.ComponentDto;
import cn.isqing.icloud.starter.variable.service.component.dto.ComponentListReq;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        condition.setFid(id);
        List<ComponentText> texts = textMapper.selectByCondition(condition);
        Map<Integer, String> map = texts.stream().collect(Collectors.groupingBy(ComponentText::getType,
                Collectors.mapping(ComponentText::getText, Collectors.joining())));

        ComponentDto dto = new ComponentDto();
        dto.setDialectConfig(map.get(ComponentTextTypeConstants.DIALECT_CONFIG));
        dto.setDependCids(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_CIDS),
                new TypeReference<Set<String>>() {
                })
        );
        dto.setDependInputParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_INPUT_PARAMS),
                new TypeReference<Map<String,String>>() {
                })
        );
        dto.setDependCRes(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_C_RES),
                new TypeReference<Map<String,String>>() {
                })
        );
        dto.setDependConstants(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_CONSTANTS),
                new TypeReference<Map<String,String>>() {
                })
        );
        dto.setDependSystemVars(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_SYSTEM_VARS),
                new TypeReference<Map<String,String>>() {
                })
        );
        dto.setSelfConstants(JSONObject.parseObject(map.get(ComponentTextTypeConstants.SELF_CONSTANTS),
                new TypeReference<Map<String,String>>() {
                })
        );
        dto.setResJudge(JSON.parseArray(map.get(ComponentTextTypeConstants.RES_JUDGE )).toArray(new String[0]));
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
        Object[][] arr = {
                {dto.getDialectConfig(), ComponentTextTypeConstants.DIALECT_CONFIG},
                {dto.getDependCids(), ComponentTextTypeConstants.DEPEND_CIDS},
                {dto.getDependInputParams(), ComponentTextTypeConstants.DEPEND_INPUT_PARAMS},
                {dto.getDependCRes(), ComponentTextTypeConstants.DEPEND_C_RES},
                {dto.getDependConstants(), ComponentTextTypeConstants.DEPEND_CONSTANTS},
                {dto.getSelfConstants(), ComponentTextTypeConstants.SELF_CONSTANTS},
                {dto.getDependSystemVars(), ComponentTextTypeConstants.DEPEND_SYSTEM_VARS},
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
