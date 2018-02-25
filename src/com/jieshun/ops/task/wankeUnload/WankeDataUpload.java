package com.jieshun.ops.task.wankeUnload;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.autode.dao.WankeUnsubmitSubsystemDAO;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.task.redisMonitor.RedisBigTableTask;

public class WankeDataUpload {

	@Autowired
	private WankeUnsubmitSubsystemDAO wankeUnsubmitSubsystemDAO;
	
	private static final Logger logger = Logger.getLogger(RedisBigTableTask.class);
	
	protected void execute() {
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_wanke");
			logger.info("万科数据上传检测任务开始执行..........");

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal=Calendar.getInstance();
			String end = simpleDateFormat.format(cal.getTime());
			cal.add(Calendar.DATE,-1);
			String start = simpleDateFormat.format(cal.getTime());
			
			Map<String, Object> map = wankeUnsubmitSubsystemDAO.queryDataUpload(start+" 00:00:00",end+" 00:00:00");

			String counts ="";
			
			if(map!=null &&map.size()!=0){
				counts = map.get("COUNTS")==null?"":map.get("COUNTS").toString();
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			wankeUnsubmitSubsystemDAO.insert(UUID.randomUUID().toString().replace("-", ""), start, counts);
			
			logger.info("万科数据上传检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("万科数据上传检测任务执行错误..........");
			e.printStackTrace();
		}
	}

}
