package com.jieshun.ops.util;
public enum DBInstanceTypeEnum {
	PRIMARY("主实例"), READONLY("只读实例"), GUARD("灾备实例"), TEMP("临时实例");

	private String context;

	public String getContext() {
		return this.context;
	}

	private DBInstanceTypeEnum(String context) {
		this.context = context;
	}

}
