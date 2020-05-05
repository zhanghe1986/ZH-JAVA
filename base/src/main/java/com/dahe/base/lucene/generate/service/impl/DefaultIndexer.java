package com.dahe.base.lucene.generate.service.impl;
import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.generate.service.Indexable;

public class DefaultIndexer implements Indexable {

	/*private static Logger log = LoggerFactory.getLogger(DefaultIndexer.class);*/
	
	protected String indexPath;
	
	protected IndexWriter writer;   //����lucenec-core jar
	
	protected Directory dir;
	
	protected OpenMode openMode;

	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#setIndexPath(java.lang.String)
     */
	public void setIndexPath(String indexPath) {
    	this.indexPath = indexPath;
    }

    /* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#getWriter()
     */
    public IndexWriter getWriter() {
    	if(writer == null){
    		initWriter(openMode);
    	}
    	return writer;
    }

	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#getDirectory()
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
			throw new IllegalArgumentException("the path which index write into is null!");
		}
		try {
			dir = FSDirectory.open(new File(indexPath));
        } catch (IOException e) {
        	/*log.error("the path [" + indexPath + "] is not exits!");*/
        	throw new IllegalArgumentException("the path [" + indexPath + "] is not exits!");
        }
	}
	
	
	protected void initWriter(OpenMode openMode){
		Directory directory = getDirectory();
		if(directory == null){
			/*log.error("no path which index write into is definded!");*/
			return;
		}
		try {
			if(writer == null){
				Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			    IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			    if(openMode == null){
			    	openMode = OpenMode.CREATE_OR_APPEND;
			    }
			    iwc.setOpenMode(openMode);
			    writer = new IndexWriter(directory,iwc);
			}
        } catch (CorruptIndexException e) {
	        /*log.error(e.getMessage());*/
        	System.out.println(e.getMessage());
        } catch (LockObtainFailedException e) {
        	/*log.error(e.getMessage());*/
        	System.out.println(e.getMessage());
        } catch (IOException e) {
        	/*log.error(e.getMessage());*/
        	System.out.println(e.getMessage());
        }
        if(writer == null){
        	/*log.error("writer init error!");*/
        }
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#createIndex(org.apache.lucene.index.IndexWriter, org.apache.lucene.document.Document, org.apache.lucene.analysis.Analyzer)
     */
	public void index(IndexWriter writer,Document doc,Analyzer analyzer){
		if(writer == null){
			/*throw new IndexCreateException("writer has not bean init.");*/
		}
		try {
			if(analyzer == null){
				writer.addDocument(doc);
			}else{
				writer.addDocument(doc,analyzer);
			}
        } catch (CorruptIndexException e) {
        	/*log.error(e.getMessage(),e);
        	throw new IndexCreateException("write index error.",e);*/
        } catch (IOException e) {
        	/*log.error(e.getMessage(),e);
        	throw new IndexCreateException("write index error.",e);*/
        }
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#createIndex(org.apache.lucene.document.Document)
     */
	public void index(Document doc){
		index(doc,null);
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#createIndex(org.apache.lucene.document.Document, org.apache.lucene.analysis.Analyzer)
     */
	public void index(Document doc,Analyzer analyzer){
		index(writer,doc,analyzer);
	}
	
	public void open() throws IOException{
		initWriter(openMode);
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#close()
     */
	public void close(){
		try {
			if(writer != null){
				writer.close();
	            writer = null;
			}
        } catch (CorruptIndexException e) {
        	/*log.error(e.getMessage(),e);*/
        } catch (IOException e) {
        	/*log.error(e.getMessage(),e);*/
        }
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#rollback()
     */
	public void rollback() {
		try {
	        writer.rollback();
        } catch (IOException e) {
        	/*log.error(e.getMessage(),e);*/
        }
	}
	
	/* (non-Javadoc)
     * @see com.hikvision.core.search.index.Indexable#commit()
     */
	public void commit(){
		try {
	        writer.commit();
        } catch (CorruptIndexException e) {
        	/*log.error(e.getMessage(),e);*/
        } catch (IOException e) {
        	/*log.error(e.getMessage(),e);*/
        }
	}

	@Override
    public void initIndexer(String indexPath, OpenMode openMode) {
		this.indexPath = indexPath;
		this.openMode = openMode;
		initWriter(openMode);
    }
	
	public void  reopen() throws IOException{
		if(writer != null){
			writer.close();
		}
		writer = null;
		initWriter(openMode);
	}

	@Override
    public void deleteAll() throws IOException {
		if(writer == null){
			/*throw new IndexCreateException("writer has not bean init.");*/
		}
		writer.deleteAll();
    }
	
}
