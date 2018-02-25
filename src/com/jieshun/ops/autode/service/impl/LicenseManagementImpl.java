package com.jieshun.ops.autode.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.LicenseManagementDAO;
import com.jieshun.ops.autode.service.ILicenseManagementService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.task.datadelete.DataDeleteTask;
import com.jieshun.ops.util.JdbcConn;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:56
 */
@Service("licenseManagementService")
public class LicenseManagementImpl implements ILicenseManagementService {

	@Autowired
	private LicenseManagementDAO licenseManagementDAO;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(LicenseManagementImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			//全部
			Map<String, Object> licenseManagementCounts = licenseManagementDAO.queryLicenseManagementCounts();
			//运行中
			if(pageSize==0){
				pageSize=Integer.parseInt(licenseManagementCounts.get("LICENSEMANAGENMENTCOUNTS").toString());
			}
			
			List<Map<String, Object>> licenseManagementLists = licenseManagementDAO.queryLicenseManagementlists(pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("licensecounts", licenseManagementCounts.get("LICENSEMANAGENMENTCOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : licenseManagementLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("project", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
				jsonObject.addProperty("enable", Integer.parseInt(map.get("ENABLED").toString())==0?false:true);
				jsonObject.addProperty("create_time", map.get("CREATE_TIME")==null?"":map.get("CREATE_TIME").toString());
				jsonObject.addProperty("licensestartTime", map.get("START_TIME")==null?"":map.get("START_TIME").toString());
				jsonObject.addProperty("licenseendTime", map.get("END_TIME")==null?"":map.get("END_TIME").toString());
				jsonObject.addProperty("project_type", map.get("PROJECT_TYPE")==null?"":map.get("PROJECT_TYPE").toString());
				jsonObject.addProperty("telephone", map.get("TELEPHONE")==null?"":map.get("TELEPHONE").toString());
				jsonObject.addProperty("duetime", map.get("DUETIME")==null?"":map.get("DUETIME").toString());
				jsonObject.addProperty("ip", map.get("IP")==null?"":map.get("IP").toString());
				result.add(jsonObject);
			}
			
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
	public String add(String project, String licensestartTime,String licenseendTime,String project_type,String telephone,int duetime,String ip) {
		TransactionStatus status = null;
		JsonObject response = new JsonObject();
		try {
			if(!StringUtil.EnsureTels(telephone)){
				response.addProperty("returnCode", 1);
				response.addProperty("message", "手机号格式异常");
				return response.toString();
			}
			
			String id = UUID.randomUUID().toString().replace("-", "");
			
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			
			licenseManagementDAO.addLicense(id, project, 1, licensestartTime, licenseendTime,project_type,telephone,duetime,ip);
			
			transactionManager.commit(status);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

	@Override
	public String queryById(String id) {
		JsonObject response = new JsonObject();
		try{
			Map<String, Object> license = licenseManagementDAO.queryById(id);
			
			JsonArray result = new JsonArray();
			JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("id", license.get("ID")==null?"":license.get("ID").toString());
			jsonObject.addProperty("project", license.get("PROJECT_NAME")==null?"":license.get("PROJECT_NAME").toString());
			jsonObject.addProperty("enable", Integer.parseInt(license.get("ENABLED").toString())==0?false:true);
			jsonObject.addProperty("create_time", license.get("CREATE_TIME")==null?"":license.get("CREATE_TIME").toString());
			jsonObject.addProperty("licensestartTime", license.get("START_TIME")==null?"":license.get("START_TIME").toString());
			jsonObject.addProperty("licenseendTime", license.get("END_TIME")==null?"":license.get("END_TIME").toString());
			jsonObject.addProperty("project_type", license.get("PROJECT_TYPE")==null?"":license.get("PROJECT_TYPE").toString());
			jsonObject.addProperty("telephone", license.get("TELEPHONE")==null?"":license.get("TELEPHONE").toString());
			jsonObject.addProperty("duetime", license.get("DUETIME")==null?"":license.get("DUETIME").toString());
			jsonObject.addProperty("ip", license.get("IP")==null?"":license.get("IP").toString());
			
			result.add(jsonObject);
			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
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

	@Override
	public String updateById(String id, String project, String licensestartTime, String licenseendTime,
			String project_type, String telephone, int duetime,String ip) {
		TransactionStatus status = null;
		JsonObject response = new JsonObject();
		try {
			if(!StringUtil.EnsureTels(telephone)){
				response.addProperty("returnCode", 1);
				response.addProperty("message", "手机号格式异常");
				return response.toString();
			}
			
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			licenseManagementDAO.updateById(id, project, licensestartTime, licenseendTime, project_type, telephone, duetime,ip);
			
			transactionManager.commit(status);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "更新异常");
			return response.toString();
		}
	}

	@Override
	public String querylicenses(String ip, String projectname, int pageIndex, int pageSize) {
		JsonObject response = new JsonObject();
		try {
			//全部
			Map<String, Object> licenseManagementCounts = licenseManagementDAO.queryLicenseManagementCountsByIpAndName(ip,projectname);
			//运行中
			if(pageSize==0){
				pageSize=Integer.parseInt(licenseManagementCounts.get("LICENSEMANAGENMENTCOUNTS").toString());
			}
			
			List<Map<String, Object>> licenseManagementLists = licenseManagementDAO.querylicenses(pageIndex*pageSize, pageSize,ip,projectname);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("licensecounts", licenseManagementCounts.get("LICENSEMANAGENMENTCOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : licenseManagementLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("project", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
				jsonObject.addProperty("enable", Integer.parseInt(map.get("ENABLED").toString())==0?false:true);
				jsonObject.addProperty("create_time", map.get("CREATE_TIME")==null?"":map.get("CREATE_TIME").toString());
				jsonObject.addProperty("licensestartTime", map.get("START_TIME")==null?"":map.get("START_TIME").toString());
				jsonObject.addProperty("licenseendTime", map.get("END_TIME")==null?"":map.get("END_TIME").toString());
				jsonObject.addProperty("project_type", map.get("PROJECT_TYPE")==null?"":map.get("PROJECT_TYPE").toString());
				jsonObject.addProperty("telephone", map.get("TELEPHONE")==null?"":map.get("TELEPHONE").toString());
				jsonObject.addProperty("duetime", map.get("DUETIME")==null?"":map.get("DUETIME").toString());
				jsonObject.addProperty("ip", map.get("IP")==null?"":map.get("IP").toString());
				result.add(jsonObject);
			}
			
			response.add("result", result);
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}
}
