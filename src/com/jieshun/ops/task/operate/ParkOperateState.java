package com.jieshun.ops.task.operate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jieshun.ops.master.dao.CloudPlatformDao;
import com.jieshun.ops.master.dao.NisspPlatformDao;
import com.jieshun.ops.master.model.NisspPlatform;
import com.jieshun.ops.master.service.NisspPlatFormService;
import com.jieshun.ops.project.dao.ProjectDao;
import com.jieshun.ops.project.dao.ProjectOperateDao;
import com.jieshun.ops.project.model.Project;

/**
 * 车场运营数据统计（日度）
 * 
 * @author 刘淦潮
 *
 */
@Component
public class ParkOperateState {

	private static final Logger logger = Logger
			.getLogger(ParkOperateState.class.getName());
	@Autowired
	private ProjectDao projectDao;
	@Autowired
	private CloudPlatformDao cloudPlatformDao;
	@Autowired
	private NisspPlatformDao nisspPlatformDao;
	@Autowired
	private NisspPlatFormService nisspPlatformService;
	@Autowired
	private ProjectOperateDao projectOperateDao;

	private static boolean running = false;

	private static int runningTasks = 0;

	private Map<String, NisspPlatform> nisspPlatforms;
	private static ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(4,
	        new BasicThreadFactory.Builder().namingPattern("ParkOperateState-schedule-pool-%d").daemon(true).build());   
//	private static ExecutorService threadPool = Executors.newFixedThreadPool(4);

	@Scheduled(cron="0 15 3 * * ? ") //每天凌晨3点15分钟执行
	//@Scheduled(cron = "20 */30 * * * ? ")
	public void execute() {
		while (running && runningTasks > 0) {
			try {
				Thread.sleep(1000 * 30);
			} catch (Exception e) {
				logger.debug(e);
			}
		}
		running = true;
		// 初始化Nissp平台的记录
		nisspPlatforms = initNisspPlatformData();
		// step1,从项目表中取出项目列表（条件：1、watch=1）
		List<Project> projects = projectDao.selectWatchProject();
		if (projects != null && projects.size() > 0) {

			// step2,逐人调用工作线程执行统计任务
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());// 从昨天开始统计
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			logger.trace("总项目数:" + projects.size());
			for (Project project : projects) {
				NisspPlatform platform = this.getNisspPlatform(project
						.getPlatformId());
				if(platform.getPlatformCode()==null||platform.getPlatformCode().trim().equals("")){
					continue;//没有平台编号，不处理
				}
				threadPool.execute(new ParkOperateStateTask(this, platform
						.getPlatformCode(), project, calendar.getTime()));
				runningTasks++;
			}
		}
		running = false;
	}

	/**
	 * 参数配置初始化
	 */
	public synchronized void decreaseTask() {
		runningTasks--;
	}

	public NisspPlatform getNisspPlatform(String platformId) {
		return this.nisspPlatforms.get(platformId);
	}

	private Map<String, NisspPlatform> initNisspPlatformData() {
		List<NisspPlatform> list = nisspPlatformDao.getAlltNisspPlatform();
		Map<String, NisspPlatform> data = new HashMap<String, NisspPlatform>();
		for (NisspPlatform pf : list) {
			data.put(pf.getId(), pf);
		}
		return data;
	}

	public ProjectDao getProjectDao() {
		return projectDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public CloudPlatformDao getCloudPlatformDao() {
		return cloudPlatformDao;
	}

	public void setCloudPlatformDao(CloudPlatformDao cloudPlatformDao) {
		this.cloudPlatformDao = cloudPlatformDao;
	}

	public NisspPlatformDao getNisspPlatformDao() {
		return nisspPlatformDao;
	}

	public void setNisspPlatformDao(NisspPlatformDao nisspPlatformDao) {
		this.nisspPlatformDao = nisspPlatformDao;
	}

	public NisspPlatFormService getNisspPlatformService() {
		return nisspPlatformService;
	}

	public void setNisspPlatformService(
			NisspPlatFormService nisspPlatformService) {
		this.nisspPlatformService = nisspPlatformService;
	}

	public ProjectOperateDao getProjectOperateDao() {
		return projectOperateDao;
	}

	public void setProjectOperateDao(ProjectOperateDao projectOperateDao) {
		this.projectOperateDao = projectOperateDao;
	}

	public Map<String, NisspPlatform> getNisspPlatforms() {
		return nisspPlatforms;
	}

	public void setNisspPlatforms(Map<String, NisspPlatform> nisspPlatforms) {
		this.nisspPlatforms = nisspPlatforms;
	}
}
