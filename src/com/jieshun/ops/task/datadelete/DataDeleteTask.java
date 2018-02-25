package com.jieshun.ops.task.datadelete;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.jieshun.ops.util.JdbcConn;
import com.jieshun.ops.util.PLATFORM;

public class DataDeleteTask extends TimerTask {
	
	private static final Logger logger = Logger.getLogger(DataDeleteTask.class);
	private static Set<JdbcConn> jdbcConns ;
	private static String url;
	private static String user;
	private static String password;
	
	public static Set<JdbcConn> getJdbcConns() {
		return jdbcConns;
	}

	public DataDeleteTask(Set<JdbcConn> jdbcConns) {
		DataDeleteTask.jdbcConns = jdbcConns;
	}

	@Override
	public void run() {
		Connection conn = null;
		ResultSet rs = null;
		try {
			for (JdbcConn jdbcConn : jdbcConns) {
				if ("devops".equals(jdbcConn.getPlatform())) {
					url = jdbcConn.getUrl();
					user = jdbcConn.getUsername();
					password = jdbcConn.getPasswd();

					// 加载驱动程序
					Class.forName("com.mysql.jdbc.Driver");

					// 连续数据库
					conn = DriverManager.getConnection(url, user, password);

					if (!conn.isClosed()) {
						logger.info("Succeeded connecting to the Database!" + url);
					}

					// 结果集
					conn.createStatement().executeUpdate("update del_data set status=0 where status=2 and ENABLE=1");
					rs = conn.createStatement().executeQuery("select * from del_data where STATUS=0 and ENABLE=1 limit 1");
					JdbcConn jdbcConnjsifs = null ;
					JdbcConn jdbcConnjscsp = getConnByPlatform(PLATFORM.CLOUD.toString());;
					while (rs.next()) {
						String code = rs.getString("SUB_CODE");
						String id = rs.getString("ID");
						String platform = rs.getString("PLATFORM");
						int isDeleted = rs.getInt("ISDELETED");
						if(platform.equals(PLATFORM.ZUYONG.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.ZUYONG.toString());
						}else if(platform.equals(PLATFORM.JSSM.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.JSSM.toString());
						}else if(platform.equals(PLATFORM.QST.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.QST.toString());
						}else if(platform.equals(PLATFORM.JSZT.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.JSZT.toString());
						}else if(platform.equals(PLATFORM.WANKE.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.WANKE.toString());
						}else if(platform.equals(PLATFORM.JINDI.getContext())){
							jdbcConnjsifs = getConnByPlatform(PLATFORM.JINDI.toString());
						} else if (platform.equals(PLATFORM.LVCHENG.getContext())) {
							jdbcConnjsifs = getConnByPlatform(PLATFORM.LVCHENG.toString());
						} else if (platform.equals(PLATFORM.BGY.getContext())) {
							jdbcConnjsifs = getConnByPlatform(PLATFORM.BGY.toString());
						} else if (platform.equals(PLATFORM.WHGA.getContext())) {
							jdbcConnjsifs = getConnByPlatform(PLATFORM.WHGA.toString());
						} else if (platform.equals(PLATFORM.LGJB.getContext())) {
							jdbcConnjsifs = getConnByPlatform(PLATFORM.LGJB.toString());
						}
						logger.info("集成服务数据库url:"+jdbcConnjsifs.getUrl());
						conn.createStatement().executeUpdate("update del_data set log = '开始删除集成服务数据',status=2,START_TIME=sysdate() where id ='"+id+"'");
						boolean jsifsFlag=deljsifsBycode(jdbcConnjsifs,id,code,isDeleted);
						if(!jsifsFlag){
							conn.createStatement().executeUpdate("update del_data set log = '删除集成服务服务数据失败，请检查日志',status=3 where id ='"+id+"'");
							return;
						}
						logger.info("云服务数据库url:"+jdbcConnjscsp.getUrl());
						conn.createStatement().executeUpdate("update del_data set log = '开始删除云服务数据' where id ='"+id+"'");
						boolean jscspFlag=deljscspBycode(jdbcConnjsifs,jdbcConnjscsp,id,code,isDeleted);
						if(!jscspFlag){
							conn.createStatement().executeUpdate("update del_data set log = '删除云服务服务数据失败，请检查日志',status=3 where id ='"+id+"'");
							return;
						}
						conn.createStatement().executeUpdate("update del_data set log='数据删除完成',status=1,END_TIME=sysdate() where id ='"+id+"'");
					}
					
					rs.close();
					conn.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				if(rs!=null){
					rs.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
				logger.error(e1);
			}
			logger.error(e);
		}
	}
	
	JdbcConn getConnByPlatform(String platform){
		for (JdbcConn jdbcConn : jdbcConns) {
			if(platform.equals(jdbcConn.getPlatform())) {
				return jdbcConn;
			}
		}
		return null;
	}
	
	boolean deljsifsBycode(JdbcConn jdbcConnjsifs,String id ,String code, int isDeleted){
		Connection conn=null;
		ResultSet rs=null;
		Connection connDevOps=null;
		try {
			// 加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");

			// 连续数据库
			connDevOps = DriverManager.getConnection(url, user, password);

			if (!connDevOps.isClosed()) {
				logger.info("Succeeded connecting to the Database!" + url);
			}

			// 连续数据库
			conn = DriverManager.getConnection(jdbcConnjsifs.getUrl(), jdbcConnjsifs.getUsername(), jdbcConnjsifs.getPasswd());

			if (!conn.isClosed()) {
				logger.info("Succeeded connecting to the Database!");
			}

			// statement用来执行SQL语句
			Statement statement = conn.createStatement();

			// 结果集
			rs = statement.executeQuery("select id as subId,NAME as subName,AREA_ID as areaId,CONTROLUNITID as controId from np_cf_subsystem where code = '"+code+"'");
			int rowCount = 0;  
			while (rs.next()) {
				rowCount++;    
				ResultSet countsRs;
				String subId = rs.getString("subId");
				String areaId = rs.getString("areaId");
				String controId = rs.getString("controId");
				String subName = rs.getString("subName");
				int count =0;
				//NP_DT_PARK_DISCOUNT
				connDevOps.createStatement().executeUpdate("update del_data set SUB_NAME='"+subName+"',log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_DISCOUNT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_DISCOUNT T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_DISCOUNT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_DT_PARK_DISCOUNT WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_DISCOUNT T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_DISCOUNT WHERE ID= '"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_PARK_OUT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_OUT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_OUT T WHERE T.CONTROLUNITID = '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_OUT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_OUT WHERE CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_OUT T WHERE T.CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_OUT WHERE ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_PARK_IN
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_IN') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_IN T WHERE T.CONTROLUNITID = '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_IN,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_IN WHERE CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_IN T WHERE T.CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_IN WHERE ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_PARK_STAY
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_STAY') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_STAY T WHERE T.CONTROLUNITID = '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_STAY,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_STAY WHERE CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_STAY T WHERE T.CONTROLUNITID = '"+controId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_STAY WHERE ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_PARK_CAR_R_CARD
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_CAR_R_CARD') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_CAR_R_CARD T INNER JOIN   NP_CD_CARD C ON T.CARD_ID= C.ID   AND C.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_CAR_R_CARD,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_CAR_R_CARD T INNER JOIN   NP_CD_CARD C ON T.CARD_ID= C.ID   AND C.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_CAR_R_CARD WHERE id='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_DT_PARK_BLACKLIST
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_BLACKLIST') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT COUNT(1) FROM NP_DT_PARK_BLACKLIST T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_BLACKLIST,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_BLACKLIST WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_BLACKLIST T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_BLACKLIST WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_EQ_PARK_SEAT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_PARK_SEAT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT COUNT(1) FROM NP_EQ_PARK_SEAT T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_PARK_SEAT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_EQ_PARK_SEAT WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_EQ_PARK_SEAT T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_PARK_SEAT WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_SYS_PERSON_R_CAR
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_SYS_PERSON_R_CAR') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(distinct person_id) FROM NP_SYS_PERSON_R_CAR T INNER JOIN   NP_SYS_PERSON P ON P.ID=T.person_id AND  P.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_SYS_PERSON_R_CAR,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT distinct T.person_id FROM NP_SYS_PERSON_R_CAR T INNER JOIN   NP_SYS_PERSON P ON P.ID=T.person_id AND  P.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_SYS_PERSON_R_CAR WHERE person_id='"+rs1.getString(1)+"'");
					}
				}
				//NP_DT_PARK_CAR
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_CAR') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_CAR T WHERE T.controlunitid = '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_CAR,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_CAR WHERE controlunitid = '"+controId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_CAR T WHERE T.controlunitid = '"+controId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_CAR WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_TRANSACTION
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_TRANSACTION') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_TRANSACTION T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_TRANSACTION,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_TRANSACTION WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_TRANSACTION T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_TRANSACTION WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_SUBSYSTEM_AUTHOR
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_SUBSYSTEM_AUTHOR') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_SUBSYSTEM_AUTHOR T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_SUBSYSTEM_AUTHOR,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_CD_CARD_SUBSYSTEM_AUTHOR WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_SUBSYSTEM_AUTHOR T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_SUBSYSTEM_AUTHOR WHERE id='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_OPERATE
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_OPERATE') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_OPERATE T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_OPERATE,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_OPERATE WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_OPERATE T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_OPERATE WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_QUIT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_QUIT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_QUIT T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_QUIT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT  T.ID FROM NP_CD_CARD_QUIT T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id ='"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_QUIT WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//NP_CD_CARD_LOSS
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_LOSS') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1)  FROM NP_CD_CARD_LOSS T INNER JOIN NP_CD_CARD C ON T.card_id=C.ID AND C.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_LOSS,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_CD_CARD_LOSS T INNER JOIN NP_CD_CARD C ON T.card_id=C.ID AND C.subsystem_id='"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_LOSS WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//NP_CD_CARD_EXCEPTION
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_EXCEPTION') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_EXCEPTION T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_EXCEPTION,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_CD_CARD_EXCEPTION WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_EXCEPTION T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_EXCEPTION WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_DEFER
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_DEFER') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_DEFER T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_DEFER,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_DEFER WHERE card_id IN (SELECT id FROM NP_CD_CARD WHERE subsystem_id= '"+subId+"') limit 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_DEFER T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id ='"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_DEFER WHERE id='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_CANCEL
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_CANCEL') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_CANCEL T INNER JOIN  NP_CD_CARD C  ON  T.card_id =C.ID  AND C.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_CANCEL,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_CANCEL T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id ='"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_CANCEL WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//NP_CD_CARD_AUTHOR
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_AUTHOR') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_AUTHOR T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_AUTHOR,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_CD_CARD_AUTHOR WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD_AUTHOR T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_AUTHOR WHERE id='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CD_CARD_RLT_EQUIP
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD_RLT_EQUIP') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD_RLT_EQUIP T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD_RLT_EQUIP,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_RLT_EQUIP WHERE card_id IN (SELECT id FROM NP_CD_CARD WHERE subsystem_id= '"+subId+"') limit 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT  T.ID FROM NP_CD_CARD_RLT_EQUIP T  INNER JOIN NP_CD_CARD C ON  T.card_id =C.ID  AND  C.subsystem_id ='"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD_RLT_EQUIP WHERE id='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_DS_OOP
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_DS_OOP') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_DS_OOP T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_DS_OOP,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_OOP WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_DS_OOP T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_OOP WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_DS_SBS
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_DS_SBS') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_DS_SBS T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_DS_SBS,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_SBS WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_DS_SBS T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_SBS WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_MS_OOP
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_MS_OOP') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_MS_OOP T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_MS_OOP,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_OOP WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_MS_OOP T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_OOP WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_MS_SBS
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_MS_SBS') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_MS_SBS T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_MS_SBS,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_SBS WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_MS_SBS T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_SBS WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_DS_EQ
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_DS_EQ') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_DS_EQ T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_DS_EQ,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_EQ WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_DS_EQ T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_DS_EQ WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_DT_SAT_PARK_OUT_MS_EQ
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_SAT_PARK_OUT_MS_EQ') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_SAT_PARK_OUT_MS_EQ T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_SAT_PARK_OUT_MS_EQ,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_EQ WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_SAT_PARK_OUT_MS_EQ T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_SAT_PARK_OUT_MS_EQ WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_AU_GROUP_R_VIDEOFILE
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_AU_GROUP_R_VIDEOFILE') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1)  FROM NP_AU_GROUP_R_VIDEOFILE T  INNER JOIN NP_EQ_AREA A ON T.area_id=A.ID AND A.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_AU_GROUP_R_VIDEOFILE,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_AU_GROUP_R_VIDEOFILE T  INNER JOIN NP_EQ_AREA A ON T.area_id=A.ID AND A.controlunitid='"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_AU_GROUP_R_VIDEOFILE WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//np_dt_sat_car_flow_ds_eq
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_car_flow_ds_eq') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_car_flow_ds_eq T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_car_flow_ds_eq,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_car_flow_ds_eq WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM np_dt_sat_car_flow_ds_eq T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_car_flow_ds_eq WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//np_dt_sat_car_flow_ds_sbs
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_car_flow_ds_sbs') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_car_flow_ds_sbs T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_car_flow_ds_sbs,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_car_flow_ds_sbs WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM np_dt_sat_car_flow_ds_sbs T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_car_flow_ds_sbs WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//np_dt_sat_cash_flow_ds_sbs
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_cash_flow_ds_sbs') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_cash_flow_ds_sbs T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_cash_flow_ds_sbs,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_cash_flow_ds_sbs WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM np_dt_sat_cash_flow_ds_sbs T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_cash_flow_ds_sbs WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//np_dt_sat_discount_ds_eq
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_discount_ds_eq') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_discount_ds_eq T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_discount_ds_eq,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_discount_ds_eq WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM np_dt_sat_discount_ds_eq T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_discount_ds_eq WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//np_dt_sat_discount_ds_sbs
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_discount_ds_sbs') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_discount_ds_sbs T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_discount_ds_sbs,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_discount_ds_sbs WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM np_dt_sat_discount_ds_sbs T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_discount_ds_sbs WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//np_dt_sat_park_out_ds_sht
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_dt_sat_park_out_ds_sht') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_park_out_ds_sht T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_dt_sat_park_out_ds_sht,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM np_dt_sat_park_out_ds_sht WHERE subsystem_id = '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT count(1) FROM np_dt_sat_park_out_ds_sht T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM np_dt_sat_park_out_ds_sht WHERE id='"+rs1.getString(1)+"'");
//					}
				}
				//NP_EQ_ROOM_R_PERSON
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_ROOM_R_PERSON') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1)  FROM NP_EQ_ROOM_R_PERSON T  INNER JOIN  NP_SYS_PERSON P ON T.person_id=P.ID AND P.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_ROOM_R_PERSON,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_EQ_ROOM_R_PERSON T  INNER JOIN  NP_SYS_PERSON P ON T.person_id=P.ID AND P.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_ROOM_R_PERSON WHERE id='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_EQ_AREA_R_PERSON
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_AREA_R_PERSON') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_AREA_R_PERSON T INNER JOIN  NP_SYS_PERSON P ON T.person_id=P.ID AND P.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_AREA_R_PERSON,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.id  FROM NP_EQ_AREA_R_PERSON T INNER JOIN  NP_SYS_PERSON P ON T.person_id=P.ID AND P.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_AREA_R_PERSON WHERE id='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_AU_USER_R_ROLE
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_AU_USER_R_ROLE') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(distinct T.USER_ID)  FROM NP_AU_USER_R_ROLE T  INNER JOIN  NP_AU_USER U ON U.ID=T.USER_ID AND U.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_AU_USER_R_ROLE,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT distinct T.USER_ID  FROM NP_AU_USER_R_ROLE T  INNER JOIN  NP_AU_USER U ON U.ID=T.USER_ID AND U.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_AU_USER_R_ROLE WHERE USER_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_AU_PERSONALITY
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_AU_PERSONALITY') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_AU_PERSONALITY T INNER JOIN   NP_AU_USER P ON P.ID=T.USER_ID AND P.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_AU_PERSONALITY,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_AU_PERSONALITY T INNER JOIN   NP_AU_USER P ON P.ID=T.USER_ID AND P.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_AU_PERSONALITY WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_AU_USER
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_AU_USER') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_AU_USER T WHERE T.CONTROLUNITID= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_AU_USER,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_AU_USER WHERE CONTROLUNITID= '"+controId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_AU_USER T WHERE T.CONTROLUNITID= '"+controId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_AU_USER WHERE ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_CF_SUBSYSTEM_MCARD_STANDARD
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_MCARD_STANDARD') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CF_SUBSYSTEM_MCARD_STANDARD M INNER JOIN NP_EQ_PARK_STANDARD S ON M.STANDARD_ID=S.ID INNER JOIN NP_EQ_PARK P ON S.PARK_ID=P.ID WHERE P.SUBSYSTEM_ID= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CF_SUBSYSTEM_MCARD_STANDARD,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT M.ID FROM NP_CF_SUBSYSTEM_MCARD_STANDARD M INNER JOIN NP_EQ_PARK_STANDARD S ON M.STANDARD_ID=S.ID INNER JOIN NP_EQ_PARK P ON S.PARK_ID=P.ID WHERE P.SUBSYSTEM_ID= '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_MCARD_STANDARD WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_CF_SUBSYSTEM_R_OPERATOR
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_R_OPERATOR') where id ='"+id+"'");
				conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_R_OPERATOR WHERE SUBSYSTEM_ID='"+subId+"'");
				//NP_CD_CARD
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CD_CARD') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CD_CARD T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CD_CARD,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_CD_CARD WHERE subsystem_id= '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CD_CARD T WHERE T.subsystem_id= '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_CD_CARD WHERE ID='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_SYS_PERSON
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_SYS_PERSON') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_SYS_PERSON T WHERE T.controlunitid= '"+controId+"' AND (T.person_type!='EMPLOYEE' or T.person_type is null)");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_SYS_PERSON,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("delete FROM NP_SYS_PERSON WHERE controlunitid= '"+controId+"' AND (person_type!='EMPLOYEE' or person_type is null) LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_SYS_PERSON T WHERE T.controlunitid= '"+controId+"' AND (T.person_type!='EMPLOYEE' or T.person_type is null) LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_SYS_PERSON WHERE ID='"+rs1.getString(1)+"'");
//						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_EQ_ROOM
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_ROOM') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_ROOM T INNER JOIN NP_EQ_AREA A ON A.ID=T.area_id AND A.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_ROOM,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_EQ_ROOM T INNER JOIN NP_EQ_AREA A ON A.ID=T.area_id AND A.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_ROOM WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID="+rs1.getString(1));
					}
				}
				//NP_DT_DOOR_INOUT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_DOOR_INOUT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_DOOR_INOUT T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_DOOR_INOUT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					conn.createStatement().executeUpdate("DELETE FROM NP_DT_DOOR_INOUT WHERE subsystem_id= '"+subId+"' LIMIT 10000");
//					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_DOOR_INOUT T WHERE T.subsystem_id= '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM NP_DT_DOOR_INOUT WHERE ID='"+rs1.getString(1)+"'");
//					}
				}
				//NP_EQ_EQUIPMENT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_EQUIPMENT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_EQUIPMENT T WHERE T.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_EQUIPMENT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_EQ_EQUIPMENT T WHERE T.controlunitid= '"+controId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_EQUIPMENT WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_EQ_PARK_STANDARD
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_PARK_STANDARD') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_PARK_STANDARD T INNER JOIN NP_EQ_PARK E ON E.ID=T.park_id AND E.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_PARK_STANDARD,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID  FROM NP_EQ_PARK_STANDARD T INNER JOIN NP_EQ_PARK E ON E.ID=T.park_id AND E.subsystem_id= '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_PARK_STANDARD WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_EQ_PARK
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_PARK') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_PARK T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_PARK,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_EQ_PARK T WHERE T.subsystem_id= '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_PARK WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_CF_SUBSYSTEM_PAYTYPE
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_PAYTYPE') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CF_SUBSYSTEM_PAYTYPE T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CF_SUBSYSTEM_PAYTYPE,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CF_SUBSYSTEM_PAYTYPE T WHERE T.subsystem_id= '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_PAYTYPE WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_CF_SUBSYSTEM_ONLINE_LOG
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_ONLINE_LOG') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CF_SUBSYSTEM_ONLINE_LOG T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CF_SUBSYSTEM_ONLINE_LOG,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CF_SUBSYSTEM_ONLINE_LOG T WHERE T.subsystem_id= '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_ONLINE_LOG WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_CF_SUBSYSTEM_CARD_TYPE
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_CARD_TYPE') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CF_SUBSYSTEM_CARD_TYPE T WHERE T.subsystem_id= '"+subId+"' AND   card_type_name<>'特殊卡类'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CF_SUBSYSTEM_CARD_TYPE,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CF_SUBSYSTEM_CARD_TYPE T WHERE T.subsystem_id= '"+subId+"' AND   card_type_name<>'特殊卡类' limit 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_CARD_TYPE WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_CF_SUBSYSTEM_EVENT_TYPE_MAP
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_CF_SUBSYSTEM_EVENT_TYPE_MAP') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_CF_SUBSYSTEM_EVENT_TYPE_MAP T WHERE T.subsystem_id='"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_CF_SUBSYSTEM_EVENT_TYPE_MAP,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_CF_SUBSYSTEM_EVENT_TYPE_MAP T WHERE T.subsystem_id= '"+subId+"' limit 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_CF_SUBSYSTEM_EVENT_TYPE_MAP WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_DT_PARK_STAT_INOUT_COUNT
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_DT_PARK_STAT_INOUT_COUNT') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_DT_PARK_STAT_INOUT_COUNT T WHERE T.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_DT_PARK_STAT_INOUT_COUNT,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_DT_PARK_STAT_INOUT_COUNT T WHERE T.subsystem_id= '"+subId+"' limit 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_DT_PARK_STAT_INOUT_COUNT WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//NP_EQ_ESTABLISH
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.NP_EQ_ESTABLISH') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM NP_EQ_ESTABLISH T WHERE T.controlunitid= '"+controId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.NP_EQ_ESTABLISH,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM NP_EQ_ESTABLISH T WHERE T.controlunitid= '"+controId+"' limit 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM NP_EQ_ESTABLISH WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//np_eq_area
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_eq_area') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM np_eq_area T WHERE T.controlunitid= '"+controId+"' and AREA_CODE <> '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jsifs.np_eq_area,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT t.id FROM np_eq_area T WHERE T.controlunitid= '"+controId+"' and AREA_CODE <> '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM np_eq_area WHERE ID='"+rs1.getString(1)+"'");
						conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+rs1.getString(1)+"'");
					}
				}
				
				if(isDeleted == 1){
					//np_cf_subsystem
					logger.info("删除jsifs.np_cf_subsystem,共"+1+"条");
					connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_cf_subsystem') where id ='"+id+"'");
					conn.createStatement().executeUpdate("DELETE FROM np_cf_subsystem WHERE ID='"+subId+"'");
					conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+subId+"'");
					//np_eq_area
					logger.info("删除jsifs.np_eq_area,共"+1+"条");
					connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jsifs.np_eq_area') where id ='"+id+"'");
					conn.createStatement().executeUpdate("DELETE FROM np_eq_area WHERE ID='"+areaId+"'");
					conn.createStatement().executeUpdate("DELETE FROM middle_sync_common_data WHERE RECORD_ID='"+areaId+"'");
				}
			}
			logger.info("子系统编号:"+code+",集成平台平台查询到的记录数:"+rowCount);
			
			rs.close();
			conn.close();
			connDevOps.close();
			
			return true;
		} catch (Exception e) {
			logger.error("集成服务删除失败,返回false"+e);
			e.printStackTrace();
			try {
				if(rs !=null){
					rs.close();
				}
				if(conn !=null){
					conn.close();
				}
				if(connDevOps !=null){
					connDevOps.close();
				}
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			return false;
		}
	}
	
	boolean deljscspBycode(JdbcConn jdbcConnjsifs,JdbcConn jdbcConnjscsp,String id ,String code,int isDeleted){
		Connection conn=null;
		ResultSet rs = null;
		Connection connDevOps=null;
		Connection connjsifs=null;
		try {
			// 加载驱动程序
			Class.forName("com.mysql.jdbc.Driver");

			// 连续数据库
			connDevOps = DriverManager.getConnection(url, user, password);

			if (!connDevOps.isClosed()) {
				logger.info("Succeeded connecting to the Database!" + url);
			}
			
			// 连续数据库
			conn = DriverManager.getConnection(jdbcConnjscsp.getUrl(), jdbcConnjscsp.getUsername(), jdbcConnjscsp.getPasswd());

			if (!conn.isClosed()) {
				logger.info("Succeeded connecting to the Database!");
			}
			
			connjsifs = DriverManager.getConnection(jdbcConnjsifs.getUrl(), jdbcConnjsifs.getUsername(), jdbcConnjsifs.getPasswd());
			
			if (!connjsifs.isClosed()) {
				logger.info("Succeeded connecting to the Database!");
			}

			// statement用来执行SQL语句
			Statement statement = conn.createStatement();

			// 结果集
			rs = statement.executeQuery("select id as subId,AREA_ID as areaId from cs_dt_subsystem where SUBSYSTEM_CODE = '"+code+"'");
			int rowCount = 0;  
			while (rs.next()) {
				rowCount++;    
				ResultSet countsRs;
				String subId = rs.getString("subId");
				String areaId = rs.getString("areaId");
				int count =0;
				//cs_app_user_inviter
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_app_user_inviter') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_app_user_inviter T INNER JOIN cs_dt_house C on C.ID =T.HOUSE_ID  AND C.area_id= '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_app_user_inviter,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_app_user_inviter T INNER JOIN cs_dt_house C on C.ID =T.HOUSE_ID  AND C.area_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_app_user_inviter WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_app_user_inviter2
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_app_user_inviter T INNER JOIN  cs_dt_person C on C.ID =T.INVITER_PERSON_ID  AND C.area_id= '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_app_user_inviter T INNER JOIN  cs_dt_person C on C.ID =T.INVITER_PERSON_ID  AND C.area_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_app_user_inviter WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_opr_equip_log
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_opr_equip_log') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_biz_user_opr_equip_log T INNER JOIN  cs_dt_card C on C.ID =T.CARD_ID  AND C.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_opr_equip_log,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_biz_user_opr_equip_log T INNER JOIN  cs_dt_card C on C.ID =T.CARD_ID  AND C.subsystem_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_opr_equip_log WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_person_r_house
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_person_r_house') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(distinct person_id) FROM cs_dt_person_r_house T INNER JOIN cs_dt_person C on C.ID =T.person_id  AND C.area_id= '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_person_r_house,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT distinct T.person_id FROM cs_dt_person_r_house T INNER JOIN cs_dt_person C on C.ID =T.person_id  AND C.area_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_person_r_house WHERE person_id='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_card_r_equip
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_card_r_equip') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(distinct T.card_id) FROM cs_dt_card_r_equip T JOIN cs_dt_card C on C.ID =T.card_id  AND C.subsystem_id= '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_card_r_equip,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT distinct T.card_id FROM cs_dt_card_r_equip T JOIN cs_dt_card C on C.ID =T.card_id  AND C.subsystem_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_card_r_equip WHERE card_id='"+rs1.getString(1)+"'");
					}
				}
				//cs_au_user_bind_phone
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_au_user_bind_phone') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_au_user_bind_phone T WHERE T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_au_user_bind_phone,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_au_user_bind_phone T WHERE T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_au_user_bind_phone WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_complain_r_pic
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_complain_r_pic') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_biz_user_complain_r_pic T INNER JOIN cs_biz_user_complain C on C.ID =T.service_id  AND C.area_id= '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_complain_r_pic,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_biz_user_complain_r_pic T INNER JOIN cs_biz_user_complain C on C.ID =T.service_id  AND C.area_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_complain_r_pic WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_complain
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_complain') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_biz_user_complain T WHERE T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_complain,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_biz_user_complain T WHERE T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_complain WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_def_card
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_def_card') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_user_def_card t INNER JOIN cs_dt_card c on t.CARD_ID =c.id and  c.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_def_card,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select t.id from cs_biz_user_def_card t INNER JOIN cs_dt_card c on t.CARD_ID =c.id and  c.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_def_card WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_card_delay
//				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_card_delay') where id ='"+id+"'");
//				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_card_delay cd INNER JOIN cs_dt_card c ON cd.CARD_ID =c.id and  c.subsystem_id = '"+subId+"'");
//				while (countsRs.next()) {
//					count = countsRs.getInt(1);
//				}
//				logger.info("删除jscsp.cs_biz_card_delay,共"+count+"条"+",子系统编号:"+code);
//				for(int i =0;i < (count+9999)/10000;i++){
//					ResultSet rs1 = conn.createStatement().executeQuery("select cd.id from cs_biz_card_delay cd INNER JOIN cs_dt_card c ON cd.CARD_ID =c.id and  c.subsystem_id = '"+subId+"' LIMIT 10000");
//					while (rs1.next()) {
//						conn.createStatement().executeUpdate("DELETE FROM cs_biz_card_delay WHERE ID='"+rs1.getString(1)+"'");
//					}
//				}
				//cs_biz_card_loss
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_card_loss') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_card_loss cl inner join  cs_dt_card c on  cl.CARD_ID =c.id and c.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_card_loss,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select c.id from cs_biz_card_loss cl inner join  cs_dt_card c on  cl.CARD_ID =c.id and c.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_card_loss WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_au_user_bind
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_au_user_bind') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_au_user_bind ub inner join cs_dt_person p on ub.PERSON_ID =p.id and area_id= '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_au_user_bind,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select ub.ID from cs_au_user_bind ub inner join cs_dt_person p on ub.PERSON_ID =p.id and area_id= '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_au_user_bind WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_area
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_area') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_user_area ua where ua.AREA_ID = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_area,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select ua.ID from cs_biz_user_area ua where ua.AREA_ID =  '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_area WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_card_service
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_card_service') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_card_service T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_card_service,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_dt_card_service T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_card_service WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_equip
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_equip') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_user_equip e inner join cs_dt_equip T on  e.EQUIP_ID =T.ID and T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_equip,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.ID from cs_biz_user_equip e inner join cs_dt_equip T on  e.EQUIP_ID =T.ID and T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_equip WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_opr_equip_log
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_opr_equip_log') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_user_opr_equip_log l INNER JOIN cs_dt_equip e on e.id = l.EQUIP_ID where e.SUBSYSTEM_ID =  '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_opr_equip_log,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select l.id from cs_biz_user_opr_equip_log l INNER JOIN cs_dt_equip e on e.id = l.EQUIP_ID where e.SUBSYSTEM_ID =  '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_opr_equip_log WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_equip
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_equip') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_equip T WHERE T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_equip,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_dt_equip T WHERE T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_equip WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_house
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_house') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_house T WHERE T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_house,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_dt_house T WHERE T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_house WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_park_booking_config
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_park_booking_config') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_park_booking_config T inner join  cs_dt_park C on  C.ID =T.park_id  AND C.park_code= '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_park_booking_config,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_park_booking_config T inner join  cs_dt_park C on  C.ID =T.park_id  AND C.park_code= '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_park_booking_config WHERE ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_cf_park_r_china_area
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_cf_park_r_china_area') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(distinct e.PARK_ID) from cs_cf_park_r_china_area e inner join cs_dt_park T on e.PARK_ID=T.ID and T.park_code = '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_cf_park_r_china_area,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select distinct e.PARK_ID from cs_cf_park_r_china_area e inner join cs_dt_park T on e.PARK_ID=T.ID and T.park_code = '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_cf_park_r_china_area WHERE PARK_ID='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_park_space
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_park_space') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_dt_park_space e inner join  cs_dt_park T on e.PARK_ID =T.ID and T.park_code = '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_park_space,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_dt_park_space e inner join  cs_dt_park T on e.PARK_ID =T.ID and T.park_code = '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_park_space WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_park_charge_standard
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_park_charge_standard') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_park_charge_standard T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_park_charge_standard,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_dt_park_charge_standard T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_park_charge_standard WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_biz_user_park
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_biz_user_park') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_biz_user_park e inner join cs_dt_park T on e.PARK_ID= T.ID and T.park_code = '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_biz_user_park,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_biz_user_park e inner join cs_dt_park T on e.PARK_ID= T.ID and T.park_code = '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_biz_user_park WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_park_attention
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_park_attention') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_park_attention e inner join cs_dt_park T on e.PARK_ID=T.ID and T.park_code = '"+code+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_park_attention,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_park_attention e inner join cs_dt_park T on e.PARK_ID=T.ID and T.park_code = '"+code+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_park_attention WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_park_booking
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_park_booking') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_park_booking e inner join cs_dt_park T  on  e.PARK_ID =T.ID and T.park_code = '"+code+"' and e.PARENT_ID is not null");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_park_booking,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_park_booking e inner join cs_dt_park T  on  e.PARK_ID =T.ID and T.park_code = '"+code+"' and e.PARENT_ID is not null LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_park_booking WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_park_booking2
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_park_booking e inner join cs_dt_park T  on  e.PARK_ID =T.ID and T.park_code = '"+code+"' and e.PARENT_ID is null");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_park_booking e inner join cs_dt_park T  on  e.PARK_ID =T.ID and T.park_code = '"+code+"' and e.PARENT_ID is null LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_park_booking WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_park
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_dt_park e where e.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_park,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.id from cs_dt_park e where e.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_park WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_card
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_card') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_card T WHERE T.subsystem_id = '"+subId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_card,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT T.ID FROM cs_dt_card T WHERE T.subsystem_id = '"+subId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_card WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_dt_person
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_person') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_person T WHERE T.area_id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_dt_person,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("SELECT count(1) FROM cs_dt_person T WHERE T.area_id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_dt_person WHERE id='"+rs1.getString(1)+"'");
					}
				}
				//cs_app_user_apply
				connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_app_user_apply') where id ='"+id+"'");
				countsRs = conn.createStatement().executeQuery("select count(1) from cs_app_user_apply e inner join cs_dt_area T on e.AREA_ID =T.ID and T.id = '"+areaId+"'");
				while (countsRs.next()) {
					count = countsRs.getInt(1);
				}
				logger.info("删除jscsp.cs_app_user_apply,共"+count+"条"+",子系统编号:"+code);
				for(int i =0;i < (count+9999)/10000;i++){
					ResultSet rs1 = conn.createStatement().executeQuery("select e.ID from cs_app_user_apply e inner join cs_dt_area T on e.AREA_ID =T.ID and T.id = '"+areaId+"' LIMIT 10000");
					while (rs1.next()) {
						conn.createStatement().executeUpdate("DELETE FROM cs_app_user_apply WHERE id='"+rs1.getString(1)+"'");
					}
				}
				if(isDeleted == 1){
					//cs_dt_subsystem
					logger.info("删除jscsp.cs_dt_subsystem,共1条"+",子系统编号:"+code);
					connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_subsystem') where id ='"+id+"'");
					conn.createStatement().executeUpdate("DELETE FROM cs_dt_subsystem WHERE ID='"+subId+"'");
					//np_eq_area
					logger.info("删除jscsp.np_eq_area,共1条"+",子系统编号:"+code);
					connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start delete jscsp.cs_dt_area') where id ='"+id+"'");
					conn.createStatement().executeUpdate("DELETE FROM cs_dt_area WHERE ID='"+areaId+"'");
				}else{
					logger.info("更新jsifs子系统和防区表"+",子系统编号:"+code);
					connDevOps.createStatement().executeUpdate("update del_data set log = CONCAT(SYSDATE(),'   start update jsifs.middle_sync_common_data') where id ='"+id+"'");
					connjsifs.createStatement().executeUpdate("update middle_sync_common_data set SYNC_FLAG=0 WHERE RECORD_ID='"+areaId+"'");
					connjsifs.createStatement().executeUpdate("update middle_sync_common_data set SYNC_FLAG=0  WHERE RECORD_ID='"+subId+"'");
				}
				
			}
			logger.info("子系统编号:"+code+",云平台查询到的记录数:"+rowCount);

			rs.close();
			conn.close();
			connDevOps.close();
			connjsifs.close();

		} catch (Exception e) {
			logger.error("云服务删除失败,返回false"+e);
			e.printStackTrace();
			try {
				if(rs !=null){
					rs.close();
				}
				if(conn !=null){
					conn.close();
				}
				if(connDevOps !=null){
					connDevOps.close();
				}
				if(connjsifs !=null){
					connjsifs.close();
				}
			} catch (SQLException e1) {
				logger.info(e1);
				e1.printStackTrace();
			}
			return false;
		}
		return true;
	}
	

}