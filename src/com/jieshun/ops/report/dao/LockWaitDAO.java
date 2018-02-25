package com.jieshun.ops.report.dao;

import java.util.List;
import java.util.Map;

import com.jieshun.ops.comm.IBaseDao;

public interface LockWaitDAO extends IBaseDao {

	List<Map<String, Object>> query();
	List<Map<String, Object>> slowsql();
	List<Map<String, Object>> processlist();
	List<Map<String, Object>> query2();
	
	void insertCloudLockWait(List<Map<String, Object>> locks);
	void insertLockWait(List<Map<String, Object>> locks);
	void insertSmLockWait(List<Map<String, Object>> locks);
	void insertZyLockWait(List<Map<String, Object>> locks);
	void insertbgy2LockWait(List<Map<String, Object>> locks);
	void insertbgy2LockWait2(List<Map<String, Object>> locks);

}