package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:57
 */
public interface DiscountDAO extends IBaseDao {

	List<Map<String, Object>> list(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("carno")String carno,@Param("starttime")String starttime,@Param("endtime")String endtime,@Param("retCode")String retCode);
	
	Map<String, Object> queryCounts(@Param("carno")String carno,@Param("starttime")String starttime,@Param("endtime")String endtime,@Param("retCode")String retCode);

	Map<String, Object> queryById(@Param("id")String id);

}