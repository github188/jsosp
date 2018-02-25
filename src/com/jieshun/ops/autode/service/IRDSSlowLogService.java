package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:20
 */
public interface IRDSSlowLogService {
	
	public String list(int pageIndex,int pageSize,String rdsHosts);
}
