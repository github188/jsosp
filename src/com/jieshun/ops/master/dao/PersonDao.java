package com.jieshun.ops.master.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.master.model.PersonDO;

public interface PersonDao {
	List<PersonDO> getReceiptorsByPlatform(@Param("platformCode") String platformCode);
	
	List<PersonDO> getReceiptorsByProject(@Param("projectCode") String projectCode);
}
