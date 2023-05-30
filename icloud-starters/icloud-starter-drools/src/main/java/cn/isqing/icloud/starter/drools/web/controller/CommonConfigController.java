package cn.isqing.icloud.starter.drools.web.controller;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.service.config.CommonConfigService;
import cn.isqing.icloud.starter.drools.service.config.dto.CommonConfigDto;
import cn.isqing.icloud.starter.drools.service.config.dto.CommonConfigListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/${i.drools.web.pre}/common_config", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonConfigController {

    @Autowired
    private CommonConfigService service;

    @PostMapping(value = "/list")
    public Response<PageResDto<CommonConfigDto>> list(@RequestBody PageReqDto<CommonConfigListReq> dto) {
        return service.list(dto);
    }

    @PostMapping(value = "/add")
    public Response<Object> add(@RequestBody CommonConfigDto dto) {
        return service.add(dto);
    }

    @PostMapping(value = "/edit")
    public Response<Object> edit(@RequestBody CommonConfigDto dto) {
        return service.edit(dto);
    }

    @PostMapping(value = "/del")
    public Response<Object> del(@RequestParam("id")Long id) {
        return service.del(id);
    }
}
