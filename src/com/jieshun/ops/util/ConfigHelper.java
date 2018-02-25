package com.jieshun.ops.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConfigHelper {

	private static HashMap<String, Properties> cached = new HashMap<String, Properties>();

	public static Properties getProperties(String fileName) {
		if (cached.containsKey(fileName)) {
			return cached.get(fileName);
		}
		Properties prop = loadProperties(fileName);
		if (prop != null) {
			cached.put(fileName, prop);
		}
		return prop;
	}

	private static Properties loadProperties(String fileName) {
		try {
			String pattern = "\\.properties$/";
			String path = fileName.matches(pattern) ? fileName : fileName + ".properties";
			InputStream in = ConfigHelper.class.getClassLoader().getResourceAsStream(path);   
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
