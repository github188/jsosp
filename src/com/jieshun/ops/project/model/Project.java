package com.jieshun.ops.project.model;

import java.util.Date;

import com.jieshun.ops.comm.BaseDataModel;

public class Project extends BaseDataModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8131009815962536180L;
	
	private String code;
	private String name;
	private String platformId;
	private String projectType;
	private int watch;
	private String watchTimeFrame;
	private String status;
	private String remark;
	private int isOnline;
	private String createUser;
	private String updateUser;
	private Date createTime;
	private Date updateTime;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public int getWatch() {
		return watch;
	}
	public void setWatch(int watch) {
		this.watch = watch;
	}
	public String getWatchTimeFrame() {
		return watchTimeFrame;
	}
	public void setWatchTimeFrame(String watchTimeFrame) {
		this.watchTimeFrame = watchTimeFrame;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public int getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
