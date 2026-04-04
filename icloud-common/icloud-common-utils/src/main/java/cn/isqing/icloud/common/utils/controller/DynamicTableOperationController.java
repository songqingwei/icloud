package cn.isqing.icloud.common.utils.controller;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.service.TableOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 动态表操作Controller
 * 通过配置生成，处理所有表操作请求
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@RestController
public class DynamicTableOperationController {

    @Autowired
    private TableOperationService tableOperationService;

    /**
     * 执行表操作
     * 
     * @param operationDto 表操作DTO（包含tableName, action, req等信息）
     * @return 操作结果
     */
    @PostMapping("/table-op/execute")
    public Response<Object> execute(@RequestBody TableOperationDto operationDto) {
        log.info("执行表操作: tableName={}, action={}", 
            operationDto.getTableName(), operationDto.getAction());
        
        try {
            Response<Object> result = tableOperationService.execute(operationDto);
            log.info("表操作完成: success={}", result.isSuccess());
            return result;
        } catch (Exception e) {
            log.error("表操作失败", e);
            return Response.error("表操作失败: " + e.getMessage());
        }
    }
}
