package com.jieshun.ops.task.cloudof;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.openfire.dao.OpenfireDao;
import com.jieshun.ops.comm.sms.SMSSender;

@Component
public class RosterMonitor {
	private static final Logger logger = Logger.getLogger(RosterMonitor.class);
	
	@Autowired
	private OpenfireDao dao;
	
	@Autowired
	private SMSSender smsSender;
	
	private static int roster_limited=20000;
	
	//@Scheduled(cron = "0 0 0/6 * * ? ")
	protected void execute() {
		try{
			DataSourceContextHolder.setDbType("ds_cloud-of");
			logger.debug("Current Data Source is :"+DataSourceContextHolder.getDbType());
			int count=dao.getRosterCount();
			String msg=null;
			if(count>=roster_limited){
				//dao.deleteRoseters();由于删除好友需要重启openfire,所以要人工删除并重启
				msg=builSendMsg(count,true);
			}else{
				msg=builSendMsg(count,false);
			}
			String[] phones=getReceiptors();
			
			for(String phone:phones){
				smsSender.sendMessage(phone, msg);
			}
			
		}catch(Exception e){
			logger.debug("RosterMonitor",e);
		}finally{
			DataSourceContextHolder.clearDbType();
			logger.debug("Current Data Source is :"+DataSourceContextHolder.getDbType());
		}
	}
	
	private String[] getReceiptors(){
		return "13823255622,13728616972,18664995365,13500064153".split(",");
	}
	
	private String builSendMsg(int count,boolean deleted){
		String msg=null;
		if(deleted){
			msg="云平台openfire的好友数已达："+count+"，已达限值："+roster_limited+", 请及时清除并重启openfire!";
		}else{
			msg="云平台openfire的好友数已达："+count+"，未达限值："+roster_limited+", 暂时无需处理!";
			
		}
		
		return msg;
	}
}
