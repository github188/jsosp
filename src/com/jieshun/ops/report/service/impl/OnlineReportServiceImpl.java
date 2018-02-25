package com.jieshun.ops.report.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.report.dao.OnlineReportDAO;
import com.jieshun.ops.report.service.IOnlineReportService;
import com.jieshun.ops.util.StringUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@Service("OnlineReportImpl")
public class OnlineReportServiceImpl implements IOnlineReportService {

	@Autowired
	private OnlineReportDAO onlineReportDAO;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(OnlineReportServiceImpl.class.getName());

	@Override
	public String queryprojectlist(int pageIndex,int pageSize,String platformid,String projectname,String projectcode,String starttime,String endtime) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			//全部
			Map<String, Object> projectlistCounts = onlineReportDAO.queryprojectlistCounts(platformid,projectname,projectcode,starttime,endtime);
			//运行中
			if(pageSize==0){
				pageSize=Integer.parseInt(projectlistCounts.get("PROJECTLISTCOUNTS").toString());
			}
			
			List<Map<String, Object>> onlineRatelists = onlineReportDAO.queryOnlineProjectlists(0, Integer.parseInt(projectlistCounts.get("PROJECTLISTCOUNTS").toString()),platformid,projectname,projectcode,starttime,endtime);
			
			DecimalFormat    df   = new DecimalFormat("######0.00");
			int count=0;
			float sum = 0;
			
			int offlinetimes=0;
			int offlinetime=0;
			for (Map<String, Object> map : onlineRatelists) {
				int offtime = Integer.parseInt(map.get("OFFLINE_TIME")==null?"":map.get("OFFLINE_TIME").toString());
				int offtimes = map.get("OFFLINE_TIMES")==null?0:Integer.parseInt(map.get("OFFLINE_TIMES").toString());
				float percent = ((float)(24*60*60-offtime)/(24*60*60));
				count ++;
				sum +=percent;
				offlinetimes+=offtimes;
				offlinetime+=offtime;
			}
			
			response.addProperty("onlineRate", df.format((sum/count)*100)+"%");
			
