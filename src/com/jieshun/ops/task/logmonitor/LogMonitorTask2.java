package com.jieshun.ops.task.logmonitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.QueriedLog;
import com.aliyun.openservices.log.request.GetLogsRequest;
import com.aliyun.openservices.log.response.GetLogsResponse;
import com.google.gson.Gson;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.elk.dao.ELKDao;
import com.jieshun.ops.util.SendMsgUtil;
import com.jieshun.ops.util.StringUtil;

public class LogMonitorTask2 {

	@Autowired
	private SMSSender smsSender;
	@Autowired
	private DataSourceTransactionManager transactionManager;
	@Autowired
	private ELKDao elkDao;
	
	private static HashMap<String, Date> localCachSet = new HashMap<>();
	private static HashMap<String, Date> deadServiceSet = new HashMap<>();

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
	public String getDeadservice() {
		return deadservice;
	}
	
	public void setDeadservice(String deadservice) {
		this.deadservice = deadservice;
	}
	public String getDeadservicePhone() {
		return deadservicePhone;
	}
	
	public void setDeadservicePhone(String deadservicePhone) {
		this.deadservicePhone = deadservicePhone;
	}

	public String getBnwyPhone() {
		return bnwyPhone;
	}
	
	public void setBnwyPhone(String bnwyPhone) {
		this.bnwyPhone = bnwyPhone;
	}
	
	public String getBnwyIp() {
		return bnwyIp;
	}
	
	public void setBnwyIp(String bnwyIp) {
		this.bnwyIp = bnwyIp;
	}
	
	public String getWhiteIp() {
		return whiteIp;
	}
	
	public void setWhiteIp(String whiteIp) {
		this.whiteIp = whiteIp;
	}
	
	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String project;
	private String logStore;
	private String phones;
	private String deadservice;
	private String deadservicePhone;
	private String bnwyPhone;
	private String bnwyIp;
	private String whiteIp;

	private static final Logger logger = Logger.getLogger(LogMonitorTask2.class);

