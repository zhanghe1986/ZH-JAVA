package com.zh.hessian.server;

public class HelloServiceImpl implements HelloService{

	@Override
	public String helloWorld(String message) {
		return "hello," + message;
	}
}
