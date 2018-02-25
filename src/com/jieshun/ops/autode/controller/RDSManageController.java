package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IRDSManageService;
import com.jieshun.ops.comm.BaseController;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:02
 */
@Controller
@RequestMapping("/auto/rdsmanage")
public class RDSManageController extends BaseController {

	@Resource
	private IRDSManageService rdsManageService;

	private static final Logger logger = Logger.getLogger(RDSManageController.class);
	
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
			
			return rdsManageService.list(pageIndex, pageSize);
			
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
			return rdsManageService.exportrds(request);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
