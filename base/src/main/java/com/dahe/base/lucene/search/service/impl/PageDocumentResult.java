package com.dahe.base.lucene.search.service.impl;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.dahe.base.lucene.search.service.DocumentResult;


public class PageDocumentResult implements DocumentResult {
	
	private int start;
	private int pageSize;
	private long total;
	private IndexSearcher searcher;
	private ScoreDoc[] scoreDocs = null;
	private int index = -1;
	
	public PageDocumentResult(IndexSearcher searcher, TopDocs topDocs) {
	    super();
	    if(searcher == null){
	    	throw new IllegalArgumentException("searcher is null");
	    }
	    this.searcher = searcher;
	    if(topDocs != null){
	    	scoreDocs = topDocs.scoreDocs;
	    	total = topDocs.totalHits;
	    }
	    
    }

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if(searcher == null || scoreDocs == null || scoreDocs.length < 1){
			return false;
		}
		if(index >= scoreDocs.length - 1){
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Document next() {
		if(hasNext()){
			ScoreDoc sd = scoreDocs[++index];
			try {
	            return searcher.doc(sd.doc);
            } catch (IOException e) {
            }
		}
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getStart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStart(int start) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPageSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPageSize(int pageSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTotal(long total) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasPrePage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNextPage() {
		// TODO Auto-generated method stub
		return false;
	}

}