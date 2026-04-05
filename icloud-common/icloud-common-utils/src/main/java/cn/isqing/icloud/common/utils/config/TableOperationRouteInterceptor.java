package cn.isqing.icloud.common.utils.config;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.service.TableOperationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 表操作路由拦截器
 * 根据配置的URL映射关系，自动路由到对应的表操作
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
public class TableOperationRouteInterceptor implements HandlerInterceptor {

    @Autowired
    private TableOperationRoutesConfig routesConfig;

    @Autowired
    private TableOperationService tableOperationService;

    private final ObjectMapper objectMapper;
    
    {
        // 初始化 ObjectMapper，支持 Java 8 日期时间类型
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper = new ObjectMapper()
                .registerModule(javaTimeModule)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式，使用字符串格式
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        
        // 只处理POST请求
        if (!"POST".equalsIgnoreCase(method)) {
            return true;
        }
        
        // 查找匹配的路由配置
        TableOperationRoute matchedRoute = findMatchedRoute(uri);
        if (matchedRoute == null) {
            // 没有匹配的路由，继续正常流程
            return true;
        }
        
        log.info("匹配到表操作路由: uri={}, route={}", uri, matchedRoute.getPath());
        
        try {
            // 读取请求体
            String requestBody = readRequestBody(request);
            
            // 构建TableOperationDto
            TableOperationDto operationDto = new TableOperationDto();
            operationDto.setTableName(matchedRoute.getTableName());
            operationDto.setAction(matchedRoute.getAction());
            operationDto.setReq(requestBody);
            operationDto.setResVoClass(matchedRoute.getResponseVoClass());
            
            // 执行表操作
            Response<Object> result = tableOperationService.execute(operationDto);
            
            // 写入响应
            writeResponse(response, result);
            
            // 返回false，表示已经处理完成，不需要继续执行Controller
            return false;
            
        } catch (Exception e) {
            log.error("表操作路由处理失败: uri={}", uri, e);
            writeResponse(response, Response.error("表操作失败: " + e.getMessage()));
            return false;
        }
    }

    /**
     * 查找匹配的路由配置
     */
    private TableOperationRoute findMatchedRoute(String uri) {
        String basePath = routesConfig.getBasePath();
        
        for (TableOperationRoute route : routesConfig.getRoutes()) {
            String fullPath = basePath + route.getPath();
            if (fullPath.equals(uri)) {
                return route;
            }
        }
        
        return null;
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
