package com.jieshun.ops.task.logmonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.common.Shard;
import com.aliyun.openservices.log.request.GetLogStoreRequest;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.request.ListShardRequest;
import com.aliyun.openservices.log.response.GetLogStoreResponse;
import com.aliyun.openservices.log.response.GetLogsResponse;
import com.aliyun.openservices.log.response.ListShardResponse;
import com.google.gson.Gson;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.elk.dao.ELKDao;
import com.jieshun.ops.report.dao.LogMonitorDAO;
import com.jieshun.ops.util.SendMsgUtil;

public class LogMonitorTask{
	
	@Autowired
	private SMSSender smsSender;
	@Autowired
	LogMonitorDAO logMonitorDAO;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	@Autowired
	private ELKDao elkDao;
	
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getLogStore() {
		return logStore;
	}

	public void setLogStore(String logStore) {
		this.logStore = logStore;
	}
	
	public String getPhones() {
		return phones;
	}
	
	public void setPhones(String phones) {
		this.phones = phones;
	}

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String project;
	private String logStore;
	private String phones;

	private static final Logger logger = Logger.getLogger(LogMonitorTask.class);

	protected void execute() {
		TransactionStatus status = null;
		try {
			logger.info("卡映射监控任务开始执行..........");
			Client client = new Client(endpoint, accessKeyId, accessKeySecret);
			Date now = new Date() ; 
			int to = (int)(now.getTime()/1000);
			int from = (int)((now.getTime()-1000*60)/1000);
			logger.info(now+":链接阿里云日志服务成功....");
			DataSourceContextHolder.setDbType("ds_cloud");
			List<Map<String, Object>> nissp = logMonitorDAO.queryNissp();
			for (Map<String, Object> map : nissp) {
				String nisspId = map.get("NISSP_ID")==null?"":map.get("NISSP_ID").toString();
				String nisspName = map.get("NISSP_NAME")==null?"":map.get("NISSP_NAME").toString();
				logger.info("正在检测智能平台:"+nisspName);
				DataSourceContextHolder.setDbType("ds_cloud");
				List<Map<String, Object>> subSystems = logMonitorDAO.querySubsystem(nisspId);
				
				HashSet<String> localCachSet = new HashSet<>();
				for (Map<String, Object> map2 : subSystems) {
					String id = map2.get("ID")==null?"":map2.get("ID").toString();
					String code = map2.get("SUBSYSTEM_CODE")==null?"":map2.get("SUBSYSTEM_CODE").toString();
					String name = map2.get("SUBSYSTEM_NAME")==null?"":map2.get("SUBSYSTEM_NAME").toString();
					
					int offset = 0;
					int line = 100;
					int size =0 ;
					do {
						GetLogsRequest getLogsRequest= new GetLogsRequest(project, logStore, from, to, "",  id, offset, line, true);
						
						GetLogsResponse getLogsResponse = client.GetLogs(getLogsRequest);
						ArrayList<QueriedLog> logs = getLogsResponse.GetLogs();
						size = logs.size();
						for (QueriedLog queriedLog : logs) {
							LogItem logItem = queriedLog.GetLogItem();
							HashMap<String, String> log = new Gson().fromJson(logItem.ToJsonString(), HashMap.class);
							String content = log.get("content");
							if(content!=null && content.contains("请先在平台设置好子系统卡类型对应的平台卡类型")){
								logger.info("查询到未做卡映射的子系统:id:" + id + ",智能平台:" + nisspName
										+ ",子系统编号:" + code + ",子系统名称:" + name);
								localCachSet.add(code + "="	+ name);
							}
						}
						offset +=100;
					} while (size!=0);
				}
				logger.info("开始拼接,"+"平台名称为:" + nisspName + ",查询到未做卡映射的子系统编号和名称:");
				StringBuffer message = new StringBuffer("平台名称为:" + nisspName + ",查询到未做卡映射的子系统编号和名称:");
				if (localCachSet != null && localCachSet.size() != 0) {
					for (String codeAndName : localCachSet) {
						message.append(codeAndName + ";");
					}
					DataSourceContextHolder.setDbType("defaultDataSource");
					DefaultTransactionDefinition def = new DefaultTransactionDefinition();
					def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
					
					status = transactionManager.getTransaction(def);
					
					String id = UUID.randomUUID().toString().replace("-", "");
					
					if(smsSender.sendMessage(phones, message.toString())!=-1){
						elkDao.add(id, "0.0.0.0", message.toString(), 1,0);
					}else {
						elkDao.add(id, "0.0.0.0", message.toString(), 0,0);
					}
					
					transactionManager.commit(status);
					localCachSet.clear();
				}
				
			}
			//			GetLogsRequest getLogsRequest= new GetLogsRequest(project, logStore, from, to, "", "dcs and fcs");
			
			DataSourceContextHolder.clearDbType();
			logger.info("卡映射监控任务结束执行..........");
		} catch (Exception e) {
			transactionManager.rollback(status);
			logger.info("卡映射监控任务执行错误..........");
			e.printStackTrace();
		}
	}
	
}