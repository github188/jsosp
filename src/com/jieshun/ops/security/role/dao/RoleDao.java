/**
 * Project Name:jsosp_yt
 * File Name:IUserDAO.java
 * Package Name:com.jieshun.jsosp.security.user.dao
 * Date:2017年3月4日上午10:14:24
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.security.role.dao;

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
public interface RoleDao extends IBaseDao {

	void addRole(@Param("id") String id, @Param("roleName") String roleName, @Param("remark") String remark);

	List<Map<String, Object>> queryRolelists(@Param("startpages") int startpages, @Param("pageSize") int pageSize,
			@Param("roleName") String roleName);
	
	HashMap<String, Object> queryRolesCounts(@Param("roleName") String roleName);
	
	HashMap<String, Object> queryRoleByRoleName(@Param("roleName") String roleName);
	
	void addRoleUser(@Param("id") String id, @Param("roleId") String roleId, @Param("userId") String userId);

}
