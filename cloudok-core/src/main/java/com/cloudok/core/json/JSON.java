package com.cloudok.core.json;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import com.cloudok.core.context.SpringApplicationContext;
import com.cloudok.core.exception.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author xiazhijian
 * @date Apr 15, 2019 4:16:54 PM
 * 
 */
public class JSON {

	/**
	 * ToJson
	 * 
	 * @param t
	 * @return
	 */
	public static <T> String toJSONString(T t) {
		if(Objects.isNull(t)) {
			return null;
		}
		try {
			return mapper().writeValueAsString(t);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ToObject
	 * 
	 * @param json
	 * @param classz
	 * @return
	 */
	public static <T> T parse(String json, Class<T> classz) {
		try {
			return mapper().readValue(json, classz);
		} catch (IOException e) {
			throw new SystemException("json convert exception");
		}
	}

	/**
	 * 获取mapper
	 * 
	 * @return
	 */
	public static ObjectMapper mapper() {
		return SpringApplicationContext.getBean(ObjectMapper.class);
	}
	
	public static <T> T parseObject(String str, TypeReference<T> typeReference) {
		try {
			return mapper().readValue(str, typeReference);
		} catch (Exception e) {
			throw new SystemException("json convert exception");
		}
	}

	public static <T> T parseObject(Reader reader, TypeReference<T> typeReference) {
		try {
			return mapper().readValue(reader, typeReference);
		} catch (IOException e) {
			throw new SystemException("json convert exception");
		}
	}
	
	/**
	 * 
	 * @param json
	 * @return
	 */
	public static JsonNode parseJSONTree(String json) {
		try {
			return mapper().readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemException("json转换失败");
		}
	}
}
