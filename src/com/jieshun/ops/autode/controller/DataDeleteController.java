package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IDataDeleteService;
import com.jieshun.ops.comm.BaseController;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:23
 */
@Controller
@RequestMapping("/auto/datadelete")
public class DataDeleteController extends BaseController {

	@Resource
	private IDataDeleteService dataDeleteService;

	private static final Logger logger = Logger.getLogger(DataDeleteController.class);
	
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
			
			return dataDeleteService.list(pageIndex, pageSize);
			
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
			String code = request.getParameter("code");
			String platform = request.getParameter("platform");
			String prefix = request.getParameter("prefix");
			boolean isDeleted = Boolean.parseBoolean(request.getParameter("isDeleted"));
			
			System.out.println(code);
			System.out.println(platform);
			
			return dataDeleteService.add(code,platform,isDeleted,prefix);
			
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
			
			return dataDeleteService.queryById(id);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
}
