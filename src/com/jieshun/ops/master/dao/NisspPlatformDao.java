package com.jieshun.ops.master.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;
import com.jieshun.ops.master.model.NisspPlatform;
import com.jieshun.ops.master.model.OpenfireDO;

/**
 * Nissp平台DAO
 * 
 * @author 刘淦潮
 *
 */
public interface NisspPlatformDao extends IBaseDao {
	/**
	 * 取平台列表记录
	 * @return
	 */
	List<NisspPlatform> getAlltNisspPlatform();

	/**
	 * 根据平台编号取平台记录
	 * @param projectCode
	 * @return
	 */
	NisspPlatform getNisspPlatformByCode(
			@Param("platformCode") String platformCode);
	
	/**
	 * 取带
	 * @return
	 */
	List<OpenfireDO> getAllPlatformIncludeOFUrl();
}
