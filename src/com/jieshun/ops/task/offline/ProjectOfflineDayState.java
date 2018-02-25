package com.jieshun.ops.task.offline;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jieshun.ops.project.dao.ProjectDao;
import com.jieshun.ops.project.model.Project;

/**
 * 前端项目离线日度统计，按项目汇总每一天的离线次数、离线时长
 * 规则：	
 * 		1、每天零晨1：00,5:00开始汇总前一天的数据
 * 		2、每个项目单独一个工作线程进行处理
 * 		3、从昨天起一起往前逐天判断是否有汇总结果，如果没有进行汇总，直到一周结束
 * @author 刘淦潮
 *
 */
@Component
public class ProjectOfflineDayState {
	
	private static final Logger logger = Logger
			.getLogger(ProjectOfflineDayState.class.getName());
	@Autowired
	private ProjectDao projectDao;
	
	//@Value("${project.offline.predays}")
	private int predays=20;
	
	private static boolean running=false;
	private static ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(4,
	        new BasicThreadFactory.Builder().namingPattern("ProjectOfflineDayState-schedule-pool-%d").daemon(true).build());   
//	private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	//@Scheduled(cron="0 0 1,5 * * ? ") //每天凌晨1点,5点钟执行一次
	//@Scheduled(cron="0 */2 * * * ? ") //第十分钟统计一下
	public void execute(){
		while(running){
			try{
				Thread.sleep(1000*30);
			}catch(Exception e){
				logger.debug(e);
			}
		}
		running=true;
		//step1,从项目表中取出项目列表（条件：1、watch=1）
		List<Project> projects=projectDao.selectWatchProject();
		if(projects!=null && projects.size()>0){
			
			//step2,逐人调用工作线程执行统计任务 
			GregorianCalendar calendar=new GregorianCalendar();
			calendar.setTime(new Date());//从昨天开始统计
			//calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			for(Project project:projects){
				threadPool.execute(new OfflineDayStateTask(project,calendar.getTime(),projectDao,predays));
			}
		}
		running=false;
	}
}
