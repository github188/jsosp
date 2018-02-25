package com.jieshun.ops.task.bizservice.api;

import java.util.Properties;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jieshun.ops.model.LogServiceRequestDO;
import com.jieshun.ops.task.bizservice.APIService;
/**
 * 3.3.2.2	生成订单协议
 * 描述:根据停车场编号，卡ID，生成停车支付订单信息。
 * 参数params:JSON格式字符串
 * {
 *	"serviceId":"3c.pay.createorderbycarno",
 *	"requestType":"DATA",
 *	"attributes":{
 *		"businesserCode": "",
 *		"parkCode": "",
 *		"orderType": "",
 *     	"carNo": ""
 *		}
 * }
 * @author 刘淦潮
 *
 */
public class CreateOrderByCarno extends APIService {
	
	private String serviceId="3c.pay.querycarbycarno";
	
	private String requestType="DATA";
	
	private Properties param;
	
	public CreateOrderByCarno(Properties param){
		this.param=param;
		Properties loginProp=new Properties();
		loginProp.setProperty("cid", this.param.getProperty("businesserCode"));
		loginProp.setProperty("usr", this.param.getProperty("businesserCode"));
		loginProp.setProperty("psw", this.param.getProperty("psw"));
		loginProp.setProperty("signKey", "");
		loginProp.setProperty("v", "2");
		this.setLoginProperties(loginProp);
	}

	@Override
	protected String buildRequestParam() {
		
				// 构造请求参数对象
		JsonObject jsonParam = new JsonObject();
		jsonParam.addProperty("serviceId", serviceId);
		jsonParam.addProperty("requestType", requestType);
		JsonObject attributes = new JsonObject();
		attributes.addProperty("businesserCode", param.getProperty("businesserCode"));
		attributes.addProperty("parkCode", param.getProperty("parkCode"));
		attributes.addProperty("orderType", param.getProperty("orderType"));
		attributes.addProperty("carNo", param.getProperty("carNo"));
		jsonParam.add("attributes", attributes);

		return jsonParam.toString();
	}

	@Override
	protected void extractResult(CloseableHttpResponse response,
			LogServiceRequestDO lsr)
			throws Exception {
		// TODO Auto-generated method stub
		lsr.setServiceId(this.serviceId);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {//成功调用
			String results = EntityUtils.toString(response.getEntity());
			JsonObject json=new JsonParser().parse(results).getAsJsonObject();
			int resultCode=json.get("resultCode").getAsInt();
			lsr.setMsgCode(resultCode+"");
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
