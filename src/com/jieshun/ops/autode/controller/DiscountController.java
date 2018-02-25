package com.jieshun.ops.autode.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jieshun.ops.autode.service.IDiscountService;
import com.jieshun.ops.comm.BaseController;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:14:29
 */
@Controller
@RequestMapping("/auto/discount")
public class DiscountController extends BaseController {

	@Resource
	private IDiscountService discountService;

	private static final Logger logger = Logger.getLogger(DiscountController.class);
	
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
			String starttime = StringUtil.Undefined2Str(request.getParameter("starttime"));
			String endtime = StringUtil.Undefined2Str(request.getParameter("endtime"));
			String carno = StringUtil.Undefined2Str(request.getParameter("carno"));
			String retCode = StringUtil.Undefined2Str(request.getParameter("retCode"));
			
			return discountService.list(pageIndex,pageSize,carno,starttime,endtime,retCode);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
	@RequestMapping(value = "/queryById", method = RequestMethod.POST)
	@ResponseBody
	public String queryById(HttpServletRequest request) {
		try {
			String id = request.getParameter("id");
			
			System.out.println(id);
			
			return discountService.queryById(id);
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"params\": {\"result\": \"fail\"}}";
		}
	}
	
}
