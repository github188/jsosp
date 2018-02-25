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
import com.jieshun.ops.autode.dao.APIDataDAO;
import com.jieshun.ops.autode.service.IAPIDataService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:20:36
 */
@Service("apiDataService")
public class APIDataImpl implements IAPIDataService {
	
	@Autowired
	private APIDataDAO apiDataDAO;

	private static final Logger logger = Logger.getLogger(APIDataImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String projectName) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> apiCounts = apiDataDAO.queryCounts(projectName);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(apiCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> apiLists = apiDataDAO.list(pageIndex*pageSize, pageSize,projectName);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", apiCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : apiLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME")));
				jsonObject.addProperty("project_name", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
				jsonObject.addProperty("project_code", map.get("PROJECT_CODE")==null?"":map.get("PROJECT_CODE").toString());
				jsonObject.addProperty("service_id", map.get("SERVICE_ID")==null?"":map.get("SERVICE_ID").toString());
				jsonObject.addProperty("request_time", map.get("REQUEST_TIME")==null?"":simpleDateFormat.format((Date)map.get("REQUEST_TIME")));
				jsonObject.addProperty("elapsed_time", map.get("ELAPSED_TIME")==null?"":map.get("ELAPSED_TIME").toString());
				jsonObject.addProperty("msg_code", map.get("MSG_CODE")==null?"":map.get("MSG_CODE").toString());
				jsonObject.addProperty("msg_desc", map.get("MSG_DESC")==null?"":map.get("MSG_DESC").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			
			
			List<Map<String, Object>> redisLists = apiDataDAO.listAPI();
			JsonArray Redisresult = new JsonArray();
			if(redisLists != null &&redisLists.size()!=0){
				for (Map<String, Object> map : redisLists) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("projectName", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
					Redisresult.add(jsonObject);
				}
			}
			response.add("apiresult", Redisresult);
			
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
