package com.jieshun.ops.autode.model;

import javax.persistence.Entity;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 用户领域对象
 */
@Entity
public class UserRole extends BaseDataModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7205434653089066076L;
	private String userId;
	private String roleId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


}