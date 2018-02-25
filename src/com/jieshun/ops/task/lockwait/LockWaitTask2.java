package com.jieshun.ops.task.lockwait;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.report.dao.LockWaitDAO;
import com.jieshun.ops.util.SendMsgUtil;
import com.jieshun.ops.util.StringUtil;

public class LockWaitTask2{
	
	@Autowired
	private SMSSender smsSender;
	@Autowired
	private LockWaitDAO lockWaitDAO;

	private String phones;
	
	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}
	private static final Logger logger = Logger.getLogger(LockWaitTask2.class);

	protected void execute() {
		try {
			logger.info("等待锁检测任务开始执行..........");
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_cloud");
			List<Map<String,Object>> locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
//				lockWaitDAO.insertCloudLockWait(locks);
				/*for (Map<String, Object> map : locks) {
					String role = map.get("role")==null?"":map.get("role").toString();
					String id = map.get("id")==null?"":map.get("id").toString();
					String user = map.get("user")==null?"":map.get("user").toString();
					String host = map.get("host")==null?"":map.get("host").toString();
					String trx_id = map.get("trx_id")==null?"":map.get("trx_id").toString();
					String trx_state = map.get("trx_state")==null?"":map.get("trx_state").toString();
					String trx_started = map.get("trx_started")==null?"":map.get("trx_started").toString();
					String duration = map.get("duration")==null?"":map.get("duration").toString();
					String lock_mode = map.get("lock_mode")==null?"":map.get("lock_mode").toString();
					String lock_type = 	map.get("lock_type")==null?"":map.get("lock_type").toString();
					String lock_table = map.get("lock_table")==null?"":map.get("lock_table").toString();
					String lock_index = map.get("lock_index")==null?"":map.get("lock_index").toString();
					String trx_query = 	map.get("trx_query")==null?"":map.get("trx_query").toString();
					String Blockee_id = map.get("Blockee_id")==null?"":map.get("Blockee_id").toString();
					String Blockee_trx = map.get("Blockee_trx")==null?"":map.get("Blockee_trx").toString();
				}*/
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("云平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "云平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_zuyong");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertZyLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("租用平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "租用平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_smzf");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertSmLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("扫码支付平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "扫码支付平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_qst");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("全生态平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "全生态平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_huaqiaocheng");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("华侨城平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "华侨城平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_jszt");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("展厅平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "展厅平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_wanke");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("万科平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "万科平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_whga");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertLockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("武汉公安平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "武汉公安平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_biguiyuan2");
			locks = lockWaitDAO.query();
			//发现锁
			if(locks!=null &&locks.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertbgy2LockWait(locks);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			if(StringUtil.checkLocks(locks)){
				logger.info("碧桂园2平台发现锁等待(duration!=0)，请前往数据库查看");
				smsSender.sendMessage(phones, "碧桂园2平台发现锁等待(duration!=0)，请前往数据库查看");
			}
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("ds_biguiyuan2");
			List<Map<String,Object>> locks2 = lockWaitDAO.query2();
			//发现锁
			if(locks2!=null &&locks2.size()>0){
//				DataSourceContextHolder.setDbType("ds_lockwait");
//				lockWaitDAO.insertbgy2LockWait2(locks2);
				DataSourceContextHolder.clearDbType();
				DataSourceContextHolder.setDbType("defaultDataSource");
				lockWaitDAO.insertLockWait(locks);
			}
			
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			
			logger.info("等待锁检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("等待锁检测任务执行错误..........");
			e.printStackTrace();
		}
	}
	
}