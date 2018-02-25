package com.jieshun.ops.autode.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.RedisBigTableMonitorDAO;
import com.jieshun.ops.autode.service.IRedisBigTableMonitorService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:26
 */
@Service("redisBigTableMonitorService")
public class RedisBigTableMonitorImpl implements IRedisBigTableMonitorService {
	
	@Autowired
	private RedisBigTableMonitorDAO redisBigTableMonitorDAO;

	private static final Logger logger = Logger.getLogger(RedisBigTableMonitorImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String redisHosts) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> redisBigTableCounts = redisBigTableMonitorDAO.queryCounts(redisHosts);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(redisBigTableCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> redisBigTableLists = redisBigTableMonitorDAO.list(pageIndex*pageSize, pageSize,redisHosts);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", redisBigTableCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : redisBigTableLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME")));
				jsonObject.addProperty("redis_host", map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString());
				jsonObject.addProperty("node", map.get("NODE")==null?"":map.get("NODE").toString());
				jsonObject.addProperty("key_name", map.get("KEY_NAME")==null?"":map.get("KEY_NAME").toString());
				jsonObject.addProperty("key_type", map.get("KEY_TYPE")==null?"":map.get("KEY_TYPE").toString());
				jsonObject.addProperty("key_value", map.get("KEY_VALUE")==null?0:Integer.parseInt(map.get("KEY_VALUE").toString()));
				result.add(jsonObject);
			}
			response.add("result", result);
			
			
			List<Map<String, Object>> redisLists = redisBigTableMonitorDAO.listRedis();
			JsonArray Redisresult = new JsonArray();
			if(redisLists != null &&redisLists.size()!=0){
				for (Map<String, Object> map : redisLists) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("redis_host", map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString());
					Redisresult.add(jsonObject);
				}
			}
			response.add("Redisresult", Redisresult);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
