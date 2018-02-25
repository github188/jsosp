package com.jieshun.ops.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GetOpenfireUserInfo
{
  private OpenfireBean openfireBean;
  private HttpClient defaultHttpClient;
  private static final Logger log = Logger.getLogger(GetOpenfireUserInfo.class);

  public GetOpenfireUserInfo(OpenfireBean openfireBean) throws ClientProtocolException, IOException {
    this.openfireBean = openfireBean;
    this.defaultHttpClient = HttpClientUtil.getClient();
    login();
  }

  public HashMap<String, String> getUserOnlineInfo() throws ClientProtocolException, IOException {
    HttpGet httpGet = new HttpGet(this.openfireBean.getSummerUrl());
    HttpResponse hr = this.defaultHttpClient.execute(httpGet);
    HashMap map = new HashMap();
    if (hr.getStatusLine().getStatusCode() == 200) {
      HttpEntity entity = hr.getEntity();
      map = new HashMap();
      if (entity != null) {
        Document doc = Jsoup.parse(EntityUtils.toString(entity));
        Elements elements = doc.select("div.jive-table tr");
        for (Element element : elements) {
          map.put(element.select("td:nth-child(2)").text(), element.select("td:nth-child(8)").text());
        }
      }
    }
    return map;
  }

  private void login() throws ClientProtocolException, IOException {
    HttpPost httpPost = new HttpPost(this.openfireBean.getLoginUrl());
    httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=gbk");
    List nvps = new ArrayList();
    nvps.add(new BasicNameValuePair("username", this.openfireBean.getUsername()));
    nvps.add(new BasicNameValuePair("password", this.openfireBean.getPassword()));
    nvps.add(new BasicNameValuePair("login", "true"));
    nvps.add(new BasicNameValuePair("url", "/index.jsp"));
    httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    HttpResponse hr = this.defaultHttpClient.execute(httpPost);
    System.out.println(hr);
  }
}