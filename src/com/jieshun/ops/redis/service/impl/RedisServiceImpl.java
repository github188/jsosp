package com.jieshun.ops.redis.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jieshun.ops.redis.dao.RedisDao;
import com.jieshun.ops.redis.model.RedisBigkey;
import com.jieshun.ops.redis.model.RedisResource;
import com.jieshun.ops.redis.service.IRedisService;
import com.jieshun.ops.util.StringUtils;

@Service("redisService")
public class RedisServiceImpl implements IRedisService {
	
	@Autowired
	private RedisDao redisDao;

	/**
	 * @see com.jieshun.ops.redis.service.getRedisResource
	 */
	@Override
	public List<RedisResource> getRedisResource(String sTime, String eTime) {
		List<RedisResource> result = new ArrayList<RedisResource>();
		//查询时间都为空则查询当前50条的数据
		if (StringUtils.isEmpty(sTime) && StringUtils.isEmpty(eTime)) {
			result = redisDao.getRedisResource();
		}
		//结束时间为空则默认为当前时间
		if (!StringUtils.isEmpty(sTime) && StringUtils.isEmpty(eTime)) {
			result = redisDao.getRedisResourceBySTime(sTime);
		}
		if (!StringUtils.isEmpty(sTime) && !StringUtils.isEmpty(eTime)) {
			result = redisDao.getRedisResourceByTime(sTime, eTime);
		}
		return result;
	}

	@Override
	public List<RedisBigkey> getRedisBigkey(String time) {
		List<RedisBigkey> result = new ArrayList<RedisBigkey>();
		//如果time为空则获取当前小时的redisbigkey数据，否则按时间查询
		if (StringUtils.isEmpty(time)) {
			result = redisDao.getRedisBigkey();
		} else {
			result = redisDao.getRedisBigkeyByTime(time);
		}
		return result;
	}

}
