package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:20:08
 */
public interface RedisMonitorDAO extends IBaseDao {

	Map<String, Object> queryRedislists();
	
	List<Map<String, Object>> queryLastRedisBigTable();
	
}