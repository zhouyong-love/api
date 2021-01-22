package com.cloudok.base.message.configure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cloudok.base.message.exception.MessageExceptionMessges;
import com.cloudok.core.exception.SystemException;

import freemarker.cache.StringTemplateLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MessageTemplateConfigure {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private MessageProperties messageProperties;

	@Bean("messageFreemarkTemplate")
	public freemarker.template.Configuration freemarkerTemplateConfiguration() {
		freemarker.template.Configuration freeMarkerConfigurer = new freemarker.template.Configuration(
				freemarker.template.Configuration.VERSION_2_3_29);
		freeMarkerConfigurer.setTemplateLoader(loader());
		freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		return freeMarkerConfigurer;
	}

	private StringTemplateLoader loader() {
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		if (!CollectionUtils.isEmpty(messageProperties.getMessage())) {
			messageProperties.getMessage().forEach((key, value) -> {
				if (!CollectionUtils.isEmpty(value.getChannel())) {
					value.getChannel().forEach((ckey, cvalue) -> {
						if (!StringUtils.isEmpty(cvalue.getTemplate().getParam())) {
							stringTemplateLoader.putTemplate(key + "." + ckey + ".param",
									readTemplate(cvalue.getTemplate().getParam()));
						}
						if (!StringUtils.isEmpty(cvalue.getTemplate().getText())) {
							stringTemplateLoader.putTemplate(key + "." + ckey + ".text",
									readTemplate(cvalue.getTemplate().getText()));
						}
						if (!StringUtils.isEmpty(cvalue.getTemplate().getTitle())) {
							stringTemplateLoader.putTemplate(key + "." + ckey + ".title",
									readTemplate(cvalue.getTemplate().getTitle()));
						}
					});
				}

			});
		}
		return stringTemplateLoader;
	}

	private String readTemplate(String template) {
		try {
			log.info("loading " + template);
			Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
					.getResources(template);
			if (resources == null || resources.length == 0) {
				throw new SystemException("消息模板 " + template + " 不存在!",MessageExceptionMessges.TEMPLATE_NOT_FOUND);
			} else if (resources.length > 1) {
				throw new SystemException("读取到多个模板文件!",MessageExceptionMessges.MULTIPLE_TEMPLATE_FOUND);
			}
			return ins2String(resources[0].getInputStream());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw new SystemException("消息模板读取失败!",MessageExceptionMessges.TEMPLATE_GENERATOR_ERROR);
		}
	}

	private String ins2String(InputStream inputStream) {
		try {
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				result.write(buffer, 0, length);
			}
			return result.toString("UTF-8");
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			throw new SystemException("模板文件读取失败!",MessageExceptionMessges.READ_TEMPLATE_FAILED);
		}
	}
}
