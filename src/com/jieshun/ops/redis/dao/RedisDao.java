package com.jieshun.ops.redis.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;
import com.jieshun.ops.redis.model.RedisBigkey;
import com.jieshun.ops.redis.model.RedisResource;

public interface RedisDao extends IBaseDao {
	
	/**
	 * 默认获取按时间倒叙排列的前50个列表
	 * @param checkTime
	 * @return
	 */
	List<RedisResource> getRedisResource();
	
	/**
	 * 默认查询当前分钟的数据
	 * @return
	 */
	List<RedisBigkey> getRedisBigkey();
	
	/**
	 * 按开始时间查询到当前时间的数据集合
	 * @param checkTime
	 * @return
	 */
	List<RedisResource> getRedisResourceBySTime(@Param("sTime")String sTime);
	
	/**
	 * 根据当前时间查询
	 * @param time
	 * @return
	 */
	List<RedisBigkey> getRedisBigkeyByTime(@Param("time") String time);
	
	/**
	 * 按时间查询
	 * @param sTime
	 * @param eTime
	 * @return
	 */
	List<RedisResource> getRedisResourceByTime(@Param("sTime")String sTime, @Param("eTime") String eTime);

}
