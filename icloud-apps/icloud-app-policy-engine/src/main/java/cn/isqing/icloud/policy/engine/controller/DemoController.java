package cn.isqing.icloud.policy.engine.controller;

import cn.isqing.icloud.common.utils.dto.PageReqDto;
import cn.isqing.icloud.common.utils.dto.PageResDto;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.VariableDto;
import cn.isqing.icloud.starter.variable.api.dto.VariablesValueReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private VariableInterface api;

    /**
     * 发布遍历集变更事件
     *
     * @param coreId
     * @param list
     * @return
     */
    @PostMapping(value = "/event")
    public Response<Object> publishVsetChangeEvent(@RequestParam("coreId") Long coreId, @RequestParam("list") List<Long> list){
        return api.publishVsetChangeEvent(coreId,list);
    }

    /**
     * 获取变量集对应组件运行结果
     *
     * @param reqDto
     * @return
     */
    @PostMapping(value = "/getCRes")
    public Response<Map<Long, String>> getComponentRes(@RequestBody VariablesValueReqDto reqDto){
        return api.getComponentRes(reqDto);
    }

    /**
     * 获取变量集对应值
     *
     * @param reqDto
     * @return
     */
    @PostMapping(value = "/getV")
    public Response<Map<Long, Object>> getValues(@RequestBody VariablesValueReqDto reqDto){
        return api.getValues(reqDto);
    }

    /**
     * 获取变量详情
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/getOne")
    public Response<cn.isqing.icloud.starter.variable.api.dto.VariableDto> getVariableById(Long id){
        return api.getVariableById(id);
    }

    /**
     * 变量分页查询
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/list")
    Response<PageResDto<VariableDto>> list(@RequestBody PageReqDto<cn.isqing.icloud.starter.variable.api.dto.VariableListReq> req){
        return api.list(req);
    }


}