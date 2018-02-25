package com.jieshun.ops.util;

public enum PLATFORM {
	ZUYONG("租用平台"), JSSM("扫码平台"), QST("全生态"), JSZT("展厅"), CLOUD("云平台"), WANKE("万科物业"), JINDI("金地物业"), LVCHENG(
			"绿城物业"), BGY("碧桂园"), WHGA("武汉公安"), LGJB("蓝光嘉宝"), BNWY("宝能物业"), YHWY("银海物业"), TJWY("天健物业"), MYJX("绵阳嘉兴");

	private String context;

	public String getContext() {
		return this.context;
	}

	private PLATFORM(String context) {
		this.context = context;
	}

}
