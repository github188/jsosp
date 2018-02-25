package com.jieshun.ops.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class OfUtil {
	
	static String url = "http://preof.jslife.net:9090/plugins/presence/status?jid=";

	public static void main(String[] args) throws Exception {
		OpenfireBean op = new OpenfireBean();
		op.setHost("http://preof.jslife.net:9090/");
		op.setOpenfire_domain("ts-test-of-n");
		op.setPassword("Tstest@jsof322");
		op.setUsername("admin");

		GetOpenfireUserInfo getOpenfireUserInfo = new GetOpenfireUserInfo(op);
		HashMap<String, String> map = getOpenfireUserInfo.getUserOnlineInfo();

		System.out.println(map);

		int i = OfUtil.isonline("0000000616");
		int j = OfUtil.isonline("1232");
		int k = OfUtil.isonline("0000000497");

		System.out.println("" + i + j + k);
	}

	// 0 - 用户不存在; 1 - 用户在线; 2 -用户离线
	public static int isonline(String code) {
		code = url+ code+"@ts-test-of-n&type=xml";
		int line = 0; // -不存在-

		try {
			URL oUrl = new URL(code);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
				if (reader != null) {
					String strFlag = reader.readLine();
					reader.close();

					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						line = 2;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						line = 0;
					} else if (strFlag.indexOf("priority") >= 0 || strFlag.indexOf("id=\"") >= 0) {
						line = 1;
					}
				}
			}
		} catch (Exception e) {
		}

		return line;
	}
	
	// 0 - 用户不存在; 1 - 用户在线; 2 -用户离线
	public static int isonline(String OfUrl,String code,String domain) throws Exception {
		String strUrl = OfUrl+ "/plugins/presence/status?jid="+code+"@"+domain+"&type=xml";
		int line = 0; // -不存在-
		
		URL oUrl = new URL(strUrl);
		URLConnection oConn = oUrl.openConnection();
		if (oConn != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(oConn.getInputStream()));
			if (reader != null) {
				String strFlag = reader.readLine();
				reader.close();
				
				if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
					line = 2;
				}
				if (strFlag.indexOf("type=\"error\"") >= 0) {
					line = 0;
				} else if (strFlag.indexOf("priority") >= 0 || strFlag.indexOf("id=\"") >= 0 || strFlag.indexOf("hash=\"") >= 0) {
					line = 1;
				}
//				else if (strFlag.indexOf("priority") >= 0 || strFlag.indexOf("id=\"") >= 0) {
//					line = 1;
//				}
			}
		}
		return line;
	}
}
