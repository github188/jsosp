package com.jieshun.ops.task.bizservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.model.LogServiceRequestDO;
import com.jieshun.ops.task.bizservice.api.QueryCarByCarno;

public class ProjectServiceTester implements Runnable {
	
	private static final Logger logger = Logger
			.getLogger(ProjectServiceTester.class);

	private JSONObject project = null;
	
	private Properties loginInfo=null;
	
	private CloudServiceMonitor monitor;
	
	public ProjectServiceTester(CloudServiceMonitor monitor,JSONObject project) {
		this.project = project;
		this.monitor=monitor;
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//读取参数
			Properties param= getParamProperties();
			logger.debug("请求参数:"+param.toString());
			String tels=param.getProperty("phones");
			logger.debug("Tels:--->"+tels);
			String licenseplate=param.getProperty("licenseplate");
			if(licenseplate==null||"".equals(licenseplate)){
				licenseplate="粤-B12345";
			}
			//构造测试参数
			QueryCarByCarno api=new QueryCarByCarno(this.project.getString("code"),licenseplate);
			loginInfo=new Properties();
			loginInfo.setProperty("cid", this.project.getString("cid"));
			loginInfo.setProperty("usr", this.project.getString("usr"));
			loginInfo.setProperty("psw", this.project.getString("psw"));
			loginInfo.setProperty("signKey", this.project.getString("signKey"));
			loginInfo.setProperty("v", "2");
			String token=getTokenFromDB(this.project.getString("cid"));
			if(token!=null) {
				api.setToken(token);
			}
			api.setLoginProperties(loginInfo);
			LogServiceRequestDO lsr=api.run();
			lsr.setProjectCode(this.project.getString("code"));
			logger.debug(lsr.toString());
			//入库
			DataSourceContextHolder.clearDbType();
			monitor.getServiceRequestDao().addLog(lsr);
			//告警
			sendMsg(lsr);
			
		}catch(IOException ioe){
			logger.debug("ProjectServiceTester",ioe);
		}catch(Exception e){
			logger.debug("ProjectServiceTester",e);
		}
	}
	
	private void sendMsg(LogServiceRequestDO lsr){
		String msg=null;
		boolean opsNote=false;
		if(lsr.getMsgCode().trim().equals("999")||lsr.getMsgCode().trim().equals("1")){
			msg="紧急处理：项目【"+lsr.getProjectCode()+"/"+project.getString("name")+"】服务没有响应，请排查前端联网产品！";
		}else if(lsr.getElapsedTime()>3000){
			msg="请关注：项目【"+lsr.getProjectCode()+"/"+project.getString("name")+"】的响应时间为"+(lsr.getElapsedTime() / 1000)
					+ "." + (lsr.getElapsedTime() % 1000) + "秒，超过3秒阀值！";
		}else if(lsr.getMsgCode().trim().equals("6")){
			msg="紧急处理：项目【"+lsr.getProjectCode()+"/"+project.getString("name")+"】服务出现异常:"+lsr.getMsgDesc()+"，请运维排查！";
			opsNote=true;
		}else if(!lsr.getMsgCode().trim().equals("0")){
			msg="紧急处理：项目【"+lsr.getProjectCode()+"/"+project.getString("name")+"】服务出现异常【编号:"+lsr.getMsgCode()+", 异常信息:"+lsr.getMsgDesc()+"】，请运维排查！";
			opsNote=true;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
		}else if(lsr.getMsgCode().trim().equals("-1")){
			//token失效，说明用户没有登录，没有使用服务，只记录，不告警
		}
		if(msg!=null){
			String[] phones=getReceiptors();
			if(opsNote){
				phones=new String[]{"13823255622"};
			}
			for(String phone:phones){
				this.monitor.getSmsSender().sendMessage(phone, msg);
			}
		}
	}
	
	private String[] getReceiptors(){
		//李胜：15019490015，刘炽明：13928957760，李杰：15013868472,朱海涛
		//13641451355,18674021615,13828857391,18926581847,13691686596,18565717752,13651492953,13500064153,18664995365,15013868541,13828750461,13823663475,18718057348,18124502312,15986662195,18118772269,15118090997,13728787027
		String[] phones={"13242057026","13823643341","13609622513","13823542740","15019490015","13928957760","15013868472","13823255622","13500064153","13641451355","13728616972","15118090997","18118772269"};
		return phones;
	}

	private Properties getParamProperties() throws IOException {
		if (this.project.getString("code") != null) {
			String path="/conf/ri/"+this.project.getString("code")+".properties";
			InputStream is = this.getClass().getResourceAsStream(path);
			if (is == null) {
				throw new IOException("配置文件"+path+"不存在！");
			}
			Properties prop=new Properties();
			prop.load(is);
			return prop;
		}
		return null;
	}
	
	private String getTokenFromDB(String businessCode) {
		DataSourceContextHolder.setDbType("ds_cloud");
		String strToken=monitor.getServiceRequestDao().getTokenFromDB(businessCode);
		Date validTime=new Date();//有效时间
		Calendar cal=Calendar.getInstance();
		cal.setTime(validTime);
		cal.add(Calendar.HOUR_OF_DAY, -2);
		validTime=cal.getTime();
		if(strToken==null) {
			logger.debug("The token is null");
		}else {
			logger.debug("strToken:---"+strToken);
			String[] tokens=strToken.split(";");
			String lastToken=null;
			Date lastTokenTime=null;
			for(int i=0;i<tokens.length;i++) {
				JSONObject token=JSONObject.parseObject(tokens[i]);
				if(lastToken==null) {
					lastToken=token.getString("token");
					lastTokenTime=token.getDate("tokenTime");
				}else {
					if(token.getDate("tokenTime").after(lastTokenTime)) {
						lastToken=token.getString("token");
						lastTokenTime=token.getDate("tokenTime");
					}
				}
			}
			//如果最后的时间在有效时间之前，返回空
			if(lastTokenTime.before(validTime)) {
				return null;
			}
			return lastToken;
		}
		DataSourceContextHolder.clearDbType();
		return null;
	}
	
	
	
	public static void main(String[] args) {
		ProjectServiceTester tester=new ProjectServiceTester(null,null);
		
		String token=tester.getTokenFromDB("test");
		System.out.println("token:-->"+token);
	}
}
