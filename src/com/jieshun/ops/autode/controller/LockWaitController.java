package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.ILockWaitService;
import com.jieshun.ops.autode.service.IRedisSourceService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:47
 */
@Controller
@RequestMapping("/auto/lockwait")
public class LockWaitController extends BaseController {

	@Resource
	private ILockWaitService lockWaitService;

	private static final Logger logger = Logger.getLogger(LockWaitController.class);
	
	/**
	 * 用户登录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/lockwait", method = RequestMethod.POST)
	@ResponseBody
	public String lockwait(HttpServletRequest request) {
		try {
			String platform = StringUtil.Undefined2Str(request.getParameter("platform"));
			
			return lockWaitService.lockwait(platform);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/slowsql", method = RequestMethod.POST)
	@ResponseBody
	public String slowsql(HttpServletRequest request) {
		try {
			String platform = StringUtil.Undefined2Str(request.getParameter("platform"));
			
			return lockWaitService.slowsql(platform);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/processlist", method = RequestMethod.POST)
	@ResponseBody
	public String processlist(HttpServletRequest request) {
		try {
			String platform = StringUtil.Undefined2Str(request.getParameter("platform"));
			
			return lockWaitService.processlist(platform);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
