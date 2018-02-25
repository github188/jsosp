package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:49
 */
public interface DBRequestDAO extends IBaseDao {

	List<Map<String, Object>> queryDbrequestlists(@Param("startpages") int startpages, @Param("pageSize") int pageSize);

	void updateById(@Param("id") String id, @Param("IsCreated") int IsCreated);

	List<Map<String, Object>> queryDbrequestCounts();

	void add(@Param("id") String id, @Param("requestPerson") String requestPerson,
			@Param("instanceName") String instanceName, @Param("dbName") String dbName,
			@Param("purpose") String purpose, @Param("requestDate") String requestDate,
			@Param("endDate") String endDate, @Param("isCreated") int isCreated);

}