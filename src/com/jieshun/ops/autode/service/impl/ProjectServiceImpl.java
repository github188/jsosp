package com.jieshun.ops.autode.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.jieshun.ops.autode.service.IProjectService;
import com.jieshun.ops.project.dao.ProjectDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:10
 */
@Service("projectService")
public class ProjectServiceImpl implements IProjectService {

	@Autowired
	private ProjectDao projectDAO;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(ProjectServiceImpl.class.getName());

	@Override
	public String updateproject(String id, String watch) {
		TransactionStatus status = null;
		JsonObject response = new JsonObject();
		try {
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			
			projectDAO.updateproject(id, watch);
			
			transactionManager.commit(status);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
			
		} catch (Exception e) {
			logger.error(e);
			transactionManager.rollback(status);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

	@Override
	public String add(String platform, String name, String code) {
		TransactionStatus status = null;
		JsonObject response = new JsonObject();
		try {
			
			Map<String,Object > map = projectDAO.quertByCode(code);
			
			if(map!=null && !map.get("COUNTS").toString().equals("0")){
				response.addProperty("returnCode", 0);
				response.addProperty("message", "子系统编号已存在");
				return response.toString();
			}
			
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			
			String id = UUID.randomUUID().toString().replace("-", "");
			
			projectDAO.add(id, platform,  name,  code);
			
			transactionManager.commit(status);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
			
		} catch (Exception e) {
			logger.error(e);
			transactionManager.rollback(status);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

	@Override
	public String query(String platform, String name, String code,String watch , int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			//全部
			Map<String, Object> projectCounts = projectDAO.queryProjectCountsByCondition(platform,name,code,watch);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(projectCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> projetLists = projectDAO.queryProjectlistsByCondition(platform,name,code,watch,pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", projectCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : projetLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("platform", map.get("PLATNAME")==null?"":map.get("PLATNAME").toString());
				jsonObject.addProperty("code", map.get("CODE")==null?"":map.get("CODE").toString());
				jsonObject.addProperty("name", map.get("NAME")==null?"":map.get("NAME").toString());
				jsonObject.addProperty("watch", map.get("WATCH")==null?false:(map.get("WATCH").toString().equals("1")?true:false));
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
	public String queryById(String projectid) {
		JsonObject response = new JsonObject();
		try {
			//全部
			Map<String, Object> projectInfo = projectDAO.queryById(projectid);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			JsonArray result = new JsonArray();
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("code", projectInfo.get("CODE")==null?"":projectInfo.get("CODE").toString());
			jsonObject.addProperty("name", projectInfo.get("NAME")==null?"":projectInfo.get("NAME").toString());
			jsonObject.addProperty("telphone", projectInfo.get("TELPHONE")==null?"":projectInfo.get("TELPHONE").toString());
			jsonObject.addProperty("platform_name", projectInfo.get("PLATFORM_NAME")==null?"":projectInfo.get("PLATFORM_NAME").toString());
			result.add(jsonObject);
			
			response.add("result", result);
			
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
	public String updateById(String projectid, String telphones) {
		JsonObject response = new JsonObject();
		try {
			//全部
			projectDAO.updateById(projectid,telphones);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

}
