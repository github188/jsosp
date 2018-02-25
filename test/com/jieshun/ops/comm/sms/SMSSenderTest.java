package com.jieshun.ops.comm.sms;

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
public class SMSSenderTest  extends
AbstractTransactionalJUnit4SpringContextTests{

	@Autowired
	private SMSSender sender;
	@Test
	public void testSendMessage() {
		String main="sadfads,sdfasd,";
		main=main.substring(0, main.lastIndexOf(",") - 1);
		//SMSSender sender=new SMSSender();
		sender.sendMessage("13823255622", main);
		fail("Not yet implemented");
	}
	public void test()
	{
		
	}

}
