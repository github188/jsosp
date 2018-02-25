package com.jieshun.ops.task.transfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jieshun.ops.comm.sms.SMSSender;

public class TransFileTask extends TimerTask {

	@Autowired
	private SMSSender smsSender;
	private static final Logger logger = Logger.getLogger(TransFileTask.class);

	public TransFileTask(String driver, String url, String user, String password, String sql, String phones) {
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

			/*while (rs.next()) {
				count = rs.getInt(1);
				logger.info("清分文件上传总数为:   " + count);
			}

			if (count < 48) {
				new SMSSender().sendMessage(phones,"清分文件异常，应上传:\t48，实际上传为:\t" + count);
			}else {
				new SMSSender().sendMessage(phones,"清分文件正常，应上传:\t48，实际上传为:\t" + count);
			}*/
			
			int all =0;
			int success =0;
			
			while (rs.next()) {
				String status = rs.getString("STATUS")==null?"":rs.getString("STATUS");
				int counts = rs.getInt("COUNTS");
				all+=counts;
				if("SUCCESS".equals(status)){
					success = counts;
				}
			}

			if(all!=0&&all==success){
				new SMSSender().sendMessage(phones,"清分文件正常，应上传:\t"+all+"，成功上传为:\t" + success);
			}else{
				new SMSSender().sendMessage(phones,"清分文件异常，应上传:\t"+all+"，成功上传为:\t" + success);
			}
			
			rs.close();
			conn.close();

		} catch (Exception e) {
			logger.error("Sorry,can`t connect the mysqlserver!", e);

		}

	}

	/*public static void sendMessage(String phone,String message) {
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://192.168.10.18:8080/jsstsms/SmsServlet");
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

			JsonArray request = new JsonArray();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("phone", phone);
			jsonObject.addProperty("content", message);
			request.add(jsonObject);

			NameValuePair[] data = { new NameValuePair("p", request.toString()) };
			post.setRequestBody(data);
			client.executeMethod(post);

			int statusCode = post.getStatusCode();
			logger.info("statusCode:" + statusCode);
			String result = new String(post.getResponseBodyAsString());
			logger.info(result);
			post.releaseConnection();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void sendMsg(String phone, String content) {
		logger.info("开始发送短信"+phone+"\t"+content);
		new SendMessageImpl().sendMessage(phone, content);
		logger.info("开始发送短信--完毕"+phone+"\t"+content);

	}*/

}