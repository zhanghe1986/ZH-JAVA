package com.dahe.base.lucene.entity;
import java.util.Set;

public class IndexBaseService {

	private Set<IndexConfig> indexConfigs;
	
    public Set<IndexConfig> getIndexConfigs() {
    	return indexConfigs;
    }
	
    public void setIndexConfigs(Set<IndexConfig> indexConfigs) {
    	this.indexConfigs = indexConfigs;
    }
	
}