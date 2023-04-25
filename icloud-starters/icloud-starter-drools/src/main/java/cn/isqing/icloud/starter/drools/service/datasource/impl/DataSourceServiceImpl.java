package cn.isqing.icloud.starter.drools.service.datasource.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.drools.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.TextConstants;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.dao.entity.CommonText;
import cn.isqing.icloud.starter.drools.dao.entity.CommonTextCondition;
import cn.isqing.icloud.starter.drools.dao.entity.DataSource;
import cn.isqing.icloud.starter.drools.dao.entity.DataSourceCondition;
import cn.isqing.icloud.starter.drools.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.DataSourceMapper;
import cn.isqing.icloud.starter.drools.service.datasource.DataSourceService;
import cn.isqing.icloud.starter.drools.service.datasource.dto.DataSourceDto;
import cn.isqing.icloud.starter.drools.service.datasource.dto.DataSourceListReq;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Validated
@Slf4j
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper mapper;

    @Autowired
    private CommonTextMapper textMapper;

    @Override
    public Response<PageResDto<DataSourceDto>> list(PageReqDto<DataSourceListReq> dto) {
        DataSourceListReq req = dto.getCondtion();
        DataSourceCondition condition = new DataSourceCondition();
        SpringBeanUtils.copyProperties(req, condition);
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<DataSourceDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            condition.setOffset(pageInfo.getOffset());
            condition.setLimit(pageInfo.getPageSize());

            List<DataSource> list = mapper.selectByCondition(condition);
            List<DataSourceDto> dtoList = list.stream().map(o -> {
                DataSourceDto d = new DataSourceDto();
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
    public Response<DataSourceDto> getText(Long id) {
        CommonTextCondition condition = new CommonTextCondition();
        condition.setType(CommonTextTypeConstants.DATA_SOURCE_CINFIG);
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<CommonText> texts = textMapper.selectByCondition(condition);
        String collect = texts.stream().collect(Collectors.mapping(CommonText::getText, Collectors.joining()));

        DataSourceDto dto = new DataSourceDto();
        dto.setConfig(
                JSONObject.parseObject(collect, new TypeReference<Map<String, Object>>() {
                })
        );
        return Response.success(dto);
    }

    @Override
    public Response<Object> add(DataSourceDto dto) {
        DataSource data = new DataSource();
        SpringBeanUtils.copyProperties(dto, data);
        // 主表入库
        mapper.insert(data);
        // 大字段入库
        insertText(dto, data.getId());
        return Response.SUCCESS;
    }

    private void insertText(DataSourceDto dto, Long id) {
        CommonText text = new CommonText();
        text.setFid(id);
        text.setType(CommonTextTypeConstants.DATA_SOURCE_CINFIG);
        String[] strings = JSON.toJSONString(dto.getConfig()).split(TextConstants.REGEX_5000);
        for (String string : strings) {
            text.setId(null);
            text.setText(string);
            textMapper.insert(text);
        }
    }


    @Override
    public Response<Object> edit(DataSourceDto dto) {
        DataSource data = new DataSource();
        SpringBeanUtils.copyProperties(dto, data);
        // 主表入库
        mapper.update(data);
        // 大字段更新
        CommonText commonText = new CommonText();
        commonText.setFid(dto.getId());
        textMapper.del(commonText);
        insertText(dto, dto.getId());
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> sw(UpdateStatusDto dto) {
        DataSource data = new DataSource();
        DataSource condition = new DataSource();
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
        CommonText commonText = new CommonText();
        commonText.setFid(id);
        textMapper.del(commonText);
        mapper.delById(id, DataSource.class);
        return Response.SUCCESS;
    }
}
