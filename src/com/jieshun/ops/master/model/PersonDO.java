package com.jieshun.ops.master.model;

import com.jieshun.ops.comm.BaseDataModel;

public class PersonDO extends BaseDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 837566379407104990L;
	private String code;
	private String name;
	private String gender;
	private String department;
	private String position;
	private String phone;
	private String email;
	private String isEmployee;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIsEmployee() {
		return isEmployee;
	}
	public void setIsEmployee(String isEmployee) {
		this.isEmployee = isEmployee;
	}
}
