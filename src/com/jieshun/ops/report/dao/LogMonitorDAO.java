package com.jieshun.ops.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

public interface LogMonitorDAO extends IBaseDao {

	List<Map<String, Object>> querySubsystem(@Param("nisspId") String nisspId);
	
	List<Map<String, Object>> queryNissp();

}