package com.jieshun.ops.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class SendMsgUtil {
	
//	private static String sendMsgUrl = "http://10.10.205.22:8080/SendMsg/sendmsg/send.do";
	private static String sendMsgUrl = "http://10.10.205.13:8080/SendMsg/sendmsg/send.do";
	
	public static boolean sendMessage(String phones, String message) {
		try {
			List<NameValuePair> params =new ArrayList<>();
			params.add(new BasicNameValuePair("phones", phones));
			params.add(new BasicNameValuePair("message", message));
			
			HttpPost httpPost = new HttpPost(sendMsgUrl);
			httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
			String param = null;
			param = EntityUtils.toString(new UrlEncodedFormEntity(params,"UTF-8"));
			// build get uri with params
			httpPost.setURI(new URIBuilder(httpPost.getURI().toString() + "?" + param).build());
			return sendHttpPost(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private static boolean sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			httpClient = HttpClients.createDefault();
			// httpPost.setConfig(config);
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}
