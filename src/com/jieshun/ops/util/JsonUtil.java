/**
 * Project Name:jsosp
 * File Name:JsonUtil.java
 * Package Name:com.jsst.ops.utils
 * Date:2017年3月6日下午1:55:24
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/  

package com.jieshun.ops.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;


/**
 * ClassName:JsonUtil <br/>
 * Function: java对象与json转换工具类. <br/>
 * Date:     2017年3月3日 下午1:55:24 <br/>
 * @author   yuteng 
 * @version
 * @since    JDK 1.7
 * @see
 */
public class JsonUtil {
	
	/**
	 * objectToJsonStr:(java对象转换为json字符串). <br/>
	 *
	 * @author yuteng
	 * @param obj java对象
	 * @return json字符串
	 * @throws MapperException
	 * @since JDK 1.7
	 */
	public static String objectToJsonStr(Object obj) throws MapperException {
		JSONValue jsonValue = JSONMapper.toJSON(obj);
		String jsonStr = jsonValue.render(false);
		return jsonStr;
	}
	
	/**
	 * simpleListToJsonStr:(list转换为json字符串). <br/>
	 *
	 * @author yuteng
	 * @param list
	 * @param claList
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @since JDK 1.7
	 */
	public static String simpleListToJsonStr(List<?> list, List<?> claList)
			throws IllegalArgumentException, IllegalAccessException {
		if (list == null || list.size() == 0) {
			return "[]";
		}
		String jsonStr = "[";
		for (Object object : list) {
			jsonStr += simpleObjectToJsonStr(object, claList) + ",";
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "]";
		return jsonStr;
	}
	
	/**
	 * simpleObjectToJsonStr:(将对象和集合封装为json字符串). <br/>
	 *
	 * @author yuteng
	 * @param obj 对象
	 * @param claList 集合 list
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @since JDK 1.7
	 */
	public static String simpleObjectToJsonStr(Object obj, List<?> claList)
			throws IllegalArgumentException, IllegalAccessException {
		if (obj == null) {
			return "null";
		}
		String jsonStr = "{";
		Class<?> cla = obj.getClass();
		Field fields[] = cla.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getType() == long.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.getLong(obj) + ",";
			} else if (field.getType() == double.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.getDouble(obj) + ",";
			} else if (field.getType() == float.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.getFloat(obj) + ",";
			} else if (field.getType() == int.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.getInt(obj) + ",";
			} else if (field.getType() == boolean.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.getBoolean(obj) + ",";
			} else if (field.getType() == Integer.class || field.getType() == Boolean.class
					|| field.getType() == Double.class || field.getType() == Float.class
					|| field.getType() == Long.class) {
				jsonStr += "\"" + field.getName() + "\":" + field.get(obj) + ",";
			} else if (field.getType() == String.class) {
				jsonStr += "\"" + field.getName() + "\":\"" + field.get(obj) + "\",";
			} else if (field.getType() == List.class) {
				String value = simpleListToJsonStr((List<?>) field.get(obj), claList);
				jsonStr += "\"" + field.getName() + "\":" + value + ",";
			} else {
				if (claList != null && claList.size() != 0 && claList.contains(field.getType())) {
					String value = simpleObjectToJsonStr(field.get(obj), claList);
					jsonStr += "\"" + field.getName() + "\":" + value + ",";
				} else {
					jsonStr += "\"" + field.getName() + "\":null,";
				}
			}
		}
		jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
		jsonStr += "}";
		return jsonStr;
	}
	
	/**
	 * 读取json文件
	 * @param Path 文件路径
	 * @return
	 */
	public String ReadJsonFile(String path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			InputStream in = JsonUtil.class.getClassLoader().getResourceAsStream(File.separator + path);
			InputStreamReader inputStreamReader = new InputStreamReader(in, "GBK");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
	
}
