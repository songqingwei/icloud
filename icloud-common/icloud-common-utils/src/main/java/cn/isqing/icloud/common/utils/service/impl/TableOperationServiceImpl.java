package cn.isqing.icloud.common.utils.service.impl;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.utils.dto.BaseCondition;
import cn.isqing.icloud.common.utils.dto.TableInfoDto;
import cn.isqing.icloud.common.utils.dto.TableOperationDto;
import cn.isqing.icloud.common.utils.enums.ActionType;
import cn.isqing.icloud.common.utils.event.TableOperationEventPublisher;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.scanner.TableInfoScanner;
import cn.isqing.icloud.common.utils.service.TableOperationService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通用表操作服务实现类
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Service
public class TableOperationServiceImpl implements TableOperationService {

    private static final String PAGE_INFO_KEY = "pageInfo";
    private static final String CONDITION_KEY = "condition";
    
    @Autowired
    private TableInfoScanner tableInfoScanner;
    
    @Autowired
    private TableOperationEventPublisher eventPublisher;

    @Override
    public Response<Object> execute(TableOperationDto tableOperationDto) {
        ActionType action = tableOperationDto.getAction();
        switch (action) {
            case PAGE_QUERY:
                return pageQuery(tableOperationDto);
            case INSERT:
                return insert(tableOperationDto);
            case UPDATE:
                return update(tableOperationDto);
            case DELETE:
                return delete(tableOperationDto);
            case DETAIL:
                return detail(tableOperationDto);
            case LIST_QUERY:
                return listQuery(tableOperationDto);
            default:
                return Response.error("不支持的操作类型: " + action);
        }
    }

