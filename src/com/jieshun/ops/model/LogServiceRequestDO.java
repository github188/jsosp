package com.jieshun.ops.model;

import java.util.Date;

import com.jieshun.ops.comm.BaseDataModel;

/**
 * 
 * @author 刘淦潮
 *                                                                                                                                                                                                                                                                                                                                                                                                                                                                
 */
public class LogServiceRequestDO extends BaseDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8498596724670498039L;
	private String projectCode; 
	private String serviceId;
	private Date responseTime;
	private Date requestTime;
	private Long elapsedTime;
	private String msgCode;
	private String msgDesc;
	private String SeqId;
	
	public String getSeqId() {
		return SeqId;
	}
	public void setSeqId(String seqId) {
		SeqId = seqId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Date getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	
	public String getMsgCode() {
		return msgCode;
	}
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}
	public String getMsgDesc() {
		return msgDesc;
	}
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	public Long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
