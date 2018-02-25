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
import com.jieshun.ops.autode.dao.BgyInoutDAO;
import com.jieshun.ops.autode.service.IBgyInoutService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:14
 */
@Service("bgyInoutService")
public class BgyInoutImpl implements IBgyInoutService {
	
	@Autowired
	private BgyInoutDAO bgyInoutDAO;

	private static final Logger logger = Logger.getLogger(BgyInoutImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> bgyInoutCounts = bgyInoutDAO.queryCounts();
			
			if(pageSize==0){
				pageSize=Integer.parseInt(bgyInoutCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> bgyInoutLists = bgyInoutDAO.list(pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", bgyInoutCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : bgyInoutLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME")));
				jsonObject.addProperty("check_date", map.get("CHECK_DATE")==null?"":map.get("CHECK_DATE").toString());
				jsonObject.addProperty("in_records", map.get("IN_RECORDS")==null?"":map.get("IN_RECORDS").toString());
				jsonObject.addProperty("out_records", map.get("OUT_RECORDS")==null?"":map.get("OUT_RECORDS").toString());
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
