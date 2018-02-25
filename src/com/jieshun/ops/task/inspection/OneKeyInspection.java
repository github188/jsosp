package com.jieshun.ops.task.inspection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.python.util.PythonInterpreter;

import com.jieshun.ops.util.StringUtils;

/**
 * 一键巡检
 * @author yt
 *
 */
public class OneKeyInspection {
	
	private static final Logger log = Logger.getLogger(OneKeyInspection.class);
	
	public String doPythonScript() {
		log.info("自动巡检功能开始，读取配置参数");
//		String initUrl = StringUtils.getPropertiesAttr("python.int.addr");
//		String bigkeyUrl = StringUtils.getPropertiesAttr("python.bigkey.addr");
		String readfileUrl = StringUtils.getPropertiesAttr("python.readfile.addr");
		String slowlog = StringUtils.getPropertiesAttr("python.slowlog.addr");
		String api = StringUtils.getPropertiesAttr("python.api.addr");
		String bgy = StringUtils.getPropertiesAttr("python.bgy.addr");
		log.info("读取配额制成功:"
//				+ "\n initUrl:" + initUrl 
//				+ "\n bigkeyUrl:" + bigkeyUrl
				+ "\n readfileUrl:" + readfileUrl 
				+ "\n slowlog:" + slowlog
				+ "\n api:" + api 
				+ "\n bgy:" + bgy);
		List<String> jobs = new ArrayList<String>();
//		jobs.add(initUrl);
//		jobs.add(bigkeyUrl);
		jobs.add(readfileUrl);
		jobs.add(slowlog);
		jobs.add(api);
		jobs.add(bgy);
		PythonInterpreter interpreter = new PythonInterpreter();
		for (String job : jobs) {
			try {
				InputStream in = new FileInputStream(job);
				interpreter.execfile(in);
				log.info(job + "执行完成。");
			} catch (FileNotFoundException e) {
				log.info(e.getMessage());
				return "ERROR";
			} 
		}
		return "SUCCESS";
	}

}
