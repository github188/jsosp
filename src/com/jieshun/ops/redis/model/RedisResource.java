package com.jieshun.ops.redis.model;

import java.sql.Date;

import javax.persistence.Entity;

import com.jieshun.ops.comm.BaseDataModel;

@Entity
public class RedisResource extends BaseDataModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	
	private Date checkTime;
	
	private String redisHost;
	
	private String node1;
	
	private String node2;
	
	private String node3;
	
	private String node4;
	
	private String node5;
	
	private String node6;
	
	private String node7;
	
	private String node8;
	
	private String total;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public String getNode1() {
		return node1;
	}

	public void setNode1(String node1) {
		this.node1 = node1;
	}

	public String getNode2() {
		return node2;
	}

	public void setNode2(String node2) {
		this.node2 = node2;
	}

	public String getNode3() {
		return node3;
	}

	public void setNode3(String node3) {
		this.node3 = node3;
	}

	public String getNode4() {
		return node4;
	}

	public void setNode4(String node4) {
		this.node4 = node4;
	}

	public String getNode5() {
		return node5;
	}

	public void setNode5(String node5) {
		this.node5 = node5;
	}

	public String getNode6() {
		return node6;
	}

	public void setNode6(String node6) {
		this.node6 = node6;
	}

	public String getNode7() {
		return node7;
	}

	public void setNode7(String node7) {
		this.node7 = node7;
	}

	public String getNode8() {
		return node8;
	}

	public void setNode8(String node8) {
		this.node8 = node8;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
