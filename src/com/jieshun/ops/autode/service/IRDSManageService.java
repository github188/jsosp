package com.jieshun.ops.autode.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:16
 */
public interface IRDSManageService {
	
	public String list(int pageIndex,int pageSize);
	
	public String exportrds(HttpServletRequest request);
	
}
