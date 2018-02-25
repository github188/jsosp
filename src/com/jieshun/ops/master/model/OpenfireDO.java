package com.jieshun.ops.master.model;


public class OpenfireDO extends NisspPlatform{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5847974255428310512L;

	//private static String loginUrl = "/login.jsp";
	private static String loginUrl = "/login.jsp";
	
	private static String summerUrl="/session-summary.jsp?range=10000&refresh=0";

	private static String precenceUrl="/plugins/presence/status" ;
	
	public String getLoginUrl() {
		return this.getOfUrl() + loginUrl;
	}

	public String getSummerUrl() {
		return this.getOfUrl() + summerUrl;
	}

	
	public String getPrecenceUrl(){
		return this.getOfUrl() + precenceUrl;
	}
}
