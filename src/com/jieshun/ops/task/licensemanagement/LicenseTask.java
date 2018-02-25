package com.jieshun.ops.task.licensemanagement;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.autode.dao.LicenseManagementDAO;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.util.SendMsgUtil;

public class LicenseTask {

	@Autowired
	private SMSSender smsSender;
	@Autowired
	private LicenseManagementDAO licenseManagementDAO;

	private static final Logger logger = Logger.getLogger(LicenseTask.class);
	
	protected void execute() {
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			logger.info("License检测任务开始执行..........");
			
			Map<String, Object> licenseManagementCounts = licenseManagementDAO.queryLicenseManagementCounts();

			int counts = Integer.parseInt(licenseManagementCounts.get("LICENSEMANAGENMENTCOUNTS").toString());

			List<Map<String, Object>> list = licenseManagementDAO.queryLicenseManagementlists(0, counts);

			for (Map<String, Object> map : list) {
				Date endDate = (Date) map.get("END_TIME");

				if (Math.abs(endDate.getTime() -  System.currentTimeMillis()) <= 15 * 24 * 60 * 60 * 1000) {
					String msg = "License即将过期!过期时间:" + map.get("END_TIME") + ",项目名:" + map.get("PROJECT_NAME")  + ",产品类型:" + map.get("PROJECT_TYPE");
//					SendMsgUtil.sendMessage(map.get("TELEPHONE") == null ? "" : map.get("TELEPHONE").toString(),msg);
					int flag = smsSender.sendMessage(map.get("TELEPHONE") == null ? "" : map.get("TELEPHONE").toString(),msg);
				}
			}
			logger.info("License检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("License检测任务执行错误..........");
			e.printStackTrace();
		}
	}
}