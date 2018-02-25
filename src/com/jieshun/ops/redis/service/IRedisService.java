package com.jieshun.ops.redis.service;

import java.util.List;

import com.jieshun.ops.redis.model.RedisBigkey;
import com.jieshun.ops.redis.model.RedisResource;

public interface IRedisService {
	
	/**
	 * 获取当前时间点的redisResource集合
	 * 如果查询为空则默认获取前50条的数据
	 * 结束时间为空则默认为当前时间
	 * 传如开始时间不能大于结束时间
	 * 
	 * @param sTime 开始时间
	 * @param eTime 结束时间
	 * @return
	 */
	public List<RedisResource> getRedisResource(String sTime, String eTime);
	
	/**
	 * 获取redisbigkey集合
	 * 如果time为空则获取当前小时的数据集合
	 * 否则获取传入的小时的数据集合
	 * 传入字符串格式按标准传入
	 * @param time 格式 ‘2017-11-23 17’
	 * @return
	 */
	public List<RedisBigkey> getRedisBigkey(String time);

}
