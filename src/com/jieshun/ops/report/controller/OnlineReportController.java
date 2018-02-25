package com.jieshun.ops.report.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.report.service.IOnlineReportService;
import com.jieshun.ops.util.StringUtil;

@Controller
@RequestMapping("/report/onlinereport")
public class OnlineReportController extends BaseController {

	@Resource
	private IOnlineReportService onlineReportService;

	private static final Logger logger = Logger.getLogger(OnlineReportController.class);
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/projectlist", method = RequestMethod.POST)
	@ResponseBody
	public String list(HttpServletRequest request) {
		try {
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String platformid = StringUtil.Undefined2Str(request.getParameter("platformid"));
			String projectname = StringUtil.Undefined2Str(request.getParameter("projectname"));
			String projectcode = StringUtil.Undefined2Str(request.getParameter("projectcode"));
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			
			return onlineReportService.queryprojectlist(pageIndex, pageSize,platformid,projectname,projectcode,starttime,endtime);
			
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
	@RequestMapping(value = "/platformlist", method = RequestMethod.POST)
	@ResponseBody
	public String queryplatformlist(HttpServletRequest request) {
		try {
			
			return onlineReportService.queryplatformlist();
			
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
	@RequestMapping(value = "/onlinereportdetail", method = RequestMethod.POST)
	@ResponseBody
	public String onlinereportdetail(HttpServletRequest request) {
		try {
			
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String id = StringUtil.Undefined2Str(request.getParameter("id"));
			
			return onlineReportService.onlinereportdetail(pageIndex,pageSize,id);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/onlinereporthandle", method = RequestMethod.POST)
	@ResponseBody
	public String onlinereporthandle(HttpServletRequest request) {
		try {
			
			String ids = StringUtil.Undefined2Str(request.getParameter("ids"));
			String reason = StringUtil.Undefined2Str(request.getParameter("reason"));
			String remark = StringUtil.Undefined2Str(request.getParameter("remark"));
			String create_user = StringUtil.Undefined2Str(request.getParameter("create_user"));
			
			return onlineReportService.onlinereporthandle(ids,reason,remark,create_user);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/offlinereport", method = RequestMethod.POST)
	@ResponseBody
	public String offlinereport(HttpServletRequest request) {
		try {
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String platformid = StringUtil.Undefined2Str(request.getParameter("platformid"));
			String projectname = StringUtil.Undefined2Str(request.getParameter("projectname"));
			String projectcode = StringUtil.Undefined2Str(request.getParameter("projectcode"));
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			String handle = StringUtil.Undefined2Str(request.getParameter("handle"));
			
			return onlineReportService.queryofflineprojectlist(pageIndex, pageSize,platformid,projectname,projectcode,starttime,endtime,handle);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/actualTimeOnlineReport", method = RequestMethod.POST)
	@ResponseBody
	public String actualTimeOnlineReport(HttpServletRequest request) {
		try {
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String platformid = StringUtil.Undefined2Str(request.getParameter("platformid"));
			
			return onlineReportService.actualTimeOnlineReport(pageIndex, pageSize,platformid);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	@RequestMapping(value = "/actualTimeOnlineReportDetail", method = RequestMethod.POST)
	@ResponseBody
	public String actualTimeOnlineReportDetail(HttpServletRequest request) {
		try {
			String platformid = StringUtil.Undefined2Str(request.getParameter("platformid"));
			
			return onlineReportService.actualTimeOnlineReportDetail(platformid);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/exportOnlineProjects", method = RequestMethod.POST)
	@ResponseBody
	public String exportOnlineProjects(HttpServletRequest request) {
		try {
			String platformid = StringUtil.Undefined2Str(request.getParameter("platformid"));
			
			return onlineReportService.exportOnlineProjects(platformid,request);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
