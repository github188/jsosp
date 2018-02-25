package com.jieshun.ops.autode.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:26
 */
public interface IBigTableManageService {
	
	public String list();
	
	public String exportrds(HttpServletRequest request);
	
}
