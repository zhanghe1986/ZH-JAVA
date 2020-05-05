package com.dahe.base.communication.entity.mina;

import java.util.HashMap;
import java.util.Map;

public class Request {
	private String protocol;
	private Map<String, String> params = new HashMap<String, String>();

	public Request() {
	}

	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setParam(String key, String value) {
		this.params.put(key, value);
	}
}
