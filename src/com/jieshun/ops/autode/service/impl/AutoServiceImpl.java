package com.jieshun.ops.autode.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.AutoDao;
import com.jieshun.ops.autode.service.IAutoService;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:17
 */
@Service("autoService")
public class AutoServiceImpl implements IAutoService {

	@Autowired
	private AutoDao autoDao;

	private static final Logger logger = Logger.getLogger(AutoServiceImpl.class.getName());

	@Override
	public String queryhistorydeployed(String starttime, String endtime) {
		List<Map<String, Object>> list = autoDao.queryhistorydeployed(starttime, endtime);
		
		JsonArray jsonArray = new JsonArray();
		
		for (Map<String, Object> map : list) {
			JsonObject jsonObject= new JsonObject();
			jsonObject.addProperty("ip", map.get("IP").toString());
			jsonObject.addProperty("type", map.get("TYPE").toString());
			jsonObject.addProperty("version", map.get("VERSION").toString());
			jsonObject.addProperty("time", map.get("TIME").toString());
			jsonObject.addProperty("status", map.get("STATUS").toString());
			jsonArray.add(jsonObject);
		}
		
		return jsonArray.toString();
	}

	@Override
	public void insertdeploy(String id, String ip, String type, String time, String version, String status) {
		autoDao.insertdeploy(id, ip, type, time, version, status);
	}

}
