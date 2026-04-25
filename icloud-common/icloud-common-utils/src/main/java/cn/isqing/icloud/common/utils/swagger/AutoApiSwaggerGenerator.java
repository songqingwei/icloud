package cn.isqing.icloud.common.utils.swagger;

import cn.isqing.icloud.common.utils.config.AutoApiRoutesConfig;
import cn.isqing.icloud.common.utils.config.TableOperationRoute;
import cn.isqing.icloud.common.utils.config.TableOperationRoutesConfig;
import cn.isqing.icloud.common.utils.dto.TableInfoDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import cn.isqing.icloud.common.utils.scanner.TableInfoScanner;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 自动API Swagger文档生成器
 * 根据TableInfoScanner和配置动态生成OpenAPI文档
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
public class AutoApiSwaggerGenerator {

    @Autowired
    private TableInfoScanner tableInfoScanner;

    @Autowired
    private AutoApiRoutesConfig autoApiConfig;

    @Autowired
    private TableOperationRoutesConfig routesConfig;

    @Autowired
    private SwaggerProperties swaggerProperties;

    /**
     * 生成auto-api的Paths
     *
     * @return Paths对象
     */
    public Paths generateAutoApiPaths() {
        if (!swaggerProperties.isAutoApiEnabled()) {
            log.debug("auto-api文档生成已禁用");
            return new Paths();
        }

        log.info("开始生成auto-api Swagger文档...");
        Paths paths = new Paths();
        Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
        String prefix = autoApiConfig.getPrefix();

        int count = 0;
        for (Map.Entry<String, TableInfoDto> entry : tableInfoMap.entrySet()) {
            String tableName = entry.getKey();
            TableInfoDto tableInfo = entry.getValue();

            // 获取表注释（从Entity类的@Schema注解）
            String tableComment = getTableComment(tableInfo);

            // 为每个表生成6个标准操作
            generatePath(paths, prefix, tableName, "page", "分页查询", tableComment);
            generatePath(paths, prefix, tableName, "list", "列表查询", tableComment);
            generatePath(paths, prefix, tableName, "detail", "详情查询", tableComment);
            generatePath(paths, prefix, tableName, "insert", "插入", tableComment);
            generatePath(paths, prefix, tableName, "update", "更新", tableComment);
            generatePath(paths, prefix, tableName, "delete", "删除", tableComment);

            count++;
        }

        log.info("已生成 {} 个表的auto-api文档，共 {} 个接口", count, count * 6);
        return paths;
    }

    /**
     * 生成配置式API的Paths
     *
     * @return Paths对象
     */
    public Paths generateTableOperationPaths() {
        if (!swaggerProperties.isTableOperationApiEnabled()) {
            log.debug("配置式API文档生成已禁用");
            return new Paths();
        }

        log.info("开始生成配置式API Swagger文档...");
        Paths paths = new Paths();
        String basePath = routesConfig.getBasePath();

        int count = 0;
        for (TableOperationRoute route : routesConfig.getRoutes()) {
            String fullPath = basePath + route.getPath();
            String tableName = route.getTableName();
            ActionType action = route.getAction();

            // 获取表信息
            TableInfoDto tableInfo = tableInfoScanner.getTableInfo(tableName);
            String tableComment = tableInfo != null ? getTableComment(tableInfo) : tableName;

            // 生成路径
            generateTableOperationPath(paths, fullPath, action, tableComment);
            count++;
        }

        log.info("已生成 {} 个配置式API文档", count);
        return paths;
    }

    /**
     * 生成单个auto-api路径
     */
    private void generatePath(Paths paths, String prefix, String tableName, 
                             String action, String summary, String tableComment) {
        String path = "/" + prefix + "/" + tableName + "/" + action;
        if (prefix.isEmpty()) {
            path = "/" + tableName + "/" + action;
        }

        PathItem pathItem = new PathItem();
        Operation operation = createOperation(summary, tableComment, action);
        pathItem.post(operation);

        paths.addPathItem(path, pathItem);
    }

    /**
     * 生成单个配置式API路径
     */
    private void generateTableOperationPath(Paths paths, String fullPath, 
                                           ActionType action, String tableComment) {
        PathItem pathItem = new PathItem();
        String summary = tableComment + " - " + getActionDescription(action);
        Operation operation = createOperation(summary, tableComment, action.name());
        pathItem.post(operation);

        paths.addPathItem(fullPath, pathItem);
    }

    /**
     * 创建Operation对象
     */
    private Operation createOperation(String summary, String description, String action) {
        Operation operation = new Operation();
        operation.setSummary(summary);
        operation.setDescription(description + " - " + getActionDescription(action));
        operation.setTags(java.util.Collections.singletonList(description));

        // 设置请求体
        RequestBody requestBody = new RequestBody();
        requestBody.setDescription("请求参数");
        requestBody.setRequired(true);

        MediaType mediaType = new MediaType();
        mediaType.setSchema(new Schema<>().type("object"));
        requestBody.setContent(new Content().addMediaType("application/json", mediaType));

        operation.setRequestBody(requestBody);

        // 设置响应
        ApiResponses responses = new ApiResponses();
        ApiResponse successResponse = new ApiResponse();
        successResponse.setDescription("成功响应");
        
        MediaType responseMedia = new MediaType();
        responseMedia.setSchema(new Schema<>().type("object"));
        successResponse.setContent(new Content().addMediaType("application/json", responseMedia));
        
        responses.addApiResponse("200", successResponse);
        operation.setResponses(responses);

        return operation;
    }

    /**
     * 获取表注释
     * 优先从Entity类的@Schema注解读取
     */
    private String getTableComment(TableInfoDto tableInfo) {
        if (tableInfo == null || tableInfo.getEntityClass() == null) {
            return "未知表";
        }

        // 尝试从Entity类的@Schema注解获取description
        try {
            io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = 
                tableInfo.getEntityClass().getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            
            if (schemaAnnotation != null && !schemaAnnotation.description().isEmpty()) {
                return schemaAnnotation.description();
            }
        } catch (Exception e) {
            log.debug("读取Entity类@Schema注解失败: {}", tableInfo.getEntityClass().getName(), e);
        }

        // 如果没有注解，返回表名
        return tableInfo.getEntityClass().getSimpleName();
    }

    /**
     * 获取操作描述
     */
    private String getActionDescription(ActionType action) {
        switch (action) {
            case PAGE_QUERY:
                return "分页查询";
            case LIST_QUERY:
                return "列表查询";
            case DETAIL:
                return "详情查询";
            case INSERT:
                return "插入";
            case UPDATE:
                return "更新";
            case DELETE:
                return "删除";
            default:
                return action.name();
        }
    }

    /**
     * 获取操作描述（字符串版本）
     */
    private String getActionDescription(String action) {
        switch (action.toLowerCase()) {
            case "page":
                return "分页查询";
            case "list":
                return "列表查询";
            case "detail":
                return "详情查询";
            case "insert":
                return "插入";
            case "update":
                return "更新";
            case "delete":
                return "删除";
            default:
                return action;
        }
    }
}
