package com.jieshun.ops.report.model;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 控制单元（组织架构）
 *
 */
public class ControlUnit extends BaseDataModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7711330083540385796L;
	
	private String id;
	
	private String cuCode;
	
	private String cuName;
	
	private String parentId;
	
	private String status;
	
	private int orderNo;
	
	private String cuType;
	
	private String fullPath;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getCuCode() {
		return cuCode;
	}

	public void setCuCode(String cuCode) {
		this.cuCode = cuCode;
	}

	public String getCuName() {
		return cuName;
	}

	public void setCuName(String cuName) {
		this.cuName = cuName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getCuType() {
		return cuType;
	}

	public void setCuType(String cuType) {
		this.cuType = cuType;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	

}
