package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IProjectService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:58
 */
@Controller
@RequestMapping("/auto/project")
public class ProjectController extends BaseController {

	@Resource
	private IProjectService projectService;

	private static final Logger logger = Logger.getLogger(ProjectController.class);
	
	@RequestMapping(value = "/updateproject", method = RequestMethod.POST)
	@ResponseBody
	public String updateproject(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			String watch = request.getParameter("watch");
			
			return projectService.updateproject(id, watch);
			
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
			String platform = request.getParameter("platform");
			String name = request.getParameter("name");
			String code = request.getParameter("code");
			
			return projectService.add(platform,name,code);
			
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
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public String query(HttpServletRequest request) {
		try {
			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			String platform = StringUtil.Undefined2Str(request.getParameter("platform"));
			String name = StringUtil.Undefined2Str(request.getParameter("name"));
			String code = StringUtil.Undefined2Str(request.getParameter("code"));
			String watch = StringUtil.Undefined2Str(request.getParameter("watch"));
			
			return projectService.query(platform,name,code,watch,pageIndex,pageSize);
			
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
			String projectid = StringUtil.Undefined2Str(request.getParameter("id"));
			
			return projectService.queryById(projectid);
			
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
			String projectid = StringUtil.Undefined2Str(request.getParameter("id"));
			String telphones = StringUtil.Undefined2Str(request.getParameter("telphones"));
			
			return projectService.updateById(projectid,telphones);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
