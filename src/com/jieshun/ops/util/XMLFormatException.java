package com.jieshun.ops.util;

import com.jieshun.ops.comm.PIMSException;

/**
 * XML格式有误异常
 */
public class XMLFormatException extends PIMSException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5117028161885966646L;

	public XMLFormatException(int code, String msg) {
		super(code, msg);
	}
}
