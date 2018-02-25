package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:11
 */
public interface NginxDAO extends IBaseDao {

	List<Map<String, Object>> infoquery(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);
	
	Map<String, Object> queryCounts(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip);
	
	List<Map<String, Object>> detailquery(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("serviceid")String serviceid,@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);
	
	Map<String, Object> querydetailCounts(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("serviceid")String serviceid);
	
	List<Map<String, Object>> infoqueryBynginx(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip);
	
	List<Map<String, Object>> infoquerytop10(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);
	
	List<Map<String, Object>> detailqueryByserviceid(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("serviceid")String serviceid);
	
	List<Map<String, Object>> detailquerytop10(@Param("starttime")String starttime,@Param("endtime")String endtime,
			@Param("sourceip")String sourceip,@Param("serviceid")String serviceid,@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);

}