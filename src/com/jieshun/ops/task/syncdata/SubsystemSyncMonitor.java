package com.jieshun.ops.task.syncdata;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.jieshun.ops.util.JdbcConn;

public class SubsystemSyncMonitor {
	
	private static final Logger logger = Logger.getLogger(SubsystemSyncMonitor.class.getName());
	private static ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(1,
	        new BasicThreadFactory.Builder().namingPattern("SubsystemSyncMonitor-schedule-pool-%d").daemon(true).build());   
//	private static ExecutorService threadPool = Executors.newFixedThreadPool(1);
	
	@Scheduled(cron = "* * 0/1 * * ? ")
	public void execute() {
		logger.info("subsystem sync job start.....");
		try {
			JdbcConn source = new JdbcConn("", "rdst6w84lo6sry387453rw.mysql.rds.aliyuncs.com", 
					"public_smzf", "public_smzf");
			JdbcConn target = new JdbcConn("", "rm-wz90awe9csx9345m3rw.mysql.rds.aliyuncs.com", 
					"ywpt_n", "64_JRvcy");
			threadPool.execute(new SubSytemSyncTask(source, target));
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace());
		}
	}

}
