package cn.isqing.icloud.common.utils.scanner;

import cn.isqing.icloud.common.utils.dao.BaseMapper;
import cn.isqing.icloud.common.utils.sql.SqlUtil;
import cn.isqing.icloud.common.utils.dto.TableInfoDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 表信息扫描器
 * 用于扫描并注册系统中的表信息
 *
 * @author songqingwei
 * @version 1.0
 */
@Slf4j
@Component
public class TableInfoScanner {

    private final ApplicationContext applicationContext;

    /**
     * 表信息映射缓存
     * key: 表名
     * value: 表信息对象
     */
    private final Map<String, TableInfoDto> tableInfoMap = new ConcurrentHashMap<>();

    public TableInfoScanner(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        log.info("=== TableInfoScanner 构造函数被调用 ===");
    }

    /**
     * Spring容器启动后自动扫描
     */
    @PostConstruct
    public void init() {
        log.info("=== TableInfoScanner 开始初始化 ===");
        scanAndInitTableInfo();
        log.info("=== TableInfoScanner 初始化完成 ===");
    }

    /**
     * 扫描并初始化表信息映射
     */
    public void scanAndInitTableInfo() {
        log.info("开始扫描表信息...");

        // 获取所有BaseMapper类型的Bean
        Map<String, BaseMapper> mapperBeans = applicationContext.getBeansOfType(BaseMapper.class);
        
        for (Map.Entry<String, BaseMapper> entry : mapperBeans.entrySet()) {
            try {
                String beanName = entry.getKey();
                BaseMapper mapper = entry.getValue();
                
                // 获取Mapper类的实际类信息
                Class<?> mapperClass = mapper.getClass();
                
                // 处理代理类：
                // 1. JDK 动态代理：类名包含 "$Proxy" 或通过 Proxy.isProxyClass() 判断，使用 getInterfaces()[0] 获取接口
                // 2. CGLIB 代理：类名包含 "$$"，使用 getSuperclass() 获取父类
                if (Proxy.isProxyClass(mapperClass) || mapperClass.getName().contains("$Proxy")) {
                    // JDK 动态代理，获取第一个接口（就是 Mapper 接口）
                    Class<?>[] interfaces = mapperClass.getInterfaces();
                    if (interfaces != null && interfaces.length > 0) {
                        mapperClass = interfaces[0];
                        log.debug("处理 JDK 动态代理: {} -> {}", mapper.getClass().getName(), mapperClass.getName());
                    }
                } else if (mapperClass.getName().contains("$$")) {
                    // CGLIB 代理，获取父类
                    mapperClass = mapperClass.getSuperclass();
                    log.debug("处理 CGLIB 代理: {} -> {}", mapper.getClass().getName(), mapperClass.getName());
                }
                
                // 从Mapper类推断Entity和Condition类
                String mapperPackageName = mapperClass.getPackage().getName();
                String mapperClassName = mapperClass.getSimpleName();
                
                // 去除Mapper后缀得到基础名称
                String baseName = mapperClassName;
                if (baseName.endsWith("Mapper")) {
                    baseName = baseName.substring(0, baseName.length() - 6);
                }
                
                // 将包路径中的 .mapper 替换为 .entity 查找Entity类
                String entityPackageName = mapperPackageName.replace(".mapper", ".entity");
                String entityClassName = entityPackageName + "." + baseName;
                
                // 加载Entity类
                Class<?> entityClass = null;
                try {
                    entityClass = Class.forName(entityClassName);
                } catch (ClassNotFoundException e) {
                    log.debug("未找到Entity类: {}", entityClassName);
                    continue;
                }
                
                // 获取表名
                String tableName = SqlUtil.getTableName(entityClass, false).replaceAll("`", "");
                
                // 创建表信息对象
                TableInfoDto tableInfoDto = new TableInfoDto();
                tableInfoDto.setEntityClass(entityClass);
                tableInfoDto.setMapper(mapper);
                
                // 将包路径中的 .mapper 替换为 .condition 查找Condition类
                findConditionClass(mapperPackageName, baseName, tableInfoDto);
                
                // 放入映射表
                tableInfoMap.put(tableName, tableInfoDto);
                log.info("注册表信息: {} -> Entity: {}, Condition: {}", 
                    tableName, 
                    entityClass.getSimpleName(),
                    tableInfoDto.getConditionClass() != null ? tableInfoDto.getConditionClass().getSimpleName() : "无");
                
            } catch (Exception e) {
                log.debug("处理Mapper时发生异常: {}", entry.getKey(), e);
            }
        }

        log.info("表信息映射初始化完成，共加载{}个表", tableInfoMap.size());
    }

    /**
     * 查找Condition类
     * 将Mapper包路径中的 .mapper 替换为 .condition，并将类名后缀 Mapper 替换为 Condition
     *
     * @param mapperPackageName Mapper的包路径
     * @param baseName 基础类名（已去除Mapper后缀）
     * @param tableInfoDto 表信息对象
     */
    private void findConditionClass(String mapperPackageName, String baseName, TableInfoDto tableInfoDto) {
        try {
            // 将包路径中的 .mapper 替换为 .condition
            String conditionPackageName = mapperPackageName.replace(".mapper", ".condition");
            String conditionClassName = conditionPackageName + "." + baseName + "Condition";
            
            Class<?> conditionClass = Class.forName(conditionClassName);
            tableInfoDto.setConditionClass(conditionClass);
            log.debug("找到Condition类: {}", conditionClassName);
        } catch (ClassNotFoundException e) {
            log.debug("未找到Condition类: {}.{}Condition", 
                mapperPackageName.replace(".mapper", ".condition"), baseName);
        }
    }

    /**
     * 手动注册表信息
     *
     * @param tableName 表名
     * @param tableInfoDto 表信息
     */
    public void registerTableInfo(String tableName, TableInfoDto tableInfoDto) {
        tableInfoMap.put(tableName, tableInfoDto);
        log.info("手动注册表信息: {}", tableName);
    }

    /**
     * 获取所有已注册的表信息
     *
     * @return 表信息映射
     */
    public Map<String, TableInfoDto> getTableInfoMap() {
        return tableInfoMap;
    }
    
    /**
     * 根据表名获取表信息
     *
     * @param tableName 表名
     * @return 表信息
     */
    public TableInfoDto getTableInfo(String tableName) {
        return tableInfoMap.get(tableName);
    }
}
