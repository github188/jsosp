package com.jieshun.ops.task.redisMonitor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.autode.dao.RedisMonitorDAO;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;

public class RedisBigTableTask {

	@Autowired
	private RedisMonitorDAO redisMonitorDAO;
	
	@Autowired
	private SMSSender smsSender;
	
	private String phones;
	
	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	private static final Logger logger = Logger.getLogger(RedisBigTableTask.class);
	
	protected void execute() {
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			logger.info("redis大key检测任务开始执行..........");
			//查询最近10分钟内最新的一条redis运行情况
			List<Map<String, Object>> list = redisMonitorDAO.queryLastRedisBigTable();

			logger.info("查询到的redis数据为:"+list);
			
			if(list==null||list.size()==0){
//				smsSender.sendMessage(phones, "redis大key监控时,没有原始数据,请检查python程序是否异常");
				logger.info("redis大key检测任务结束执行..........");
				return;
			}
			
			StringBuffer str = new StringBuffer();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : list) {
				String id = map.get("ID")==null?"":map.get("ID").toString();
				String check_time = map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME"));
				String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
				String NODE = map.get("NODE")==null?"":map.get("NODE").toString();
				String key_name = map.get("KEY_NAME")==null?"":map.get("KEY_NAME").toString();
				String key_type = map.get("KEY_TYPE")==null?"":map.get("KEY_TYPE").toString();
				String key_value = map.get("KEY_VALUE")==null?"":map.get("KEY_VALUE").toString();
				str.append("主键为:"+key_name+",有"+key_value+"个;");
			}
			if(str==null||str.length()==0){
				logger.info("redis大key检测任务结束执行..........");
				return;
			}
			smsSender.sendMessage(phones, "redis大key监控时,发现大key,"+str.toString());
			
			logger.info("redis大key检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("redis大key检测任务执行错误..........");
			e.printStackTrace();
		}
	}

}