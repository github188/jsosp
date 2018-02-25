package com.jieshun.ops.autode.service;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:18:02
 */
public interface ILockWaitService {
	
	public String lockwait(String platform);

	public String slowsql(String platform);

	public String processlist(String platform);
}
