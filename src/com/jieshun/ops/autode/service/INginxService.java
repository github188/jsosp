package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:06
 */
public interface INginxService {
	
	public String infoquery(String starttime,String endtime,String sourceip,int pageIndex,int pageSize);
	public String detailquery(String starttime,String endtime,String sourceip,String serviceid,int pageIndex,int pageSize);
}
