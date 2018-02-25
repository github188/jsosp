package com.jieshun.ops.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jieshun.ops.comm.IBaseDao;

public interface DcMonitorTaskDao extends IBaseDao {
	List<Map<String, String>> listDC(@Param("yestd")String yestd);
	List<Map<String, String>> listOutDC(@Param("yestd")String yestd);
	List<Map<String, Object>> listDCProjects();
	List<Map<String, Object>> listjsifsInProjects(@Param("yestd")String yestd,@Param("today")String today,@Param("projects")List<String> projects);
	List<Map<String, Object>> listjsifsOutProjects(@Param("yestd")String yestd,@Param("today")String today,@Param("projects")List<String> projects);
	void insertJsifs(List<Map<String, Object>> monitors);
	void updateJsifs(List<Map<String, Object>> monitors);
	void updateDC(List<Map<String, String>> list);
	void updatePics(List<HashMap<String, String>> picList);
	void updateOutJsifs(List<Map<String, Object>> monitors);
	void updateOutDC(List<Map<String, String>> list);
	void updateOutPics(List<HashMap<String, String>> picList);
}
