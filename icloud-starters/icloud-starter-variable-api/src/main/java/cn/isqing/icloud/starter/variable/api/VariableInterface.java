package cn.isqing.icloud.starter.variable.api;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.api.dto.VariableDto;
import cn.isqing.icloud.starter.variable.api.dto.VariableListReq;
import cn.isqing.icloud.starter.variable.api.dto.VariableValueDto;
import cn.isqing.icloud.starter.variable.api.dto.VariablesValueReqDto;

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
    Response<Map<Long, String>> getComponentRes(VariablesValueReqDto reqDto);

    /**
     * 获取变量集对应值
     *
     * @param reqDto
     * @return
     */
    Response<Map<Long, Object>> getValues(VariablesValueReqDto reqDto);

    /**
     * 获取变量值
     *
     * @param reqDto
     * @return
     */
    Response<List<VariableValueDto>> getVarValue(VariablesValueReqDto reqDto);

    /**
     * 获取变量详情
     *
     * @param id
     * @return
     */
    Response<VariableDto> getVariableById(Long id);

    /**
     * 变量分页查询
     *
     * @param req
     * @return
     */
    Response<PageResDto<VariableDto>> list(PageReqDto<VariableListReq> req);
}
