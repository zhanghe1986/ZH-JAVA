package com.dahe.base.lucene.entity;
public class IndexConfig {

	private String name;
	
	private String path = "D:/lucene_file";
	
    public String getName() {
    	return name;
    }
	
    public void setName(String name) {
    	this.name = name;
    }
	
    public String getPath() {
    	return path;
    }
	
    public void setPath(String path) {
    	this.path = path;
    }
}