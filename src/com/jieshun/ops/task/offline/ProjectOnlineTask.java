package com.jieshun.ops.task.offline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.master.model.OpenfireDO;
import com.jieshun.ops.project.model.Project;
import com.jieshun.ops.project.model.ProjectOnOffLine;
import com.jieshun.ops.util.BeanUtil;

/**
 * 项目离线监控处理工作线程
 * 
 * @author Administrator
 *
 */
public class ProjectOnlineTask implements Runnable {
	private static final Logger logger = Logger
			.getLogger(ProjectOnlineTask.class.getName());
	private ProjectOnlineMonitor monitor;
	private OpenfireDO openfireDo;
	
	public ProjectOnlineTask(ProjectOnlineMonitor monitor,
			OpenfireDO of) {
		this.monitor = monitor;
		this.openfireDo = of;
	}

	/**
	 * 获取当前平台需要监控的子系统列表
	 * 
	 * @return
	 */
	private Map<String, String> getPlatformProjects(String platformId)
			throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		List<Project> subList = this.monitor.getProjectDao()
				.getPlatformProjects(platformId);
		if (subList != null && subList.size() > 0) {
			for (Project sub : subList) {
				map.put(sub.getCode().trim(), sub.getIsOnline() == 1 ? "ONLINE"
						: "OFFLINE");
			}
		}
		return map;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			IOpenfireService openfire = null;
			if(this.openfireDo.getOfHostName()==null||"".equals(this.openfireDo.getOfHostName())){
				openfire=new OpenfireConnector(this.openfireDo);
			}else{
				openfire = new OpenfireHandler(this.openfireDo);
			}
			
			// 取平台所有项目列表
			Map<String, String> allProjects = getPlatformProjects(this.openfireDo
					.getId());
			//保存数据
			batchSaveLog(openfire,allProjects);
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//释放
			this.monitor.decreaseTask();
		}
	}


	private void batchSaveLog(IOpenfireService openfire,Map<String, String> allProjects) {
		List<ProjectOnOffLine> events=new ArrayList<ProjectOnOffLine>();
		List<Project> projects=new ArrayList<Project>();
		for (String projectCode : allProjects.keySet()) {
			ProjectOnOffLine event=new ProjectOnOffLine();
			Project project=new Project();
			event.setId(BeanUtil.createUUID());
			event.setProjectCode(projectCode);
			project.setCode(projectCode);
			// 从openfire服务器抓取在线数据
			if(openfire.isOnline(projectCode)){
				if(allProjects.get(projectCode).equals("OFFLINE")){
					event.setEventName("ONLINE");
					project.setIsOnline(1);
					events.add(event);
					projects.add(project);
				}
				
			}else{
				if (allProjects.get(projectCode).equals("ONLINE")) {
					event.setEventName("OFFLINE");
					project.setIsOnline(0);
					events.add(event);
					projects.add(project);
				}
				
			}
			
		}
		/*DataSourceContextHolder.setDbType("defaultDataSource");
		//开启事务
		DataSourceTransactionManager tm=new DataSourceTransactionManager(this.monitor.getDataSource());
		TransactionStatus status = null;
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		status = tm.getTransaction(def);*/
		logger.debug("this.monitor.getProjectDao().batchAddOnlineEvent(events)'s size is "+events.size());
		if(events.size()>0) {
			this.monitor.getProjectDao().batchAddOnlineEvent(events);
		}
		logger.debug("this.monitor.getProjectDao().batchUpdateProjectOnlineStatus(projects)'s size is "+projects.size());
		if(projects.size()>0) {
			this.monitor.getProjectDao().batchUpdateProjectOnlineStatus(projects);
		}
		//tm.commit(status);
		DataSourceContextHolder.clearDbType();
	}
}