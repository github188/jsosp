package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IHttpCheckOneKeyService;
import com.jieshun.ops.autode.service.IHttpCheckService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:36
 */
@Controller
@RequestMapping("/auto/httpCheckOnkey")
public class HttpCheckOneKeyController extends BaseController {

	@Resource
	private IHttpCheckOneKeyService httpCheckOneKeyService;

	private static final Logger logger = Logger.getLogger(HttpCheckOneKeyController.class);
	
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
			String httpService = StringUtil.Undefined2Str(request.getParameter("httpService"));
			
			return httpCheckOneKeyService.list(pageIndex,pageSize,httpService);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
