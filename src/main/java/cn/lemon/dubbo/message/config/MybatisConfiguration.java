package cn.lemon.dubbo.message.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cn.lemon.mybatis.pageable.MysqlPageInterceptor;
import cn.lemon.mybatis.pageable.PageInterceptor;

/**
 * Mybatis配置
 * @date 2017年8月3日 下午8:03:12 <br>
 * @author lonyee
 */
@Configuration
@EnableTransactionManagement
@AutoConfigureAfter({ DatabaseConfiguration.class })  
@MapperScan(basePackages={"cn.lemon.**.dao"})
public class MybatisConfiguration {
    private static Logger logger = LoggerFactory.getLogger(MybatisConfiguration.class);  
    
    @Value("${mybatis.typeAliasesPackage}")
    private String  typeAliasesPackage;
    @Value("${mybatis.mapperLocations}")
    private String  mapperLocations;
    
    @Resource(name="dataSource")
    private DataSource dataSource;
    
    @Bean(name="sqlSessionFactory")
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sessionFactory.setMapperLocations(resolver.getResources(mapperLocations));
            
            PageInterceptor pageInterceptor = new MysqlPageInterceptor();
            sessionFactory.setPlugins(new Interceptor[]{pageInterceptor});
            return sessionFactory.getObject();
        } catch (Exception e) {
            logger.warn("Could not confiure mybatis session factory", e);
            return null;
        }
    }
    
	@Bean
    public PlatformTransactionManager transactionManager() {
		PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        return platformTransactionManager;
    }
}
