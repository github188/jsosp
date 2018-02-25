package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:15:44
 */
public interface BigTabbleDAO extends IBaseDao {

	List<Map<String, Object>> list();

}