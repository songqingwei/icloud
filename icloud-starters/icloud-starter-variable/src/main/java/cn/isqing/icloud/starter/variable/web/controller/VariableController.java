package cn.isqing.icloud.starter.variable.web.controller;

import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.service.variable.VariableService;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableActionsDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping(value = "/${i.variable.web.pre}/variable",produces = MediaType.APPLICATION_JSON_VALUE)
public class VariableController {

    @Autowired
    private VariableService service;

    @PostMapping(value = "/list")
    public Response<PageResDto<VariableDto>> list(@RequestBody PageReqDto<VariableListReq> req){
        if(req.getCondtion().getActionId()!=null){
            return service.listWithAction(req);
        }
        return service.listNoAction(req);
    }

    @PostMapping(value = "/add")
    public Response<Object> add(@RequestBody VariableDto req){
        return service.add(req);
    }

    @PostMapping(value = "/edit")
    public Response<Object> edit(@RequestBody VariableDto req){
        return service.edit(req);
    }

    @PostMapping(value = "/sw")
    public Response<Object> sw(@RequestBody UpdateStatusDto req){
        return service.sw(req);
    }

    @PostMapping(value = "/del")
    public Response<Object> sw(@RequestParam("id") Long id){
        return service.del(id);
    }

    @PostMapping(value = "/associate/action/add")
    public Response<Object> associateAction(@RequestBody VariableActionsDto req){
        return service.associateAction(req);
    }


}
