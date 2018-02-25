package com.jieshun.ops.task.wanke;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

public interface WankeDao extends IBaseDao {
	
	List<Map<String,String>> getNoDataUploadProjects(@Param("beginDate") String beginDate);
}
