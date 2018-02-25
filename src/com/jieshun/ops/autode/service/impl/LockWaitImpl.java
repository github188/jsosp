package com.jieshun.ops.autode.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jieshun.ops.autode.dao.RedisSourceDAO;
import com.jieshun.ops.autode.service.ILockWaitService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.report.dao.LockWaitDAO;
import com.jieshun.ops.util.PLATFORM;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:01
 */
@Service("lockWaitService")
public class LockWaitImpl implements ILockWaitService {
	
	@Autowired
	private LockWaitDAO lockWaitDAO;

	private static final Logger logger = Logger.getLogger(LockWaitImpl.class.getName());

	@Override
	public String lockwait(String platform) {
		JsonObject response = new JsonObject();
		try {
			
			DataSourceContextHolder.clearDbType();
			if("CLOUD".equals(platform)){
				DataSourceContextHolder.setDbType("ds_cloud");
			}else if("ZUYONG".equals(platform)){
				DataSourceContextHolder.setDbType("ds_zuyong");
			}else if("JSSM".equals(platform)){
				DataSourceContextHolder.setDbType("ds_smzf");
			}else if("WANKE".equals(platform)){
				DataSourceContextHolder.setDbType("ds_wanke");
			}else if("BGY".equals(platform)){
				DataSourceContextHolder.setDbType("ds_biguiyuan2");
			}
			
			List<Map<String, Object>> lockwaits = lockWaitDAO.query();
			
//			JsonArray result = new JsonParser().parse(lockwaits.toString()).getAsJsonArray();
			
			JsonArray result = new JsonArray();
			
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : lockwaits) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("role", map.get("role")==null?"":map.get("role").toString());
				jsonObject.addProperty("id", map.get("id")==null?"":map.get("id").toString());
				jsonObject.addProperty("user", map.get("user")==null?"":map.get("user").toString());
				jsonObject.addProperty("host", map.get("host")==null?"":map.get("host").toString());
				jsonObject.addProperty("trx_id", map.get("trx_id")==null?"":map.get("trx_id").toString());
				jsonObject.addProperty("trx_state", map.get("trx_state")==null?"":map.get("trx_state").toString());
				jsonObject.addProperty("trx_started", map.get("trx_started")==null?"":map.get("trx_started").toString());
				jsonObject.addProperty("duration", map.get("duration")==null?"":map.get("duration").toString());
				jsonObject.addProperty("trx_started", map.get("trx_started")==null?"":map.get("trx_started").toString());
				jsonObject.addProperty("lock_type", map.get("lock_type")==null?"":map.get("lock_type").toString());
				jsonObject.addProperty("lock_table", map.get("lock_table")==null?"":map.get("lock_table").toString());
				jsonObject.addProperty("lock_index", map.get("lock_index")==null?"":map.get("lock_index").toString());
				jsonObject.addProperty("trx_query", map.get("trx_query")==null?"":map.get("trx_query").toString());
				jsonObject.addProperty("blockee_id", map.get("blockee_id")==null?"":map.get("blockee_id").toString());
				jsonObject.addProperty("blockee_trx", map.get("blockee_trx")==null?"":map.get("blockee_trx").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String slowsql(String platform) {
		JsonObject response = new JsonObject();
		try {
			
			DataSourceContextHolder.clearDbType();
			if("CLOUD".equals(platform)){
				DataSourceContextHolder.setDbType("ds_cloud");
			}else if("ZUYONG".equals(platform)){
				DataSourceContextHolder.setDbType("ds_zuyong");
			}else if("JSSM".equals(platform)){
				DataSourceContextHolder.setDbType("ds_smzf");
			}else if("WANKE".equals(platform)){
				DataSourceContextHolder.setDbType("ds_wanke");
			}else if("BGY".equals(platform)){
				DataSourceContextHolder.setDbType("ds_biguiyuan2");
			}
			
			List<Map<String, Object>> slowsqls = lockWaitDAO.slowsql();
			
//			JsonArray result = new JsonParser().parse(slowsqls.toString().replaceAll(":", "：").replace("/", "")).getAsJsonArray();
			
			JsonArray result = new JsonArray();
			
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : slowsqls) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("start_time", map.get("start_time")==null?"":map.get("start_time").toString());
				jsonObject.addProperty("user_host", map.get("user_host")==null?"":map.get("user_host").toString());
				jsonObject.addProperty("query_time", map.get("query_time")==null?"":map.get("query_time").toString());
				jsonObject.addProperty("lock_time", map.get("lock_time")==null?"":map.get("lock_time").toString());
				jsonObject.addProperty("rows_sent", map.get("rows_sent")==null?"":map.get("rows_sent").toString());
				jsonObject.addProperty("rows_examined", map.get("rows_examined")==null?"":map.get("rows_examined").toString());
				jsonObject.addProperty("db", map.get("db")==null?"":map.get("db").toString());
				jsonObject.addProperty("last_insert_id", map.get("last_insert_id")==null?"":map.get("last_insert_id").toString());
				jsonObject.addProperty("server_id", map.get("server_id")==null?"":map.get("server_id").toString());
				jsonObject.addProperty("sql_text", map.get("sql_text")==null?"":map.get("sql_text").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String processlist(String platform) {
		JsonObject response = new JsonObject();
		try {
			
			DataSourceContextHolder.clearDbType();
			if("CLOUD".equals(platform)){
				DataSourceContextHolder.setDbType("ds_cloud");
			}else if("ZUYONG".equals(platform)){
				DataSourceContextHolder.setDbType("ds_zuyong");
			}else if("JSSM".equals(platform)){
				DataSourceContextHolder.setDbType("ds_smzf");
			}else if("WANKE".equals(platform)){
				DataSourceContextHolder.setDbType("ds_wanke");
			}else if("BGY".equals(platform)){
				DataSourceContextHolder.setDbType("ds_biguiyuan2");
			}
			
			List<Map<String, Object>> processlists = lockWaitDAO.processlist();
			
//			JsonArray result = new JsonParser().parse(slowsqls.toString().replaceAll(":", "：").replace("/", "")).getAsJsonArray();
			
			JsonArray result = new JsonArray();
			
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : processlists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("user", map.get("USER")==null?"":map.get("USER").toString());
				jsonObject.addProperty("host", map.get("HOST")==null?"":map.get("HOST").toString());
				jsonObject.addProperty("db", map.get("DB")==null?"":map.get("DB").toString());
				jsonObject.addProperty("command", map.get("COMMAND")==null?"":map.get("COMMAND").toString());
				jsonObject.addProperty("time", map.get("TIME")==null?"":map.get("TIME").toString());
				jsonObject.addProperty("state", map.get("STATE")==null?"":map.get("STATE").toString());
				jsonObject.addProperty("info", map.get("INFO")==null?"":map.get("INFO").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
