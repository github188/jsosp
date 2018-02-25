package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:30
 */
public interface APIDataDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("projectName")String projectName);
	
	List<Map<String, Object>> listAPI();
	
	Map<String, Object> queryCounts(@Param("projectName")String projectName);

}