package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:57
 */
public interface HttpCheckDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("httpService")String httpService);
	
	List<Map<String, Object>> listHttp();
	
	Map<String, Object> queryCounts(@Param("httpService")String httpService);

}