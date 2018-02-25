package com.jieshun.ops.task.offline;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jieshun.ops.comm.mybatis.dbconn.DynamicDataSource;
import com.jieshun.ops.master.dao.NisspPlatformDao;
import com.jieshun.ops.master.model.NisspPlatform;
import com.jieshun.ops.master.model.OpenfireDO;
import com.jieshun.ops.project.dao.OpenfireDAO;
import com.jieshun.ops.project.dao.ProjectDao;

/**
 * 项目离线监控
 */
@Component
public class ProjectOnlineMonitor {
	private static final Logger logger = Logger
			.getLogger(ProjectOnlineMonitor.class.getName());
	@Autowired
	private NisspPlatformDao nisspPlatformDao;
	
	@Autowired
	private ProjectDao projectDao;
	
	@Autowired
	private DynamicDataSource dataSource;

	private static ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(4,
	        new BasicThreadFactory.Builder().namingPattern("ProjectOnlineMonitor-schedule-pool-%d").daemon(true).build());   
//	private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	private static int runningTasks=0;
	
	//private static boolean running=false;

	//@Scheduled(cron = "20 0 2 * * ? ")
	// 每分钟执行一次
	public void execute() {
		logger.debug("ProjectOnlineMonitor----------->0");
		if(runningTasks>0) {
			return;
		}
		//running=true;
		logger.debug("ProjectOnlineMonitor----------->1");
		List<OpenfireDO> result = nisspPlatformDao.getAllPlatformIncludeOFUrl();
		for(OpenfireDO of:result){
			
			//没有设定编号的平台，不统计
			if(of.getPlatformCode()==null||of.getPlatformCode().trim().equals("")){
				continue;
			}
			threadPool.execute(new ProjectOnlineTask(this,of));
			runningTasks++;
		}
		logger.debug("ProjectOnlineMonitor----------->2");
		//running=false;
	}

	/**
	 * 参数配置初始化
	 */
	public synchronized void decreaseTask() {
		runningTasks--;
	}

	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public DynamicDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DynamicDataSource dataSource) {
		this.dataSource = dataSource;
	}

	

	
}
