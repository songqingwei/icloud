package cn.isqing.icloud.starter.variable.service.config.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.variable.dao.entity.CommonConfig;
import cn.isqing.icloud.starter.variable.dao.entity.CommonConfigCondition;
import cn.isqing.icloud.starter.variable.dao.entity.CommonText;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonConfigMapper;
import cn.isqing.icloud.starter.variable.service.config.CommonConfigService;
import cn.isqing.icloud.starter.variable.service.config.dto.CommonConfigDto;
import cn.isqing.icloud.starter.variable.service.config.dto.CommonConfigListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class CommonConfigServiceImpl implements CommonConfigService {

    @Autowired
    private CommonConfigMapper mapper;
    
    @Override
    public Response<PageResDto<CommonConfigDto>> list(PageReqDto<CommonConfigListReq> dto) {
        CommonConfigListReq req = dto.getCondition();
        CommonConfigCondition condition = new CommonConfigCondition();
        SpringBeanUtils.copyProperties(req, condition);
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<CommonConfigDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            condition.setOffset(pageInfo.getOffset());
            condition.setLimit(pageInfo.getPageSize());

            List<CommonConfig> list = mapper.selectByCondition(condition);
            List<CommonConfigDto> dtoList = list.stream().map(o -> {
                CommonConfigDto d = new CommonConfigDto();
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
    public Response<Object> add(CommonConfigDto dto) {
        CommonConfig data = new CommonConfig();
        SpringBeanUtils.copyProperties(dto, data);
        mapper.update(data);
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> edit(CommonConfigDto dto) {
        return null;
    }

    @Override
    public Response<Object> del(Long id) {
        CommonText commonText = new CommonText();
        commonText.setFid(id);
        mapper.delById(id, CommonConfig.class);
        return Response.SUCCESS;
    }
}
