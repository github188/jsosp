package com.jieshun.ops.task.ytk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.util.SendMsgUtil;

public class YTKTask extends TimerTask {

	@Autowired
	private SMSSender smsSender;
	private static final Logger logger = Logger.getLogger(YTKTask.class);

	public YTKTask(String driver, String url, String user, String password, String sql, String phones) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.sql = sql;
		this.phones = phones;
	}

	private String driver;
	private String url;
	private String user;
	private String password;
	private String sql;
	private String phones;
	
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
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
			ResultSet rs = statement.executeQuery(sql);

			int count = 0;

			while (rs.next()) {
				count = rs.getInt(1);
				logger.info("清分文件上传总数为:   " + count);
			}

			if (count != 3) {
				new SMSSender().sendMessage(phones,"YTK文件上传异常，应上传:\t3，实际上传为:\t" + count);
			}else {
				new SMSSender().sendMessage(phones,"YTK文件上传正常，应上传:\t3，实际上传为:\t" + count);
			}

			rs.close();
			conn.close();

		} catch (Exception e) {
			logger.error("Sorry,can`t connect the mysqlserver!", e);

		}

	}

}