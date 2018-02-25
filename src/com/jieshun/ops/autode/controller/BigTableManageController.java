package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IBigTableManageService;
import com.jieshun.ops.comm.BaseController;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:10
 */
@Controller
@RequestMapping("/auto/btmanage")
public class BigTableManageController extends BaseController {

	@Resource
	private IBigTableManageService bigTableManageService;

	private static final Logger logger = Logger.getLogger(BigTableManageController.class);
	
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
//			int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
//			int pageSize = Integer.parseInt(request.getParameter("pageSize"));
			
			return bigTableManageService.list();
			
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
	@RequestMapping(value = "/exportrds", method = RequestMethod.POST)
	@ResponseBody
	public String exportrds(HttpServletRequest request) {
		try {
			return bigTableManageService.exportrds(request);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
