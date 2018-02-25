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
import com.jieshun.ops.autode.dao.RdsSlowLogOneKeyDAO;
import com.jieshun.ops.autode.service.IRdsSlowLogOneKeyService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:22
 */
@Service("rdsSlowLogOneKeyService")
public class RdsSlowLogOneKeyImpl implements IRdsSlowLogOneKeyService {
	
	@Autowired
	private RdsSlowLogOneKeyDAO rdsSlowLogOneKeyDAO;

	private static final Logger logger = Logger.getLogger(RdsSlowLogOneKeyImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String rdsHosts) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> redisBigTableCounts = rdsSlowLogOneKeyDAO.queryCounts(rdsHosts);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(redisBigTableCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> redisBigTableLists = rdsSlowLogOneKeyDAO.list(pageIndex*pageSize, pageSize,rdsHosts);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", redisBigTableCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : redisBigTableLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("db_instance_id", map.get("DB_INSTANCE_ID")==null?"":map.get("DB_INSTANCE_ID").toString());
				jsonObject.addProperty("execute_start_time", map.get("EXECUTE_START_TIME")==null?"":simpleDateFormat.format((Date)map.get("EXECUTE_START_TIME")));
				jsonObject.addProperty("sql_param", map.get("SQL_PARAM")==null?"":map.get("SQL_PARAM").toString());
				jsonObject.addProperty("client_ip", map.get("CLIENT_IP")==null?"":map.get("CLIENT_IP").toString());
				jsonObject.addProperty("db_nanme", map.get("DB_NANME")==null?"":map.get("DB_NANME").toString());
				jsonObject.addProperty("query_times", map.get("QUERY_TIMES")==null?0:Integer.parseInt(map.get("QUERY_TIMES").toString()));
				jsonObject.addProperty("lock_times", map.get("LOCK_TIMES")==null?0:Integer.parseInt(map.get("LOCK_TIMES").toString()));
				jsonObject.addProperty("parser_row_counts", map.get("PARSER_ROW_COUNTS")==null?0:Integer.parseInt(map.get("PARSER_ROW_COUNTS").toString()));
				jsonObject.addProperty("return_row_counts", map.get("RETURN_ROW_COUNTS")==null?0:Integer.parseInt(map.get("RETURN_ROW_COUNTS").toString()));
				result.add(jsonObject);
			}
			response.add("result", result);
			
			
			List<Map<String, Object>> rdsLists = rdsSlowLogOneKeyDAO.listRDS();
			JsonArray RDSresult = new JsonArray();
			if(rdsLists != null &&rdsLists.size()!=0){
				for (Map<String, Object> map : rdsLists) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("db_instance_id", map.get("DB_INSTANCE_ID")==null?"":map.get("DB_INSTANCE_ID").toString());
					RDSresult.add(jsonObject);
				}
			}
			response.add("RDSresult", RDSresult);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
