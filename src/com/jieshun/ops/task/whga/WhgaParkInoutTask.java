package com.jieshun.ops.task.whga;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.util.SendMsgUtil;

public class WhgaParkInoutTask extends TimerTask {
	
	@Autowired
	private SMSSender smsSender;
	public WhgaParkInoutTask(String driver, String url, String user, String password, String parkcodes, String phones) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.parkcodes = parkcodes;
		this.phones = phones;
	}

	private static final Logger logger = Logger.getLogger(WhgaParkInoutTask.class);

	private String driver;
	private String url;
	private String user;
	private String password;
	private String parkcodes;
	private String phones;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getParkcodes() {
		return parkcodes;
	}

	public void setParkcodes(String parkcodes) {
		this.parkcodes = parkcodes;
	}

	public String getPhones() {
		return phones;
	}

	public void setPhones(String phones) {
		this.phones = phones;
	}


	
	@Override
	public void run() {
		try {
			logger.info("开始执行武汉公安监控任务");
			// 加载驱动程序
			Class.forName(driver);

			// 连续数据库
			Connection conn = DriverManager.getConnection(url, user, password);

			if (!conn.isClosed()) {
				logger.info("Succeeded connecting to the Database!");
			}

			// statement用来执行SQL语句
			Statement statement = conn.createStatement();

			// 结果集
//			ResultSet rs = statement.executeQuery("select id,name from np_eq_park where status='NORMAL' and code in ('"+parkcodes+"')" );
			ResultSet rs = statement.executeQuery("select p.id as id,p.name as name from np_eq_park p "
					+ "left join np_cf_subsystem s on p.SUBSYSTEM_ID=s.ID "
					+ "where p.status='NORMAL' and s.STATUS='NORMAL' and s.CODE in ('"+parkcodes+"')" );

			StringBuffer msg = new StringBuffer();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = new Date(( System.currentTimeMillis() - (4 * 60 * 60 * 1000))-(60*1000));
			Date end = new Date(( System.currentTimeMillis())-(60*1000));
			String startdate = simpleDateFormat.format(start);
			String enddate = simpleDateFormat.format(end);
			msg.append("武汉公安检测,时间段:" + startdate + "---" + enddate);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				System.out.println(id + name);

				Statement statement1 = conn.createStatement();
				int parkin0 = 0;
				int parkin1 = 0;
				ResultSet in0 = statement1
						.executeQuery("select count(1) from np_dt_park_in where IS_SYNC='0' and park_id = '" + id
								+ "' and create_time>'" + startdate + "'and create_time<'" + enddate + "'");
				while (in0.next()) {
					parkin0 = in0.getInt(1);
				}
				ResultSet in1 = statement1
						.executeQuery("select count(1) from np_dt_park_in where IS_SYNC='1' and park_id = '" + id
								+ "' and create_time>'" + startdate + "'and create_time<'" + enddate + "'");
				while (in1.next()) {
					parkin1 = in1.getInt(1);
				}
				System.out.println(parkin0 + parkin1);

				Statement statement2 = conn.createStatement();
				int parkout0 = 0;
				int parkout1 = 0;
				ResultSet out0 = statement2
						.executeQuery("select count(1) from np_dt_park_out where IS_SYNC='0' and park_id = '" + id
								+ "' and create_time>'" + startdate + "'and create_time<'" + enddate + "'");
				while (out0.next()) {
					parkout0 = out0.getInt(1);
				}
				ResultSet out1 = statement2
						.executeQuery("select count(1) from np_dt_park_out where IS_SYNC='1' and park_id = '" + id
								+ "' and create_time>'" + startdate + "'and create_time<'" + enddate + "'");
				while (out1.next()) {
					parkout1 = out1.getInt(1);
				}
				System.out.println(parkout0 + parkout1);
				if(parkout0==0 && parkin0==0 && parkout1!=0 && parkin1!=0){
//					sendMsg(phones, msg.toString()+";停车场名:" + name + ",数据推送正常;入场应推送:" + (parkin1+parkin0)
//							+ ";入场实际推送:" + parkin1 + ";出场应推送:" + (parkout0+parkout1) + ";出场实际推送:" + parkout1 + ";");
					new SMSSender().sendMessage(phones, msg.toString()+";停车场名:" + name + ",数据推送正常;入场应推送:" + (parkin1+parkin0)
							+ ";入场实际推送:" + parkin1 + ";出场应推送:" + (parkout0+parkout1) + ";出场实际推送:" + parkout1 + ";");
				}else {
//					sendMsg(phones, msg.toString()+";停车场名:" + name + ",数据推送异常;入场应推送:" + (parkin1+parkin0)
//							+ ";入场实际推送:" + parkin1 + ";出场应推送:" + (parkout0+parkout1) + ";出场实际推送:" + parkout1 + ";");
					new SMSSender().sendMessage(phones, msg.toString()+";停车场名:" + name + ",数据推送异常;入场应推送:" + (parkin1+parkin0)
							+ ";入场实际推送:" + parkin1 + ";出场应推送:" + (parkout0+parkout1) + ";出场实际推送:" + parkout1 + ";");
				}
			}

			rs.close();
			conn.close();
			logger.info("结束执行武汉公安监控任务");
		} catch (Exception e) {
			logger.error("Sorry,can`t connect the mysqlserver!", e);

		}

	}

	/*public static void sendMsg(String phone, String content) {
		logger.info("开始发送短信" + phone + "\t" + content);
		new SendMessageImpl().sendMessage(phone, content);
		logger.info("开始发送短信--完毕" + phone + "\t" + content);

	}*/
	
}