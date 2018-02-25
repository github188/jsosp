/**
 * Project Name:jsosp
 * File Name:UserDO.java
 * Package Name:com.jsst.ops.user.entity
 * Date:2017年3月1日下午4:25:46
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.security.user.model;

/**
 * ClassName:UserDO <br/>
 * Function: 用户数据模型. <br/>
 * Date: 2017年3月1日 下午4:25:46 <br/>
 * 
 * @author yuteng
 * @version
 * @since JDK 1.7
 * @see
 */
public class UserDO {

	// 主键
	private String id;

	// 帐号
	private String account;

	// 密码
	private String password;

	// 真实名称
	private String userName;

	// 所在单位/部门
	private String unitName;

	// 移动电话号码
	private String telephone;

	// 备注
	private String remark;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
