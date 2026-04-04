package cn.isqing.icloud.common.utils.config;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import cn.isqing.icloud.common.utils.scanner.TableInfoScanner;
import cn.isqing.icloud.common.utils.service.TableOperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 自动API路由拦截器
 * 根据URL路径自动推断表名和操作类型，实现零配置CRUD
 * 
 * URL规范: /auto-api/{tableName}/{action}
 * 例如:
 *   POST /auto-api/sys_user/page    -> 分页查询
 *   POST /auto-api/sys_user/insert  -> 插入
 *   POST /auto-api/sys_user/update  -> 更新
 *   POST /auto-api/sys_user/delete  -> 删除
 *   POST /auto-api/sys_user/detail  -> 详情查询
 *   POST /auto-api/sys_user/list    -> 列表查询（不分页）
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
public class AutoApiRouteInterceptor implements HandlerInterceptor {

    private static final String AUTO_API_PREFIX = "/auto-api";
    
    /**
     * 支持的操作类型
     */
    private static final Set<String> SUPPORTED_ACTIONS = new HashSet<>(Arrays.asList(
        "page", "insert", "update", "delete", "detail", "list"
    ));

    @Autowired
    private TableInfoScanner tableInfoScanner;

    @Autowired
    private TableOperationService tableOperationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // 只处理POST请求
        if (!"POST".equalsIgnoreCase(method)) {
            return true;
        }
        
        // 检查是否是自动API路径
        if (!uri.startsWith(AUTO_API_PREFIX + "/")) {
            return true;
        }
        
        // 解析路径: /auto-api/{tableName}/{action}
        String[] pathParts = parsePath(uri);
        if (pathParts == null || pathParts.length != 2) {
            log.debug("自动API路径格式不正确: {}", uri);
            return true;
        }
        
        String tableName = pathParts[0];
        String actionStr = pathParts[1].toLowerCase();
        
        // 验证操作类型是否支持
        if (!SUPPORTED_ACTIONS.contains(actionStr)) {
            log.debug("不支持的操作类型: {}", actionStr);
            return true;
        }
        
        // 验证表是否存在
        if (tableInfoScanner.getTableInfo(tableName) == null) {
            log.warn("未找到表信息: {}", tableName);
            writeResponse(response, Response.error("未找到表: " + tableName));
            return false;
        }
        
        log.info("自动API路由: uri={}, table={}, action={}", uri, tableName, actionStr);
        
        try {
            // 读取请求体
            String requestBody = readRequestBody(request);
            
            // 构建TableOperationDto
            TableOperationDto operationDto = buildOperationDto(tableName, actionStr, requestBody);
            
            // 执行表操作
            Response<Object> result = tableOperationService.execute(operationDto);
            
            // 写入响应
            writeResponse(response, result);
            
            // 返回false，表示已经处理完成
            return false;
            
        } catch (Exception e) {
            log.error("自动API处理失败: uri={}", uri, e);
            writeResponse(response, Response.error("操作失败: " + e.getMessage()));
            return false;
        }
    }

    /**
     * 解析路径，提取表名和操作类型
     * 
     * @param uri 请求URI
     * @return [tableName, action] 或 null
     */
    private String[] parsePath(String uri) {
        // 移除前缀
        String path = uri.substring(AUTO_API_PREFIX.length());
        
        // 去除首尾斜杠
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        
        // 分割路径
        String[] parts = path.split("/");
        
        // 必须是两段: {tableName}/{action}
        if (parts.length != 2) {
            return null;
        }
        
        return parts;
    }

    /**
     * 构建TableOperationDto
     */
    private TableOperationDto buildOperationDto(String tableName, String actionStr, String requestBody) {
        TableOperationDto operationDto = new TableOperationDto();
        operationDto.setTableName(tableName);
        operationDto.setAction(mapAction(actionStr));
        operationDto.setReq(requestBody);
        
        // 根据表信息设置VO类
        var tableInfo = tableInfoScanner.getTableInfo(tableName);
        if (tableInfo != null && tableInfo.getEntityClass() != null) {
            operationDto.setResVoClass(tableInfo.getEntityClass());
        }
        
        return operationDto;
    }

    /**
     * 映射操作类型字符串到ActionType枚举
     */
    private ActionType mapAction(String actionStr) {
        switch (actionStr.toLowerCase()) {
            case "page":
                return ActionType.PAGE_QUERY;
            case "insert":
                return ActionType.INSERT;
            case "update":
                return ActionType.UPDATE;
            case "delete":
                return ActionType.DELETE;
            case "detail":
                return ActionType.DETAIL;
            case "list":
                return ActionType.LIST_QUERY;
            default:
                throw new IllegalArgumentException("不支持的操作类型: " + actionStr);
        }
    }

    /**
     * 读取请求体
     */
    private String readRequestBody(HttpServletRequest request) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * 写入响应
     */
    private void writeResponse(HttpServletResponse response, Response<?> result) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
