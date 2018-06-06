package com.plumLi.idea_for_share.configuration.datasource;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.Properties;
import javax.sql.DataSource;

/**
 * mybatis的SqlSessionFactory配置
 *
 * @author licunzhi
 * @date 2018/3/14
 */
@Configuration
@MapperScan(basePackages = {"com.plumLi.idea_for_share.mapper"}, sqlSessionTemplateRef = "tappSqlSessionTemplate")
public class PlumLiDbConfiguration {

    @Bean(name = "tappDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.plumLi")
    @Primary
    public DataSource getDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 获取SqlSessionFactory
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapping/*.xml"));

        //分页插件,配置多数据源后PageHelper会失效，需要手动配置
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        //dao层参数配置，传pageNumKey，pageSizeKey参数后会实现分页
        properties.setProperty("params", "pageNum=pageNumKey;pageSize=pageSizeKey;");
        Interceptor interceptor = new PageInterceptor();
        interceptor.setProperties(properties);
        bean.setPlugins(new Interceptor[] {interceptor});

        return bean.getObject();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(
                    @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
