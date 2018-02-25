package com.jieshun.ops.task.offline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jieshun.ops.master.model.OpenfireDO;
import com.jieshun.ops.util.HttpClientUtil;
import com.jieshun.ops.util.StringUtils;

public class OpenfireConnector implements IOpenfireService{

	private CloseableHttpClient defaultHttpClient;

	private OpenfireDO openfireDo;
	
	private Map<String, String> data=null;

	public OpenfireConnector(OpenfireDO openfireDo) {
		this.openfireDo = openfireDo;
		defaultHttpClient = HttpClientUtil.getClient();
	}

	@Override
	public boolean isOnline(String jid) {
		if(data==null){
			try {
				data=getOFOnlineProject();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		if(data.containsKey(jid) && "ONLINE".equals(data.get(jid))){
			return true;
		}
		return false;
	}
	/**
	 * 根据openfire帐号获取openfire在线项目集合
	 * 
	 * @param openfire帐号
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> getOFOnlineProject() throws Exception {

		// 先登录openfire服务
		if (!login()) {
			return null;
		}

		HttpGet httpGet = new HttpGet(this.openfireDo.getSummerUrl());
		HttpResponse hr = defaultHttpClient.execute(httpGet);
		if (hr.getStatusLine().getStatusCode() == 200) {
			Map<String, String> onlineMap = new HashMap<String, String>();
			HttpEntity entity = hr.getEntity();
			// 在线项目集合
			if (entity != null) {
				String responseString = EntityUtils.toString(entity, "UTF-8");
				System.out.println("Response content: " + responseString);
				Document doc = Jsoup.parse(responseString);
				Elements elements = doc.select("div.jive-table tr");
				String code = "";
				String status = "";
				for (Element element : elements) {
					code = element.select("td:nth-child(2)").text();
					status = element.select("td:nth-child(8)").text();

					if (!StringUtils.isEmpty(code.trim())
							&& !StringUtils.isEmpty(status.trim())) {
						if (!status.equalsIgnoreCase("Online")) {
							status = "Online";
						}
						onlineMap.put(code.trim(), status.trim().toUpperCase());
					}
				}
			}
			return onlineMap;
		}
		return null;
	}

	/**
	 * 登录openfire服务
	 * 
	 * @return
	 */
	private boolean login() {
		try {
			HttpPost httpPost = new HttpPost(this.openfireDo.getLoginUrl());
			httpPost.addHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=gbk");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", this.openfireDo
					.getOfAccount()));
			nvps.add(new BasicNameValuePair("password", this.openfireDo
					.getOfPasswd()));
			nvps.add(new BasicNameValuePair("login", "true"));
			nvps.add(new BasicNameValuePair("url", "/index.jsp"));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse hr = defaultHttpClient.execute(httpPost);
			if (hr.getStatusLine().getStatusCode() == 302){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

}
