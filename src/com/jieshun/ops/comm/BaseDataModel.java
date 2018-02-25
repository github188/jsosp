package com.jieshun.ops.comm;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 领域对象接口，所以model类都要实现此接口
 * @author Administrator
 *
 */
public abstract class BaseDataModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;

	public JsonObject toJsonObject(){
		Gson gson=new Gson();
		return gson.toJsonTree(this).getAsJsonObject();
	}
	
	/**
	 * 转成json字符串
	 */
	@Override
	public String toString(){
		return new Gson().toJson(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
