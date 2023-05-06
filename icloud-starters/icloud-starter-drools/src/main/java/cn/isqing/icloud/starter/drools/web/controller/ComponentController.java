package cn.isqing.icloud.starter.drools.web.controller;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.service.component.ComponentService;
import cn.isqing.icloud.starter.drools.service.component.dto.ComponentDto;
import cn.isqing.icloud.starter.drools.service.component.dto.ComponentListReq;
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
@RequestMapping(value = "/${i.drools.web.pre}/component",produces = MediaType.APPLICATION_JSON_VALUE)
public class ComponentController {

    @Autowired
    private ComponentService service;

    @PostMapping(value = "/list")
    public Response<PageResDto<ComponentDto>> list(@RequestBody PageReqDto<ComponentListReq> req){
        return service.list(req);
    }

    @PostMapping(value = "/add")
    public Response<Object> add(@RequestBody ComponentDto req){
        return service.add(req);
    }

    @PostMapping(value = "/edit")
    public Response<Object> edit(@RequestBody ComponentDto req){
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

}
