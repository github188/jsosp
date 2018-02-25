package com.jieshun.ops.task.bizservice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.jieshun.ops.model.LogServiceRequestDO;
import com.jieshun.ops.util.StringUtils;

/**
 * 捷顺业务接口演示代码
 * 
 * @author 刘淦潮
 * 
 */
public abstract class APIService {
	
	private static final Logger logger = Logger
			.getLogger(APIService.class);

	private static String funcurl = "http://www.jslife.com.cn/jsaims/as";
	
	private static String loginUrl="http://www.jslife.com.cn/jsaims/login";

	private String token = null;

	private Properties loginProp;

	protected void setLoginProperties(Properties prop) {
		this.loginProp = prop;
	}

	/**
	 * 构造HTTP请求实体
	 * 
	 * @param param
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	protected HttpEntity constructHttpEntity(String param)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, LoginException {
		/*if (token == null) {
			token = new Login().login(loginProp.getProperty("cid"), loginProp.getProperty("usr"),
					loginProp.getProperty("psw"));
		}*/
		if(token==null) {
			logger.debug("Token 为空!!!!");
			throw new LoginException(-1,"登录失败，无法获取token!");
		}
		String signKey = (loginProp.getProperty("signKey") == null) ? "" : loginProp.getProperty("signKey");
		logger.debug("当前signKey---->" + signKey);

		// 生成MD5签名
		MessageDigest md5Tool = MessageDigest.getInstance("MD5");
		byte[] md5Data = md5Tool.digest((param + signKey).getBytes("UTF-8"));
		String sn = StringUtils.toHexString(md5Data);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("cid", this.loginProp.getProperty("cid")));
		list.add(new BasicNameValuePair("v", this.loginProp.getProperty("v")));
		list.add(new BasicNameValuePair("p", param));
		list.add(new BasicNameValuePair("sn", sn));// MD5特征码
		list.add(new BasicNameValuePair("tn", token));// 取token

		HttpEntity en = new UrlEncodedFormEntity(list, "UTF-8");

		logger.debug("调用参数:" + param);
		return en;
	}

	/**
	 * API执行方法，此方法是一个模板方法，子类无需实现
	 */
	final public LogServiceRequestDO run() {

		// TODO Auto-generated method stub
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse response = null;
		LogServiceRequestDO lsr = new LogServiceRequestDO();
		long startTime = 0;
		try {
			lsr.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			lsr.setProjectCode(loginProp.getProperty("code"));

			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(funcurl))
					.setEntity(constructHttpEntity(buildRequestParam())).build();
			// 发起请求
			startTime = System.currentTimeMillis();
			lsr.setRequestTime(new Date(startTime));
			response = httpclient.execute(requst);

			long endTime = System.currentTimeMillis();
			lsr.setResponseTime(new Date(endTime));
			lsr.setElapsedTime(endTime - startTime);

			extractResult(response, lsr);
			return lsr;
		} catch (Exception e) {
			e.printStackTrace();
			if(startTime>0) {
				long endTime = System.currentTimeMillis();
				lsr.setResponseTime(new Date(endTime));
				lsr.setElapsedTime(endTime - startTime);
			}
			lsr.setMsgCode("-1");
			lsr.setMsgDesc(e.getMessage());
			return lsr;
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
	 * 设置已知的token
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setLoginUrl(String url) {
		loginUrl=url;
	}
	
	public void setServiceURL(String url) {
		funcurl=url;
	}

	/**
	 * 构造服务请求的参数
	 * 
	 * @return
	 */
	protected abstract String buildRequestParam();

	/**
	 * 解释请求结果
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected abstract void extractResult(CloseableHttpResponse response, LogServiceRequestDO lsr) throws Exception;
}
