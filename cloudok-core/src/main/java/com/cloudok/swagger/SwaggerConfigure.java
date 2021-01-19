package com.cloudok.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import lombok.Getter;
import lombok.Setter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;

/**
 * 
 * @author xiazhijian
 *
 */

@Configuration
@ConditionalOnProperty(prefix = "spring.swagger",value = {"enable"},havingValue = "true")
@EnableSwagger2
public class SwaggerConfigure {
	
	@Autowired
	private SwaggerProperties properties;
	
	@Bean
	public ApiInfo swaggerApiInfo() {
		return new ApiInfoBuilder().title(properties.getTitle())
		.description(properties.getDescription())
		.termsOfServiceUrl(properties.getTermsOfServiceUrl())
		.version(properties.getVersion()).build();
	}
	
	

	@Getter @Setter
	@Component
	@ConfigurationProperties(prefix = "spring.swagger")
	public static class SwaggerProperties{
		
		private boolean enable;
		
		private String basePackage;
		
		private String title;
		
		private String description;
		
		private String termsOfServiceUrl;
		
		private String version;
	}
	
}
