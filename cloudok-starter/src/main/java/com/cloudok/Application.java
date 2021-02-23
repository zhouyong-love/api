package com.cloudok;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.cloudok.base.dict.enums.scan.EnumSan;

@EnableScheduling
@SpringBootApplication(scanBasePackages = { "com.cloudok" })
@MapperScan("com.cloudok.**.mapper")
@EnumSan(basePackages = "com.cloudok")
public class Application {

	@Value("${server.tomcat.basedir:/tmp/api}")
	private String temp;
	
	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedMethod("*");
		return corsConfiguration;
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig());
		return new CorsFilter(source);
	}

	@Bean
	public DatabaseIdProvider databaseIdProvider() {
		DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
		Properties p = new Properties();
		p.setProperty("MySQL", "mysql");
		p.setProperty("PostgreSQL", "postgresql");
		databaseIdProvider.setProperties(p);
		return databaseIdProvider;
	}
	
	@Bean
	public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(temp);
        factory.setMaxFileSize(DataSize.of(50, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(50, DataUnit.MEGABYTES));
        factory.setFileSizeThreshold(DataSize.of(100, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();

    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
