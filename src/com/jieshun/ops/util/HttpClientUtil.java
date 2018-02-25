package com.jieshun.ops.util;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("deprecation")
public class HttpClientUtil {
	
	public static DefaultHttpClient getClient() {
		
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);

		HttpProtocolParams.setUseExpectContinue(params, true);

		SchemeRegistry schReg = new SchemeRegistry();

		schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

		schReg.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 433));

		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}
	
}
