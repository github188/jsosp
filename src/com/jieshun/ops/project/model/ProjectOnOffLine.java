package com.jieshun.ops.project.model;

import java.util.Date;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 项目在线离线日志
 * @author 刘淦潮
 *
 */
public class ProjectOnOffLine extends BaseDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2243624196786772567L;
	private String projectCode;
	private String eventName;
	private Date eventTime;
	private String handleId;
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public String getHandleId() {
		return handleId;
	}
	public void setHandleId(String handleId) {
		this.handleId = handleId;
	}
	
}
