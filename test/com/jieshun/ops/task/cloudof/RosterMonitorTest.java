package com.jieshun.ops.task.cloudof;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring.xml",
		"classpath:spring-mvc.xml", "classpath:spring-mybatis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class RosterMonitorTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private RosterMonitor monitor;

	@Test
	public void testExecute() {
		try {
			monitor.execute();

		} catch (Exception e) {
			fail("Not yet implemented");
		}

	}

}
