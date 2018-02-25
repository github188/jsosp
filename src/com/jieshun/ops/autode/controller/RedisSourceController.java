package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IRedisSourceService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:20
 */
@Controller
@RequestMapping("/auto/redisSource")
public class RedisSourceController extends BaseController {

	@Resource
	private IRedisSourceService redisSourceService;

	private static final Logger logger = Logger.getLogger(RedisSourceController.class);
	
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
			String redisHosts = StringUtil.Undefined2Str(request.getParameter("redisHosts"));
			
			return redisSourceService.list(pageIndex,pageSize,redisHosts);
			
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
	@RequestMapping(value = "/listbyline", method = RequestMethod.POST)
	@ResponseBody
	public String listbyline(HttpServletRequest request) {
		try {
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			
			return redisSourceService.listbyline(starttime,endtime);
			
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
	@RequestMapping(value = "/listbynodeline", method = RequestMethod.POST)
	@ResponseBody
	public String listbynodeline(HttpServletRequest request) {
		try {
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			
			return redisSourceService.listbynodeline(starttime,endtime);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
