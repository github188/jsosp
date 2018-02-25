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
import com.jieshun.ops.autode.dao.WankeUnsubmitSubsystemDAO;
import com.jieshun.ops.autode.service.IWankeUnsubmitSubsystemService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:23:20
 */
@Service("wankeUnsubmitSubsystemService")
public class WankeUnsubmitSubsystemImpl implements IWankeUnsubmitSubsystemService {
	
	@Autowired
	private WankeUnsubmitSubsystemDAO wankeUnsubmitSubsystemDAO;

	private static final Logger logger = Logger.getLogger(WankeUnsubmitSubsystemImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> bgyInoutCounts = wankeUnsubmitSubsystemDAO.queryCounts();
			
			if(pageSize==0){
				pageSize=Integer.parseInt(bgyInoutCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> bgyInoutLists = wankeUnsubmitSubsystemDAO.list(pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", bgyInoutCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : bgyInoutLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":map.get("CHECK_TIME").toString());
				jsonObject.addProperty("check_date", map.get("CHECK_DATE")==null?"":map.get("CHECK_DATE").toString());
				jsonObject.addProperty("subsystems", map.get("SUBSYSTEMS")==null?"":map.get("SUBSYSTEMS").toString());
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

}
