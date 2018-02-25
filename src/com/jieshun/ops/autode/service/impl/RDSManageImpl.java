package com.jieshun.ops.autode.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse.DBInstanceAttribute;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse.DBInstanceAttribute.DBInstanceType;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesRequest;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse.DBInstance;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jieshun.ops.autode.service.IRDSManageService;
import com.jieshun.ops.util.DBInstanceTypeEnum;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * @author huozhuangning
 * @date 2018年1月29日 上午11:22:14
 */
@Service("rdsManageService")
public class RDSManageImpl implements IRDSManageService {
	
	private final static int PageSize = 30;
    //APP KEY
    private final static String regionId = "cn-shenzhen";
    // APP密钥
    private final static String accessKeyId = "LTAIEyyoyp54iANB";
    //API域名
    private final static String secret = "iVTELHy1GqjxdGefpFoCvHsY1gQyzQ";

	private static final Logger logger = Logger.getLogger(RDSManageImpl.class.getName());

	@Override
	public String list(int pageIndex,int pageSize) {
		JsonObject response = new JsonObject();
		try {
			int PageNumber = 1;
			logger.info("接收到查询rds列表的请求.........");
			// 资源在某个region内，必须指定region
    		
			DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
    		// 必填项,如:rm-wz934c9ky80f90038
    		describeDBInstancesRequest.setRegionId("cn-shenzhen");
    		describeDBInstancesRequest.setEngine("MySQL");
    		describeDBInstancesRequest.setPageSize(PageSize);
    		describeDBInstancesRequest.setPageNumber(PageNumber);
    		IClientProfile profile= DefaultProfile
    				.getProfile(regionId,accessKeyId,secret);
    		IAcsClient client = new DefaultAcsClient(profile);
    		
    		DescribeDBInstancesResponse res= client.getAcsResponse(describeDBInstancesRequest);
    		// 重启实例当只返回RequestId公共参数是，表示接口调用成功，重启成功。
//    		List<DBInstance> list = response.getItems();
    		int pageCount = res.getPageRecordCount();
    		int totalCount = res.getTotalRecordCount();
    		System.out.println(pageCount+","+totalCount);
    		int counts = totalCount/pageCount;
    		JsonArray result = new JsonArray();
    		for (int i = 0; i < counts; i++) {
    			describeDBInstancesRequest.setPageNumber(PageNumber);
    			DescribeDBInstancesResponse resp= client.getAcsResponse(describeDBInstancesRequest);
    			List<DBInstance> list = resp.getItems();
	    		for (DBInstance dbInstance : list) {
	    			String DBInstanceId = dbInstance.getDBInstanceId();
	    			String DBInstanceDescription = dbInstance.getDBInstanceDescription();
	    			
    			
    				JsonObject jsonObject = new JsonObject();
    				jsonObject.addProperty("desc", DBInstanceDescription);
    				jsonObject.addProperty("name", DBInstanceId);
    				
    				DescribeDBInstanceAttributeRequest describeDBInstanceAttributeRequest = new DescribeDBInstanceAttributeRequest();
    				// 必填项,如:rm-wz934c9ky80f90038
    				describeDBInstanceAttributeRequest.setRegionId("cn-shenzhen");
    				describeDBInstanceAttributeRequest.setDBInstanceId(DBInstanceId);
    				// 资源在某个region内，必须指定region
    				
    				DescribeDBInstanceAttributeResponse resp1= client.getAcsResponse(describeDBInstanceAttributeRequest);
    				// 重启实例当只返回RequestId公共参数是，表示接口调用成功，重启成功。
    				DBInstanceAttribute dbInstanceAttribute = resp1.getItems().get(0);
					String DBInstanceId1 = dbInstanceAttribute.getDBInstanceId();
					DBInstanceType DBInstanceType = dbInstanceAttribute.getDBInstanceType();
					String ConnectionString = dbInstanceAttribute.getConnectionString();
					String Port = dbInstanceAttribute.getPort();
					long DBInstanceMemory = dbInstanceAttribute.getDBInstanceMemory();//单位:M
					int DBInstanceStorage = dbInstanceAttribute.getDBInstanceStorage();//单位:GB
					
					String DBInstanceTypeName = "";
					for (DBInstanceTypeEnum s : DBInstanceTypeEnum.values()) {
						if(s.toString().equals(DBInstanceType.getStringValue().toUpperCase())){
							DBInstanceTypeName = s.getContext();
							break;
						}
					}
					jsonObject.addProperty("clustername", DBInstanceTypeName);
					jsonObject.addProperty("ip", ConnectionString);
					jsonObject.addProperty("port", Port);
					jsonObject.addProperty("memory", DBInstanceMemory+"M");
					jsonObject.addProperty("storage", DBInstanceStorage+"GB");
    				
    				result.add(jsonObject);
	    		}
    			PageNumber++;
			}
    		
			
			response.addProperty("counts", totalCount);
			response.addProperty("returnCode", 0);
			response.addProperty("message", "查询成功");
			response.add("result", result);
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}

	@Override
	public String exportrds(HttpServletRequest request) {
		JsonObject response = new JsonObject();
		try {
			String path = "excels" + System.getProperty("file.separator") +  System.currentTimeMillis() + ".xls";
			FileOutputStream os;
			os = new FileOutputStream(new File(request.getSession()
					.getServletContext().getRealPath("")
					+ System.getProperty("file.separator") + path));
			// 创建工作薄
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			// 创建新的一页
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);
			Label condition0 = new Label(0, 0, "#");
			sheet.addCell(condition0);
			Label condition1 = new Label(1, 0, "集群名称");
			sheet.addCell(condition1);
			Label condition2 = new Label(2, 0, "实例名称");
			sheet.addCell(condition2);
			Label condition3 = new Label(3, 0, "IP");
			sheet.addCell(condition3);
			Label condition4 = new Label(4, 0, "端口");
			sheet.addCell(condition4);
			Label condition5 = new Label(5, 0, "内存");
			sheet.addCell(condition5);
			Label condition6 = new Label(6, 0, "存储空间");
			sheet.addCell(condition6);
			Label condition7 = new Label(7, 0, "实例描述");
			sheet.addCell(condition7);
						
			int PageNumber = 1;
			logger.info("接收到导出rds列表的请求.........");
			// 资源在某个region内，必须指定region
    		
			DescribeDBInstancesRequest describeDBInstancesRequest = new DescribeDBInstancesRequest();
    		// 必填项,如:rm-wz934c9ky80f90038
    		describeDBInstancesRequest.setRegionId("cn-shenzhen");
    		describeDBInstancesRequest.setEngine("MySQL");
    		describeDBInstancesRequest.setPageSize(PageSize);
    		describeDBInstancesRequest.setPageNumber(PageNumber);
    		IClientProfile profile= DefaultProfile
    				.getProfile(regionId,accessKeyId,secret);
    		IAcsClient client = new DefaultAcsClient(profile);
    		
    		DescribeDBInstancesResponse res= client.getAcsResponse(describeDBInstancesRequest);
    		// 重启实例当只返回RequestId公共参数是，表示接口调用成功，重启成功。
//    		List<DBInstance> list = response.getItems();
    		int pageCount = res.getPageRecordCount();
    		int totalCount = res.getTotalRecordCount();
    		System.out.println(pageCount+","+totalCount);
    		int counts = totalCount/pageCount;
    		int count=0;
    		for (int i = 0; i < counts; i++) {
    			describeDBInstancesRequest.setPageNumber(PageNumber);
    			DescribeDBInstancesResponse resp= client.getAcsResponse(describeDBInstancesRequest);
    			List<DBInstance> list = resp.getItems();
	    		for (DBInstance dbInstance : list) {
	    			String DBInstanceId = dbInstance.getDBInstanceId();
	    			String DBInstanceDescription = dbInstance.getDBInstanceDescription();
    			
    				JsonObject jsonObject = new JsonObject();
    				jsonObject.addProperty("desc", DBInstanceDescription);
    				jsonObject.addProperty("name", DBInstanceId);
    				
    				DescribeDBInstanceAttributeRequest describeDBInstanceAttributeRequest = new DescribeDBInstanceAttributeRequest();
    				// 必填项,如:rm-wz934c9ky80f90038
    				describeDBInstanceAttributeRequest.setRegionId("cn-shenzhen");
    				describeDBInstanceAttributeRequest.setDBInstanceId(DBInstanceId);
    				// 资源在某个region内，必须指定region
    				
    				DescribeDBInstanceAttributeResponse resp1= client.getAcsResponse(describeDBInstanceAttributeRequest);
    				// 重启实例当只返回RequestId公共参数是，表示接口调用成功，重启成功。
    				DBInstanceAttribute dbInstanceAttribute = resp1.getItems().get(0);
					String DBInstanceId1 = dbInstanceAttribute.getDBInstanceId();
					DBInstanceType DBInstanceType = dbInstanceAttribute.getDBInstanceType();
					String ConnectionString = dbInstanceAttribute.getConnectionString();
					String Port = dbInstanceAttribute.getPort();
					long DBInstanceMemory = dbInstanceAttribute.getDBInstanceMemory();//单位:M
					int DBInstanceStorage = dbInstanceAttribute.getDBInstanceStorage();//单位:GB
					
					String DBInstanceTypeName = "";
					for (DBInstanceTypeEnum s : DBInstanceTypeEnum.values()) {
						if(s.toString().equals(DBInstanceType.getStringValue().toUpperCase())){
							DBInstanceTypeName = s.getContext();
							break;
						}
					}
					jsonObject.addProperty("clustername", DBInstanceTypeName);
					jsonObject.addProperty("ip", ConnectionString);
					jsonObject.addProperty("port", Port);
					jsonObject.addProperty("memory", DBInstanceMemory+"M");
					jsonObject.addProperty("storage", DBInstanceStorage+"GB");
					
					sheet.addCell(new Label(0, count+1, String.valueOf(count)));
					sheet.addCell(new Label(1, count+1, DBInstanceTypeName));
					sheet.addCell(new Label(2, count+1, DBInstanceId));
					sheet.addCell(new Label(3, count+1, ConnectionString));
					sheet.addCell(new Label(4, count+1, Port));
					sheet.addCell(new Label(5, count+1, String.valueOf(DBInstanceMemory)+"M"));
					sheet.addCell(new Label(6, count+1, String.valueOf(DBInstanceStorage)+"GB"));
					sheet.addCell(new Label(7, count+1, DBInstanceDescription));
    				count++;
	    		}
    			PageNumber++;
			}
			
			workbook.write();
			workbook.close();
			os.close();
			
			response.addProperty("returnCode", 0);
			response.addProperty("url", path.replace("\\", "/"));
			response.addProperty("message", "查询成功");
			
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			response.addProperty("returnCode", 1);
			response.addProperty("message", "查询异常");
			return response.toString();
		}
	}


}
