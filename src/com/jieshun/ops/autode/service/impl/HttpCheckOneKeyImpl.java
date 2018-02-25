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
import com.jieshun.ops.autode.dao.HttpCheckOneKeyDAO;
import com.jieshun.ops.autode.service.IHttpCheckOneKeyService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:52
 */
@Service("httpCheckOneKeyService")
public class HttpCheckOneKeyImpl implements IHttpCheckOneKeyService {
	
	@Autowired
	private HttpCheckOneKeyDAO httpCheckOneKeyDAO;

	private static final Logger logger = Logger.getLogger(HttpCheckOneKeyImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String httpService) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> redisBigTableCounts = httpCheckOneKeyDAO.queryCounts(httpService);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(redisBigTableCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> redisBigTableLists = httpCheckOneKeyDAO.list(pageIndex*pageSize, pageSize,httpService);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", redisBigTableCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : redisBigTableLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("oper_time", map.get("OPER_TIME")==null?"":simpleDateFormat.format((Date)map.get("OPER_TIME")));
				jsonObject.addProperty("platform_name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				jsonObject.addProperty("app_type", map.get("APP_TYPE")==null?"":map.get("APP_TYPE").toString());
				jsonObject.addProperty("app_name", map.get("APP_NAME")==null?"":map.get("APP_NAME").toString());
				jsonObject.addProperty("request_url", map.get("REQUEST_URL")==null?"":map.get("REQUEST_URL").toString());
				jsonObject.addProperty("allocate_item", map.get("ALLOCATE_ITEM")==null?"":map.get("ALLOCATE_ITEM").toString());
				jsonObject.addProperty("allocate_value", map.get("ALLOCATE_VALUE")==null?"":map.get("ALLOCATE_VALUE").toString());
				jsonObject.addProperty("warnint_value", map.get("WARNINT_VALUE")==null?"":map.get("WARNINT_VALUE").toString());
				jsonObject.addProperty("check_status", map.get("CHECK_STATUS")==null?"":map.get("CHECK_STATUS").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			
			
			List<Map<String, Object>> httpLists = httpCheckOneKeyDAO.listHttp();
			JsonArray Httpresult = new JsonArray();
			if(httpLists != null &&httpLists.size()!=0){
				for (Map<String, Object> map : httpLists) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("platform_name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
					Httpresult.add(jsonObject);
				}
			}
			response.add("Httpresult", Httpresult);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
