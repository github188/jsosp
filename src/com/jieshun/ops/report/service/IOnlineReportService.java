package com.jieshun.ops.report.service;

import javax.servlet.http.HttpServletRequest;

public interface IOnlineReportService {
	
	public String queryprojectlist(int pageIndex,int pageSize,String platformid,String projectname,String projectcode,String starttime,String endtime);
	
	public String queryplatformlist();

	public String onlinereportdetail(int pageIndex,int pageSize,String id);
	
	public String onlinereporthandle(String ids,String onlinereporthandle,String remark,String create_user);

	public String queryofflineprojectlist(int pageIndex, int pageSize, String platformid, String projectname,
			String projectcode, String starttime, String endtime, String handle);

	public String actualTimeOnlineReport(int pageIndex, int pageSize, String platformid);

	public String actualTimeOnlineReportDetail( String platformid);

	public String exportOnlineProjects(String platformid, HttpServletRequest request);
	
	
}
