package com.zh.test;

import com.zh.entity.RedisStore;

public class Test {

	public static void main(String[] args) {
		RedisStore redisClient = new RedisStore();
    	//redisClient.set("2", "value_2", 30000L);
		System.out.println("1:"+redisClient.get("1"));
		System.out.println("2:"+redisClient.get("2"));
	}
}
