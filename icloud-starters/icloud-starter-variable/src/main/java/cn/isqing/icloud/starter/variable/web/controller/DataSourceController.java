package cn.isqing.icloud.starter.variable.web.controller;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.variable.service.datasource.DataSourceService;
import cn.isqing.icloud.starter.variable.service.datasource.dto.DataSourceDto;
import cn.isqing.icloud.starter.variable.service.datasource.dto.DataSourceListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/${i.variable.web.pre}/datasource",produces = MediaType.APPLICATION_JSON_VALUE)
public class DataSourceController {

    @Autowired
    private DataSourceService service;

    @PostMapping(value = "/list")
    public Response<PageResDto<DataSourceDto>> list(@RequestBody PageReqDto<DataSourceListReq> req){
        return service.list(req);
    }

    @PostMapping(value = "/text")
    public Response<DataSourceDto> getText(Long id) {
        return service.getText(id);
    }

    @PostMapping(value = "/add")
    public Response<Object> add(@RequestBody DataSourceDto req){
        return service.add(req);
    }

    @PostMapping(value = "/edit")
    public Response<Object> edit(@RequestBody DataSourceDto req){
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