    @Override
    public Response<Object> pageQuery(TableOperationDto tableOperationDto) {
        try {
            String tableName = tableOperationDto.getTableName();
            String reqJson = tableOperationDto.getReq();
            
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            if (tableInfoDto.getMapper() == null) {
                return Response.error("未找到Mapper: " + tableName);
            }
            
            if (tableInfoDto.getConditionClass() == null) {
                return Response.error("未找到Condition类: " + tableName);
            }
            
            // 解析请求参数
            JSONObject jsonObject = JSON.parseObject(reqJson);
            PageReqDto.PageInfo pageInfo = jsonObject.getObject(PAGE_INFO_KEY, PageReqDto.PageInfo.class);
            if (pageInfo == null) {
                pageInfo = new PageReqDto.PageInfo();
            }
            
            BaseCondition condition = (BaseCondition) jsonObject.getObject(CONDITION_KEY, tableInfoDto.getConditionClass());
            if (condition == null) {
                condition = (BaseCondition) tableInfoDto.getConditionClass().getDeclaredConstructor().newInstance();
            }
            
            // 设置分页参数（如果需要）
            if (!pageInfo.isSelectAll()) {
                condition.setLimit(pageInfo.getPageSize());
                condition.setOffset(pageInfo.getOffset());
            }
            
            BaseMapper mapper = tableInfoDto.getMapper();
            PageResDto res = new PageResDto<>();
            if (pageInfo.isNeedTotal()) {
                Long count = mapper.countByCondition(condition);
                res.setTotal(count);
            }
            if (pageInfo.isNeedList()) {
                List list = mapper.selectByCondition(condition);
                res.setList(list);
            }
            
            return Response.success(res);
        } catch (Exception e) {
            log.error("分页查询失败", e);
            return Response.error("分页查询失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Object> insert(TableOperationDto tableOperationDto) {
        String tableName = tableOperationDto.getTableName();
        String reqJson = tableOperationDto.getReq();
        Long dataId = null;
        Object data = null;
        boolean success = false;

        try {
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            data = JSON.parseObject(reqJson, tableInfoDto.getEntityClass());
            BaseMapper mapper = tableInfoDto.getMapper();
            mapper.insert(data);
            
            // 提取ID值
            JSONObject jsonObject = JSON.parseObject(JsonUtil.toJsonString(data));
            dataId = jsonObject.getLong("id");
            success = true;
            
            return Response.SUCCESS;
        } catch (Exception e) {
            log.error("插入失败", e);
            return Response.error("插入失败: " + e.getMessage());
        } finally {
            // 发布事件
            eventPublisher.publishInsertEvent(tableName, dataId, data, success);
        }
    }

    @Override
    public Response<Object> update(TableOperationDto tableOperationDto) {
        String tableName = tableOperationDto.getTableName();
        String reqJson = tableOperationDto.getReq();
        Object data = null;
        boolean success = false;

        try {
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            data = JSON.parseObject(reqJson, tableInfoDto.getEntityClass());
            BaseMapper mapper = tableInfoDto.getMapper();
            mapper.update(data);
            success = true;
            
            return Response.SUCCESS;
        } catch (Exception e) {
            log.error("更新失败", e);
            return Response.error("更新失败: " + e.getMessage());
        } finally {
            // 发布事件
            eventPublisher.publishUpdateEvent(tableName, null, data, success);
        }
    }

    @Override
    public Response<Object> delete(TableOperationDto tableOperationDto) {
        String tableName = tableOperationDto.getTableName();
        String reqJson = tableOperationDto.getReq();
        Object data = null;
        boolean success = false;

        try {
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            data = JSON.parseObject(reqJson, tableInfoDto.getEntityClass());
            BaseMapper mapper = tableInfoDto.getMapper();
            mapper.del(data);
            success = true;
            
            return Response.SUCCESS;
        } catch (Exception e) {
            log.error("删除失败", e);
            return Response.error("删除失败: " + e.getMessage());
        } finally {
            // 发布事件
            eventPublisher.publishDeleteEvent(tableName, null, data, success);
        }
    }

    @Override
    public Response<Object> detail(TableOperationDto tableOperationDto) {
        try {
            String tableName = tableOperationDto.getTableName();
            String reqJson = tableOperationDto.getReq();
            
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            BaseMapper mapper = tableInfoDto.getMapper();
            
            // 从请求中提取ID
            JSONObject jsonObject = JSON.parseObject(reqJson);
            Long id = jsonObject.getLong("id");
            
            if (id == null) {
                return Response.error("缺少ID参数");
            }
            
            // 查询详情
            Object result = mapper.selectById(id, tableInfoDto.getEntityClass());
            
            if (result == null) {
                return Response.error("数据不存在");
            }
            
            return Response.success(result);
        } catch (Exception e) {
            log.error("详情查询失败", e);
            return Response.error("详情查询失败: " + e.getMessage());
        }
    }

    @Override
    public Response<Object> listQuery(TableOperationDto tableOperationDto) {
        try {
            String tableName = tableOperationDto.getTableName();
            String reqJson = tableOperationDto.getReq();
            
            // 获取表信息
            Map<String, TableInfoDto> tableInfoMap = tableInfoScanner.getTableInfoMap();
            TableInfoDto tableInfoDto = tableInfoMap.get(tableName);
            if (tableInfoDto == null) {
                return Response.error("未找到表信息: " + tableName);
            }
            
            if (tableInfoDto.getConditionClass() == null) {
                return Response.error("未找到Condition类: " + tableName);
            }
            
            // 解析请求参数
            BaseCondition condition = (BaseCondition) JSON.parseObject(reqJson, tableInfoDto.getConditionClass());
            if (condition == null) {
                condition = (BaseCondition) tableInfoDto.getConditionClass().getDeclaredConstructor().newInstance();
            }
            
            BaseMapper mapper = tableInfoDto.getMapper();
            List list = mapper.selectByCondition(condition);
            
            return Response.success(list);
        } catch (Exception e) {
            log.error("列表查询失败", e);
            return Response.error("列表查询失败: " + e.getMessage());
        }
    }
}
