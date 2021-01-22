package com.cloudok.base.dict.enums.scan;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.cloudok.base.dict.enums.EnumHandler;
import com.cloudok.base.dict.enums.EnumInfo;
import com.cloudok.base.dict.enums.annotation.Enum;
import com.cloudok.base.dict.enums.annotation.EnumValue;

import lombok.extern.java.Log;

@Log
public class EnumRegister implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

	private ResourceLoader resourceLoader;

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes annoAttrs = AnnotationAttributes
				.fromMap(annotationMetadata.getAnnotationAttributes(EnumSan.class.getName()));
		String[] basePackages = annoAttrs.getStringArray("basePackages");
		if (basePackages.length == 0) {
			basePackages = new String[] {
					((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName() };
		}

		EnumSanServiceClassPathScanHandle scanHandle = new EnumSanServiceClassPathScanHandle(registry,
				false);

		if (resourceLoader != null) {
			scanHandle.setResourceLoader(resourceLoader);
		}
		scanHandle.setBeanNameGenerator(new EnumBeanNameGenerator());
		Set<BeanDefinitionHolder> beanDefinitionHolders = scanHandle.doScan(basePackages);
		beanDefinitionHolders.forEach(item->{
			try {
				log.info("san emum beanName:"+ item.getBeanName()+" className:"+item.getBeanDefinition().getBeanClassName());
				Class<?> clasz=Class.forName(item.getBeanDefinition().getBeanClassName());
				Enum enumz=clasz.getAnnotation(Enum.class);
				Field[] fields = clasz.getDeclaredFields();
				List<EnumInfo> enumInfos = new ArrayList<>();
				List<EnumValue> enumValues = new ArrayList<>();
				for (Field field : fields) {
					try {
						Object obj = field.get(field.getName());
						EnumValue ev = field.getAnnotation(EnumValue.class);
						if (obj instanceof EnumInfo && ev != null) {
							enumInfos.add((EnumInfo)obj);
							enumValues.add(ev);
						}
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
				EnumHandler.addEnum(enumz, enumInfos, enumValues);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}

	public static class EnumSanServiceClassPathScanHandle extends ClassPathBeanDefinitionScanner {

		public EnumSanServiceClassPathScanHandle(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
			super(registry, useDefaultFilters);
		}

		@Override
		protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
			addIncludeFilter(new AnnotationTypeFilter(Enum.class));
			Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
			return beanDefinitionHolders;
		}
	}
	
	public static class EnumBeanNameGenerator extends AnnotationBeanNameGenerator{

		@Override
		public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
			try {
				Enum  e = Class.forName(definition.getBeanClassName()).getAnnotation(Enum.class);
				return "enum_"+e.type();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return super.generateBeanName(definition, registry);
		}
		
	} 
}
