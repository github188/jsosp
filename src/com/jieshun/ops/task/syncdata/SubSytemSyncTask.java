package com.jieshun.ops.task.syncdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jieshun.ops.project.dao.ProjectDao;
import com.jieshun.ops.project.model.Project;
import com.jieshun.ops.util.JdbcConn;
import com.jieshun.ops.util.StringUtil;
import com.jieshun.ops.util.StringUtils;


public class SubSytemSyncTask implements Runnable {
	
	private static final Logger logger = Logger.getLogger(SubSytemSyncTask.class);
	
	private static JdbcConn source;
	
	private static JdbcConn target;
	
	private ProjectDao projectDao;
	
	private String url;
	
	private String user;
	
	private String pwd;
	
	private static String sql1 = "select * from cs_dt_subsystem where status='NORMAL'";
	
	private static String sql2 = "select * from mas_project where status = 'NORMAL'";
	
	public SubSytemSyncTask(JdbcConn source, JdbcConn target) {
		SubSytemSyncTask.source = source;
		SubSytemSyncTask.target = target;
	}

	@Override
	public void run() {
		logger.info("subsystem synchronized start......");
		Connection conn = null;
		ResultSet rs = null;
		Map<String, Project> proMap = new HashMap<String, Project>();
		List<Project> proList = new ArrayList<Project>();
		Map<String, Project> tempMap = new HashMap<String, Project>();
		try {
			logger.info("get the source data infomation......");
			// 获取需要迁移的子系统集合
			url = source.getUrl();
			user = source.getUsername();
			pwd = source.getPasswd();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			if (!conn.isClosed()) {
				logger.info("Succeeded connecting to the source database!");
			}
			rs = conn.createStatement().executeQuery(sql1);
			Project pro;
			while (rs.next()) {
				pro = new Project();
				pro.setId(rs.getString("ID"));
				pro.setCode(rs.getString("SUBSYSTEM_CODE"));
				pro.setName(rs.getString("SUBSYSTEM_NAME"));
				pro.setPlatformId(rs.getString("NISSP_ID"));
				pro.setCreateTime(StringUtil.getCurTime());
				pro.setUpdateTime(StringUtil.getCurTime());
				pro.setIsOnline(0);
				pro.setStatus("NORMAL");
				proMap.put(rs.getString("SUBSYSTEM_CODE"), pro);
			}
			rs.close();
			conn.close();
			
			logger.info("get the target data infomation......");
			url = target.getUrl();
			user = target.getUsername();
			pwd = target.getPasswd();
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pwd);
			if (!conn.isClosed()) {
				logger.info("Succeeded connecting to the target database!");
			}
			rs = conn.createStatement().executeQuery(sql2);
			while (rs.next()) {
				pro = new Project();
				pro.setId(rs.getString("ID"));
				pro.setCode(rs.getString("CODE"));
				pro.setName(rs.getString("NAME"));
				tempMap.put(rs.getString("CODE"), pro);
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("get the new subsystem infomation......");
		if (proMap != null && proMap.size() > 0) {
			Iterator<String> it = proMap.keySet().iterator();
			String key;
			while (it.hasNext()) {
				key = (String) it.next();
				if (tempMap != null && tempMap.size() > 0) {
					if (!tempMap.containsKey(key)) {
						proList.add(proMap.get(key));
					}
				}
			}
		}
		if (proList != null && proList.size() > 0) {
			logger.info("SubSytemSyncTask starting save......");
			try {
				url = target.getUrl();
				user = target.getUsername();
				pwd = target.getPasswd();
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(url, user, pwd);
				if (!conn.isClosed()) {
					logger.info("Succeeded connecting to the database!");
				}
				for (Project p : proList) {
					projectDao.add(StringUtils.getUUId(), p.getPlatformId(), p.getName(), p.getCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			logger.info("(SubSytemSyncTask) nothing to save......");
		}
	}

}
