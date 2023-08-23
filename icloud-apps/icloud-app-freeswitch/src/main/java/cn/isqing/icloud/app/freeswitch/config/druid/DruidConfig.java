package cn.isqing.icloud.app.freeswitch.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@MapperScan(value = "cn.isqing.icloud.app.freeswitch.dao.mapper", sqlSessionFactoryRef = "iFreeswitchSqlSessionFactory")
@Configuration
public class DruidConfig {

    @Bean(name = "iFreeswitchDataSource", initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(prefix = "i.freeswitch.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }

    

    @Bean(name = "iFreeswitchTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("iFreeswitchDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "iFreeswitchSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("iFreeswitchDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/ifreeswitch/**/*xml"));
        SqlSessionFactory factory = factoryBean.getObject();
        org.apache.ibatis.session.Configuration configuration = factory.getConfiguration();
        // 开启驼峰命名转换
        configuration.setMapUnderscoreToCamelCase(true);
        // configuration.addInterceptor(自定义拦截器实现类); // 方法入参可注入
        return factory;
    }

}
