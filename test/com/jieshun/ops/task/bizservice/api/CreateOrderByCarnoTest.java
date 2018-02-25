package com.jieshun.ops.task.bizservice.api;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * 压力测试码
 * 
 * @author 刘淦潮
 *
 */

public class CreateOrderByCarnoTest {
	private static final Logger logger = Logger
			.getLogger(CreateOrderByCarnoTest.class);
	private static Set<String> tasks = new HashSet<String>();

	private ExecutorService threadPool;
	
	private static int projectCount=50;// 50个项目

	private static String initProjectCode = "0000007000";//初始化项目编号，会从此编号开始自动创建项目编号，每次自增1
	private static  DecimalFormat df=new DecimalFormat("0000000000");//格式：10位数字
	private static synchronized void control(String key, boolean added) {
		if (added) {
			tasks.add(key);
		} else {
			tasks.remove(key);
		}
		logger.debug("action:"+added+"\tkey:"+key);
	}

	@Test
	public void testRun() {
		threadPool = new ScheduledThreadPoolExecutor(1,
		        new BasicThreadFactory.Builder().namingPattern("CreateOrderByCarnoTest-schedule-pool-%d").daemon(true).build());   
//		threadPool = Executors.newFixedThreadPool(projectCount);


		for (int i = 0; i < 10; i++) {
			String code = increaseProjectCode(initProjectCode, i);
			control(code, true);
			threadPool.execute(new ProjectWorker(this, code));
			logger.debug("正在模拟项目["+code+"]的压力测试！");
		}

	}

	/**
	 * 项目编号自增
	 * @param preCode 前一个编号
	 * @param initCode 初始化编号
	 * @return
	 */
	private String increaseProjectCode(String initCode, int increase) {
		int projectCode=Integer.parseInt(initCode);
		projectCode+=increase;
		return df.format(projectCode);
	}

}

/**
 * 项目工作代理
 * @author habibliu
 *
 */
class ProjectWorker implements Runnable {
	private static final Logger logger = Logger
			.getLogger(ProjectWorker.class);
	private CreateOrderByCarnoTest tester;
	private String projectCode;
	//private static NumberFormat formatter = NumberFormat.getInstance("");
	private static  DecimalFormat df=new DecimalFormat("00000");
	private static  DecimalFormat df2=new DecimalFormat("0000");
	private static String[] areaCode = { "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z", "A1", "B1", "C1", "D1", "E1", "F1",
			"G1", "H1", "I1", "J1", "K1", "L1", "M1", "N1", "O1", "P1", "Q1",
			"R1", "S1", "T1", "U1", "V1", "W1", "X1", "Y1", "Z1" };

	public ProjectWorker(CreateOrderByCarnoTest tester, String projectCode) {
		this.tester = tester;
		this.projectCode = projectCode;
	}

	@Override
	public void run() {
		ExecutorService threadPool = new ScheduledThreadPoolExecutor(1,
		        new BasicThreadFactory.Builder().namingPattern("CloudServiceMonitor-schedule-pool-%d").daemon(true).build());// 100个并发
		// TODO Auto-generated method stub
		String[] licensePlates = initLicensePlate();
		for (String lp : licensePlates) {
			Properties param = buildParamter(lp);
			param.put("carNo", lp);
			threadPool.execute(new ActionWorker(param));
			logger.debug("正在模拟项目["+this.projectCode+"],车牌号["+lp+"]的请求！");
		}
	}
	
	/**
	 * 
	 *	构造如下参数
	 *	"attributes":{
	 *		"businesserCode": "",
	 *		"parkCode": "",
	 *		"orderType": "",
	 *     	"carNo": ""
	 *		}
	 * }
	 * @return
	 */
	private Properties buildParamter(String lp) {
		Properties prop=new Properties();
		prop.put("businesserCode", "880051201000000");//默认商户号
		prop.put("parkCode", this.projectCode);
		prop.put("orderType", "VNP");
		prop.put("carNo", lp);
		prop.put("psw", "123456");
		return prop;
	}

	/**
	 * 构建100个车牌
	 * 
	 * @return
	 */
	private String[] initLicensePlate() {
		String lp = "粤-";
		String code=this.projectCode.substring(this.projectCode.length()-3);
		code=areaCode[Integer.parseInt(code)];
		lp+=code;
		String[] licensePlates=new String[100];
		String lic=null;
		for(int i=0;i<100;i++){
			if(code.length()<2){
				lic=lp+df.format(i);
			}else{
				lic=lp+df2.format(i);
			}
			licensePlates[i]=lic;
		}
		return licensePlates;
	}
	
	public static void main(String[] args){
		
		ProjectWorker worker=new ProjectWorker(null,"0000007000");
		worker.run();
	}

}

/**
 * 请求代理
 * @author habibliu
 *
 */
class ActionWorker implements Runnable {

	private Properties param;

	public ActionWorker(Properties param) {
		this.param = param;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				CreateOrderByCarno action = new CreateOrderByCarno(param);
				action.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
