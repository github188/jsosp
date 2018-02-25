package com.jieshun.ops.task.wanke;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jieshun.ops.comm.mybatis.dbconn.DataSourceContextHolder;
import com.jieshun.ops.comm.sms.SMSSender;
import com.jieshun.ops.master.dao.PersonDao;
import com.jieshun.ops.master.model.PersonDO;
import com.jieshun.ops.util.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class DataUploadMonitor {
	
	private static final Logger logger = Logger
			.getLogger(DataUploadMonitor.class);

	@Autowired
	private WankeDao dao;
	
	@Autowired
	private SMSSender smsSender;

	@Autowired
	private PersonDao personDao;

	private SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<String,String> exceptions=new HashMap<String,String>();
	static {
		exceptions.put("0000004192", "武汉万科阳逻物流园");
		exceptions.put("0000006556", "漳州万科城15-16号楼门禁");
		exceptions.put("0000006422", "无锡信成花园二期");
		exceptions.put("0000006423", "无锡信成花园三期");
		exceptions.put("0000007167", "宁波雅戈尔新村二村");
		exceptions.put("0000008131", "深圳金色家园");
		exceptions.put("0000008024", "壹海城");
	}

	//@Scheduled(cron = "0 30 8,15 * * ? ")
	//@Scheduled(cron = "0 0/3 * * * ? ")
	protected void execute() {
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(new Date());// 从昨天开始统计
			DataSourceContextHolder.setDbType("ds_wanke");
			List<Map<String, String>> results = dao
					.getNoDataUploadProjects(dateformat.format(calendar.getTime()));
			DataSourceContextHolder.clearDbType();
			
			// 构造短信内容，并发送
			sendMsg(results);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	private void sendMsg(List<Map<String, String>> results) {
		List<PersonDO> receiptors = personDao
				.getReceiptorsByPlatform("WANKE");//万科
		logger.info("接入信息人员数量："+receiptors.size());
		String[] phones = { "15013825413", "13823642043", "13823644931", "13923860490", "13823643341", "13609622513",
				"18565717752", "13823255622", "15013824425", "15013823743", "15013825342", "13501657241", "13764295879",
				"13501662471", "13761785830", "13918610573", "13917389287", "13818735724", "13918775763", "13902694597",
				"13825728216", "15805167829", "15168208418", "13606700515", "13702310197", "15198200327", "13928154897",
				"13811506072", "15013823314", "15013826412", "18876974277", "18689573860", "13923620110", "13928662985",
				"13928652559" };
		//String[] phones = {"13823255622","13641451355"};
//		String path = getConfigPath();
//		logger.info("根据配置路径读取不需要监控的项目：path[" + path + "]");
//		if (path != null && !"".equals(path.trim())) {
//			readJsonFile(path);
//		} else {
//			logger.info("根据配置路径读取不需要监控的项目：path无法获取，任务不执行，请查看配置！");
//			return;
//		}
//		logger.info("根据配置路径读取不需要监控的项目成功:项目数[" + exceptions.size() + "]");
		
		//getExceptProMap();
		logger.info("根据配置路径读取不需要监控的项目：exceptions大小[" + exceptions.size() + "]");
		
		String msg = constructMsg(results);
		if(msg==null){
			msg="恭喜！截止到现在，未发现未上传数据的项目！";
		}
		// 短信内容不能超过3000，超过2800开始截取内容逐段发送
		boolean flag = true;
		while (flag) {
			// 截取前2800个字符
			String t;
			if (msg.length() > 1000) {
				t = msg.substring(0, 1000);
				// 截取后的内容
				msg = msg.substring(1000 + 1, msg.length());
			} else {
				t = msg;
				flag = false;
			}
			// 发送截取的内容
			for (String s : phones) {
				smsSender.sendMessage(s, t);
				logger.info("监控信息已经发送:\n手机号：" + s + ",信息：" + t);
			}
		}
	}

	private String constructMsg(List<Map<String, String>> results) {
		if (results != null && results.size() > 0) {
			String start = "万科未上传出入场数据项目共 "+results.size()+" 个，如下: ";
			String main = "";
			String end = "。 请关注，尽快处理！";
			for (Map<String, String> map : results) {
				String matchKey = map.get("code");
				if (exceptions.containsKey(matchKey)) {
					continue;
				}
				main += map.get("name") + "[" + map.get("code") + "],";
			}
			// 如果经过项目过滤后，没有项目有问题，即返回null
			if (main.trim().equals("")) {
				return null;
			}
			main = main.substring(0, main.lastIndexOf(",") );
			return start + main + end;
		}
		return null;
	}
	
	private void getExceptProMap() {
		String path = StringUtils.getPropertiesAttr("wanke.file.path");
		JSONArray wanke = StringUtils.readJsonFile(path);
		int size = wanke.size();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObject = wanke.getJSONObject(i);
			exceptions.put(jsonObject.get("code").toString(), jsonObject.get("name").toString());
		}
	}
	
}
