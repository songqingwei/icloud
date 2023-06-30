package cn.isqing.icloud.app.policy.engine.controller;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableListReq;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariablesValueReqDto;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariablePageResDto;
import com.alibaba.dubbo.config.annotation.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@RestController
@RequestMapping(value = "/demo",produces = MediaType.APPLICATION_JSON_VALUE)
public class DemoController {

    @Reference(group = "${i.variable.dubbo.group:iVariable}", timeout = -1, retries = -1, version = "1.0.0")
    @Lazy
    private VariableInterface api;

    /**
     * 发布遍历集变更事件
     *
     * @param coreId
     * @param list
     * @return
     */
    @PostMapping(value = "/event")
    public Response<Object> publishVsetChangeEvent(@RequestParam("coreId") String coreId, @RequestBody List<Long> list){
        return api.publishVsetChangeEvent(coreId,list);
    }

    /**
     * 获取变量集对应组件运行结果
     *
     * @param reqDto
     * @return
     */
    @PostMapping(value = "/getCRes")
    public Response<Map<Long, String>> getComponentRes(@RequestBody ApiVariablesValueReqDto reqDto){
        return api.getComponentRes(reqDto);
    }

    /**
     * 获取变量集对应值
     *
     * @param reqDto
     * @return
     */
    @PostMapping(value = "/getV")
    public Response<Map<Long, Object>> getValues(@RequestBody ApiVariablesValueReqDto reqDto){
        return api.getValues(reqDto);
    }

    /**
     * 获取变量详情
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/getOne")
    public Response<ApiVariableDto> getVariableById(@RequestParam("id") Long id){
        return api.getVariableById(id);
    }

    /**
     * 变量分页查询
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/list")
    Response<ApiVariablePageResDto<ApiVariableDto>> list(@RequestBody PageReqDto<ApiVariableListReq> req){
        return api.list(req);
    }


}
