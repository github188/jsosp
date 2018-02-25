/**
 * Project Name:jsosp_yt
 * File Name:IUserDAO.java
 * Package Name:com.jieshun.jsosp.security.user.dao
 * Date:2017年3月4日上午10:14:24
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.system.packages.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * ClassName:IUserDAO <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年3月4日 上午10:14:24 <br/>
 * 
 * @author JHT
 * @version
 * @since JDK 1.7
 * @see
 */
public interface PackagesDao extends IBaseDao {
	void addPackage(@Param("id") String id, @Param("componentId") String componentId,
			@Param("versionDate") String versionDate, @Param("principal") String principal,
			@Param("releaseNote") String releaseNote, @Param("dbScript") String dbScript,
			@Param("paraFile") String paraFile, @Param("componentFile") String componentFile,
			@Param("remark") String remark,@Param("keep_path") String keep_path);

	List<Map<String, Object>> queryPackageList(@Param("startpages") int startpages, @Param("pageSize") int pageSize,
			@Param("componentId") String componentId, @Param("versionDate") String versionDate,
			@Param("principal") String principal, @Param("hasDbScript") String hasDbScript,
			@Param("hasParaFile") String hasParaFile);

	HashMap<String, Object> queryPackagesCounts(@Param("componentId") String componentId,
			@Param("versionDate") String versionDate, @Param("principal") String principal,
			@Param("hasDbScript") String hasDbScript, @Param("hasParaFile") String hasParaFile);

}
