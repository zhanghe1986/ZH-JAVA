package com.dahe.base.common.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static String object2Json(Object obj) {
		if (obj == null) {
			return "";
		}
		String result = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			result = objectMapper.writeValueAsString(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static Map<?, ?> json2Map(String json) {
		return json2Object(json, Map.class);
	}
	
	public static <T> T json2Object(String json, Class<T> clazz) {
		T result = null;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			result = objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static <T> T conventerObject(Object sourceObject, Class<T> destinationObjectType) {
		String jsonContent = object2Json(sourceObject);
		return json2Object(jsonContent, destinationObjectType);
	}
	
	public static <T> List<T> fromJsonList(String json, Class<T> clazz) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}
}
