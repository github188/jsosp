package com.jieshun.ops.autode.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.DBRequestDAO;
import com.jieshun.ops.autode.service.IDBRequestService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.task.datadelete.DataDeleteTask;
import com.jieshun.ops.util.JdbcConn;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:27
 */
@Service("dbRequestService")
public class DBRequestServiceImpl implements IDBRequestService {

	@Autowired
	private DBRequestDAO dbRequestDAO;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(DBRequestServiceImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//全部
			List<Map<String, Object>> DbRequestCounts = dbRequestDAO.queryDbrequestCounts();
			
			int count = 0;
			int counting = 0;
			int counted = 0;
			for (Map<String, Object> map : DbRequestCounts) {
				count += Integer.parseInt(map.get("COUNT").toString());
				if("0".equals(map.get("ISCREATED").toString())){
					counting=Integer.parseInt(map.get("COUNT").toString());
				}else{
					counted=Integer.parseInt(map.get("COUNT").toString());
					
				}
			}
			response.addProperty("DBCreatingCounts", counting);
			response.addProperty("DBCreatedCounts", counted);
			response.addProperty("counts", count);
			if(pageSize==0){
				pageSize=count;
			}
			List<Map<String, Object>> dbRequestLists = dbRequestDAO.queryDbrequestlists(pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : dbRequestLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("person", map.get("REQUESTPERSON")==null?"":map.get("REQUESTPERSON").toString());
				jsonObject.addProperty("instanceName", map.get("INSTANCENAME")==null?"":map.get("INSTANCENAME").toString());
				jsonObject.addProperty("dbName", map.get("DBNAME")==null?"":map.get("DBNAME").toString());
				jsonObject.addProperty("purpose", map.get("PURPOSE")==null?"":map.get("PURPOSE").toString());
				jsonObject.addProperty("requestDate", map.get("REQUESTDATE")==null?"":simpleDateFormat.format(map.get("REQUESTDATE")));
				jsonObject.addProperty("endDate", map.get("ENDDATE")==null?"":simpleDateFormat.format(map.get("ENDDATE")));
				jsonObject.addProperty("isCreated", "0".equals(map.get("ISCREATED").toString())?"false":"true");
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
	public String add(String requestPerson,String instanceName,String dbName,String purpose,String requestDate,String endDate) {
		JsonObject response = new JsonObject();
		try {
			String id = UUID.randomUUID().toString().replace("-", "");

			dbRequestDAO.add(id,requestPerson,instanceName,dbName,purpose,requestDate,endDate,0);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

	@Override
	public String updateById(String id,int IsCreated) {
		JsonObject response = new JsonObject();
		try{
			dbRequestDAO.updateById(id,IsCreated);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "更新成功");
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "更新异常");
			return response.toString();
		}
	}
	
	JdbcConn getConnByPlatform(String platform){
		Set<JdbcConn> jdbcConns = DataDeleteTask.getJdbcConns();
		for (JdbcConn jdbcConn : jdbcConns) {
			if(platform.equals(jdbcConn.getPlatform())){
				return jdbcConn;
			}
		}
		return null;
	}
}