	protected void execute() {
		try {
			String[] dead = deadservice.split(",");
			HashMap<String,String> ips = new HashMap<>(); 
			HashSet<String> deadservices =new HashSet<>();
			for (int i = 0; i < dead.length; i++) {
				deadservices.add(dead[i]);
			}
			
			logger.info("dcs监控任务开始执行..........");
			Client client = new Client(endpoint, accessKeyId, accessKeySecret);
			Date now = new Date() ; 
			int to = (int) ((now.getTime() - 1000 * 60 * 1) / 1000);
			int from = (int) ((now.getTime() - 1000 * 60 * 2) / 1000);
			logger.info(now+":链接阿里云日志服务成功....");

			int offset = 0;
			int line = 100;
			int size = 0;
			do {
				GetLogsRequest getLogsRequest = new GetLogsRequest(project, logStore, from, to, "", "dcs and fcs and fm",
						offset, line, true);

				GetLogsResponse getLogsResponse = client.GetLogs(getLogsRequest);
				ArrayList<QueriedLog> logs = getLogsResponse.GetLogs();
				size = logs.size();
				System.out.println("dcs监控任务,检测到的条数有:"+size);
				for (QueriedLog queriedLog : logs) {
					LogItem logItem = queriedLog.GetLogItem();
					HashMap<String, String> log = new Gson().fromJson(logItem.ToJsonString(), HashMap.class);
					String ip = queriedLog.GetSource();
					String content = log.get("content");
					String hostname = log.get("__tag__:__hostname__");
					ips.put(ip, hostname);
					HashMap<String, String> map = StringUtil.msg(content);
					String dcs = map.get("dcs");
					String fcs = map.get("fcs");
					String fm = map.get("fm");
					
					if(localCachSet.containsKey(ip)){
						if(Integer.parseInt(map.get("dcs")) < 100 && Integer.parseInt(map.get("fcs")) < 500){
							logger.info("已有报警ip:"+ip+",查询到的数据:" + content);
							localCachSet.remove(ip);
							String id = UUID.randomUUID().toString().replace("-", "");
							String tel = phones;
							if(ip.equals(bnwyIp)){
								tel=bnwyPhone;
							}
							if("10.10.201.2,10.10.201.75,10.10.201.76,10.10.201.3,10.10.201.53,10.10.201.32,10.10.201.33,10.10.201.70,10.10.201.77,10.10.201.56,10.10.201.57,10.10.201.125,10.10.201.126,10.10.201.123".contains(ip)){
								tel+=",13066833656";
							}
							if((whiteIp==null || !whiteIp.contains(ip)) &&Integer.parseInt(map.get("dcs")) >= 100){
								if(smsSender.sendMessage(tel, "业务已恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs)!=-1){
									elkDao.add(id, ip, "业务已恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs, 1, 1);
								}else {
									elkDao.add(id, ip, "业务已恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs, 0, 1);
								}
							}
						}
					}
					if(!localCachSet.containsKey(ip)){
						if(Integer.parseInt(map.get("dcs")) >= 100 || Integer.parseInt(map.get("fcs")) >= 500){
							logger.info("未有报警ip:"+ip+",查询到的数据:" + content);
							localCachSet.put(ip,new Date());
							String id = UUID.randomUUID().toString().replace("-", "");
							String tel = phones;
							if(ip.equals(bnwyIp)){
								tel=bnwyPhone;
							}
							//黄家欢电话,仅对租用和扫码有效
							if("10.10.201.2,10.10.201.75,10.10.201.76,10.10.201.3,10.10.201.53,10.10.201.32,10.10.201.33,10.10.201.70,10.10.201.77,10.10.201.56,10.10.201.57,10.10.201.125,10.10.201.126,10.10.201.123".contains(ip)){
								tel+=",13066833656";
							}
							if((whiteIp==null || !whiteIp.contains(ip)) &&Integer.parseInt(map.get("dcs")) >= 100){
								if(smsSender.sendMessage(tel, "业务出现高峰，很快自动恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs)!=-1){
									elkDao.add(id, ip, "业务出现高峰，很快自动恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs, 1, 0);
								}else {
									elkDao.add(id, ip, "业务出现高峰，很快自动恢复,服务器ip:" + ip + ",主机名:" + hostname + ",业务队列:" + dcs+",文件队列:" + fcs, 0, 0);
								}
							}
						}
					}
				}
				offset += 100;
			} while (size != 0);
			
			logger.info("开始检测服务假死情况，检测的ip:"+deadservices.toString());
			
			Set<String> ipsKey = ips.keySet();
			
			for (String deadservice : deadservices) {
				boolean flag = false;
				for (String ip : ipsKey) {
					if(deadservice.equals(ip)){
						flag = true;
						break;
					}
				}
				//未打印日志
				if(!flag){
					if(!deadServiceSet.containsKey(deadservice)){
						logger.info("未有报警ip,应用日志超过一分钟未打印,请检查服务器ip为:"+deadservice);
						deadServiceSet.put(deadservice, new Date());
						String id = UUID.randomUUID().toString().replace("-", "");
						if(smsSender.sendMessage(deadservicePhone, "应用日志超过一分钟未打印,请检查服务是否假死,ip为:"+deadservice)!=-1){
							elkDao.add(id, deadservice, "应用日志超过一分钟未打印,请检查服务是否假死,ip为:"+deadservice, 1, 0);
						}else {
							elkDao.add(id, deadservice, "应用日志超过一分钟未打印,请检查服务是否假死,ip为:"+deadservice, 0, 0);
						}
						
					}else if(deadServiceSet.containsKey(deadservice)){
						logger.info("已有报警ip,应用日志超过一分钟未打印,请检查服务器ip为:"+deadservice);
						
					}
					//有日志打印
				}else{
					deadServiceSet.remove(deadservice);
				}
			}
			
//			for (int i = 0; i < deadservices.length; i++) {
//				boolean flag = false;
//				for (String string : ipsKey) {
//					
//				}
//				if(!flag){
//					String id = UUID.randomUUID().toString().replace("-", "");
//					if(SendMsgUtil.sendMessage(phones, "服务假死,请检查服务器ip为:"+deadservices[i])){
//						elkDao.add(id, deadservices[i], "服务假死,请检查服务器ip为:"+deadservices[i], 1, 0);
//					}else {
//						elkDao.add(id, deadservices[i], "服务假死,请检查服务器ip为:"+deadservices[i], 0, 0);
//					}
//				}
//			}
			
			logger.info("结束检测服务假死情况，检测的ip:"+deadservices.toString());
			
			logger.info("开始检测支付网关日志");
			//支付网关public-zfwg 检测
			offset = 0;
			line = 100;
			size = 0;
			List<String> handshakeIps = new ArrayList<>(); 
			do {
				GetLogsRequest getLogsRequest = new GetLogsRequest(project, logStore, from, to, "", "remote and handshake",
						offset, line, true);

				GetLogsResponse getLogsResponse = client.GetLogs(getLogsRequest);
				ArrayList<QueriedLog> logs = getLogsResponse.GetLogs();
				size = logs.size();
				System.out.println(size);
				for (QueriedLog queriedLog : logs) {
					LogItem logItem = queriedLog.GetLogItem();
					HashMap<String, String> log = new Gson().fromJson(logItem.ToJsonString(), HashMap.class);
					String ip = queriedLog.GetSource();
					String content = log.get("content");
					String hostname = log.get("__tag__:__hostname__");
					
//					if("public-zfwg".equals(hostname)){
					if(!handshakeIps.contains(ip)){
						logger.info("支付网关发现异常日志:"+content);
						String id = UUID.randomUUID().toString().replace("-", "");
						if(smsSender.sendMessage(deadservicePhone, "ip为:"+ip+",发现异常日志:"+content+",请及时处理")!=-1){
							elkDao.add(id, deadservice,"ip为:"+ip+",发现异常日志:"+content+",请及时处理", 1, 0);
						}else {
							elkDao.add(id, deadservice, "ip为:"+ip+",发现异常日志:"+content+",请及时处理", 0, 0);
						}
						logger.info("结束检测支付网关日志");
						logger.info("dcs监控任务结束执行..........");
						handshakeIps.add(ip);
//						return ;
					}
//					}
				}
				offset += 100;
			} while (size != 0);
			logger.info("结束检测支付网关日志");
			
			logger.info("开始检测Jsaims日志");
			//支付网关public-zfwg 检测
			offset = 0;
			line = 100;
			size = 0;
			List<String> JsaimsIps = new ArrayList<>(); 
			do {
				GetLogsRequest getLogsRequest = new GetLogsRequest(project, logStore, from, to, "", "java.net.SocketTimeoutException and __tag__:__path__: /home/jht/logs/jscsp-comm.log ",
						offset, line, true);
				
				GetLogsResponse getLogsResponse = client.GetLogs(getLogsRequest);
				ArrayList<QueriedLog> logs = getLogsResponse.GetLogs();
				size = logs.size();
				System.out.println(size);
				for (QueriedLog queriedLog : logs) {
					LogItem logItem = queriedLog.GetLogItem();
					HashMap<String, String> log = new Gson().fromJson(logItem.ToJsonString(), HashMap.class);
					String ip = queriedLog.GetSource();
					String content = log.get("content");
					String hostname = log.get("__tag__:__hostname__");
					
					if(!JsaimsIps.contains(ip)){
						logger.info("Jsaims发现异常日志:"+content);
						String id = UUID.randomUUID().toString().replace("-", "");
						if(smsSender.sendMessage(phones, "ip为:"+ip+",主机名为"+hostname+",发现异常日志:"+content+",请及时处理")!=-1){
							elkDao.add(id, deadservice,"ip为:"+ip+",主机名为"+hostname+",发现异常日志:"+content+",请及时处理", 1, 0);
						}else {
							elkDao.add(id, deadservice,"ip为:"+ip+",主机名为"+hostname+",发现异常日志:"+content+",请及时处理", 0, 0);
						}
						logger.info("结束检测Jsaims日志");
						logger.info("Jsaims监控任务结束执行..........");
						JsaimsIps.add(ip);
					}
				}
				offset += 100;
			} while (size != 0);
			logger.info("结束检测Jsaims日志");
			
			logger.info("dcs监控任务结束执行..........");
			
		} catch (Exception e) {
			logger.info("dcs监控任务执行错误..........");
			e.printStackTrace();
		}
	}

}