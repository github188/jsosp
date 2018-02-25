package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:19:03
 */
public interface IRedisSourceService {
	
	public String list(int pageIndex,int pageSize,String redisHosts);

	public String listbyline(String starttime,String endtime);

	public String listbynodeline(String starttime, String endtime);
}
