/**
 * Project Name:jsosp_yt
 * File Name:UserController.java
 * Package Name:com.jieshun.jsosp.security.user.controller
 * Date:2017年3月4日上午11:01:52
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/  

package com.jieshun.ops.security.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.jieshun.ops.security.user.service.IUserService;
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
@RequestMapping("/security/user")
public class UserController {
	
	@Resource	
	private IUserService userService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request){
		String account = StringUtil.Undefined2Str(request.getParameter("account"));
		String password = StringUtil.Undefined2Str(request.getParameter("password"));
		String userName = StringUtil.Undefined2Str(request.getParameter("userName"));
		String unitName = StringUtil.Undefined2Str(request.getParameter("unitName"));
		String telephone = StringUtil.Undefined2Str(request.getParameter("telephone"));
		String remark = StringUtil.Undefined2Str(request.getParameter("remark"));
		String roles = StringUtil.Undefined2Str(request.getParameter("roles"));
		ArrayList<HashMap<String, Object>> roleArray = new Gson().fromJson(roles, ArrayList.class);
		
		String userId = UUID.randomUUID().toString().replace("-", "");
		return userService.addUser(userId, account, password, userName, unitName, telephone, remark,roleArray);
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(HttpServletRequest request){
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String account = StringUtil.Undefined2Str(request.getParameter("account"));
		String userName = StringUtil.Undefined2Str(request.getParameter("userName"));
		String telephone = StringUtil.Undefined2Str(request.getParameter("telephone"));
		
		return userService.queryUserlists(pageIndex, pageSize, account, userName, telephone);
		
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request){
		String account = StringUtil.Undefined2Str(request.getParameter("account"));
		String passwd = StringUtil.Undefined2Str(request.getParameter("passwd"));
		
		return userService.login(account,passwd);
		
	}
	
}
