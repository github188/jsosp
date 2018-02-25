package com.jieshun.ops.project.model;

import java.util.Date;

import com.jieshun.ops.comm.BaseDataModel;

public class ProjectOperateDay extends BaseDataModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1291686700175123729L;
	
	private String projectCode;
	private Date bizDate;
	private int mPostpayTimes=0;
	private float mPostpayAmount=0;
	private int cPostpayTimes=0;
	private float cPostpayAmount=0;
	private int discountTimes=0;
	private float discountAmount=0;
	private int postpayFeeTimes=0;
	private int prepayFeeTimes=0;
	private int mPrepayTimes=0;
	private float mPrepayAmount=0;
	private int cPrepayTimes=0;
	private float cPrepayAmount=0;
	private int totalTimes=0;
	
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
	public int getMPostpayTimes() {
		return mPostpayTimes;
	}
	public void setMPostpayTimes(int mPostpayTimes) {
		this.mPostpayTimes = mPostpayTimes;
	}
	public float getMPostpayAmount() {
		return mPostpayAmount;
	}
	public void setMPostpayAmount(float mPostpayAmount) {
		this.mPostpayAmount = mPostpayAmount;
	}
	public int getCPostpayTimes() {
		return cPostpayTimes;
	}
	public void setCPostpayTimes(int cPostpayTimes) {
		this.cPostpayTimes = cPostpayTimes;
	}
	public float getCPostpayAmount() {
		return cPostpayAmount;
	}
	public void setCPostpayAmount(float cPostpayAmount) {
		this.cPostpayAmount = cPostpayAmount;
	}
	public int getDiscountTimes() {
		return discountTimes;
	}
	public void setDiscountTimes(int discountTimes) {
		this.discountTimes = discountTimes;
	}
	public float getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(float discountAmount) {
		this.discountAmount = discountAmount;
	}
	public int getPostpayFeeTimes() {
		return postpayFeeTimes;
	}
	public void setPostpayFeeTimes(int postpayFeeTimes) {
		this.postpayFeeTimes = postpayFeeTimes;
	}
	public int getPrepayFeeTimes() {
		return prepayFeeTimes;
	}
	public void setPrepayFeeTimes(int prepayFeeTimes) {
		this.prepayFeeTimes = prepayFeeTimes;
	}
	public int getMPrepayTimes() {
		return mPrepayTimes;
	}
	public void setMPrepayTimes(int mPrepayTimes) {
		this.mPrepayTimes = mPrepayTimes;
	}
	public float getMPrepayAmount() {
		return mPrepayAmount;
	}
	public void setMPrepayAmount(float mPrepayAmount) {
		this.mPrepayAmount = mPrepayAmount;
	}
	public int getCPrepayTimes() {
		return cPrepayTimes;
	}
	public void setCPrepayTimes(int cPrepayTimes) {
		this.cPrepayTimes = cPrepayTimes;
	}
	
	public float getCPrepayAmount() {
		return cPrepayAmount;
	}
	public void setCPrepayAmount(float cPrepayAmount) {
		this.cPrepayAmount = cPrepayAmount;
	}
	public int getTotalTimes() {
		return totalTimes;
	}
	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

}
