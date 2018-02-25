/**
 * Project Name:jsosp
 * File Name:IUserService.java
 * Package Name:com.jsst.ops.security.user.service
 * Date:2017年3月8日上午11:34:39
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.security.role.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ClassName:IUserService <br/>
 * Function: user业务逻辑层接口. <br/>
 * Reason: user视图与数据层交互处理. <br/>
 * Date: 2017年3月8日 上午11:34:39 <br/>
 * 
 * @author JHT
 * @version
 * @since JDK 1.7
 * @see
 */
public interface IRoleService {

	public String addRole(String id, String roleName, String remark,ArrayList<HashMap<String, Object>> userArray);

	public String queryRolelists(int pageIndex, int pageSize, String roleName);

}