			List<Map<String, Object>> onlineProjectlists = onlineReportDAO.queryOnlineProjectlists(pageIndex*pageSize, pageSize,platformid,projectname,projectcode,starttime,endtime);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("projectlistCounts", projectlistCounts.get("PROJECTLISTCOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			
//			int count=0;
//			float sum = 0;
			
//			int offlinetimes=0;
//			int offlinetime=0;
			for (Map<String, Object> map : onlineProjectlists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("platform_name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				jsonObject.addProperty("code", map.get("PROJECT_CODE")==null?"":map.get("PROJECT_CODE").toString());
				jsonObject.addProperty("name", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
				jsonObject.addProperty("date", simpleDateFormat.format(map.get("STATE_DATE")==null?"":(Date)map.get("STATE_DATE")));
				int offtimes = map.get("OFFLINE_TIMES")==null?0:Integer.parseInt(map.get("OFFLINE_TIMES").toString());
				jsonObject.addProperty("offtimes",offtimes );
				int offtime = Integer.parseInt(map.get("OFFLINE_TIME")==null?"":map.get("OFFLINE_TIME").toString());
				jsonObject.addProperty("offtime", offtime);
				float percent = ((float)(24*60*60-offtime)/(24*60*60));
				jsonObject.addProperty("percent", df.format(percent*100)+"%");
				result.add(jsonObject);
//				count ++;
//				sum +=percent;
//				offlinetimes+=offtimes;
//				offlinetime+=offtime;
			}
			
			response.addProperty("offlinetimes", offlinetimes);
			response.addProperty("offlinetime", offlinetime);
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
	public String queryplatformlist() {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			List<Map<String, Object>> platformlist = onlineReportDAO.queryplatformlist();
			
			JsonArray result = new JsonArray();
			
			for (Map<String, Object> map : platformlist) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				result.add(jsonObject);
			}
			
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

	@Override
	public String onlinereportdetail(int pageIndex,int pageSize,String id) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			Map<String, Object> projectlistCounts = onlineReportDAO.onlinereportdetailCounts(id);
			
			List<Map<String, Object>> list = onlineReportDAO.onlinereportdetail(pageIndex*pageSize, pageSize,id);
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : list) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID") == null ? "" : map.get("ID").toString());
				jsonObject.addProperty("code", map.get("CODE") == null ? "" : map.get("CODE").toString());
				jsonObject.addProperty("name",map.get("NAME") == null ? "" : map.get("NAME").toString());
				Date event_time = (Date)map.get("EVENT_TIME");
				jsonObject.addProperty("event_time",simpleDateFormat.format(event_time));
				jsonObject.addProperty("event_name",map.get("EVENT_NAME") == null ? "" : map.get("EVENT_NAME").toString());
				jsonObject.addProperty("reason_type",map.get("REASON_TYPE") == null ? "" : map.get("REASON_TYPE").toString());
				jsonObject.addProperty("create_user",map.get("CREATE_USER") == null ? "" : map.get("CREATE_USER").toString());
				result.add(jsonObject);
			}

			response.addProperty("counts", projectlistCounts.get("counts").toString());
			response.add("result", result);

			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String onlinereporthandle(String ids, String reason,String remark,String create_user) {
		TransactionStatus status = null;
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
			status = transactionManager.getTransaction(def);
			
			for (String id : ids.split(",")) {
				List<Map<String, Object>> list = onlineReportDAO.queryofflineHandleById(id);
				
				if(list == null ||list.size()==0){
					String handleId = UUID.randomUUID().toString().replace("-", "");
					onlineReportDAO.inserthandleByofflineId(handleId, reason,create_user,remark);
					onlineReportDAO.updateofflineByid(id, handleId);
				}else{
					String handleId = list.get(0).get("HANDLE_ID") == null ? "" : list.get(0).get("HANDLE_ID").toString();
//				onlineReportDAO.updateofflineByid(id, handleId);
					onlineReportDAO.updatehandleHandleId(handleId, reason,remark,create_user);
				}
			}
			
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
	public String queryofflineprojectlist(int pageIndex, int pageSize, String platformid, String projectname,
			String projectcode, String starttime, String endtime, String handle) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			//全部
			Map<String, Object> projectlistCounts = onlineReportDAO.queryofflineprojectlistCounts(platformid,projectname,projectcode,starttime,endtime,handle);
			//运行中
			if(pageSize==0){
				pageSize=Integer.parseInt(projectlistCounts.get("PROJECTLISTCOUNTS").toString());
			}
			
			List<Map<String, Object>> onlineProjectlists = onlineReportDAO.queryofflineProjectlists(pageIndex*pageSize, pageSize,platformid,projectname,projectcode,starttime,endtime,handle);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("projectlistCounts", projectlistCounts.get("PROJECTLISTCOUNTS").toString());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			DecimalFormat    df   = new DecimalFormat("######0.00");
			
			for (Map<String, Object> map : onlineProjectlists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("platform_name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				jsonObject.addProperty("code", map.get("PROJECT_CODE")==null?"":map.get("PROJECT_CODE").toString());
				jsonObject.addProperty("name", map.get("PROJECT_NAME")==null?"":map.get("PROJECT_NAME").toString());
				jsonObject.addProperty("date", simpleDateFormat.format(map.get("STATE_DATE")==null?"":(Date)map.get("STATE_DATE")));
				jsonObject.addProperty("offtimes", map.get("OFFLINE_TIMES")==null?0:Integer.parseInt(map.get("OFFLINE_TIMES").toString()));
				jsonObject.addProperty("createUser", map.get("CREATE_USER")==null?"":map.get("CREATE_USER").toString());
				int offtime = Integer.parseInt(map.get("OFFLINE_TIME")==null?"":map.get("OFFLINE_TIME").toString());
				jsonObject.addProperty("offtime", offtime);
				float percent = ((float)(24*60*60-offtime)/(24*60*60));
				jsonObject.addProperty("percent", df.format(percent*100)+"%");
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
	public String actualTimeOnlineReport(int pageIndex, int pageSize, String platformid) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			//全部
			Map<String, Object> actualTimeOnlineReportCounts = onlineReportDAO.actualTimeOnlineReportCounts(platformid);
			//运行中
			if(pageSize==0){
				pageSize=Integer.parseInt(actualTimeOnlineReportCounts.get("COUNTS").toString());
			}
			
			List<Map<String, Object>> offlineProjectlists = onlineReportDAO.actualTimeOnlineReportlists(pageIndex*pageSize, pageSize,platformid);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("offcounts", actualTimeOnlineReportCounts.get("COUNTS").toString());
			response.addProperty("counts", actualTimeOnlineReportCounts.get("PLATCOUNTS").toString());
			
			JsonArray result = new JsonArray();
			int All=0;
			int Off=0;
			DecimalFormat    df   = new DecimalFormat("######0.00");
			for (Map<String, Object> map : offlineProjectlists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("platform_name", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				int offcount =  map.get("OFFCOUNT")==null?0:Integer.parseInt(map.get("OFFCOUNT").toString());
				int allcounts =  map.get("ONCOUNTS")==null?0:Integer.parseInt(map.get("ONCOUNTS").toString())+offcount;
				jsonObject.addProperty("offcounts", offcount);
				jsonObject.addProperty("allcounts", allcounts);
				jsonObject.addProperty("offRate", df.format(((float)offcount/allcounts)*100));
				Off +=offcount;
				All +=allcounts;
				result.add(jsonObject);
			}
			
			response.add("result", result);
			response.addProperty("offRateAll", df.format(((float)Off/All)*100));
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String actualTimeOnlineReportDetail(String platformid) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			List<Map<String, Object>> offlineProjectlists = onlineReportDAO.actualTimeOnlineReportDetaillists(platformid);
			
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.addProperty("counts", offlineProjectlists.size());
			
			JsonArray result = new JsonArray();
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (Map<String, Object> map : offlineProjectlists) {
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", map.get("ID")==null?"":map.get("ID").toString());
				jsonObject.addProperty("platform", map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString());
				jsonObject.addProperty("name", map.get("NAME")==null?"":map.get("NAME").toString());
				jsonObject.addProperty("code", map.get("PROJECT_CODE")==null?"":map.get("PROJECT_CODE").toString());
				jsonObject.addProperty("event_name", map.get("EVENT_NAME")==null?"":map.get("EVENT_NAME").toString());
				jsonObject.addProperty("event_time", simpleDateFormat.format(map.get("EVENT_TIME")==null?"":(Date)map.get("EVENT_TIME")));
				jsonObject.addProperty("offtimes", map.get("EVENT_TIME")==null?"":StringUtil.CheckDates((Date)map.get("EVENT_TIME")));
				result.add(jsonObject);
			}
			
			response.add("result", result);
			
			return response.toString().replaceAll("OFFLINE", "离线");
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String exportOnlineProjects(String platformid, HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			List<Map<String, Object>> offlineProjectlists = onlineReportDAO.actualTimeOnlineReportDetaillists(platformid);
			
			String path = "excels" + System.getProperty("file.separator") +  System.currentTimeMillis() + ".xls";
			FileOutputStream os;
			os = new FileOutputStream(new File(request.getSession()
					.getServletContext().getRealPath("")
					+ System.getProperty("file.separator") + path));
			// 创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			// 创建新的一页
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);
			Label condition0 = new Label(0, 0, "查询条件");
			sheet.addCell(condition0);
			Label condition1 = new Label(1, 0, "平台名称");
			sheet.addCell(condition1);
			Label condition2 = new Label(2, 0, "项目名称");
			sheet.addCell(condition2);
			Label condition3 = new Label(3, 0, "项目编号");
			sheet.addCell(condition3);
			Label condition4 = new Label(4, 0, "事件名称");
			sheet.addCell(condition4);
			Label condition5 = new Label(5, 0, "发生时间");
			sheet.addCell(condition5);
			
			JsonArray result = new JsonArray();
			int count = 0;
			for (Map<String, Object> map : offlineProjectlists) {
				JsonObject jsonObject = new JsonObject();
				String platform = map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString();
				String name = map.get("NAME")==null?"":map.get("NAME").toString();
				String code = map.get("PROJECT_CODE")==null?"":map.get("PROJECT_CODE").toString();
				String event_name = map.get("EVENT_NAME")==null?"":map.get("EVENT_NAME").toString();
				String event_time = map.get("EVENT_TIME")==null?"":map.get("EVENT_TIME").toString();
				
				sheet.addCell(new Label(0, count+1, "#"));
				sheet.addCell(new Label(1, count+1, platform));
				sheet.addCell(new Label(2, count+1, name));
				sheet.addCell(new Label(3, count+1, code));
				sheet.addCell(new Label(4, count+1, event_name));
				sheet.addCell(new Label(5, count+1, event_time));
				
				result.add(jsonObject);
				count++;
			}
			
			workbook.write();
			workbook.close();
			os.close();
			
//			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("url", path.replace("\\", "/"));
			response.addProperty("message", "查询成功");
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}
	
}
