/**
 * Project Name:jsosp_yt
 * File Name:UserController.java
 * Package Name:com.jieshun.jsosp.security.user.controller
 * Date:2017年3月4日上午11:01:52
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/  

package com.jieshun.ops.elk.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.elk.service.IELkService;
import com.jieshun.ops.elk.service.impl.ELkServiceImpl;
import com.jieshun.ops.util.StringUtil;

/**
 * ClassName:UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:   TODO ADD REASON. <br/>
 * Date:     2017年3月4日 上午11:01:52 <br/>
 * @author   JHT 
 * @version
 * @since    JDK 1.7
 * @see
 */
@Controller
@RequestMapping("/elk")
public class ELkController {
	
	@Resource
	private IELkService elLkService;
	
	private static final Logger logger = Logger.getLogger(ELkServiceImpl.class.getName());
	
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	@ResponseBody
	public String sendMsg(HttpServletRequest request){
		String ip = StringUtil.Undefined2Str(request.getParameter("ip"));
		String phones = StringUtil.Undefined2Str(request.getParameter("phones"));
		String message = StringUtil.Undefined2Str(request.getParameter("message"));
		int isRecovery = Integer.parseInt(StringUtil.Undefined2Str(request.getParameter("isRecovery")));
		
		logger.info("接收到请求:ip="+ip+"&phones="+phones+"&message="+message+"&isRecovery="+isRecovery);
		
		return elLkService.add(ip,phones, message,isRecovery);
	}
	
	@RequestMapping(value = "/sendMsg1", method = RequestMethod.POST)
	@ResponseBody
	public String sendMsg1(HttpServletRequest request){
		String phones = StringUtil.Undefined2Str(request.getParameter("phones"));
		String message = StringUtil.Undefined2Str(request.getParameter("message"));
		
		logger.info("接收到请求:phones="+phones+"&message="+message);
		
		return elLkService.sendMsg(phones, message);
	}
	
	@RequestMapping(value = "/cardMapped", method = RequestMethod.POST)
	@ResponseBody
	public String cardMapped(HttpServletRequest request){
		String platform = StringUtil.Undefined2Str(request.getParameter("platform"));
		String stat_date = StringUtil.Undefined2Str(request.getParameter("stat_date"));
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		return elLkService.cardMapped(pageIndex,pageSize,platform, stat_date);
	}
	
}
