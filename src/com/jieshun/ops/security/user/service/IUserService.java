/**
 * Project Name:jsosp
 * File Name:IUserService.java
 * Package Name:com.jsst.ops.security.user.service
 * Date:2017年3月8日上午11:34:39
 * Copyright (c) 2017, 捷顺科技 All Rights Reserved. 
 * 
*/

package com.jieshun.ops.security.user.service;

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
public interface IUserService {

	public String addUser(String userId, String account, String password, String userName, String unitName, String telephone,
			String remark,ArrayList<HashMap<String, Object>> roleArray);

	public String queryUserlists(int pageIndex, int pageSize, String account, String userName, String telephone);

	public String login(String account, String passwd);

}
