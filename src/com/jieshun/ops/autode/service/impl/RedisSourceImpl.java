package com.jieshun.ops.autode.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.RedisSourceDAO;
import com.jieshun.ops.autode.service.IRedisSourceService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:31
 */
@Service("redisSourceService")
public class RedisSourceImpl implements IRedisSourceService {
	
	@Autowired
	private RedisSourceDAO redisSourceDAO;

	private static final Logger logger = Logger.getLogger(RedisSourceImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String redisHosts) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> redisBigTableCounts = redisSourceDAO.queryCounts(redisHosts);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(redisBigTableCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> redisBigTableLists = redisSourceDAO.list(pageIndex*pageSize, pageSize,redisHosts);
			
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
				jsonObject.addProperty("node1", map.get("NODE1")==null?"":map.get("NODE1").toString());
				jsonObject.addProperty("node2", map.get("NODE2")==null?"":map.get("NODE2").toString());
				jsonObject.addProperty("node3", map.get("NODE3")==null?"":map.get("NODE3").toString());
				jsonObject.addProperty("node4", map.get("NODE4")==null?"":map.get("NODE4").toString());
				jsonObject.addProperty("node5", map.get("NODE5")==null?"":map.get("NODE5").toString());
				jsonObject.addProperty("node6", map.get("NODE6")==null?"":map.get("NODE6").toString());
				jsonObject.addProperty("node7", map.get("NODE7")==null?"":map.get("NODE7").toString());
				jsonObject.addProperty("node8", map.get("NODE8")==null?"":map.get("NODE8").toString());
				jsonObject.addProperty("total", map.get("TOTAL")==null?"":map.get("TOTAL").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			
			
			List<Map<String, Object>> redisLists = redisSourceDAO.listRedis();
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

	@Override
	public String listbyline(String starttime,String endtime) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			List<Map<String, Object>> redisLists = redisSourceDAO.listbyline(starttime,endtime);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			Set<String> redisResource = new HashSet<>();
			
			for (Map<String, Object> map : redisLists) {
				String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
				redisResource.add(redis_host);
			}
			response.addProperty("counts", redisResource.size());
			
			JsonArray result = new JsonArray();
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (String redisName : redisResource) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("name", redisName);
				JsonArray jsonArray = new JsonArray();
				for (Map<String, Object> map : redisLists) {
					String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
					String date = map.get("DATE")==null?"":map.get("DATE").toString();
					String total = map.get("TOTAL")==null?"":map.get("TOTAL").toString();
					JsonObject json = new JsonObject();
					if(redisName!=null && redisName.equals(redis_host)){
						json.addProperty("date", date);
						json.addProperty("total", total);
						jsonArray.add(json);
					}
					
				}
				jsonObject.add("value", jsonArray);
				result.add(jsonObject);
			}
			response.add("result", result);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String listbynodeline(String starttime, String endtime) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			List<Map<String, Object>> redisLists = redisSourceDAO.listbynodeline(starttime,endtime);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			Set<String> redisResource = new HashSet<>();
			
			for (Map<String, Object> map : redisLists) {
				String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
				redisResource.add(redis_host);
			}
			response.addProperty("counts", redisResource.size());
			
			JsonArray result = new JsonArray();
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (String redisName : redisResource) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("name", redisName);
				JsonArray jsonArray = new JsonArray();
				for (Map<String, Object> map : redisLists) {
					String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
					String date = map.get("DATE")==null?"":map.get("DATE").toString();
					String node1 = map.get("NODE1")==null?"":map.get("NODE1").toString();
					String node2 = map.get("NODE2")==null?"":map.get("NODE2").toString();
					String node3 = map.get("NODE3")==null?"":map.get("NODE3").toString();
					String node4 = map.get("NODE4")==null?"":map.get("NODE4").toString();
					String node5 = map.get("NODE5")==null?"":map.get("NODE5").toString();
					String node6 = map.get("NODE6")==null?"":map.get("NODE6").toString();
					String node7 = map.get("NODE7")==null?"":map.get("NODE7").toString();
					String node8 = map.get("NODE1")==null?"":map.get("NODE8").toString();
					JsonObject json = new JsonObject();
					if(redisName!=null && redisName.equals(redis_host)){
						json.addProperty("date", date);
						json.addProperty("node1", node1);
						json.addProperty("node2", node2);
						json.addProperty("node3", node3);
						json.addProperty("node4", node4);
						json.addProperty("node5", node5);
						json.addProperty("node6", node6);
						json.addProperty("node7", node7);
						json.addProperty("node8", node8);
						jsonArray.add(json);
					}
					
				}
				jsonObject.add("value", jsonArray);
				result.add(jsonObject);
			}
			response.add("result", result);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
