package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.ILicenseManagementService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:41
 */
@Controller
@RequestMapping("/auto/license")
public class LicenseManagementController extends BaseController {

	@Resource
	private ILicenseManagementService licenseManagementService;

	private static final Logger logger = Logger.getLogger(LicenseManagementController.class);
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(HttpServletRequest request) {
		try {
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			return licenseManagementService.list(pageIndex, pageSize);
			
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
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request) {
		try {
			String project = request.getParameter("project");
			String licensestartTime = request.getParameter("licensestartTime");
			String licenseendTime = request.getParameter("licenseendTime");
			String project_type = request.getParameter("project_type");
			String telephone = request.getParameter("telephone");
			String ip = request.getParameter("ips");
			int duetime = Integer.parseInt(request.getParameter("duetime"));
			
			return licenseManagementService.add(project,licensestartTime,licenseendTime,project_type,telephone,duetime,ip);
			
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
	@RequestMapping(value = "/queryById", method = RequestMethod.POST)
	@ResponseBody
	public String queryById(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			
			System.out.println(id);
			
			return licenseManagementService.queryById(id);
			
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
	@RequestMapping(value = "/updateById", method = RequestMethod.POST)
	@ResponseBody
	public String updateById(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String project = request.getParameter("project");
			String project_type = request.getParameter("project_type");
			String licensestartTime = request.getParameter("licensestartTime");
			String licenseendTime = request.getParameter("licenseendTime");
			String telephone = request.getParameter("telephone");
			String ip = request.getParameter("ip");
			int duetime = Integer.parseInt(request.getParameter("duetime"));
			
			System.out.println(id);
			
			return licenseManagementService.updateById(id,project,licensestartTime,licenseendTime,project_type,telephone,duetime,ip);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/querylicenses", method = RequestMethod.POST)
	@ResponseBody
	public String querylicenses(HttpServletRequest request) {
		try {
			String ip = StringUtil.Undefined2Str(request.getParameter("ip"));
			String projectname = StringUtil.Undefined2Str(request.getParameter("projectname"));
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			return licenseManagementService.querylicenses(ip,projectname,pageIndex,pageSize);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
}
