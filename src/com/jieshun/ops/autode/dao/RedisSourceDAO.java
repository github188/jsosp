package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:30
 */
public interface RedisSourceDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("redisHosts")String redisHosts);
	
	List<Map<String, Object>> listRedis();
	
	Map<String, Object> queryCounts(@Param("redisHosts")String redisHosts);

	List<Map<String, Object>> listbyline(@Param("starttime")String starttime,@Param("endtime")String endtime);

	List<Map<String, Object>> listbynodeline(@Param("starttime")String starttime,@Param("endtime")String endtime);

}