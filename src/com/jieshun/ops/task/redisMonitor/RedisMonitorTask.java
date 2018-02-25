package com.jieshun.ops.task.redisMonitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.autode.dao.RedisMonitorDAO;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;

public class RedisMonitorTask {

	@Autowired
	private RedisMonitorDAO redisMonitorDAO;
	
	private float memory = 32*1024;//redis内存共32G 
	
	@Autowired
	private SMSSender smsSender;
	
	private String phones;
	
	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}

	private static final Logger logger = Logger.getLogger(RedisMonitorTask.class);
	
	protected void execute() {
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			logger.info("redis资源检测任务开始执行..........");
			//查询最近10分钟内最新的一条redis运行情况
			Map<String, Object> map = redisMonitorDAO.queryRedislists();

			logger.info("查询到的redis数据为:"+map);
			
			if(map==null||map.size()==0){
				smsSender.sendMessage(phones, "redis资源监控时,没有原始数据,请检查python程序是否异常");
				return;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String id = map.get("ID")==null?"":map.get("ID").toString();
			String check_time = map.get("CHECK_TIME")==null?"":simpleDateFormat.format((Date)map.get("CHECK_TIME"));
			String redis_host = map.get("REDIS_HOST")==null?"":map.get("REDIS_HOST").toString();
			float node1 = GToM(map.get("NODE1")==null?"":map.get("NODE1").toString());
			float node2 = GToM(map.get("NODE2")==null?"":map.get("NODE2").toString());
			float node3 = GToM(map.get("NODE3")==null?"":map.get("NODE3").toString());
			float node4 = GToM(map.get("NODE4")==null?"":map.get("NODE4").toString());
			float node5 = GToM(map.get("NODE5")==null?"":map.get("NODE5").toString());
			float node6 = GToM(map.get("NODE6")==null?"":map.get("NODE6").toString());
			float node7 = GToM(map.get("NODE7")==null?"":map.get("NODE7").toString());
			float node8 = GToM(map.get("NODE8")==null?"":map.get("NODE8").toString());
			float total = GToM(map.get("TOTAL")==null?"":map.get("TOTAL").toString());
			ArrayList<Float> lists = new ArrayList<>();
			lists.add(node1);
			lists.add(node2);
			lists.add(node3);
			lists.add(node4);
			lists.add(node5);
			lists.add(node6);
			lists.add(node7);
			lists.add(node8);

			for (float i : lists) {
				if(i/(memory/lists.size())>0.5){
					smsSender.sendMessage(phones, "redis("+redis_host+")检测异常:node"+(lists.indexOf(i)+1)+"节点内存使用率超过50%");
					return ;
				}
				if(i/(memory/lists.size())>0.4){
					smsSender.sendMessage(phones, "redis("+redis_host+")检测异常:node"+(lists.indexOf(i)+1)+"节点内存使用率超过40%");
					return ;
				}
				if(i/(memory/lists.size())>0.3){
					smsSender.sendMessage(phones, "redis("+redis_host+")检测异常:node"+(lists.indexOf(i)+1)+"节点内存使用率超过30%");
					return ;
				}
			}
			
			
			logger.info("redis资源检测任务结束执行..........");
		} catch (Exception e) {
			logger.info("redis资源检测任务执行错误..........");
			e.printStackTrace();
		}
	}

	private float GToM(String node){
		if(node.endsWith("M")){
			return Float.parseFloat(node.substring(0, node.length()-1));
		}else{
			return Float.parseFloat(node.substring(0, node.length()-1))*1024;
		}
	}
}