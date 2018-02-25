package com.jieshun.ops.autode.service;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:18
 */
public interface IAutoService {
	
	public String queryhistorydeployed(String starttime,String endtime);
	
	public void insertdeploy(String id,String ip,String type,String time,String version,String status);
}
