package com.jieshun.ops.project.model;

import java.util.Date;

import com.jieshun.ops.comm.BaseDataModel;

public class OfflineDayState extends BaseDataModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8891016077216536202L;
	
	private String projectId;
	private Date stateDate;
	
	private int offlineTimes=0;
	private long offlineTime=0;
	private Date createTime;
	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public Date getStateDate() {
		return stateDate;
	}
	public void setStateDate(Date stateDate) {
		this.stateDate = stateDate;
	}
	public int getOfflineTimes() {
		return offlineTimes;
	}
	public void setOfflineTimes(int offlineTimes) {
		this.offlineTimes = offlineTimes;
	}
	public long getOfflineTime() {
		return offlineTime;
	}
	public void setOfflineTime(long offlineTime) {
		this.offlineTime = offlineTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
