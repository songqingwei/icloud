package cn.isqing.icloud.starter.admin.config.druid;

import cn.isqing.icloud.starter.admin.config.MyBeanNameGenerator;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@MapperScan(value = "cn.isqing.icloud.starter.admin.dao", sqlSessionFactoryRef = "iAdminSqlSessionFactory", nameGenerator = MyBeanNameGenerator.class)
@Configuration
public class DruidConfig {

    private static final String FILTER_SLF4J_PREFIX = "i.admin.datasource.filter.slf4j";

    @Bean(name = "iAdminDataSource", initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "i.admin.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    @Bean("iAdminSlf4jLogFilter")
    @ConfigurationProperties(FILTER_SLF4J_PREFIX)
    @ConditionalOnProperty(prefix = FILTER_SLF4J_PREFIX, name = "enabled")
    public Slf4jLogFilter slf4jLogFilter(@Qualifier("iAdminDataSource") DruidDataSource dataSource) {
        try {
            Slf4jLogFilter filter = new Slf4jLogFilter();
            Field filtersField = DruidDataSource.class.getSuperclass().getDeclaredField("filters");
            filtersField.setAccessible(true);
            List<Filter> filters = (List<Filter>) filtersField.get(dataSource);
            Iterator iterator = filters.iterator();
            while (iterator.hasNext()) {
                Object obj = iterator.next();
                if (obj instanceof Slf4jLogFilter) {
                    return (Slf4jLogFilter) obj;
                }
            }
            filters.add(filter);
            filter.configFromProperties(dataSource.getConnectProperties());
            return filter;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    @Bean(name = "iAdminTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("iAdminDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "iAdminSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("iAdminDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/iadmin/**/*xml"));
        SqlSessionFactory factory = factoryBean.getObject();
        org.apache.ibatis.session.Configuration configuration = factory.getConfiguration();
        // 开启驼峰命名转换
        configuration.setMapUnderscoreToCamelCase(true);
        // configuration.addInterceptor(自定义拦截器实现类); // 方法入参可注入
        return factory;
    }

}
