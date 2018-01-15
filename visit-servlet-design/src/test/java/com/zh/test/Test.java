package com.zh.test;

import com.zh.entity.RequestManager;

public class Test {

	public static void main(String[] args) {
		RequestManager rm = new RequestManager();
    	rm.sendPost("http://ip:port/zh/test", "start=1");
	}
}