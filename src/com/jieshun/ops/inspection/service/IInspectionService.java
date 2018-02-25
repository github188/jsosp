package com.jieshun.ops.inspection.service;

import java.util.List;

import com.jieshun.ops.inspection.model.Inspection;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:24:09
 */
public interface IInspectionService {
	/**
	 * 一键巡检根据传入的任务名称来执行
	 * 任务名称 init bigkey readfile slowlog api bgy
	 * 为空则不执行
	 * 
	 * @param list
	 * @return 各个任务执行的状况
	 */
	public String doPythonScript(List<Inspection> list);

}
