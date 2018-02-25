package com.jieshun.ops.dao;

import java.util.List;

import com.jieshun.ops.model.LogServiceRequestDO;

public interface ServiceRequestDao {
	
	void addLog(LogServiceRequestDO lsrd);
	
	void batchAddLog(List<LogServiceRequestDO> lsrds);
	
	String getTokenFromDB(String code);
}
