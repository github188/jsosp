package com.jieshun.ops.task.offline;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.jieshun.ops.project.dao.ProjectDao;
import com.jieshun.ops.project.model.OfflineDayState;
import com.jieshun.ops.project.model.Project;
import com.jieshun.ops.project.model.ProjectOnOffLine;

/**
 * 项目离线日度汇总
 * 
 * @author 刘淦潮
 *
 */
public class OfflineDayStateTask implements Runnable {

	private Project project;

	private Date startDate;
	
	private  int PREDAYS = 7;

	private ProjectDao projectDao;

	private SimpleDateFormat dformater = new SimpleDateFormat("yyyy-MM-dd ");

	private static final Logger logger = Logger
			.getLogger(OfflineDayStateTask.class.getName());

	public OfflineDayStateTask(Project project, Date startDate, ProjectDao dao,int predays) {
		this.project = project;
		this.startDate = (Date) startDate.clone();
		this.projectDao = dao;
		this.PREDAYS=predays;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (project != null && startDate != null) {
			Calendar stateDate = new GregorianCalendar();
			stateDate.setTime(this.startDate);
			state(stateDate.getTime());
			// 如果要统计之前的7天，就要处理
			for (int i = 1; i <= PREDAYS; i++) {
				stateDate.add(Calendar.DATE, -1);
				state(stateDate.getTime());
			}
		}
	}

	private void state(Date stateDate) {
	
		List<ProjectOnOffLine> logs = this.projectDao.selectOnOffLineEvents(
				project.getCode(), dformater.format(stateDate));
		OfflineDayState dayState = summarizing(logs, stateDate);

		dayState.setProjectId(project.getId());
		dayState.setStateDate(stateDate);
		// 先判断项目startDate是否已经有日度统计，如果有就不统计
		List<OfflineDayState> dayStates = this.projectDao
				.selectOfflineDayState(project.getId(),
						dformater.format(stateDate));

		if (dayStates != null && dayStates.size() > 0) {
			OfflineDayState existRecord = dayStates.get(0);
			// 只有数据有变化，才执行数据库更新操作，减少对数据库的压力
			if (existRecord.getOfflineTimes() != dayState.getOfflineTimes()
					|| existRecord.getOfflineTime() != dayState
							.getOfflineTime()) {
				dayState.setId(dayStates.get(0).getId());
				saveToDB(dayState, false);
			}

		} else {

			saveToDB(dayState, true);
		}

	}

	private OfflineDayState summarizing(List<ProjectOnOffLine> logs,
			Date stateDate) {
		//初始化
		// 次数
		 int offlineTimes = 0;
		// 时长,单位为秒
		 long offlineTime = 0;
		
		// 先按事件发生时间从早到晚排序

		Collections.sort(logs, new ProjectOnOffLineSort());

		GregorianCalendar gcalendar = new GregorianCalendar();

		gcalendar.setTime(stateDate);
		gcalendar.set(Calendar.HOUR_OF_DAY, 0);// 00：00：00开始的时候，
		gcalendar.set(Calendar.MINUTE, 0);
		gcalendar.set(Calendar.SECOND, 0);
		gcalendar.set(Calendar.MILLISECOND, 0);
		long lastLineTime = gcalendar.getTimeInMillis();
		OfflineDayState dayState = new OfflineDayState();
		int loop = 0;
		boolean duplicatedOnline=false;
		boolean isToday = false;
		if (gcalendar.get(Calendar.DATE) == new GregorianCalendar()
				.get(Calendar.DATE)) {
			isToday = true;
		}
		for (ProjectOnOffLine log : logs) {

			if (log.getEventName().equals("ONLINE")) {// 之前是离线
				if(!duplicatedOnline){
					offlineTimes++;
					offlineTime += (log.getEventTime().getTime() - lastLineTime) / 1000;
				}else{//连接是在线的日志，只需要便新的一下最后的
					duplicatedOnline=true;
				}
			} else {// 说明从00：00：00开始的时候，就是在线
				lastLineTime = log.getEventTime().getTime();
				if (loop == logs.size() - 1) {// 最后一条日志是离线的话，如果是当天的，就要截止当时时间，否则截止到00：00都是离线
					offlineTimes++;
					if (isToday) {
						offlineTime += (System.currentTimeMillis() - lastLineTime) / 1000;
					} else {
						gcalendar.add(Calendar.DATE, 1);
						offlineTime += (gcalendar.getTimeInMillis() - lastLineTime) / 1000;
					}
				}
				duplicatedOnline=false;//重置是否重复在线日志的状态为false;
			}
			loop++;
		}

		dayState.setOfflineTimes(offlineTimes);
		dayState.setOfflineTime(offlineTime);
		if (offlineTimes == 0) {// 如果没有离线日志，判断项目当前是否在线
			if (isToday == true) {// 如果是当天，即离线时长从当天00：00：00开始
				if (this.project.getIsOnline() == 0) {// 记下离线次数（1），及离线时长
					dayState.setOfflineTimes(1);
					offlineTime = (System.currentTimeMillis() - lastLineTime) / 1000;
					dayState.setOfflineTime(offlineTime);
				}
			} else {
				// 如果是当天，即要判断最后天次的日志是否离线，如果是，即该天全天离线
				ProjectOnOffLine lastEvent = projectDao
						.getProjectLastedOnOffLineEvent(this.project.getCode());
				if (lastEvent == null
						|| lastEvent.getEventName().equals("OFFLINE")) {
					dayState.setOfflineTimes(1);
					dayState.setOfflineTime(24 * 60 * 60);
				}
			}

		}
		return dayState;
	}

	private void saveToDB(OfflineDayState dayState, boolean insert) {
		try {

			if (dayState.getId() == null) {
				String id = UUID.randomUUID().toString().replace("-", "");
				dayState.setId(id);
			}
			if (insert) {
				projectDao.addOffLineDayState(dayState.getId(),
						dayState.getProjectId(),
						dformater.format(dayState.getStateDate()),
						dayState.getOfflineTimes(), dayState.getOfflineTime());
			} else {
				projectDao.updateOffLineDayState(dayState.getId(),
						dayState.getOfflineTimes(), dayState.getOfflineTime());
			}
		} catch (Exception e) {
			logger.error("saveToDB's Error:" + e);
		}
	}
}

/**
 * 排序
 *
 */
class ProjectOnOffLineSort implements Comparator<ProjectOnOffLine> {

	@Override
	public int compare(ProjectOnOffLine o1, ProjectOnOffLine o2) {
		return o1.getEventTime().compareTo(o2.getEventTime());
	}

}
