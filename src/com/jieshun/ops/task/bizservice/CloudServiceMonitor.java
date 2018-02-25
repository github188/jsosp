package com.jieshun.ops.task.bizservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.dao.ServiceRequestDao;

public class CloudServiceMonitor {
	private static final Logger logger = Logger
			.getLogger(CloudServiceMonitor.class);
	
	@Autowired
	private ServiceRequestDao serviceRequestDao;
	
	@Autowired
	private SMSSender smsSender;
	
	private static ScheduledExecutorService threadPool = new ScheduledThreadPoolExecutor(4,
	        new BasicThreadFactory.Builder().namingPattern("CloudServiceMonitor-schedule-pool-%d").daemon(true).build());   
	
//	private static ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	public void execute() {
		//读取要测试的项目
		try {
			List<JSONObject> projects = getMonitorProjects();
			
			//讯取每个项目要测试的API
			for(JSONObject project:projects){
				threadPool.execute(new ProjectServiceTester(this,project));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.debug("CloudServiceMonitor.execute",e);
		}
		
		
	}
	
	/**
	 * 从watch-projects.json文件中逐行读取
	 * @return
	 * @throws IOException 
	 */
	private List<JSONObject> getMonitorProjects() throws IOException{
		//读取conf/ri/watch-projects.json文件
        InputStream is = this.getClass().getResourceAsStream("/conf/ri/watch-projects.json");
        if(is==null){
        	throw new IOException("配置文件/conf/ri/watch-projects.json不存在！");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String lineTxt = null;
        List<JSONObject> projects=new ArrayList<JSONObject>();
        while ((lineTxt = br.readLine()) != null) {
        	if(lineTxt.matches("^#.*")){
        		continue;
        	}
        	JSONObject projectJson=jsonToProperties(lineTxt);
        	projects.add(projectJson);
        }
        br.close();

        return projects;
	}
	
	private JSONObject jsonToProperties(String line){
		JSONObject jsonObj = JSONObject.parseObject(line); //将字符串{“id”：1}
		
		return jsonObj;
	}

	public ServiceRequestDao getServiceRequestDao() {
		return serviceRequestDao;
	}

	public void setServiceRequestDao(ServiceRequestDao serviceRequestDao) {
		this.serviceRequestDao = serviceRequestDao;
	}

	public SMSSender getSmsSender() {
		return smsSender;
	}

	public void setSmsSender(SMSSender smsSender) {
		this.smsSender = smsSender;
	}

}
