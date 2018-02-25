package com.jieshun.ops.autode.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jieshun.ops.autode.service.IAutoService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.FTPUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:13:57
 */
@Controller
@RequestMapping("/jsosp")
public class AutoController extends BaseController {

	@Resource
	private IAutoService autoService;

	private static final Logger logger = Logger.getLogger(AutoController.class);
	private static String baseUrl = "http://localhost:8000/";
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/automation", method = RequestMethod.POST)
	@ResponseBody
	public String automation(HttpServletRequest request) {
		try {
			String service = "jsaims".equals(request.getParameter("service"))?"jsstApp":request.getParameter("service");
			String ip = request.getParameter("ip");
			String date = request.getParameter("date");
			String address = request.getParameter("address");
			String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			
			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("service", service);
			attributes.addProperty("ip", ip);
			attributes.addProperty("date", date);
			attributes.addProperty("address", address);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);

			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");

			String url = baseUrl + "automation/";

			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("自动化部署SUCCESS");
					autoService.insertdeploy(UUID.randomUUID().toString().replace("-", "") , ip, service, time, date, "部署成功");
				} else {
					logger.info("自动化部署FAIL");
					autoService.insertdeploy(UUID.randomUUID().toString().replace("-", "") , ip, service, time, date, "部署失败");
				}
				return results;
			} else {
				System.out.println("自动化部署FAIL:内部错误");
				autoService.insertdeploy(UUID.randomUUID().toString().replace("-", "") , ip, service, time, date, "部署失败");
				return "{\"params\": {\"result\": \"fail\"}}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryversion", method = RequestMethod.POST)
	@ResponseBody
	public String queryversion(HttpServletRequest request) {
		try {
			String ip = request.getParameter("ip");
			
			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("ip", ip);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);
			
			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");
			
			String url = baseUrl + "queryversion/";
			
			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("查询历史版本信息SUCCESS");
				} else {
					logger.info("查询历史版本信息FAIL");
				}
				return results;
			} else {
				System.out.println("查询历史版本信息FAIL:内部错误");
				return "{\"params\": {\"result\": \"fail\"}}";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}

	/**
	 * 用户登出
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/backup", method = RequestMethod.POST)
	@ResponseBody
	public String backup(HttpServletRequest request) {
		try {
			String ip = request.getParameter("ip");
			String date = request.getParameter("date");
			String service = request.getParameter("service");

			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("ip", ip);
			attributes.addProperty("date", date);
			attributes.addProperty("service", service);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);

			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");

			String url = baseUrl + "backup/";

			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("回滚SUCCESS");
				} else {
					logger.info("回滚FAIL");
				}
				return results;
			} else {
				System.out.println("回滚FAIL:内部错误");
				return "{\"params\": {\"result\": \"fail\"}}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}

	@RequestMapping(value = "/restart", method = RequestMethod.POST)
	@ResponseBody
	public String restart(final HttpServletRequest request) {
		try {
			Task task = new Task(request.getParameter("ip"), request.getParameter("service"), baseUrl);
			ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1,
					new BasicThreadFactory.Builder().namingPattern("AutoController-restart-schedule-pool-%d").daemon(true).build());
			long delay = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(request.getParameter("date")).getTime()
					- System.currentTimeMillis();

			scheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);

			return "{\"params\": {\"result\": \"success\"}}";

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}

	}

	@RequestMapping(value = "/machine", method = RequestMethod.POST)
	@ResponseBody
	public String machine(final HttpServletRequest request) {
		try {
			String ip = request.getParameter("ip");

			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("ip", ip);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);

			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");

			String url = baseUrl + "machine/";

			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("获取SUCCESS");
				} else {
					logger.info("获取FAIL");
				}
				return results;
			} else {
				System.out.println("获取FAIL:内部错误");
				return "{\"params\": {\"result\": \"fail\"}}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}

	@RequestMapping(value = "/checkproperties", method = RequestMethod.POST)
	@ResponseBody
	public String checkproperties(final HttpServletRequest request) {
		StringBuffer history = new StringBuffer();
		JsonObject resp = new JsonObject();
		JsonObject params1 = new JsonObject();
		JsonObject params2 = new JsonObject();
		try {
			String clientip = request.getRemoteAddr();
			System.out.println(clientip);
			String ip = request.getParameter("ip");
			params2.addProperty("ip", ip);
			params2.addProperty("result", "true");
			String path = request.getParameter("path");
			String file = request.getParameter("file");
			String service = request.getParameter("service");
			//从服务器上拷贝到ansible服务器
			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("ip", ip);
			attributes.addProperty("file", file);
			attributes.addProperty("service", service);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);

			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");

			String url = baseUrl + "checkproperties/";

			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);

			history.append("ip:"+ip+",");
			history.append("服务类型:"+service+";;");
			/*history.append("对比当前运行配置结果如下,"+"备份文件是否缺少配置项:");
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("拷贝文件从"+ip+"到ansible服务器成功");
					history.append("否");
					params2.addProperty("result", "true");
				} else {
					logger.info("拷贝文件从"+ip+"到ansible服务器失败");
					String diff = params.get("history").toString().replace("\"", "").replace("\\n", "\n");
					logger.info("python返回结果："+diff);
					ArrayList<String> bak = new ArrayList<>();
					ArrayList<String> inuse = new ArrayList<>();
					
					String[] diffs = diff.split("\n");
					for (int i = 0; i < diffs.length; i++) {
						if(diffs[i].startsWith("<")&&diffs[i].trim().length()!=1){
							bak.add(diffs[i].substring(1).trim());
						}
						if(diffs[i].startsWith(">")&&diffs[i].trim().length()!=1){
							inuse.add(diffs[i].substring(1).trim());
						}
					}
					logger.info("bak:"+bak+",inuse"+inuse);
					int flag = 0;
					for (Iterator iterator = inuse.iterator(); iterator.hasNext();) {
						String use = (String) iterator.next();
						if(!bak.contains(use)&&!use.trim().equals("\\r")){
							history.append(use+",");
							flag = 1;
							params2.addProperty("result", "false");
						}
						
					}
					if(flag ==0 ){
						history.append("否");
						params2.addProperty("result", "true");
					}
//					return "{\"params\": {\"result\": \"fail\",\"history\": \""+history+"\"}}";
				}
			} else {
				System.out.println("拷贝文件从"+ip+"到ansible服务器异常:内部错误");
//				return "{\"params\": {\"result\": \"fail\"}}";
				history.append("内部错误");
				params2.addProperty("result", "false");
			}
			history.append(";;");*/
			//从ftp拷贝到ansible服务器
			
			history.append("对比外发配置结果如下,"+"备份文件是否缺少配置项:");
			FTPUtil ftp = new FTPUtil("10.10.202.2", 21, "bs", "bs");
			String[] files = file.split(",");
			for (int i = 0; i < files.length; i++) {
				ftp.get(path+files[i], "/home/jht/test/check."+service+"/"+ip+"/"+files[i]);
			}
			int flag = 0;
			for (int i = 0; i < files.length; i++) {
				history.append(files[i]+":");
				if("jscsp".equals(service)){
					BufferedReader  ftpFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/"+files[i])));
					BufferedReader  localFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/home/jht/projects/project/00.SetBak/com.jht.cloud.config/config/"+files[i])));
					ArrayList<String> ftpKeys = new ArrayList<>();
					String ftpKey;
					ArrayList<String> localKeys = new ArrayList<>();
					String localKey;
					while ((ftpKey = ftpFile.readLine()) != null) {
						if(!ftpKey.trim().isEmpty()&&!ftpKey.trim().contains("#")&&ftpKey.trim().contains("=")){
							ftpKeys.add(ftpKey.trim().split("=")[0].trim());
						}
					}
					while ((localKey = localFile.readLine()) != null) {
						if(!localKey.trim().isEmpty()&&!localKey.trim().contains("#")&&localKey.trim().contains("=")){
							localKeys.add(localKey.trim().split("=")[0].trim());
						}
					}
					for (String string : ftpKeys) {
						logger.info("ftpKeys:"+string);
					}
					for (String string : localKeys) {
						logger.info("localKeys:"+string);
					}
					ftpFile.close();
					localFile.close();
					
					for (Iterator iterator = ftpKeys.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if(!localKeys.contains(key)){
							history.append(key+",   ");
							flag = 1;
							params2.addProperty("result", "false");
						}
					}
					if(flag == 0){
						history.append("否");
					}
					flag = 0;
					logger.info("返回信息:   "+history);
				}
				if("jsaims".equals(service)){
					BufferedReader  ftpFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/"+files[i])));
					BufferedReader  localFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/home/jht/projects/project/00.SetBak/jsaims/"+files[i])));
					ArrayList<String> ftpKeys = new ArrayList<>();
					String ftpKey;
					ArrayList<String> localKeys = new ArrayList<>();
					String localKey;
					while ((ftpKey = ftpFile.readLine()) != null) {
						if(!ftpKey.trim().isEmpty()&&!ftpKey.trim().contains("#")&&ftpKey.trim().contains("=")){
							ftpKeys.add(ftpKey.trim().split("=")[0].trim());
						}
					}
					while ((localKey = localFile.readLine()) != null) {
						if(!localKey.trim().isEmpty()&&!localKey.trim().contains("#")&&localKey.trim().contains("=")){
							localKeys.add(localKey.trim().split("=")[0].trim());
						}
					}
					for (String string : ftpKeys) {
						logger.info("ftpKeys:"+string);
					}
					for (String string : localKeys) {
						logger.info("localKeys:"+string);
					}
					ftpFile.close();
					localFile.close();
					
					for (Iterator iterator = ftpKeys.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if(!localKeys.contains(key)){
							history.append(key+",   ");
							flag = 1;
							params2.addProperty("result", "false");
						}
					}
					if(flag == 0){
						history.append("否");
					}
					flag = 0;
					logger.info("返回信息:   "+history);
				}
				if("jsifs".equals(service)){
					BufferedReader  ftpFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/"+files[i])));
					BufferedReader  localFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/home/jht/projects/project/00.SetBak/"+files[i])));
					ArrayList<String> ftpKeys = new ArrayList<>();
					String ftpKey;
					ArrayList<String> localKeys = new ArrayList<>();
					String localKey;
					while ((ftpKey = ftpFile.readLine()) != null) {
						if(!ftpKey.trim().isEmpty()&&!ftpKey.trim().contains("#")&&ftpKey.trim().contains("=")){
							ftpKeys.add(ftpKey.trim().split("=")[0].trim());
						}
					}
					while ((localKey = localFile.readLine()) != null) {
						if(!localKey.trim().isEmpty()&&!localKey.trim().contains("#")&&localKey.trim().contains("=")){
							localKeys.add(localKey.trim().split("=")[0].trim());
						}
					}
					for (String string : ftpKeys) {
						logger.info("ftpKeys:"+string);
					}
					for (String string : localKeys) {
						logger.info("localKeys:"+string);
					}
					ftpFile.close();
					localFile.close();
					
					for (Iterator iterator = ftpKeys.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if(!localKeys.contains(key)){
							history.append(key+",   ");
							flag = 1;
							params2.addProperty("result", "false");
						}
					}
					if(flag == 0){
						history.append("否");
					}
					flag = 0;
					logger.info("返回信息:   "+history);
				}
				if("jsis".equals(service)){
					BufferedReader  ftpFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/"+files[i])));
					BufferedReader  localFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/home/jht/projects/project/00.SetBak/"+files[i])));
					ArrayList<String> ftpKeys = new ArrayList<>();
					String ftpKey;
					ArrayList<String> localKeys = new ArrayList<>();
					String localKey;
					while ((ftpKey = ftpFile.readLine()) != null) {
						if(!ftpKey.trim().isEmpty()&&!ftpKey.trim().contains("#")&&ftpKey.trim().contains("=")){
							ftpKeys.add(ftpKey.trim().split("=")[0].trim());
						}
					}
					while ((localKey = localFile.readLine()) != null) {
						if(!localKey.trim().isEmpty()&&!localKey.trim().contains("#")&&localKey.trim().contains("=")){
							localKeys.add(localKey.trim().split("=")[0].trim());
						}
					}
					for (String string : ftpKeys) {
						logger.info("ftpKeys:"+string);
					}
					for (String string : localKeys) {
						logger.info("localKeys:"+string);
					}
					ftpFile.close();
					localFile.close();
					
					for (Iterator iterator = ftpKeys.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if(!localKeys.contains(key)){
							history.append(key+",   ");
							flag = 1;
							params2.addProperty("result", "false");
						}
					}
					if(flag == 0){
						history.append("否");
					}
					flag = 0;
					logger.info("返回信息:   "+history);
				}
				if("jsstApp".equals(service)){
					BufferedReader  ftpFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/"+files[i])));
					BufferedReader  localFile = new BufferedReader (new InputStreamReader(new FileInputStream("/home/jht/test/check."+service+"/"+ip+"/home/jht/projects/project/00.SetBak/jsstApp/"+files[i])));
					ArrayList<String> ftpKeys = new ArrayList<>();
					String ftpKey;
					ArrayList<String> localKeys = new ArrayList<>();
					String localKey;
					while ((ftpKey = ftpFile.readLine()) != null) {
						if(!ftpKey.trim().isEmpty()&&!ftpKey.trim().contains("#")&&ftpKey.trim().contains("=")){
							ftpKeys.add(ftpKey.trim().split("=")[0].trim());
						}
					}
					while ((localKey = localFile.readLine()) != null) {
						if(!localKey.trim().isEmpty()&&!localKey.trim().contains("#")&&localKey.trim().contains("=")){
							localKeys.add(localKey.trim().split("=")[0].trim());
						}
					}
					for (String string : ftpKeys) {
						logger.info("ftpKeys:"+string);
					}
					for (String string : localKeys) {
						logger.info("localKeys:"+string);
					}
					ftpFile.close();
					localFile.close();
					
					for (Iterator iterator = ftpKeys.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						if(!localKeys.contains(key)){
							history.append(key+",   ");
							flag = 1;
							params2.addProperty("result", "false");
						}
					}
					if(flag == 0){
						history.append("否");
					}
					flag = 0;
					logger.info("返回信息:   "+history);
				}
			}
			
			params1.addProperty("result", "success");
			params1.addProperty("history", history.toString());
			resp.add("params", params1);
			resp.add("params2", params2);
			
			return resp.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("对比FAIL" + e);
			
			params2.addProperty("result", "false");
			
			params1.addProperty("result", "fail");
			params1.addProperty("history", history.toString()+e);
			resp.add("params", params1);
			resp.add("params2", params2);
			
			return resp.toString();
		}
	}
	
	@RequestMapping(value = "/queryhistorydeployed", method = RequestMethod.POST)
	@ResponseBody
	public String queryhistorydeployed(final HttpServletRequest request) {
		try {
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			
			String response  = autoService.queryhistorydeployed(starttime, endtime);
			
			return response;

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
}

class Task implements Runnable {

	private String ip;
	private String service;
	private String baseUrl;
	private static final Logger logger = Logger.getLogger(Task.class);

	Task(String ip, String service, String baseUrl) {
		this.ip = ip;
		this.service = service;
		this.baseUrl = baseUrl;
	}

	@Override
	public void run() {
		try {

			JsonObject request1 = new JsonObject();
			JsonObject jsonParam = new JsonObject();
			JsonObject attributes = new JsonObject();
			attributes.addProperty("ip", ip);
			attributes.addProperty("service", service);
			jsonParam.add("data", attributes);
			request1.add("params", jsonParam);

			HttpEntity en = new StringEntity(request1.toString(), "UTF-8");

			String url = baseUrl + "restart/";

			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).setEntity(en).build();
			response = httpclient.execute(requst);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String results = EntityUtils.toString(response.getEntity());
				JsonObject json = new JsonParser().parse(results).getAsJsonObject();
				JsonObject params = (JsonObject) json.get("params");
				String result = params.get("result").toString();
				System.out.println(params.get("result").toString());
				if (result.replace("\"", "").equals("success")) {
					logger.info("重启SUCCESS");
				} else {
					logger.info("重启FAIL");
				}
				// return results;
			} else {
				System.out.println("重启FAIL:内部错误");
				// return "{\"params\": {\"result\": \"fail\"}}";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}