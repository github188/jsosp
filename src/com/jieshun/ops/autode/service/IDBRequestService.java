package com.jieshun.ops.autode.service;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:29
 */
public interface IDBRequestService {
	
	public String list(int pageIndex,int pageSize);
	
	public String add(String requestPerson,String instanceName,String dbName,String purpose,String requestDate,String endDate);

	public String updateById(String id,int IsCreated);
}
