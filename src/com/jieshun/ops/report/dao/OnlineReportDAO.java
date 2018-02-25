package com.jieshun.ops.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

public interface OnlineReportDAO extends IBaseDao {

	List<Map<String, Object>> queryOnlineProjectlists(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize, @Param("platformid") String platformid,
			@Param("projectname") String projectname, @Param("projectcode") String projectcode,
			@Param("starttime") String starttime, @Param("endtime") String endtime);

	Map<String, Object> queryprojectlistCounts(@Param("platformid") String platformid,
			@Param("projectname") String projectname, @Param("projectcode") String projectcode,
			@Param("starttime") String starttime, @Param("endtime") String endtime);

	List<Map<String, Object>> queryplatformlist();

	List<Map<String, Object>> onlinereportdetail(@Param("startpages") int startpages, @Param("pageSize") int pageSize,
			@Param("id") String id);

	Map<String, Object> onlinereportdetailCounts(@Param("id") String id);

	void inserthandleByofflineId(@Param("id") String id, @Param("reason") String reason,
			@Param("create_user") String create_user, @Param("remark") String remark);

	void updateofflineByid(@Param("id") String id, @Param("handleId") String handleId);

	List<Map<String, Object>> queryofflineHandleById(@Param("id") String id);

	void updatehandleHandleId(@Param("id") String id, @Param("reason") String reason, @Param("remark") String remark,
			@Param("create_user") String create_user);

	Map<String, Object> queryofflineprojectlistCounts(@Param("platformid") String platformid,
			@Param("projectname") String projectname, @Param("projectcode") String projectcode,
			@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("handle") String handle);

	List<Map<String, Object>> queryofflineProjectlists(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize, @Param("platformid") String platformid,
			@Param("projectname") String projectname, @Param("projectcode") String projectcode,
			@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("handle") String handle);

	Map<String, Object> actualTimeOnlineReportCounts(@Param("platformid")String platformid);

	List<Map<String, Object>> actualTimeOnlineReportlists(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize, @Param("platformid") String platformid);

	List<Map<String, Object>> actualTimeOnlineReportDetaillists(@Param("platformid")String platformid);

}