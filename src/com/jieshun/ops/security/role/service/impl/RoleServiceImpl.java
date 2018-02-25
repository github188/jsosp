package com.jieshun.ops.security.role.service.impl;

import java.util.ArrayList;
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
import com.jieshun.ops.security.role.dao.RoleDao;
import com.jieshun.ops.security.role.service.IRoleService;

@Service("roleService")
public class RoleServiceImpl implements IRoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(RoleServiceImpl.class.getName());

	@Override
	public String addRole(String roleId, String roleName, String remark,ArrayList<HashMap<String, Object>> userArray) {
		JsonObject response = new JsonObject();
		TransactionStatus status = null;
		try {
			Map<String, Object> roles = roleDao.queryRolesCounts(roleName);
			if (roles == null || (long)roles.get("ROLECOUNTS")>=1) {
				response.addProperty("returnCode", 409);
				response.addProperty("message", "帐号已存在");
				return response.toString();
			}
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			status = transactionManager.getTransaction(def);
			roleDao.addRole(roleId, roleName, remark);
			for (Map<String, Object> map : userArray) {
				String id = UUID.randomUUID().toString().replace("-", "");
				roleDao.addRoleUser(id, roleId, map.get("id").toString());
			}
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			transactionManager.commit(status);
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增用户异常");
			return response.toString();
		}
	}

	@Override
	public String queryRolelists(int pageIndex, int pageSize, String roleName) {
		JsonObject response = new JsonObject();
		try {
			HashMap<String, Object> roleCounts = roleDao.queryRolesCounts(roleName);
			if(pageSize==0){
				pageSize=Integer.parseInt(roleCounts.get("ROLECOUNTS").toString());
			}
			List<Map<String, Object>> roles = roleDao.queryRolelists(pageIndex*pageSize, pageSize, roleName);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("roleCounts", roleCounts.get("ROLECOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : roles) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID").toString());
				jsonObject.addProperty("roleName", map.get("ROLENAME").toString());
				jsonObject.addProperty("remark", map.get("REMARK")==null?"":map.get("REMARK").toString());
				jsonObject.addProperty("createTime", map.get("CREATETIME").toString());
				result.add(jsonObject);
			}
			
			response.add("result", result);
			
			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
