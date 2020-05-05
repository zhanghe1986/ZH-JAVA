package com.dahe.base.lucene.search.service;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;

public interface Readerable {	
	
	public abstract void setIndexPath(String indexPath);
	
	public abstract IndexReader getReader();
	
	public abstract Directory getDirectory();
	
	public abstract void close();
	
	public abstract void open();
	
	public abstract void  reopen();
}
