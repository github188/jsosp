package com.jieshun.ops.task.zbt;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ZbtParkInoutCheck {

	private static final Logger logger = Logger.getLogger(ZbtParkInoutCheck.class);

	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
//	private static final long PERIOD_DAY =  1000;
	
/*	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}*/

	public ZbtParkInoutCheck() {
		System.out.println("123");
	}
	
	public ZbtParkInoutCheck(String time,String driver, String url, String user, String password, String parkcodes, String phones) {
		System.out.println("123456789");
		try {
//			Properties p = new Properties();
//			p.load(new FileInputStream(new File("config.properties")));

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0])); // 凌晨1点
			calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
			calendar.set(Calendar.SECOND, 0);
			Date date = calendar.getTime(); // 第一次执行定时任务的时间
			// 如果第一次执行定时任务的时间 小于当前的时间
			// 此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
			if (date.before(new Date())) {
				date = addDay(date, 1);
			}
//			Timer timer = new Timer();
			ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
			        new BasicThreadFactory.Builder().namingPattern("ZbtParkInoutCheck-schedule-pool-%d").daemon(true).build());
			ZbtParkInoutTask task = new ZbtParkInoutTask(driver,url,user,password,parkcodes.replace(",", "','"),phones);

			executorService.scheduleAtFixedRate(task, date.getTime() -System.currentTimeMillis(), PERIOD_DAY,TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("程序发生异常", e);
		}
	
	}
	
	// 增加或减少天数
	public static Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

}
