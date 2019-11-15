package com.wantao.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wantao
 * @date 2019-01-31 20:05
 * @description: json的通用返回类
 */
public class Message {
	// 状态码（自定义）200 成功 404 失败
	private int code;
	private String msg;
	// 服务器要返回给浏览器的数据
	private Map<String, Object> data = new HashMap<String, Object>();


	public static Message success() {
		Message message = new Message();
		message.setCode(200);
		message.setMsg("处理成功");
		return message;
	}

	public static Message fail() {
		Message message = new Message();
		message.setCode(404);
		message.setMsg("处理失败");
		return message;
	}
	//定义快捷添加信息的方法
	public Message add(String key, Object value) {
		//以下两种形式任选其一
		this.data.put(key, value);
		//this.getData().put(key, value);
		return this;
	}

	public Message() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
