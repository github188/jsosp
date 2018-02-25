/**
 * Project Name:jsosp_yt
 * File Name:UserController.java
 * Package Name:com.jieshun.jsosp.security.user.controller
 * Date:2017年3月4日上午11:01:52
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/  

package com.jieshun.ops.security.role.controller;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.jieshun.ops.security.role.service.IRoleService;
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
@RequestMapping("/security/role")
public class RoleController {
	
	@Resource
	private IRoleService roleService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request){
		String roleName = StringUtil.Undefined2Str(request.getParameter("roleName"));
		String remark = StringUtil.Undefined2Str(request.getParameter("remark"));
		String perms = StringUtil.Undefined2Str(request.getParameter("perms"));
		String users = StringUtil.Undefined2Str(request.getParameter("users"));
		ArrayList userArray = new Gson().fromJson(users, ArrayList.class);
		
		String roleId = UUID.randomUUID().toString().replace("-", "");
		return roleService.addRole(roleId,roleName, remark,userArray);
		
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(HttpServletRequest request){
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize =Integer.parseInt(request.getParameter("pageSize"));
		String roleName = StringUtil.Undefined2Str(request.getParameter("roleName"));
		
		return roleService.queryRolelists(pageIndex, pageSize, roleName);
		
	}
	
}
