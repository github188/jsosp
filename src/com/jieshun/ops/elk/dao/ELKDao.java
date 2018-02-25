/**
 * Project Name:jsosp_yt
 * File Name:IUserDAO.java
 * Package Name:com.jieshun.jsosp.security.user.dao
 * Date:2017年3月4日上午10:14:24
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.elk.dao;

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
public interface ELKDao extends IBaseDao {

	void add(@Param("id") String id, @Param("ip") String ip, @Param("message") String message,
			@Param("issend") int issend, @Param("isRecovery") int isRecovery);

	Map<String, Object> query(@Param("ip") String ip, @Param("issend") int issend, @Param("isRecovery") int isRecovery);

	List<HashMap<String, Object>> queryCardMapped(@Param("startpages") int startpages, @Param("pageSize") int pageSize,
			@Param("platform") String platform, @Param("stat_date") String stat_date);

	HashMap<String, Object> queryCardMappedCounts(@Param("platform") String platform,
			@Param("stat_date") String stat_date);

}
