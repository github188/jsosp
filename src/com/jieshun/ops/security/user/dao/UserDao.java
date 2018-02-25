/**
 * Project Name:jsosp_yt
 * File Name:IUserDAO.java
 * Package Name:com.jieshun.jsosp.security.user.dao
 * Date:2017年3月4日上午10:14:24
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.security.user.dao;

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
public interface UserDao extends IBaseDao {

	void addUser(@Param("id") String id, @Param("account") String account, @Param("password") String password,
			@Param("userName") String userName, @Param("unitName") String unitName,
			@Param("telephone") String telephone, @Param("remark") String remark);

	void addUserRole(@Param("id") String id, @Param("userId") String userId, @Param("roleId") String roleId);

	List<Map<String, Object>> queryUserlists(@Param("startpages") int startpages, @Param("pageSize") int pageSize,
			@Param("account") String account, @Param("userName") String userName, @Param("telephone") String telephone);

	HashMap<String, Object> queryUserCounts(@Param("account") String account, @Param("userName") String userName,
			@Param("telephone") String telephone);

	List<Map<String, Object>> queryUserByAccount(@Param("account") String account);
}
