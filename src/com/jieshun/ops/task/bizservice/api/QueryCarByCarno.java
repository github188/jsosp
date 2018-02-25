package com.jieshun.ops.task.bizservice.api;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jieshun.ops.model.LogServiceRequestDO;
import com.jieshun.ops.task.bizservice.APIService;
/**
 * 3.3.2.1	查询相似车辆协议
 * 描述: 查询停车场中与用户车牌相似的车辆，供用户确认进行支付。
 * 参数params:JSON格式字符串
 * {
 *	"serviceId":"3c.pay.querycarbycarno",
 *	"requestType":"DATA",
 *	"attributes":{
 *		"parkCode": "",
 *      "carNo": "",
 *		"isCallBack": "",
 *      "notifyUrl": ""
 *	}
 * }
 * @author 刘淦潮
 */
public class QueryCarByCarno  extends APIService{
	
	private String serviceId="3c.pay.querycarbycarno";
	
	private String requestType="DATA";
	
	private String parkCode;
	
	private String licensePlate;
	
	public QueryCarByCarno(String parkCode,String licensePlate){
		this.parkCode=parkCode;
		this.licensePlate=licensePlate;
	}

	@Override
	protected String buildRequestParam() {
		
		// 构造请求参数对象
		JsonObject jsonParam = new JsonObject();
		jsonParam.addProperty("serviceId", serviceId);
		jsonParam.addProperty("requestType", requestType);
		JsonObject attributes = new JsonObject();
		attributes.addProperty("parkCode", parkCode);
		attributes.addProperty("carNo", licensePlate);
		jsonParam.add("attributes", attributes);

		return jsonParam.toString();
	}

	@Override
	protected void extractResult(CloseableHttpResponse response,LogServiceRequestDO lsr) throws Exception {
		// TODO Auto-generated method stub
		lsr.setServiceId(this.serviceId);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {//成功调用
			String results = EntityUtils.toString(response.getEntity());
			JsonObject json=new JsonParser().parse(results).getAsJsonObject();
			int resultCode=json.get("resultCode").getAsInt();
			lsr.setMsgCode(resultCode+"");
			lsr.setSeqId(json.get("seqId")==null?"":json.get("seqId").toString().replace("\"", ""));
			if(resultCode==0){
				lsr.setMsgDesc("请求成功！");
			}else{
				lsr.setMsgDesc(json.get("message").toString());
			}
		} else {
			lsr.setMsgCode(statusCode+"");
			lsr.setMsgDesc("网络异常！");
		}
		
	}


}
