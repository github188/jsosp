package com.jieshun.ops.autode.service;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:17:51
 */
public interface ILicenseManagementService {
	
	public String list(int pageIndex,int pageSize);
	
	public String add(String project, String licensestartTime,String licenseendTime,String project_type,String telephone,int duetime,String ip);

	public String queryById(String id);
	
	public String updateById(String id,String project,String licensestartTime,String licenseendTime,String project_type,String telephone,int duetime,String ip);

	public String querylicenses(String ip, String projectname, int pageIndex, int pageSize);
}
