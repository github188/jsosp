package com.jieshun.ops.project.model;

import java.util.Date;

public class ProjectCarPostpayDO {
	private String projectCode;
	private Date bizDate;
	private String payTypeName;
	private int outTimes;
	private float ysAmount;//应收金额
	private float ssAmount;//实收金额
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public Date getBizDate() {
		return bizDate;
	}
	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public int getOutTimes() {
		return outTimes;
	}
	public void setOutTimes(int outTimes) {
		this.outTimes = outTimes;
	}
	public float getYsAmount() {
		return ysAmount;
	}
	public void setYsAmount(float ysAmount) {
		this.ysAmount = ysAmount;
	}
	public float getSsAmount() {
		return ssAmount;
	}
	public void setSsAmount(float ssAmount) {
		this.ssAmount = ssAmount;
	}

}
