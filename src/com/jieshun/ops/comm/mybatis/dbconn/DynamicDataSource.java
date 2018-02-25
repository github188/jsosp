package com.jieshun.ops.comm.mybatis.dbconn;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		 return DataSourceContextHolder.getDbType();  
	}

}
