package cn.isqing.icloud.starter.drools.web.controller;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.UpdateStatusDto;
import cn.isqing.icloud.starter.drools.service.template.RuleTemplateService;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateDto;
import cn.isqing.icloud.starter.drools.service.template.dto.RuleTemplateListReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping(value = "/${i.drools.web.pre}/template",produces = MediaType.APPLICATION_JSON_VALUE)
public class RuleTemplateController {

    @Autowired
    private RuleTemplateService service;

    @PostMapping(value = "/list")
    public Response<PageResDto<RuleTemplateDto>> list(@RequestBody PageReqDto<RuleTemplateListReq> req){
        return service.list(req);
    }
    @PostMapping(value = "/baselist")
    Response<PageResDto<RuleTemplateDto>> baseList(@RequestBody PageReqDto<RuleTemplateListReq> dto){
        return service.baseList(dto);
    }
    @PostMapping(value = "/text")
    Response<RuleTemplateDto> getText(Long id){
        return service.getText(id);
    }
    @PostMapping(value = "/baseinfo")
    Response<RuleTemplateDto> baseInfo(Long id){
        return service.baseInfo(id);
    }
    @PostMapping(value = "/busi")
    Response<Map<String,String>> getBusi(Long id){
        return service.getBusi(id);
    }


    @PostMapping(value = "/add")
    public Response<Object> add(@RequestBody RuleTemplateDto req){
        return service.add(req);
    }

    @PostMapping(value = "/edit")
    public Response<Object> edit(@RequestBody RuleTemplateDto req){
        return service.edit(req);
    }

    @PostMapping(value = "/sw")
    public Response<Object> sw(@RequestBody UpdateStatusDto req){
        return service.sw(req);
    }

    @PostMapping(value = "/del")
    public Response<Object> del(@RequestParam("id") Long id){
        return service.del(id);
    }

}
