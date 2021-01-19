/**
 * 前端Long类型精度丢失问题解决
 * @author xzj
 *
 */
package com.cloudok.core.json;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
@ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer.class)
public class JacksonConfig {
	
	@Bean("jackson2ObjectMapperBuilderCustomizer")
	public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
	    Jackson2ObjectMapperBuilderCustomizer customizer = new Jackson2ObjectMapperBuilderCustomizer() {
	        @Override
	        public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
	            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance)
	                    .serializerByType(Long.TYPE, ToStringSerializer.instance).serializerByType(long.class, ToStringSerializer.instance);
	        }
	    };
	    return customizer;
	}
}