package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.enums.status.YesOrNo;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.kit.StrUtil;
import cn.isqing.icloud.common.utils.sql.SqlUtil;
import cn.isqing.icloud.starter.variable.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.variable.common.constants.DataSourceTypeConstatnts;
import cn.isqing.icloud.starter.variable.common.constants.SqlResConstants;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.variable.common.enums.DataSourceType;
import cn.isqing.icloud.starter.variable.common.enums.SqlComponentDialectType;
import cn.isqing.icloud.starter.variable.dao.entity.CommonText;
import cn.isqing.icloud.starter.variable.dao.entity.CommonTextCondition;
import cn.isqing.icloud.starter.variable.dao.entity.DataSourceCondition;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.DataSourceMapper;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = DataSourceTypeConstatnts.SQL)
public class SqlComponentExecFlow extends BaseComponentExecFlow {

    @Autowired
    private DataSourceMapper dataSourceMapper;
    @Autowired
    private CommonTextMapper commonTextMapper;

    // 数据源缓存
    private static final Map<Long, DataSource> DS_MAP = new ConcurrentHashMap<>();
    private static final Map<Long, JdbcTemplate> JDBC_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        DataSourceCondition condition = new DataSourceCondition();
        condition.setType(DataSourceType.SQL.getCode());
        condition.setIsActive(YesOrNo.YES.ordinal());
        condition.setSelectFiled(SqlConstants.ID);
        List<Long> list = dataSourceMapper.selectLongByCondition(condition);
        list.parallelStream().forEach(sid -> {
            try {
                CommonTextCondition text = new CommonTextCondition();
                text.setFid(sid);
                text.setType(CommonTextTypeConstants.DATA_SOURCE_CINFIG);
                text.setOrderBy(SqlConstants.ID_ASC);
                List<CommonText> texts = commonTextMapper.selectByCondition(text);
                String s = texts.stream().collect(Collectors.mapping(CommonText::getText, Collectors.joining()));
                Map<String, Object> configMap = JSONObject.parseObject(s, new TypeReference<Map<String, Object>>() {
                });
                cacheDataSource(sid, configMap);
            } catch (Exception e) {
                log.error("初始化数据源{}异常：{}", sid, e.getMessage(), e);
            }
        });
    }

    private String insertRes = "{\"" + SqlResConstants.INSERT_RES_ID + "\":%d}";
    private String updateRes = "{\"" + SqlResConstants.UPDATE_RES_A_ROWS + "\":%d}";

    @Override
    protected void registerRes(ComponentExecContext context) {
        ComponentExecDto resDto = context.getExecDto();
        resDto.getAboveResMap().put(context.getComponent().getId(), context.getExecRes());
    }

    @Override
    protected Response<Object> replace(String[] requestParams, String path, Object value) {
        String[] sqlArr = requestParams;
        String v = SqlUtil.escapeSqlValue(String.valueOf(value));
        if (path.startsWith("${")) {
            boolean b = StrUtil.isNumber(v);
            if (!b) {
                return Response.error("检测到sql注入");
            }
        } else if (path.startsWith("##{")) {
            v = "\"" + v + "\"";
        } else {//#{
            v = "\'" + v + "\'";
        }
        sqlArr[0] = sqlArr[0].replace(path, v);
        return Response.SUCCESS;
    }

    @Override
    protected void pre(ComponentExecContext context) {
        // 数据源
        cacheDataSource(context.getComponent().getDataSourceId(), context.getDsConfig());
        String sql = (String) JsonUtil.extract(context.getDialectConfig(), SqlComponentDialectType.SQL.getJsonPath());
        final String[] sqlArr = {sql};
        context.setRequestParamsTpl(sqlArr);
    }

    @Override
    protected void execComponent(ComponentExecContext context) {
        Object request = context.getRequestParamsTpl();
        String[] sqlArr = (String[]) request;
        String sql = sqlArr[0];
        JdbcTemplate template = JDBC_MAP.get(context.getComponent().getDataSourceId());
        if (sql.startsWith("insert ") || sql.startsWith("INSERT ")) {
            GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
            template.update(conn -> {
                PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                return preparedStatement;
            }, generatedKeyHolder);
            Long id = generatedKeyHolder.getKey().longValue();
            context.setExecRes(String.format(insertRes, id));
            return;
        }
        if (sql.startsWith("select ") || sql.startsWith("SELECT ")) {
            List<Map<String, Object>> list = template.queryForList(sql);
            if (list.size() == 1 && list.get(0) != null && list.get(0).size() == 1) {
                Map<String, Object> map = list.get(0);
                map.entrySet().stream().findFirst().ifPresent(e -> {
                    Object value = e.getValue();
                    context.setExecRes(String.valueOf(value));
                });
                return;
            }
            context.setExecRes(JsonUtil.toJsonString(list));
            return;
        }
        int i = template.update(sql);
        context.setExecRes(String.format(updateRes, i));
    }

    private void cacheDataSource(Long sourceId, Map<String, Object> dsConfig) {
        DS_MAP.computeIfAbsent(sourceId, id -> {
            DruidDataSource dataSource = new DruidDataSource();
            DataBinder dataBinder = new DataBinder(dataSource);
            dataBinder.setIgnoreInvalidFields(true);
            dataBinder.setIgnoreUnknownFields(true);
            MutablePropertyValues propertyValues = new MutablePropertyValues(dsConfig);
            dataBinder.bind(propertyValues);
            JdbcTemplate template = new JdbcTemplate();
            template.setDataSource(dataSource);
            JDBC_MAP.put(id, template);
            try {
                dataSource.init();
            } catch (SQLException e) {
                log.error("初始化数据库连接池异常:{}", e.getMessage(), e);
            }
            return dataSource;
        });
    }


}
