package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:15
 */
public interface RDSSlowLogDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("rdsHosts")String rdsHosts);
	
	List<Map<String, Object>> listRDS();
	
	Map<String, Object> queryCounts(@Param("rdsHosts")String rdsHosts);

}