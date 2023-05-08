package cn.isqing.icloud.starter.drools.web.controller;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.drools.service.input.dto.InputDto;
import cn.isqing.icloud.starter.drools.service.input.flow.InputFlow;
import cn.isqing.icloud.starter.drools.service.input.flow.InputFlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping(value = "/${i.drools.web.pre}/input", produces = MediaType.APPLICATION_JSON_VALUE)
public class InputController {

    @Autowired
    private InputFlow flow;

    @PostMapping(value = "/pushEvent")
    public Response<Object> event(@RequestBody InputDto req) {
        InputFlowContext context = new InputFlowContext();
        context.setInputDto(req);
        return flow.exec(context);
    }

}
