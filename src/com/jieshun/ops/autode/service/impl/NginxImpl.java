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
import com.jieshun.ops.autode.dao.NginxDAO;
import com.jieshun.ops.autode.service.INginxService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:05
 */
@Service("nginxService")
public class NginxImpl implements INginxService {
	
	@Autowired
	private NginxDAO nginxDAO;

	private static final Logger logger = Logger.getLogger(NginxImpl.class.getName());

	@Override
	public String infoquery(String starttime,String endtime,String sourceip,int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> nginxCounts = nginxDAO.queryCounts(starttime,endtime,sourceip);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(nginxCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> nginxLists = nginxDAO.infoquery(starttime,endtime,sourceip,pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", nginxCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : nginxLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME")));
				jsonObject.addProperty("host_ip", map.get("HOST_IP")==null?"":map.get("HOST_IP").toString());
				jsonObject.addProperty("host_name", map.get("HOST_NAME")==null?"":map.get("HOST_NAME").toString());
				jsonObject.addProperty("acc_ip", map.get("ACC_IP")==null?"":map.get("ACC_IP").toString());
				jsonObject.addProperty("acc_times", map.get("ACC_TIMES")==null?"":map.get("ACC_TIMES").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			
			List<Map<String, Object>> pieByniginxLists = nginxDAO.infoqueryBynginx(starttime,endtime,sourceip);
			JsonArray pieByniginxresult = new JsonArray();
			
			for (Map<String, Object> map : pieByniginxLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("host_name", map.get("HOST_NAME")==null?"":map.get("HOST_NAME").toString());
				jsonObject.addProperty("count", map.get("COUNT")==null?0:Integer.parseInt(map.get("COUNT").toString()));
				pieByniginxresult.add(jsonObject);
			}
			response.add("result1", pieByniginxresult);
			
			List<Map<String, Object>> pietop10Lists = nginxDAO.infoquerytop10(starttime,endtime,sourceip,pageIndex*pageSize, pageSize);
			JsonArray pietop10result = new JsonArray();
			
			for (Map<String, Object> map : pietop10Lists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("acc_ip", map.get("ACC_IP")==null?"":map.get("ACC_IP").toString());
				jsonObject.addProperty("count", map.get("COUNT")==null?0:Math.round(Float.parseFloat(map.get("COUNT").toString())));
				pietop10result.add(jsonObject);
			}
			response.add("result2", pietop10result);
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String detailquery(String starttime, String endtime, String sourceip, String serviceid, int pageIndex,
			int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> nginxCounts = nginxDAO.querydetailCounts(starttime,endtime,sourceip,serviceid);
			
			if(pageSize==0){
				pageSize=Integer.parseInt(nginxCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> nginxLists = nginxDAO.detailquery(starttime,endtime,sourceip,serviceid,pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", nginxCounts.get("COUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : nginxLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("check_time", map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME")));
				jsonObject.addProperty("host_ip", map.get("HOST_IP")==null?"":map.get("HOST_IP").toString());
				jsonObject.addProperty("host_name", map.get("HOST_NAME")==null?"":map.get("HOST_NAME").toString());
				jsonObject.addProperty("service_type", map.get("SERVICE_TYPE")==null?"":map.get("SERVICE_TYPE").toString());
				jsonObject.addProperty("acc_time", map.get("ACC_TIME")==null?"":simpleDateFormat.format((Date)map.get("ACC_TIME")));
				jsonObject.addProperty("acc_ip", map.get("ACC_IP")==null?"":map.get("ACC_IP").toString());
				jsonObject.addProperty("cid", map.get("CID")==null?"":map.get("CID").toString());
				jsonObject.addProperty("park_no", map.get("PARK_NO")==null?"":map.get("PARK_NO").toString());
				result.add(jsonObject);
			}
			response.add("result", result);
			
			List<Map<String, Object>> nginxByserviceidLists = nginxDAO.detailqueryByserviceid(starttime,endtime,sourceip,serviceid);
			JsonArray result1 = new JsonArray();
			
			for (Map<String, Object> map : nginxByserviceidLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("service_type", map.get("SERVICE_TYPE")==null?"":map.get("SERVICE_TYPE").toString());
				jsonObject.addProperty("count", map.get("COUNT")==null?0:Math.round(Float.parseFloat(map.get("COUNT").toString())));
				result1.add(jsonObject);
			}
			response.add("result1", result1);
			
			List<Map<String, Object>> nginxtop10Lists = nginxDAO.detailquerytop10(starttime,endtime,sourceip,serviceid,pageIndex*pageSize, pageSize);
			JsonArray result2 = new JsonArray();
			
			for (Map<String, Object> map : nginxtop10Lists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("acc_ip", map.get("ACC_IP")==null?"":map.get("ACC_IP").toString());
				jsonObject.addProperty("count", map.get("COUNT")==null?0:Math.round(Float.parseFloat(map.get("COUNT").toString())));
				result2.add(jsonObject);
			}
			response.add("result2", result2);
			
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
