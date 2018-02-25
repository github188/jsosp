package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:53
 */
public interface IRedisBigTableMonitorService {
	
	public String list(int pageIndex,int pageSize,String redisHosts);
}
