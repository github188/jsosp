package com.jieshun.ops.autode.service;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:35
 */
public interface IDataDeleteService {
	
	public String list(int pageIndex,int pageSize);
	
	public String add(String code, String platform, boolean isDeleted,String prefix);

	public String queryById(String id);
}
