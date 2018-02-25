package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:53
 */
public interface DataDeleteDAO extends IBaseDao {

	List<Map<String, Object>> queryDataDeletelists(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);

	Map<String, Object> queryById(@Param("id") String id);

	Map<String, Object> queryDataDeleteCounts();

	Map<String, Object> queryDataDeleteCounts1();

	Map<String, Object> queryDataDeleteCounts2();

	List<Map<String, Object>> queryDataDeleteNotStarted();

	void addDataDelete(@Param("id") String id, @Param("sub_code") String sub_code, @Param("sub_name") String sub_name,
			@Param("status") int status, @Param("enable") int enable, @Param("start_time") String start_time,
			@Param("end_time") String end_time, @Param("log") String log, @Param("platform") String platform,
			@Param("isDeleted") int isDeleted,@Param("prefix") String prefix);

}