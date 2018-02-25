package com.jieshun.ops.autode.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.BigTabbleDAO;
import com.jieshun.ops.autode.service.IBigTableManageService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:22
 */
@Service("bigTableManageService")
public class BigTableManageImpl implements IBigTableManageService {
	
	@Autowired
	private BigTabbleDAO bigTabbleDAO;

	private static final Logger logger = Logger.getLogger(BigTableManageImpl.class.getName());

	@Override
	public String list() {
		JsonObject response = new JsonObject();
		try {
			JsonArray result = new JsonArray();
			
			DataSourceContextHolder.setDbType("ds_cloud");
			List<Map<String, Object>> list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_zuyong");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_smzf");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_wanke");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_biguiyuan2");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_qst");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.setDbType("ds_jszt");
			list = bigTabbleDAO.list();
			for (Map<String, Object> map : list) {
				JsonObject jsonObject= new JsonObject();
				jsonObject.addProperty("table_schema", map.get("table_schema").toString());
				jsonObject.addProperty("table_name", map.get("table_name").toString());
				jsonObject.addProperty("table_rows", map.get("table_rows").toString());
				result.add(jsonObject);
			}
			
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			response.addProperty("counts", result.size());
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.add("result", result);
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String exportrds(HttpServletRequest request) {
		return "";
	}


}
