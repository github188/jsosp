package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:54
 */
public interface WankeUnsubmitSubsystemDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);
	
	Map<String, Object> queryCounts();

	Map<String, Object> queryDataUpload(@Param("start")String start,@Param("end")String end);
	
	void insert(@Param("id") String id, @Param("check_date") String check_date,
			@Param("subsystems") String subsystems);

}