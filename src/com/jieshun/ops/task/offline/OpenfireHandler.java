package com.jieshun.ops.task.offline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.jieshun.ops.master.model.OpenfireDO;

public class OpenfireHandler implements IOpenfireService{

	private static String type = "xml";


	private OpenfireDO openfireDo;

	public OpenfireHandler(OpenfireDO openfireDo) {
		this.openfireDo = openfireDo;
	}

	@Override
	public boolean isOnline(String jid) {
		

		try {
			URL oUrl = new URL(this.openfireDo.getPrecenceUrl()+"?jid="+jid+"@"+this.openfireDo.getOfHostName()+"&type="+this.type);
			URLConnection oConn = oUrl.openConnection();
			if (oConn != null) {
				BufferedReader oIn = new BufferedReader(new InputStreamReader(
						oConn.getInputStream()));
				if (null != oIn) {
					String strFlag = oIn.readLine();
					oIn.close();
					if (strFlag.indexOf("type=\"unavailable\"") >= 0) {
						return false;
					}
					if (strFlag.indexOf("type=\"error\"") >= 0) {
						return false;
					} else  {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
