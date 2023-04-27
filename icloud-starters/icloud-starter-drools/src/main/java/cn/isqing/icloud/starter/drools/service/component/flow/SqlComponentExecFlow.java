package cn.isqing.icloud.starter.drools.service.component.flow;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.kit.StrUtil;
import cn.isqing.icloud.common.utils.sql.SqlUtil;
import cn.isqing.icloud.starter.drools.common.constants.DataSourceTypeConstatnts;
import cn.isqing.icloud.starter.drools.common.constants.SqlResConstants;
import cn.isqing.icloud.starter.drools.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.drools.common.enums.SqlComponentDialectType;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@RouteType(r1 = DataSourceTypeConstatnts.SQL)
public class SqlComponentExecFlow extends BaseComponentExecFlow {

    // 数据源缓存
    private static final Map<Long, DataSource> DS_MAP = new ConcurrentHashMap<>();
    private static final Map<Long, JdbcTemplate> JDBC_MAP = new ConcurrentHashMap<>();

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
        String v;
        v = SqlUtil.escapeSqlValue((String) value);

        sqlArr[0] = sqlArr[0].replace("#{" + path + "}", "\'" + v + "\'");
        String placeholder = "${" + path + "}";
        if (sqlArr[0].indexOf(placeholder) > -1) {
            boolean b = StrUtil.isNumber(v);
            if (!b) {
                return Response.error("检测到sql注入");
            }
        }
        sqlArr[0] = sqlArr[0].replace(placeholder, v);
        return Response.SUCCESS;
    }

    @Override
    protected void pre(ComponentExecContext context) {
        // 数据源
        cacheDataSource(context);
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
            context.setExecRes(JsonUtil.toJsonString(list));
            return;
        }
        int i = template.update(sql);
        context.setExecRes(String.format(updateRes, i));
    }

    private void cacheDataSource(ComponentExecContext context) {
        Long sourceId = context.getComponent().getDataSourceId();
        DS_MAP.computeIfAbsent(sourceId, id -> {
            DruidDataSource dataSource = new DruidDataSource();
            DataBinder dataBinder = new DataBinder(dataSource);
            dataBinder.setIgnoreInvalidFields(true);
            dataBinder.setIgnoreUnknownFields(true);
            MutablePropertyValues propertyValues = new MutablePropertyValues(context.getDsConfig());
            dataBinder.bind(propertyValues);
            JdbcTemplate template = new JdbcTemplate();
            template.setDataSource(dataSource);
            JDBC_MAP.put(id, template);
            return dataSource;
        });
    }


}
