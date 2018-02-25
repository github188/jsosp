package com.jieshun.ops.autode.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jieshun.ops.comm.IBaseDao;


/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:49
 */
public interface SubsystemOnlineDao extends IBaseDao {

	List<Map<String, Object>> queryProjectList();

	void updateProjects(List<HashMap<String, String>> list);

	void insertlogProjects(List<HashMap<String, Object>> loglist);

}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          