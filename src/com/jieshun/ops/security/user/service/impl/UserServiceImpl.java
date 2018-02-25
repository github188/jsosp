package com.jieshun.ops.security.user.service.impl;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.security.user.dao.UserDao;
import com.jieshun.ops.security.user.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

	@Transactional
	@Override
	public String addUser(String userId, String account, String password, String userName, String unitName, String telephone,
			String remark,ArrayList<HashMap<String, Object>> roleArray)  {
		JsonObject response = new JsonObject();
		TransactionStatus status = null;
		try {
			List<Map<String, Object>> users = userDao.queryUserByAccount(account);
			if (users.size()>=1) {
				response.addProperty("returnCode", 409);
				response.addProperty("message", "帐号已存在");
				return response.toString();
			}
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			status = transactionManager.getTransaction(def);
			userDao.addUser(userId, account, password, userName, unitName, telephone, remark);
			for (Map<String, Object> map : roleArray) {
				String id = UUID.randomUUID().toString().replace("-", "");
				userDao.addUserRole(id, userId, map.get("id").toString());
			}
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			transactionManager.commit(status);
			return response.toString();
		} catch (Exception e) {
			transactionManager.rollback(status);
			logger.error(e);
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增用户异常");
			return response.toString();
		}
	}

	@Override
	public String queryUserlists(int pageIndex, int pageSize, String account, String userName, String telephone){
		JsonObject response = new JsonObject();
		try {
			
			HashMap<String, Object> userCounts = userDao.queryUserCounts(account, userName, telephone);
			if(pageSize==0){
				pageSize=Integer.parseInt(userCounts.get("USERCOUNTS").toString());
			}

			List<Map<String, Object>> users = userDao.queryUserlists(pageIndex*pageSize, pageSize, account, userName, telephone);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("userCounts", userCounts.get("USERCOUNTS").toString());

			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : users) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID").toString());
				jsonObject.addProperty("account", map.get("ACCOUNT").toString());
				jsonObject.addProperty("userName", map.get("USERNAME").toString());
				jsonObject.addProperty("unitName", map.get("UNITNAME")==null?"":map.get("UNITNAME").toString());
				jsonObject.addProperty("telephone", map.get("TELEPHONE")==null?"":map.get("TELEPHONE").toString());
				jsonObject.addProperty("createTime", map.get("CREATETIME").toString());
				jsonObject.addProperty("remark", map.get("REMARK")==null?"":map.get("REMARK").toString());
				result.add(jsonObject);
			}
			
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
	public String login(String account, String passwd) {
		JsonObject response = new JsonObject();
		try {
			List<Map<String, Object>> users = userDao.queryUserByAccount(account);
			if (users == null || users.size() == 0) {
				response.addProperty("returnCode", 1);
				response.addProperty("message", "帐号不存在");
				return response.toString();
			}

			Map<String, Object> user = users.get(0);
			String password = user.get("PASSWORD").toString();
			if(passwd.equals(password)){
				response.addProperty("returnCode", 0);
				response.addProperty("message", "登录成功");
				return response.toString();
			}
			response.addProperty("returnCode", 1);
			response.addProperty("message", "账号或密码错误");
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "登录异常");
			return response.toString();
		}
	}

}
