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
import com.jieshun.ops.autode.dao.DiscountDAO;
import com.jieshun.ops.autode.service.IDiscountService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:48
 */
@Service("discountService")
public class DiscountImpl implements IDiscountService {
	
	@Autowired
	private DiscountDAO discountDAO;

	private static final Logger logger = Logger.getLogger(DiscountImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize,String carno,String starttime,String endtime,String retCode) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> discountCounts = discountDAO.queryCounts(carno,starttime,endtime,retCode);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(discountCounts.get("COUNTS").toString());
			}
			List<Map<String, Object>> discountLists = discountDAO.list(pageIndex*pageSize, pageSize,carno,starttime,endtime,retCode);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", discountCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : discountLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("park_code", map.get("PARK_CODE")==null?"":map.get("PARK_CODE").toString());
				jsonObject.addProperty("park_name", map.get("PARK_NAME")==null?"":map.get("PARK_NAME").toString());
				jsonObject.addProperty("carno", map.get("CAR_NO")==null?"":map.get("CAR_NO").toString());
				jsonObject.addProperty("seqid", map.get("SEQID")==null?"":map.get("SEQID").toString());
				jsonObject.addProperty("retcode", map.get("RETCODE")==null?"":map.get("RETCODE").toString());
				jsonObject.addProperty("retmsg", map.get("RETMSG")==null?"":map.get("RETMSG").toString());
				jsonObject.addProperty("cloud", map.get("RESPONSE_TIME")==null?"":simpleDateFormat.format((Date)map.get("RESPONSE_TIME")));
				jsonObject.addProperty("jsifs", map.get("RETURN_TIME")==null?"":simpleDateFormat.format((Date)map.get("RETURN_TIME")));
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
	public String queryById(String id) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			Map<String, Object> discountMap = discountDAO.queryById(id);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("node", discountMap.get("NODE1")==null?"":discountMap.get("NODE1").toString());
			jsonObject.addProperty("info", discountMap.get("NODE1INFO")==null?"":"云订单服务接受请求");
//			jsonObject.addProperty("serviceid", discountMap.get("NODE1SERVICEID")==null?"":discountMap.get("NODE1SERVICEID").toString());
			result.add(jsonObject);
			
			JsonObject jsonObject1 = new JsonObject();
			jsonObject1.addProperty("node", discountMap.get("NODE2")==null?"":discountMap.get("NODE2").toString());
			jsonObject1.addProperty("info", discountMap.get("NODE2INFO")==null?"未接收到云订单服务请求":"接受云订单服务请求");
//			jsonObject1.addProperty("serviceid", discountMap.get("NODE2SERVICEID")==null?"":discountMap.get("NODE2SERVICEID").toString());
			result.add(jsonObject1);
			
			JsonObject jsonObject2 = new JsonObject();
			jsonObject2.addProperty("node", discountMap.get("NODE3")==null?"":discountMap.get("NODE3").toString());
			jsonObject2.addProperty("info", discountMap.get("NODE3INFO")==null?"未接收到G3/1631响应":"接收到G3/1631响应");
//			jsonObject2.addProperty("serviceid", discountMap.get("NODE3SERVICEID")==null?"":discountMap.get("NODE3SERVICEID").toString());
			result.add(jsonObject2);
			
			JsonObject jsonObject3 = new JsonObject();
			jsonObject3.addProperty("node", discountMap.get("NODE4")==null?"":discountMap.get("NODE4").toString());
			jsonObject3.addProperty("info", discountMap.get("NODE4INFO")==null?"未接收到G3/1631返回":"接收到G3/1631返回");
//			jsonObject3.addProperty("serviceid", discountMap.get("NODE4SERVICEID")==null?"":discountMap.get("NODE4SERVICEID").toString());
			result.add(jsonObject3);
			
			JsonObject jsonObject4 = new JsonObject();
			jsonObject4.addProperty("node", discountMap.get("NODE5")==null?"":discountMap.get("NODE5").toString());
			jsonObject4.addProperty("info", discountMap.get("NODE5INFO")==null?"未接收到集成服务返回":"接收到集成服务返回");
//			jsonObject3.addProperty("serviceid", discountMap.get("NODE4SERVICEID")==null?"":discountMap.get("NODE4SERVICEID").toString());
			result.add(jsonObject4);
			
			response.add("result", result);
			
			if(discountMap.get("NODE1")==null ||discountMap.get("NODE2")==null ||discountMap.get("NODE3")==null
					|| discountMap.get("NODE4")==null ||discountMap.get("NODE5")==null){
				response.addProperty("resultMsg", "响应异常");
			}else{
				response.addProperty("resultMsg", "响应正常");
			}
			
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
