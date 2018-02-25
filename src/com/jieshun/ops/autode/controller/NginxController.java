package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.INginxService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:52
 */
@Controller
@RequestMapping("/auto/nginx")
public class NginxController extends BaseController {

	@Resource
	private INginxService nginxService;

	private static final Logger logger = Logger.getLogger(NginxController.class);
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/infoquery", method = RequestMethod.POST)
	@ResponseBody
	public String infoquery(HttpServletRequest request) {
		try {
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			String sourceip = StringUtil.Undefined2Str(request.getParameter("sourceip"));
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			return nginxService.infoquery(starttime,endtime,sourceip,pageIndex,pageSize);
			
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
	@RequestMapping(value = "/detailquery", method = RequestMethod.POST)
	@ResponseBody
	public String detailquery(HttpServletRequest request) {
		try {
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			String sourceip = StringUtil.Undefined2Str(request.getParameter("sourceip"));
			String serviceid = StringUtil.Undefined2Str(request.getParameter("serviceid"));
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			return nginxService.detailquery(starttime,endtime,sourceip,serviceid,pageIndex,pageSize);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
