package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:25
 */
public interface IRdsSlowLogOneKeyService {
	
	public String list(int pageIndex,int pageSize,String rdsHosts);
}
