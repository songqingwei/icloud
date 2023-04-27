package cn.isqing.icloud.starter.variable.service.variable.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dao.MybatisUtils;
import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.common.utils.validation.group.AddGroup;
import cn.isqing.icloud.common.utils.validation.group.EditGroup;
import cn.isqing.icloud.starter.variable.common.constants.TableJoinConstants;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.dao.entity.*;
import cn.isqing.icloud.starter.variable.dao.mapper.ActionVariableMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.VariableMapper;
import cn.isqing.icloud.starter.variable.service.variable.VariableService;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableActionsDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableListReq;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Validated
public class VariableServiceImpl implements VariableService {

    @Autowired
    private VariableMapper mapper;
    @Autowired
    private ActionVariableMapper avMapper;
    @Resource(name = "iVariableSqlSessionFactory")
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Response<PageResDto<VariableDto>> listWithAction(PageReqDto<VariableListReq> dto) {
        VariableListReq req = dto.getCondition();
        ActionVariableCondition left = new ActionVariableCondition();
        left.setActionId(req.getActionId());
        left.setOrderBy(SqlConstants.ID_ASC);
        left.setSelectFiled(ActionVariableFiled.ACTION_ID);

        VariableCondition right = new VariableCondition();
        SpringBeanUtils.copyProperties(req,right);
        right.setSelectFiled(SqlConstants.ALL_FIELD);

        PageReqDto.PageInfo pageInfo = dto.getPageInfo();

        PageResDto<VariableDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            //组装结果dto
            left.setLimit(pageInfo.getPageSize());
            left.setOffset(pageInfo.getOffset());
            List<VariableDto> dtoList = avMapper.leftJoinSelect(left,right, TableJoinConstants.ACTION_VARIABLE);
            resDto.setList(dtoList);
        }
        if (pageInfo.isNeedTotal()) {
            Long count = avMapper.leftJoinCount(left,right, TableJoinConstants.ACTION_VARIABLE);
            resDto.setTotal(count);
        }
        return Response.success(resDto);
    }

    @Override
    public Response<PageResDto<VariableDto>> listNoAction(PageReqDto<VariableListReq> dto) {
        VariableListReq req = dto.getCondition();
        VariableCondition condition = new VariableCondition();
        SpringBeanUtils.copyProperties(req,condition);
        condition.setIsDel(YesOrNo.NO.ordinal());
        PageReqDto.PageInfo pageInfo = dto.getPageInfo();
        PageResDto<VariableDto> resDto = new PageResDto<>();
        if (pageInfo.isNeedList()) {
            //组装结果dto
            condition.setLimit(pageInfo.getPageSize());
            condition.setOffset(pageInfo.getOffset());
            List<Variable> dtoList = mapper.selectByCondition(condition);
            resDto.setList(dtoList.stream().map(v->{
                VariableDto dto1 = new VariableDto();
                SpringBeanUtils.copyProperties(v,dto1);
                return dto1;
            }).collect(Collectors.toList()));
        }
        if (pageInfo.isNeedTotal()) {
            Long count = mapper.countByCondition(condition);
            resDto.setTotal(count);
        }
        return Response.success(resDto);
    }

    @Override
    public Response<Object> add(@Validated({AddGroup.class, Default.class}) VariableDto dto) {
        Variable variable = new Variable();
        SpringBeanUtils.copyProperties(dto, variable);
        LocalDateTime now = TimeUtil.now();
        variable.setCreateTime(now);
        variable.setUpdateTime(now);
        // 主表入库
        mapper.insert(variable);
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> edit(@Validated({EditGroup.class, Default.class}) VariableDto dto) {
        Variable variable = new Variable();
        SpringBeanUtils.copyProperties(dto, variable);
        LocalDateTime now = TimeUtil.now();
        variable.setUpdateTime(now);
        // 主表入库
        mapper.update(variable);
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> sw(UpdateStatusDto dto) {
        Variable variable = new Variable();
        Variable condition = new Variable();
        variable.setVersion(dto.getVersion() + 1);
        variable.setIsActive(dto.getStatus());
        LocalDateTime now = TimeUtil.now();
        variable.setUpdateTime(now);

        condition.setId(dto.getId());
        condition.setVersion(dto.getVersion());

        // 更新
        int i = mapper.updateByCondition(variable, condition);
        if (i == 0) {
            return Response.ERROR;
        }
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> del(Long id) {
        ActionVariable actionVariable = new ActionVariable();
        actionVariable.setVid(id);
        avMapper.del(actionVariable);

        Variable variable = new Variable();
        variable.setId(id);
        variable.setIsDel(YesOrNo.YES.toValue());
        LocalDateTime now = TimeUtil.now();
        variable.setUpdateTime(now);
        // 更新
        int i = mapper.update(variable);
        if (i == 0) {
            return Response.ERROR;
        }
        return Response.SUCCESS;
    }

    @Override
    public Response<Object> associateAction(VariableActionsDto dto) {
        Long vid = dto.getId();
        ActionVariable actionVariable = new ActionVariable();
        actionVariable.setVid(vid);
        avMapper.del(actionVariable);

        if(dto.getActions()!=null && !dto.getActions().isEmpty()){
            List<ActionVariable> list = dto.getActions().stream().map(actionId -> {
                ActionVariable av = new ActionVariable();
                av.setVid(vid);
                av.setActionId(actionId);
                return av;
            }).collect(Collectors.toList());
            MybatisUtils.batchSave(sqlSessionFactory, list, avMapper.getClass(), (busi, mapper) -> mapper.insert(busi));
        }
        return Response.SUCCESS;
    }
}
