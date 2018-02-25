package com.jieshun.ops.elk.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.elk.dao.ELKDao;
import com.jieshun.ops.elk.service.IELkService;
import com.jieshun.ops.util.SendMsgUtil;
import com.jieshun.ops.util.StringUtil;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:23:47
 */
@Service("elkService")
public class ELkServiceImpl implements IELkService {

	@Autowired
	private SMSSender smsSender;
	@Autowired
	private ELKDao elkDao;
	@Autowired
	private DataSourceTransactionManager transactionManager;

	private static final Logger logger = Logger.getLogger(ELkServiceImpl.class.getName());

	@Override
	public String add(String ip, String phones, String message, int isRecovery) {
		JsonObject response = new JsonObject();
		TransactionStatus status = null;
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			status = transactionManager.getTransaction(def);

			String id = UUID.randomUUID().toString().replace("-", "");

			Map<String, Object> map = elkDao.query(ip, 1,isRecovery);
			Map<String, Object> map1 = elkDao.query(ip, 1,1-isRecovery);
			
			if(map == null || map.size() == 0){
				logger.info("未发送过ip"+ip+",报警类型(isRecovery):"+isRecovery);
//				boolean flag = SendMsgUtil.sendMessage(phones, message);
				int flag = smsSender.sendMessage(phones, message);

				if (flag!=-1) {
					elkDao.add(id, ip, message, 1,isRecovery);
				} else {
					elkDao.add(id, ip, message, 0,isRecovery);
				}
			}else {
				logger.info("已发送过ip"+ip+",报警类型(isRecovery):"+isRecovery);
				if (isRecovery == 1) {
					Date lastRecover = (Timestamp) map.get("CREATE_TIME");
					Date lastWarn = (Timestamp) map1.get("CREATE_TIME");
					logger.info("短信类型:恢复短信,上次报警时间:"+lastWarn+";上次恢复时间:"+lastRecover);
					if(lastWarn.getTime()>=lastRecover.getTime()){
						logger.info("短信类型:恢复短信,上次报警时间:"+lastWarn+";上次恢复时间L:"+lastRecover+"恢复时间大于报警时间,准备报警......");
						int flag = smsSender.sendMessage(phones, message);

						if (flag!=-1) {
							elkDao.add(id, ip, message, 1,isRecovery);
						} else {
							elkDao.add(id, ip, message, 0,isRecovery);
						}
					}
				}else {
					Date lastWarn = (Timestamp) map.get("CREATE_TIME");
					Date lastRecover = (Timestamp) map1.get("CREATE_TIME");
					logger.info("短信类型:报警短信,上次报警时间:"+lastWarn+";上次恢复时间L:"+lastRecover);
					if(lastRecover.getTime()>=lastWarn.getTime()){
						logger.info("短信类型:报警短信,上次报警时间:"+lastWarn+";上次恢复时间L:"+lastRecover+"恢复时间大于报警时间,准备报警......");
						int flag = smsSender.sendMessage(phones, message);

						if (flag!=-1) {
							elkDao.add(id, ip, message, 1,isRecovery);
						} else {
							elkDao.add(id, ip, message, 0,isRecovery);
						}
					}
				}
			}
			
//			if (isRecovery == 1) {
//				boolean flag = SendMsgUtil.sendMessage(phones, message);
//
//				if (flag) {
//					elkDao.add(id, ip, message, 1,isRecovery);
//				} else {
//					elkDao.add(id, ip, message, 0,isRecovery);
//				}
//			} else {
//
//				if (map == null || map.size() == 0) {
//					boolean flag = SendMsgUtil.sendMessage(phones, message);
//
//					if (flag) {
//						elkDao.add(id, ip, message, 1,isRecovery);
//					} else {
//						elkDao.add(id, ip, message, 0,isRecovery);
//					}
//				} else {
//					
//					if(mapRecov ==null || mapRecov.size()==0){
//						
//						Date last = (Timestamp) map.get("CREATE_TIME");
//						Date now = new Date();
//						
//						long mins = (now.getTime() - last.getTime()) / (60 * 1000);
//						
//						if (mins >= 60 * 24) {
//							boolean flag = SendMsgUtil.sendMessage(phones, message);
//							
//							if (flag) {
//								elkDao.add(id, ip, message, 1,isRecovery);
//							} else {
//								elkDao.add(id, ip, message, 0,isRecovery);
//							}
//						}
//					}else{
//						System.out.println(mapRecov.get("CREATE_TIME").getClass());
//						
//						Date lastRecover = (Timestamp) mapRecov.get("CREATE_TIME");
//						Date lastWarn = (Timestamp) map.get("CREATE_TIME");
//						//如果最新的恢复断线大于最新的报警短信，则继续报警
//						if(lastRecover.getTime() > lastWarn.getTime()){
//							boolean flag = SendMsgUtil.sendMessage(phones, message);
//							
//							if (flag) {
//								elkDao.add(id, ip, message, 1,isRecovery);
//							} else {
//								elkDao.add(id, ip, message, 0,isRecovery);
//							}
//						}
//					}
//
//				}
//
//			}

			transactionManager.commit(status);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增用户异常");
			return response.toString();
		}
	}

	@Override
	public String sendMsg(String phone, String message) {
		JsonObject response = new JsonObject();
		TransactionStatus status = null;
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

			status = transactionManager.getTransaction(def);

			String id = UUID.randomUUID().toString().replace("-", "");

			if(smsSender.sendMessage(phone, message)!=-1){
				elkDao.add(id, "0.0.0.0", message, 1,0);
			}else {
				elkDao.add(id, "0.0.0.0", message, 0,0);
			}
			
			
			transactionManager.commit(status);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "新增成功");

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "新增用户异常");
			return response.toString();
		}
	}

	@Override
	public String cardMapped(int pageIndex,int pageSize,String platform, String stat_date) {
		JsonObject response = new JsonObject();
		try {
			DataSourceContextHolder.clearDbType();
			DataSourceContextHolder.setDbType("defaultDataSource");
			HashMap<String, Object> counts = elkDao.queryCardMappedCounts(platform, stat_date);
			response.addProperty("counts", counts.get("COUNT").toString());
			
			List<HashMap<String, Object>> list = elkDao.queryCardMapped(pageIndex*pageSize, pageSize,platform, stat_date);
			
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			JsonArray result = new JsonArray();
			
			for (HashMap<String, Object> hashMap : list) {
				String plat = "";
//				String ip = hashMap.get("IP")==null?"":hashMap.get("IP").toString();
				String message = hashMap.get("MESSAGE")==null?"":hashMap.get("MESSAGE").toString();
				String create_time = simpleDateFormat.format(hashMap.get("CREATE_TIME")==null?"":(Date)hashMap.get("CREATE_TIME"));
				String[] msg = message.split(";");
				plat = msg[0].split(":")[1].split(",")[0];
				String code = msg[0].split(":")[2].split("=")[0];
				String name = msg[0].split(":")[2].split("=")[1];
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("platform", plat);
				jsonObject.addProperty("subsystemcode", code);
				jsonObject.addProperty("subsystemname", name);
				jsonObject.addProperty("createtime", create_time);
				result.add(jsonObject);
				for (int i = 1; i < msg.length; i++) {
					String code1 = msg[i].split("=")[0];
					String name1 = msg[i].split("=")[1];
					JsonObject jsonObject1 = new JsonObject();
					jsonObject1.addProperty("platform", plat);
					jsonObject1.addProperty("subsystemcode", code1);
					jsonObject1.addProperty("subsystemname", name1);
					jsonObject1.addProperty("createtime", create_time);
					result.add(jsonObject1);
				}
				
			}

			response.add("result", result);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");

			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

}
