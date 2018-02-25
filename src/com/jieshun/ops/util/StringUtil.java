package com.jieshun.ops.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StringUtil {
	
	public static String ConvertStream2Json(InputStream inputStream)
    {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try
        {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1)
            {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonStr;
    }
	
	public static String md5(String str){
		String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
					i += 256;
				}
                if (i < 16) {
					buf.append("0");
				}
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
		return re_md5;
	}
	
	public static String Undefined2Str(String input){
		if(input==null||"undefined".equals(input)){
			return "";
		}else{
			return input;
		}
	}
	
	public static boolean EnsureTels(String teles){
		try {
			String[] tels = teles.split(",");
			for (int i = 0; i < tels.length; i++) {
				if(tels[i].length()!=11){
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String CheckDates(Date date){
		try {
			Date now =new Date();
			
			long period = now.getTime()-date.getTime(); 
			String ret = "";
			if(period > 60*1000){
				ret =  ">1分钟";
			}
			if (period > 60*60*1000) {
				ret =  ">1小时";
			}
			if (period > 24*60*60*1000) {
				ret =  ">1天";
			}
			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static HashMap<String, String> msg(String str) {
		HashMap<String, String> returnMap = new HashMap<>();
		String[] msgs = str.split(" ");
		for (String msg : msgs) {
			if (msg.contains("dcs")) {
				returnMap.put("dcs", msg.replace(",", "").substring(msg.indexOf(":") + 1));
			}
			if (msg.contains("fm")) {
				returnMap.put("fm", msg.replace(",", "").substring(msg.indexOf(":") + 1));
			}
			if (msg.contains("fcs")) {
				returnMap.put("fcs", msg.replace(",", "").substring(msg.indexOf(":") + 1));
			}
		}
		return returnMap;
	}
	
	public static boolean checkLocks(List<Map<String,Object>> locks){
		for (Map<String, Object> map : locks) {
			if(!"0".equals(map.get("duration").toString())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @description 获取当前系统时间（java.sql）
	 * @return Timestamp
	 */
	public static Timestamp getCurTime() {
		Date nowTime = new Date();
		Timestamp result = new Timestamp(nowTime.getTime());
		return result;
	}
	
}
