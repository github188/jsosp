package com.jieshun.ops.task.datadelete;

import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;

import com.jieshun.ops.util.JdbcConn;

public class DataDeleteCheck {

	private static final Logger logger = Logger.getLogger(DataDeleteCheck.class);

	private static final long PERIOD_DAY = 2 * 60 * 1000;
	
	Set<JdbcConn> jdbcConns ;

	public Set<JdbcConn> getJdbcConns() {
		return jdbcConns;
	}

	public void setJdbcConns(Set<JdbcConn> jdbcConns) {
		this.jdbcConns = jdbcConns;
		try {
			ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
			        new BasicThreadFactory.Builder().namingPattern("DataDeleteCheck-schedule-pool-%d").daemon(true).build());
			DataDeleteTask task = new DataDeleteTask(jdbcConns);

			executorService.scheduleAtFixedRate(task,0, PERIOD_DAY,TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("程序发生异常", e);
		}
	}
}
