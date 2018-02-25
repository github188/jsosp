package com.jieshun.ops.task.subsystemonline;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.autode.dao.SubsystemOnlineDao;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.util.OfUtil;

public class SubsystemOnlineTask{
	
	@Autowired
	private SMSSender smsSender;
	@Autowired
	private SubsystemOnlineDao subsystemOnlineDao;
	
	private static HashMap<String, Date> localCachSet = new HashMap<>();//保存离线的项目编号和离线时间

	private static final Logger logger = Logger.getLogger(SubsystemOnlineTask.class);

	protected void execute() {
		try {
			logger.info("前端在线情况任务开始执行..........");
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			List<Map<String,Object>> projectLists = subsystemOnlineDao.queryProjectList();
			
			if(projectLists ==null || projectLists.size()==0){
				logger.info("不存在需要检测的子系统");
				return ;
			}
			List<HashMap<String, String>> list = new ArrayList<>();
			List<HashMap<String, Object>> loglist = new ArrayList<>();
			HashMap<String, Object> logmap = new HashMap<>();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Map<String, Object> map : projectLists) {
				String id = map.get("ID")==null?"":map.get("ID").toString();
				String code = map.get("CODE")==null?"":map.get("CODE").toString();
				String name = map.get("NAME")==null?"":map.get("NAME").toString();
				String telphone = map.get("TELPHONE")==null?"":map.get("TELPHONE").toString();
				String platform_name = map.get("PLATFORM_NAME")==null?"":map.get("PLATFORM_NAME").toString();
				String of_url = map.get("OF_URL")==null?"":map.get("OF_URL").toString();
				String of_host_name = map.get("OF_HOST_NAME")==null?"":map.get("OF_HOST_NAME").toString();
				if(of_url.endsWith("/")){
					of_url=of_url.substring(0, of_url.length()-1);
				}
				int online = OfUtil.isonline(of_url,code,of_host_name);
				// 0 - 用户不存在; 1 - 用户在线; 2 -用户离线
				if(online==0){
					logger.info("前端子系统编号不存在:"+platform_name+","+name+","+code);
				}else if (online==2){
					logger.info("前端子系统编号不在线:"+platform_name+","+name+","+code);
					if(localCachSet.get(code)!=null){
						logger.info("已有报警,子系统编号为:"+platform_name+","+name+","+code+"离线时间为:"+simpleDateFormat.format(localCachSet.get(code)));
					}
					if(localCachSet.get(code)==null){
						logger.info("子系统不在线,子系统编号为:"+platform_name+","+name+","+code+"离线时间为:"+simpleDateFormat.format(new Date()));
						localCachSet.put(code, new Date());
						logmap.put("id", id);
						logmap.put("online", "OFFLINE");
						logmap.put("time", new Date());
						loglist.add(logmap);
						if(!telphone.isEmpty()){
							smsSender.sendMessage(telphone, "子系统不在线,子系统编号为:"+platform_name+","+name+","+code+"离线时间为:"+simpleDateFormat.format(new Date()));
						}
					}
				}else if (online==1){
					if(localCachSet.get(code)!=null){
						logger.info("前端子系统编号恢复在线:"+platform_name+","+name+","+code);
						localCachSet.remove(code);
						logmap.put("id", id);
						logmap.put("online", "ONLINE");
						logmap.put("time", new Date());
						loglist.add(logmap);
						if(!telphone.isEmpty()){
							smsSender.sendMessage(telphone, "子系统恢复在线,子系统编号为:"+platform_name+","+name+","+code+"恢复时间为:"+simpleDateFormat.format(new Date()));
						}
					}
				}
				HashMap<String, String> map1 = new HashMap<>();
				map1.put("id", id);
				map1.put("online", (online==1)?"1":"0");
				list.add(map1);
			}
			
			subsystemOnlineDao.updateProjects(list);
			if(loglist!=null &&loglist.size()!=0){
				subsystemOnlineDao.insertlogProjects(loglist);
			}
			
			logger.info("前端在线情况任务结束执行..........");
		} catch (Exception e) {
			logger.info("前端在线情况任务执行错误..........");
			e.printStackTrace();
		}
	}
	
}