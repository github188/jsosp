package com.jieshun.ops.master.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jieshun.ops.master.model.PersonDO;

@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:spring.xml",
		"classpath:spring-mvc.xml", "classpath:spring-mybatis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonDaoTest  extends
AbstractTransactionalJUnit4SpringContextTests{

	private static final Logger logger = Logger
			.getLogger(PersonDaoTest.class);
	@Autowired
	private  PersonDao personDao;
	
	@Test
	public void testGetReceiptorsByPlatform() {
		try{
			List<PersonDO> list=personDao.getReceiptorsByPlatform("wanke");
			for(PersonDO pd:list){
				logger.info(pd.toString());
			}
			Assert.assertTrue(list!=null);
		}catch(Exception e){
			logger.debug("testGetReceiptorsByPlatform", e);
			fail("Not yet implemented");
		}
	}

	@Test
	public void testGetReceiptorsByProject() {
		fail("Not yet implemented");
	}

}
