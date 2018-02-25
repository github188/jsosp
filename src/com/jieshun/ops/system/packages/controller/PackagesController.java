/**
 * Project Name:jsosp_yt
 * File Name:UserController.java
 * Package Name:com.jieshun.jsosp.security.user.controller
 * Date:2017年3月4日上午11:01:52
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/  

package com.jieshun.ops.system.packages.controller;

import java.io.File;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.jieshun.ops.system.packages.service.IPackagesService;
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
@RequestMapping("/master/softwarepackage")
public class PackagesController {
	
	@Resource
	private IPackagesService packagesService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(HttpServletRequest request){
		return packagesService.addPackage(request);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String list(HttpServletRequest request){
		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String componentId = StringUtil.Undefined2Str(request.getParameter("componentId"));
		String versionDate = StringUtil.Undefined2Str(request.getParameter("versionDate"));
		String principal = StringUtil.Undefined2Str(request.getParameter("principal"));
		String hasDbScript = StringUtil.Undefined2Str(request.getParameter("hasDbScript"));
		String hasParaFile = StringUtil.Undefined2Str(request.getParameter("hasParaFile"));
		
		return packagesService.queryPackageList(pageIndex, pageSize, componentId, versionDate, principal, hasDbScript, hasParaFile);
		
	}
	
}
