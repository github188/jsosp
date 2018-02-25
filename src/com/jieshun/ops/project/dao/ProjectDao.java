package com.jieshun.ops.project.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;
import com.jieshun.ops.master.model.OpenfireDO;
import com.jieshun.ops.project.model.OfflineDayState;
import com.jieshun.ops.project.model.Project;
import com.jieshun.ops.project.model.ProjectOnOffLine;

public interface ProjectDao extends IBaseDao {
	/**
	 * 保存日度统计记录
	 * 
	 * @param id
	 * @param projectId
	 * @param stateDate
	 * @param offlineTimes
	 * @param offlineTime
	 */
	void addOffLineDayState(@Param("id") String id,
			@Param("projectId") String projectId,
			@Param("stateDate") String stateDate,
			@Param("offlineTimes") Integer offlineTimes,
			@Param("offlineTime") Long offlineTime);

	/**
	 * 更新
	 * 
	 * @param id
	 * @param offlineTimes
	 * @param offlineTime
	 */
	void updateOffLineDayState(@Param("id") String id,
			@Param("offlineTimes") Integer offlineTimes,
			@Param("offlineTime") Long offlineTime);

	List<Project> selectWatchProject();

	/**
	 * 根据项目编号取项目档案
	 * 
	 * @param projectCode
	 * @return
	 */
	Project lookupByProjectCode(@Param("projectCode") String projectCode);

	/**
	 * 取某项目某一天的离线日志记录-
	 * 
	 * @param projectCode
	 * @param stateDate
	 * @return
	 */
	List<ProjectOnOffLine> selectOnOffLineEvents(
			@Param("projectCode") String projectCode,
			@Param("stateDate") String stateDate);

	/**
	 * 找某项目某一天的日度统计记录
	 * 
	 * @param projectId
	 * @param stateDate
	 * @return
	 */
	List<OfflineDayState> selectOfflineDayState(
			@Param("projectId") String projectId,
			@Param("stateDate") String stateDate);

	/**
	 * 取项目最近一次离线日志
	 * 
	 * @param projectCode
	 * @return
	 */
	ProjectOnOffLine getProjectLastedOnOffLineEvent(
			@Param("projectCode") String projectCode);

	/**
	 * 获取平台上的所有项目
	 * 
	 * @param platformId
	 *            平台ID
	 * @return
	 */
	List<Project> getPlatformProjects(@Param("platformId") String platformId);

	/**
	 * 更新项目状态
	 * @param projectCode
	 * @param onlineStatus
	 */
	void updateProjectOnlineStatus(@Param("projectCode") String projectCode,
			@Param("onlineStatus") int onlineStatus);
	
	void addOnlineEvent(@Param("id") String id,@Param("projectCode") String projectCode,
			@Param("eventName") String eventName);
	
	void updateproject(@Param("id") String id,@Param("watch") String watch);

	void add(@Param("id")String id,@Param("platform")String platform, @Param("name")String name,@Param("code") String code);
	
	Map<String, Object> quertByCode(@Param("code") String code);

	Map<String, Object> queryProjectCountsByCondition(@Param("platform")String platform, @Param("name")String name, @Param("code")String code,@Param("watch")String watch);

	List<Map<String, Object>> queryProjectlistsByCondition(@Param("platform")String platform,@Param("name") String name,
			@Param("code")String code,@Param("watch")String watch,@Param("startpages") int startpages,	@Param("pageSize") int pageSize);
	
	/**
	 * 批量插入事件
	 * @param events
	 */
	void batchAddOnlineEvent(List<ProjectOnOffLine> events);
	
	/**
	 * 批量更新项目状态
	 * @param projects
	 */
	void batchUpdateProjectOnlineStatus(List<Project> projects);

	Map<String, Object> queryById(@Param("projectid")String projectid);

	void updateById(@Param("projectid")String projectid, @Param("telphones")String telphones);
}
