package com.jieshun.ops.task.bizservice;

import java.io.IOException;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 用户登录类
 * @author 刘淦潮
 *
 */
public class Login {
	
	
	private static String loginurl="http://www.jslife.com.cn/jsaims/login";
	
	
	public Login(){
		
	}
	
	/**
	 * 用户登录
	 * @param cid 客户编号
	 * @param usr 用户帐户
	 * @param psw 用户密码
	 * @return
	 */
	public  String login(String cid, String usr, String psw) throws LoginException {
		String url = loginurl;
		
		System.out.println("cid:"+cid+"\tusr:"+usr+"\tpsw:"+psw);
		BasicCookieStore cookieStore = new BasicCookieStore();
		CloseableHttpClient httpclient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore).build();
		CloseableHttpResponse response = null;
		try {

			ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("cid", cid));
			list.add(new BasicNameValuePair("usr", usr));
			list.add(new BasicNameValuePair("psw", psw));
			HttpEntity en = new UrlEncodedFormEntity(list, "UTF-8");

			HttpUriRequest login = RequestBuilder.post().setUri(new URI(url))
					.setEntity(en).build();
			response = httpclient.execute(login);
			int statusCode=response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json=new JsonParser().parse(results).getAsJsonObject();
				int resultCode=json.get("resultCode").getAsInt();
				if(resultCode==0){
					System.out.println("登录成功！");
					return json.get("token").getAsString();
				}else{
					throw new LoginException(resultCode,json.get("message").getAsString());
				}
			} else {
				throw new LoginException(statusCode,"登录失败！");
				
			}
		}catch(LoginException e){
			throw e;
		} catch (Exception e) {
			throw new LoginException(-1,e.getMessage());
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}

	}
	
}
