package com.jieshun.ops.util;

public class JdbcConn {

	private String platform;
	private String url;
	private String username;
	private String passwd;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public JdbcConn(String platform,String url, String username, String passwd) {
		super();
		this.platform = platform;
		this.url = url;
		this.username = username;
		this.passwd = passwd;
	}

}
