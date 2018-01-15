package com.zh.entity;

public interface IDataStore {

public boolean set(String key,String value,long exp);
	public String get(String key);
}