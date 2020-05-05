package com.dahe.base.lucene.search.service.impl;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.search.service.Readerable;


public class DefaultReader implements Readerable {

	/*private static Logger log = LoggerFactory.getLogger(DefaultReader.class);*/
	
	protected String indexPath;
	
	protected IndexReader reader;
	
	protected Directory dir;

	/* (non-Javadoc)
     * @see com.hikvision.core.search.search.Readerable#setIndexPath(java.lang.String)
     */
	public void setIndexPath(String indexPath) {
    	this.indexPath = indexPath;
    }

    /* (non-Javadoc)
     * @see com.hikvision.core.search.search.Readerable#getReader()
     */
    public IndexReader getReader() {
    	if(reader == null){
    		initReader();
    	}
    	
    	return reader;
    }


	/* (non-Javadoc)
     * @see com.hikvision.core.search.search.Readerable#getDirectory()
     */
	public Directory getDirectory(){
		if(dir == null){
			initDirectory();
		}
        return dir;
	}
	
	private void initDirectory(){
		if(indexPath == null || indexPath.equals("")){
			/*log.error("the path which index write into is null!");*/
			throw new IllegalArgumentException("the path which index reader into is null!");
		}
		try {
			dir = FSDirectory.open(new File(indexPath));
        } catch (IOException e) {
        	/*log.error("the path [" + indexPath + "] is not exits!");*/
        	throw new IllegalArgumentException("the path [" + indexPath + "] is not exits!");
        }
	}
	
	private void initReader() {
		Directory directory = getDirectory();
		if(directory == null){
			/*log.error("no path which for search is definded!");*/
			return;
		}
		try {
			reader = DirectoryReader.open(dir);
        } catch (CorruptIndexException e) {
        	 /*log.error(e.getMessage(),e);*/
        } catch (IOException e) {
        	 /*log.error(e.getMessage(),e);*/
        } 
    }
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.search.Readerable#close()
     */
	public void close(){
		try {
			if(reader != null){
				reader.close();
				reader = null;
			}
        } catch (IOException e) {
        	/*log.error(e.getMessage(),e);*/
        }
	}

	@Override
    public void open() {
		if(reader != null){
			reader = null;
		}
		initReader();
    }

	@Override
    public void reopen() {
		if(reader != null && reader.getRefCount() > 0){
			try {
	            reader.close();
            } catch (IOException e) {
            	/*log.error(e.getMessage(),e);*/
            }
			reader = null;
		}
		initReader();
    }
	
}