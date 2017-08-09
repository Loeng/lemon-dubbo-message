package cn.lemon.dubbo.message.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

/** 
 * 数据源配置
 * @author lonyee
 */
@Configuration
public class DatabaseConfiguration {
	private static Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);
	
	@Primary
	@Bean(name= "dataSource", destroyMethod= "close", initMethod="init")
    @ConfigurationProperties(prefix="spring.dataSource")
    public DataSource dataSource() {
		logger.debug("Configruing DataSource");
		return new DruidDataSource();
    }
}
