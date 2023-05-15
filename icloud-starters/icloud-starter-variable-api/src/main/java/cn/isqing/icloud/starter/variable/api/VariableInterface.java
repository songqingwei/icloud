package cn.isqing.icloud.starter.variable.api;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableListReq;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariablesValueReqDtoApi;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariablePageResDto;

import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface VariableInterface {

    /**
     * 发布遍历集变更事件
     *
     * @param coreId
     * @param list
     * @return
     */
    Response<Object> publishVsetChangeEvent(String coreId, List<Long> list);

    /**
     * 获取变量集对应组件运行结果
     *
     * @param reqDto
     * @return
     */
    Response<Map<Long, String>> getComponentRes(ApiVariablesValueReqDtoApi reqDto);

    /**
     * 获取变量集对应值
     *
     * @param reqDto
     * @return
     */
    Response<Map<Long, Object>> getValues(ApiVariablesValueReqDtoApi reqDto);

    /**
     * 获取变量详情
     *
     * @param id
     * @return
     */
    Response<ApiVariableDto> getVariableById(Long id);

    /**
     * 变量分页查询
     *
     * @param req
     * @return
     */
    Response<ApiVariablePageResDto<ApiVariableDto>> list(PageReqDto<ApiVariableListReq> req);
}
