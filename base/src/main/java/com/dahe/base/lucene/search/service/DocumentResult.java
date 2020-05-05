package com.dahe.base.lucene.search.service;
import java.util.Iterator;

import org.apache.lucene.document.Document;

public interface DocumentResult extends Iterator<Document>{
	
	public abstract long getTotal();
	
	public int getStart();
	
    public void setStart(int start);
	
    public int getPageSize();
	
    public void setPageSize(int pageSize);
	
    public void setTotal(long total);

	public boolean hasPrePage();
	
	public boolean hasNextPage();
}