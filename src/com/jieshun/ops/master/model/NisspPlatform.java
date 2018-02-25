package com.jieshun.ops.master.model;

import java.sql.Date;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 平台对象类
 * 
 * @author 刘淦潮
 *
 */
public class NisspPlatform extends BaseDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -820953269748860076L;
	private String platformCode;
	private String platformName;
	private String ofUrl;
	private String ofHostName;
	private String ofAccount;
	private String ofPasswd;
	private String status;

	private String createUser;

	private Date createTime;

	private String updateUser;

	private Date updateTime;

	private String remark;

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getOfUrl() {
		return ofUrl;
	}

	public void setOfUrl(String ofUrl) {
		this.ofUrl = ofUrl;
	}

	public String getOfAccount() {
		return ofAccount;
	}

	public void setOfAccount(String ofAccount) {
		this.ofAccount = ofAccount;
	}

	public String getOfPasswd() {
		return ofPasswd;
	}

	public void setOfPasswd(String ofPasswd) {
		this.ofPasswd = ofPasswd;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOfHostName() {
		return ofHostName;
	}

	public void setOfHostName(String ofHostName) {
		this.ofHostName = ofHostName;
	}
}
