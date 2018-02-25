package com.jieshun.ops.autode.service;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:11
 */
public interface IProjectService {
	
	public String add(String platform,String name,String code);

	public String updateproject(String id, String watch);

	public String query(String platform, String name, String code,String watch , int pageIndex,int pageSize);

	public String queryById(String projectid);

	public String updateById(String projectid, String telphones);

}
