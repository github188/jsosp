package com.jieshun.ops.autode.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:16:07
 */
public interface LicenseManagementDAO extends IBaseDao {

	List<Map<String, Object>> queryLicenseManagementlists(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize);
	
	List<Map<String, Object>> querylicenses(@Param("startpages") int startpages,
			@Param("pageSize") int pageSize,@Param("ip")String ip,@Param("projectname")String projectname);

	Map<String, Object> queryById(@Param("id") String id);

	Map<String, Object> queryLicenseManagementCounts();

	List<Map<String, Object>> queryDataDeleteNotStarted();

	void addLicense(@Param("id") String id, @Param("project_name") String project_name, @Param("enabled") int enabled,
			@Param("start_time") String start_time, @Param("end_time") String end_time,
			@Param("project_type") String project_type, @Param("telephone") String telephone,
			@Param("duetime") int duetime, @Param("ip") String ip);

	public void updateById(@Param("id") String id, @Param("project") String project,
			@Param("licensestartTime") String licensestartTime, @Param("licenseendTime") String licenseendTime,
			@Param("project_type") String project_type, @Param("telephone") String telephone,
			@Param("duetime") int duetime, @Param("ip") String ip);

	Map<String, Object> queryLicenseManagementCountsByIpAndName(@Param("ip")String ip, @Param("projectname")String projectname);
}