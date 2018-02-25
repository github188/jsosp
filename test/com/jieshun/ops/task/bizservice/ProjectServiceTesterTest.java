package com.jieshun.ops.task.bizservice;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;

@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring.xml",
		"classpath:spring-mvc.xml", "classpath:spring-mybatis.xml","classpath:spring-job.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectServiceTesterTest {

	@Autowired
	private CloudServiceMonitor monitor;
	
	@Test
	public void testRun() {
		//String str="{'signKey':'dca52e9e8d95fb40945b79f92e9e25a4', 'v':'2', 'psw':'888888', 'name':'岳阳市临港产业新区岳阳碧桂园', 'usr':'880073001005158', 'code':'0000005234', 'cid':'880073001005158'}";
		String str="{'code':'0000001907','name':'下沙京基滨河时代(商业)','cid':'880075501001835','usr':'880075501001835','psw':'147741','v':'2','signKey':'f71db2f8ea5b534b4c58c410cff0ed25'}";
		JSONObject jsonObj = JSONObject.parseObject(str);
		ProjectServiceTester tester=new ProjectServiceTester(monitor,jsonObj);
		tester.run();
		fail("Not yet implemented");
	}

}
