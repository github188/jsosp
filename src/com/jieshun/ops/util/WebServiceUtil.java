package com.jieshun.ops.util;

public class WebServiceUtil {
	
/*	static String url = "http://autherp.jd.com/services/jdssoService?wsdl";
	
	public WebServiceUtil() {
		// TODO Auto-generated constructor stub
	}
	
	static public String encryption(String string) {
		try {
			Object[] params = new Object[]{string};
			Service service = new Service();
			Call call;
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(url);// 设置服务地址，指明远程调用的类
			call.setEncodingStyle("utf-8");// 设置传入服务端的字符集格式如utf-8等，注意： //
			// Integer,int,Date想要传递必须有此方法,
			call.setOperationName(new QName("http://service.webservice.sso.jd.com/", "encryption"));// 设置远程调用类中的方法
			// 参数必须按照XSD中的顺序添加到Call中，否则则不能与Object数组的值对应，会报错。
			call.addParameter("string", XMLType.XSD_STRING, ParameterMode.IN);// 参数名，参数类
			// //参数模式
//		call.addParameter("arg1", XMLType.XSD_INT, ParameterMode.IN);// IN:输入参数，
			// //OUT输出参数，INOUT输入输出参数
			call.setReturnType(XMLType.XSD_STRING);// 设置返回参数类型
			String m = (String) call.invoke(params);// 调用服务的方法，并传参
			System.out.println(m);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(RemoteException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return string;

	}*/
}
