package com.cloudok.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ReflectionUtils {

	private static ConcurrentHashMap<Class<?>, Field[]> classCache = new ConcurrentHashMap<Class<?>, Field[]>();

	public static Field[] getAllFields(Class<?> clazz) {

		Field[] fields = classCache.get(clazz);
		if (fields != null) {
			return fields;
		}
		synchronized (clazz) {
			fields = classCache.get(clazz);
			if (fields != null) {
				return fields;
			}
			List<Field> fieldList = new ArrayList<>();
			while (clazz != null) {
				fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
				clazz = clazz.getSuperclass();
			}
			fields = new Field[fieldList.size()];
			fieldList.toArray(fields);
			return fields;
		}

	}

	public static Field[] getAllFieldByType(Class<?> clazz, Class<?> fieldType) {
		List<Field> fieldList = new ArrayList<>();
		fieldList.addAll(Arrays.asList(getAllFields(clazz)));
		return fieldList.stream().filter(item -> item.getType() == fieldType).collect(Collectors.toList()).toArray(new Field[] {});
	}
	
	
}
