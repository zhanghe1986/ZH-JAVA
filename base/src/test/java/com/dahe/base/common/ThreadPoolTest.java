package com.dahe.base.common;

import com.dahe.base.common.util.BaseThreadPoolExecutor;

public class ThreadPoolTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BaseThreadPoolExecutor.execute(new FirstIOThread("1", "1"));
		BaseThreadPoolExecutor.execute(new SecondIOThread("2", "2"));
		BaseThreadPoolExecutor.submit(new FirstIOThread("1-1", "1-1"));
		BaseThreadPoolExecutor.submit(new SecondIOThread("2-2", "2-2"));
	}
}


/* Threads are as follow */
class FirstIOThread implements Runnable {
	private String a;
	private String b;
	
	public FirstIOThread(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void run() {
		long beginTime = System.currentTimeMillis();
		System.out.println("[FirstIOThread] a:" + a + " b" + b);
		long executeTime = System.currentTimeMillis() - beginTime;
		System.out.println("[FirstIOThread] First io-thread execute time:" + executeTime);
	}
}

class SecondIOThread implements Runnable {
	private String a;
	private String b;
	
	public SecondIOThread(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void run() {
		long beginTime = System.currentTimeMillis();
		System.out.println("[SecondIOThread] a:" + a + " b" + b);
		long executeTime = System.currentTimeMillis() - beginTime;
		System.out.println("[SecondIOThread] Second io-thread execute time:" + executeTime);
	}
}
