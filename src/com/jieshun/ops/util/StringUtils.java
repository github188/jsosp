package com.jieshun.ops.util;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;



/**
 * 工具类
 * @author yt
 *
 */
public class StringUtils {
	
	/**
	 * 判断字符串是否为空 是返回true
	 * @param String
	 * @return boolean
	 */
	public static boolean isEmpty(String s) {
		if (s == null || s.equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * @description 判断对象是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if ((obj instanceof List)) {
			return ((List<?>) obj).size() == 0;
		}
		if ((obj instanceof String)) {
			return ((String) obj).trim().equals("");
		}
		return false;
	}
	
	/**
	 * 获取客户端请求的ip
	 * @param HttpServletRequest
	 * @return 客户端ip
	 */
	public static String getRequestIP(HttpServletRequest req) {
		String ip = req.getHeader("X-Forwarded-For");
		if (!isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = req.getHeader("X-Real-IP");
		if (!StringUtils.isEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = req.getRemoteAddr();
		if (!isEmpty(ip) && ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	
	/**
	 * 生成32位字符串
	 * @return String
	 */
	public static String getUUId() {
		String result = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return result;
	}
	
	/** 
	 * 返回长度为【strLength】的随机数，在前面补0 
	 * @return String
	 */  
	public static String getFixLenthString(int strLength) {  
	    Random rm = new Random();  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    String fixLenthString = String.valueOf(pross);  
	    return fixLenthString.substring(1, strLength + 1);  
	}
	
	/**
	 * 获取config.peoperties配置文件中配置项的属性
	 * @return
	 */
	public static String getPropertiesAttr(String name) {
		String attr = "";
		try {
			Properties pro = ConfigHelper.getProperties("config");
			attr = pro.getProperty(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attr;
	}
	
	/**
	 * 获取项目绝对路径
	 * @return
	 */
	public static String getProBasePath() {
		String base = "";
		File directory = new File("");// 设定为当前文件夹
		try {
			base = directory.getCanonicalPath();
		} catch (Exception e) {
			
		}
		return base;
	}
	
	public static JSONArray readJsonFile(String filePath) { 
		String JsonContext = new JsonUtil().ReadJsonFile(filePath);  
        JSONArray jsonArray = JSONArray.fromObject(JsonContext);  
		return jsonArray;
	}
	
	/**
	 * 字节转16进制字符串
	 * @param bytes 字节数组
	 * @return 返回16进制的字符串
	 */
	public static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(String.format("%02X", bytes[i]));
		}
		return buffer.toString();
	}
	
	
//	public static void main(String[] args) {
//		StringUtils su = new StringUtils();
//		String json = "[{\"phone\":\"18603071010\",\"content\":\"测试测试测试\"},"
//				+ "{\"phone\":\"18603071010\",\"content\":\"测试测试测试\"},"
//				+ "{\"phone\":\"18603071010\",\"content\":\"测试测试测试\"}]";
//		
//	}
	
}
