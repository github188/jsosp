package com.jieshun.ops.comm.sms;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class SMSSender {
	private static final Logger logger = Logger.getLogger(SMSSender.class);

	//@Value("${jsstsms.ur}")
	//private static String smsUrl="http://jsstsms.jslife.net:18080/jsstsms/SmsServlet";
	private static String smsUrl="http://10.10.205.4:18080/jsstsms/SmsServlet";

	public int sendMessage(String phones, String message) {
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse response = null;
		int result=-1;
		try {
			// 发起请求
			long startTime = System.currentTimeMillis();
			HttpUriRequest requst = RequestBuilder.post()
					.setUri(new URI(smsUrl))
					.setEntity(getEntity(phones, message)).build();
			requst.addHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=utf-8");
			response = httpclient.execute(requst);
			result=extractResult(response);
			logger.info("短信发送状态【"+result+"】:"+transform(result));
			long endTime = System.currentTimeMillis();
			logger.info("SMSSender调用耗时----->" + (endTime - startTime) / 1000
					+ "." + (endTime - startTime) % 1000 + "秒");
			return result;
		} catch (Exception e) {
			logger.info("短信发送状态【"+result+"】:"+transform(result));
			logger.debug("sendMessage Exception", e);
			return -1;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private HttpEntity getEntity(String phones, String message)
			throws UnsupportedEncodingException {
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		JsonArray ja = new JsonArray();
		JsonObject jsonParam = new JsonObject();
		jsonParam.addProperty("phone", phones);
		jsonParam.addProperty("content", message);
		ja.add(jsonParam);
		list.add(new BasicNameValuePair("p", ja.toString()));

		HttpEntity en = new UrlEncodedFormEntity(list, "UTF-8");
		return en;
	}

	protected int extractResult(CloseableHttpResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {// 成功调用
			String results = EntityUtils.toString(response.getEntity());
			JsonObject json = new JsonParser().parse(results).getAsJsonObject();
			return json.get("resultCode").getAsInt();
			
		} else {
			return -1;
		}
	}

	private String transform(int code){
		String msg="";
		switch(code){
		case -1:
			msg="未知异常！";
			break;
		case 0:
			msg="发送成功！";
			break;
		case 9:
			msg="访问地址不存在！";
			break;
		case 102:
			msg="短信内容超过1000字（包括1000字）或为空！";
			break;
		case 103:
			msg="手机号码超过200个或合法手机号码为空或者与通道类型不匹配！";
			break;
		case 109:
			msg="业务代码不存在或者通道关闭！";
			break;
		case 110:
			msg="扩展号不合法！";
			break;
		}
		return msg;
	}
}
