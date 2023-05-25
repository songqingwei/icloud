package cn.isqing.icloud.starter.drools.service.action.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.dao.entity.Action;
import cn.isqing.icloud.starter.drools.dao.entity.ActionCondition;
import cn.isqing.icloud.starter.drools.dao.mapper.ActionMapper;
import cn.isqing.icloud.starter.drools.service.action.ActionService;
import cn.isqing.icloud.starter.drools.service.action.dto.ActionDto;
import cn.isqing.icloud.starter.drools.service.action.dto.ActionListReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionMapper mapper;

    @Override
    public Response<PageResDto<ActionDto>> list(PageReqDto<ActionListReq> dto) {
        ActionListReq req = dto.getCondition();
        ActionCondition condition = new ActionCondition();
        SpringBeanUtils.copyProperties(req, condition);
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<ActionDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            condition.setOffset(pageInfo.getOffset());
            condition.setLimit(pageInfo.getPageSize());

            List<Action> list = mapper.selectByCondition(condition);
            List<ActionDto> dtoList = list.stream().map(o -> {
                ActionDto d = new ActionDto();
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
    public Response<Object> add(ActionDto dto) {
        Action data = new Action();
        SpringBeanUtils.copyProperties(dto, data);
        // 主表入库
        mapper.insert(data);
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> edit(ActionDto dto) {
        Action data = new Action();
        SpringBeanUtils.copyProperties(dto, data);
        Optional.ofNullable(dto.getVersion()).ifPresent(v->{
            data.setVersion(v+1);
        });
        // 主表入库
        mapper.update(data);
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> sw(UpdateStatusDto dto) {
        Action data = new Action();
        Action condition = new Action();
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
        mapper.delById(id,Action.class);
        return Response.SUCCESS;
    }
}
