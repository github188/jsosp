package com.jieshun.ops.task.wanke;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:spring.xml",
		"classpath:spring-mvc.xml", "classpath:spring-mybatis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DataUploadMonitorTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	@Autowired
	private DataUploadMonitor monitor;
	
	@Test
	public void testExecute() {
		fail("Not yet implemented");
		
	}

}
