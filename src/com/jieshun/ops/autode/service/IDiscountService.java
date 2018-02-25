package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:47
 */
public interface IDiscountService {
	
	public String list(int pageIndex,int pageSize,String carno,String starttime,String endtime,String retCode);

	public String queryById(String id);
}
