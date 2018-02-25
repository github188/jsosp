package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:35
 */
public interface AutoDao extends IBaseDao {

	List<Map<String, Object>> queryhistorydeployed(@Param("starttime") String starttime,
			@Param("endtime") String endtime);

	void insertdeploy(@Param("id") String id, @Param("ip") String ip, @Param("type") String type,
			@Param("time") String time, @Param("version") String version, @Param("status") String status);

}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          