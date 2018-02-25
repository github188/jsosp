package com.jieshun.ops.autode.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.dao.DataDeleteDAO;
import com.jieshun.ops.autode.service.IDataDeleteService;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.task.datadelete.DataDeleteTask2;
import com.jieshun.ops.util.JdbcConn;
import com.jieshun.ops.util.PLATFORM;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:21:39
 */
@Service("dataDeleteService")
public class DataDeleteServiceImpl implements IDataDeleteService {

	@Autowired
	private DataDeleteDAO dataDeleteDAO;
	@Autowired
	private DataDeleteTask2 dataDeleteTask2;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(DataDeleteServiceImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			//全部
			Map<String, Object> DataDeleteCounts = dataDeleteDAO.queryDataDeleteCounts();
			//运行中
			Map<String, Object> DataDeleteCounts1 = dataDeleteDAO.queryDataDeleteCounts1();
			//未开始
			Map<String, Object> DataDeleteCounts2 = dataDeleteDAO.queryDataDeleteCounts2();
			
			if(pageSize==0){
				pageSize=Integer.parseInt(DataDeleteCounts.get("DATADELETECOUNTS").toString());
			}
			
			List<Map<String, Object>> dataDeleteLists = dataDeleteDAO.queryDataDeletelists(pageIndex*pageSize, pageSize);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("datadeletecount", DataDeleteCounts.get("DATADELETECOUNTS").toString());
			response.addProperty("datadeletecount1", DataDeleteCounts1.get("DATADELETECOUNTS").toString());
			response.addProperty("datadeletecount2", DataDeleteCounts2.get("DATADELETECOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : dataDeleteLists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("code", map.get("SUB_CODE")==null?"":map.get("SUB_CODE").toString());
				jsonObject.addProperty("name", map.get("SUB_NAME")==null?"":map.get("SUB_NAME").toString());
				jsonObject.addProperty("status", map.get("STATUS")==null?"":map.get("STATUS").toString());
				jsonObject.addProperty("enable", map.get("ENABLE")==null?"":map.get("ENABLE").toString());
				jsonObject.addProperty("starttime", map.get("START_TIME")==null?"":map.get("START_TIME").toString());
				jsonObject.addProperty("endtime", map.get("END_TIME")==null?"":map.get("END_TIME").toString());
				jsonObject.addProperty("createtime", map.get("CREATE_TIME")==null?"":map.get("CREATE_TIME").toString());
				jsonObject.addProperty("platform", map.get("PLATFORM")==null?"":map.get("PLATFORM").toString());
				jsonObject.addProperty("log", map.get("LOG")==null?"":map.get("LOG").toString());
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
	public String add(String code, String platform,boolean isDeleted,String prefix) {
		JsonObject response = new JsonObject();
		try {
			String id = UUID.randomUUID().toString().replace("-", "");

			int status = 0; // 未开始
			int enable = 1;// 启动任务
			String start_time = null;
			String end_time = null;
			String log = "";
			int isDel = 0;
			if(isDeleted){
				isDel = 1;
			}
			
			JdbcConn jdbcConn = getConnByPlatform(platform.toUpperCase());
			String url = jdbcConn.getUrl();
			String user = jdbcConn.getUsername();
			String password = jdbcConn.getPasswd();
			
			// 加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");

			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);

			if (!conn.isClosed()){
				logger.info("Succeeded connecting to the Database!" + url);
			}
			// 结果集
			ResultSet rs = conn.createStatement().executeQuery("select NAME from np_cf_subsystem where code ='"+code+"'");
			
			String subName = "" ;
			while (rs.next()) {
				subName = rs.getString("NAME");
			}
			
			rs.close();
			conn.close();
//			if(!"".equals(subName)){
				dataDeleteDAO.addDataDelete(id, code, subName, status, enable, start_time, end_time, "任务未开始",
						PLATFORM.valueOf(platform.toUpperCase()).getContext(),isDel,prefix);
				response.addProperty("returnCode", 0);
				response.addProperty("message", "新增成功");
				return response.toString();
//			}
//			response.addProperty("returnCode", 0);
//			response.addProperty("message", "查询不到该子系统");
//			return response.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增异常");
			return response.toString();
		}
	}

	@Override
	public String queryById(String id) {
		JsonObject response = new JsonObject();
		try{
			Map<String, Object> dataDelete = dataDeleteDAO.queryById(id);
			
			JsonArray result = new JsonArray();
			JsonObject jsonObject = new JsonObject();
			
			jsonObject.addProperty("id", dataDelete.get("ID")==null?"":dataDelete.get("ID").toString());
			jsonObject.addProperty("log", dataDelete.get("LOG")==null?"":dataDelete.get("LOG").toString());
			
			result.add(jsonObject);
			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			return response.toString();
		} catch (Exception e) {
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}
	
	JdbcConn getConnByPlatform(String platform){
		Set<JdbcConn> jdbcConns = dataDeleteTask2.getJdbcConns();
		for (JdbcConn jdbcConn : jdbcConns) {
			if(platform.equals(jdbcConn.getPlatform())){
				return jdbcConn;
			}
		}
		return null;
	}
}
