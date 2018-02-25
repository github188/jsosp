package com.jieshun.ops.inspection.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.inspection.model.Inspection;
import com.jieshun.ops.inspection.service.impl.InspectionServiceImpl;
import com.jieshun.ops.util.StringUtil;
import com.jieshun.ops.util.StringUtils;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:23:43
 */
@Controller
@RequestMapping("/inspection")
public class InspectionController {
	
	@Resource
	private InspectionServiceImpl inspectionService;
	private static String baseUrl="http://localhost:8000/";
	
	private static final Logger log = Logger.getLogger(InspectionController.class);
	
	@RequestMapping(value = "/doInspection", method = RequestMethod.POST)
	@ResponseBody
	public String doInspection(HttpServletRequest request) {
		String init = StringUtil.Undefined2Str(request.getParameter("init"));
		String bigkey = StringUtil.Undefined2Str(request.getParameter("bigkey"));
		String readfile = StringUtil.Undefined2Str(request.getParameter("readfile"));
		String slowlog = StringUtil.Undefined2Str(request.getParameter("slowlog"));
		String api = StringUtil.Undefined2Str(request.getParameter("api"));
		String bgy = StringUtil.Undefined2Str(request.getParameter("bgy"));
		List<Inspection> list = new ArrayList<Inspection>();
		Inspection i;
		if (!StringUtils.isEmpty(init)) {
			i = new Inspection();
			i.setName(init);
			i.setAddress("python.int.addr");
			list.add(i);
		}
		if (!StringUtils.isEmpty(bigkey)) {
			i = new Inspection();
			i.setName(bigkey);
			i.setAddress("python.bigkey.addr");
			list.add(i);
		}
		if (!StringUtils.isEmpty(readfile)) {
			i = new Inspection();
			i.setName(readfile);
			i.setAddress("python.readfile.addr");
			list.add(i);
		}
		if (!StringUtils.isEmpty(slowlog)) {
			i = new Inspection();
			i.setName(slowlog);
			i.setAddress("python.slowlog.addr");
			list.add(i);
		}
		if (!StringUtils.isEmpty(api)) {
			i = new Inspection();
			i.setName(api);
			i.setAddress("python.api.addr");
			list.add(i);
		}
		if (!StringUtils.isEmpty(bgy)) {
			i = new Inspection();
			i.setName(bgy);
			i.setAddress("python.bgy.addr");
			list.add(i);
		}
		String result = inspectionService.doPythonScript(list);
		log.info(result);
		return result;
	}
	
	@RequestMapping(value = "/doinsp", method = RequestMethod.POST)
	@ResponseBody
	public String doinsp(HttpServletRequest request) {
		try {
			String url = baseUrl + "doinsp/";
	
			BasicCookieStore cookieStore = new BasicCookieStore();
			CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			CloseableHttpResponse response = null;
			HttpUriRequest requst = RequestBuilder.post().setUri(new URI(url)).build();
			response = httpclient.execute(requst);
	
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				log.info("一键巡检调用成功");
			} else {
				log.info("一键巡检调用失败");
				return "1";
			}
			return "0";
		} catch (Exception e) {
			log.info("一键巡检调用失败");
			e.printStackTrace();
			return "1";
		}
	}
	
}
