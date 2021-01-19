package com.cloudok.base.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@ConditionalOnProperty(prefix = "spring.swagger", value = { "enable" }, havingValue = "true")
public class BaseSwaggerConfigure {

	@Value("${spring.profiles.active}")
	private String active;
	
	@Autowired
	private ApiInfo apiInfo;

	@Bean
	public Docket baseSwagger() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new ParameterBuilder().name("Authorization").description("Authorization").parameterType("header")
				.required(false).modelRef(new ModelRef("string")).build());
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).groupName("base").select()
				.apis(!("dev".equals(active) || "local".equals(active) || "test".equals(active))
						? RequestHandlerSelectors.none()
						: RequestHandlerSelectors.basePackage("com.cloudok.base"))
				.paths(PathSelectors.any()).build().globalOperationParameters(parameters);

	}
}
