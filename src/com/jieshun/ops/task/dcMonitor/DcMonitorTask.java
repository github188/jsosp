package com.jieshun.ops.task.dcMonitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.dao.DcMonitorTaskDao;
import com.jieshun.ops.util.OSSUtil;

public class DcMonitorTask{
	
	private static final Logger logger = Logger.getLogger(DcMonitorTask.class);

	@Autowired
	private OSSUtil ossUtil;
	@Autowired
	private DcMonitorTaskDao dcMonitorTaskDao;
	
	protected void execute() {
		try {
			logger.info("数据中心检测任务开始执行..........");
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_jhtdc");
			
			Calendar cal=Calendar.getInstance();
			Date today=cal.getTime();
			cal.add(Calendar.DATE,-1);
			Date time=cal.getTime();
			String yestd = new SimpleDateFormat("yyyyMMdd").format(time);
			
			List<Map<String, String>> DClist = dcMonitorTaskDao.listDC(yestd);
			List<Map<String, String>> DCOutlist = dcMonitorTaskDao.listOutDC(yestd);
			
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			List<Map<String, Object>> projectList = dcMonitorTaskDao.listDCProjects();
			List<String> ppp = new ArrayList<>();
			//获取图片总数
			ArrayList<HashMap<String, String>> picList = new ArrayList<>();
			//出场图片
			ArrayList<HashMap<String, String>> picOutList = new ArrayList<>();
			
			ossUtil.init();
			for (Map<String, Object> map : projectList) {
				String code = map.get("CODE")==null?"":map.get("CODE").toString();
				ppp.add(code);
				HashMap<String, String> picMap = new HashMap<>();
				picMap.put("CODE", code);
				picMap.put("PICCOUNTS", ""+ossUtil.ListOSSCounts(code+"/NISSP_IMG_PARK_IN/"+yestd+"/"));
				picMap.put("TIME", yestd);
				picList.add(picMap);
				HashMap<String, String> picOutMap = new HashMap<>();
				picOutMap.put("CODE", code);
				picOutMap.put("PICCOUNTS", ""+ossUtil.ListOSSCounts(code+"/NISSP_IMG_PARK_OUT/"+yestd+"/"));
				picOutMap.put("TIME", yestd);
				picOutList.add(picOutMap);
			}
			ossUtil.shutdown();
			
			List<Map<String, Object>> jsifsInList = new ArrayList<>();
			List<Map<String, Object>> jsifsOutList = new ArrayList<>();
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_smzf_r");
			List<Map<String, Object>> smzfInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> smzfOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(smzfInList);
			jsifsOutList.addAll(smzfOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_zuyong_r");
			List<Map<String, Object>> zuyongInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> zuyongOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(zuyongInList);
			jsifsOutList.addAll(zuyongOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_qst_r");
			List<Map<String, Object>> qstInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> qstOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(qstInList);
			jsifsOutList.addAll(qstOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_wanke_r");
			List<Map<String, Object>> wankeInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> wankeOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(wankeInList);
			jsifsOutList.addAll(wankeOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_biguiyuan2_r");
			List<Map<String, Object>> biguiyuan2InList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> biguiyuan2OutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(biguiyuan2InList);
			jsifsOutList.addAll(biguiyuan2OutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_hqc_r");
			List<Map<String, Object>> hqcInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> hqcOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(hqcInList);
			jsifsOutList.addAll(hqcOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_tjwy_r");
			List<Map<String, Object>> tjwyInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> tjwyOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(tjwyInList);
			jsifsOutList.addAll(tjwyOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_smsy_r");
			List<Map<String, Object>> smsyInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> smsyOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(smsyInList);
			jsifsOutList.addAll(smsyOutList);
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_zswy_r");
			List<Map<String, Object>> zswyInList = dcMonitorTaskDao.listjsifsInProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			List<Map<String, Object>> zswyOutList = dcMonitorTaskDao.listjsifsOutProjects(new SimpleDateFormat("yyyy-MM-dd").format(time),new SimpleDateFormat("yyyy-MM-dd").format(today), ppp);
			jsifsInList.addAll(zswyInList);
			jsifsOutList.addAll(zswyOutList);

			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			if(jsifsInList!=null && jsifsInList.size()>0){
				dcMonitorTaskDao.insertJsifs(projectList);
				dcMonitorTaskDao.updateJsifs(jsifsInList);
				dcMonitorTaskDao.updateDC(DClist);
				dcMonitorTaskDao.updatePics(picList);
			}
			
			if(jsifsOutList!=null && jsifsOutList.size()>0){
				dcMonitorTaskDao.updateOutJsifs(jsifsOutList);
				dcMonitorTaskDao.updateOutDC(DCOutlist);
				dcMonitorTaskDao.updateOutPics(picOutList);
			}
			
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			logger.info("数据中心检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("数据中心检测任务执行错误..........");
			e.printStackTrace();
		}
	}
	
}