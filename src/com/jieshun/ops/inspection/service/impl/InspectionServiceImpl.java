package com.jieshun.ops.inspection.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import com.jieshun.ops.inspection.model.Inspection;
import com.jieshun.ops.inspection.service.IInspectionService;
import com.jieshun.ops.util.StringUtils;

/**
 * 一键巡检
 * @author yt
 *
 */
@Service("inspectionService")
public class InspectionServiceImpl implements IInspectionService {
	
	private static final Logger log = Logger.getLogger(InspectionServiceImpl.class);
	
	@Override
	public String doPythonScript(List<Inspection> list) {
		if (list != null && list.size() > 0) {
			PythonInterpreter interpreter = new PythonInterpreter();
			log.info("自动巡检功能开始，读取配置参数，任务数量[" + list.size() + "]");
			String result = "";
			String a = "";
			for (Inspection o : list) {
				log.info("自动巡检["+o.getName()+"]，开始执行。");
				a = StringUtils.getPropertiesAttr(o.getAddress());
				try {
					if (!StringUtils.isEmpty(a)) {
						InputStream in = new FileInputStream(a);
						interpreter.execfile(in);
						log.info(o.getName() + "执行完成。");
						result += o.getName() + ":" + "SUCCESS;";
					} else {
						log.info(o.getName() + "读取文件路径失败。");
						result += o.getName() + ":" + "FAIL;";
					}
				} catch (FileNotFoundException e) {
					log.info(e.getMessage());
					return "ERROR";
				} 
			}
			return result;
		} else {
			return "ERROR";
		}
	}
	
}
