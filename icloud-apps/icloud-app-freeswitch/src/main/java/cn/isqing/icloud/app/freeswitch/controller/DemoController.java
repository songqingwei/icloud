package cn.isqing.icloud.app.freeswitch.controller;

import cn.isqing.icloud.app.freeswitch.service.inbound.InboundService;
import cn.isqing.icloud.common.api.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping(value = "/demo", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {

    @Autowired
    private InboundService inboundService;

    @GetMapping(value = "/call")
    public Response<Object> publishVsetChangeEvent(@RequestParam("l") String l, @RequestParam("r") String r) {
        inboundService.call("", l, r);
        return Response.SUCCESS;
    }


}
