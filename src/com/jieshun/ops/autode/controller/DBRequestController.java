package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IDBRequestService;
import com.jieshun.ops.autode.service.IDataDeleteService;
import com.jieshun.ops.comm.BaseController;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:19
 */
@Controller
@RequestMapping("/auto/dbrequest")
public class DBRequestController extends BaseController {

	@Resource
	private IDBRequestService dbRequestService;

	private static final Logger logger = Logger.getLogger(DBRequestController.class);
	
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
			
			return dbRequestService.list(pageIndex, pageSize);
			
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
			String requestPerson = request.getParameter("requestPerson");
			String instanceName = request.getParameter("instanceName");
			String dbName = request.getParameter("dbName");
			String purpose = request.getParameter("purpose");
			String requestDate = request.getParameter("requestDate");
			String endDate = request.getParameter("endDate");
//			int isCreated = Integer.parseInt(request.getParameter("isCreated"));
			
			return dbRequestService.add(requestPerson,instanceName,dbName,purpose,requestDate,endDate);
			
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
	@RequestMapping(value = "/updateById", method = RequestMethod.POST)
	@ResponseBody
	public String updateById(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			int IsCreated = Integer.parseInt(request.getParameter("IsCreated"));
			
			return dbRequestService.updateById(id,IsCreated);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
}
