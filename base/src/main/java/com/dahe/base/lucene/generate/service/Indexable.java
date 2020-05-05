package com.dahe.base.lucene.generate.service;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
public interface Indexable {
	
	public abstract void initIndexer(String indexPath,OpenMode openMode);
	
	public abstract IndexWriter getWriter();
	
	public abstract void index(IndexWriter writer, Document doc, Analyzer analyzer);
	
	public abstract void index(Document doc);
	
	public abstract void index(Document doc, Analyzer analyzer);
	
	public abstract void open() throws IOException;
	
	public abstract void close();
	
	public abstract void rollback();
	
	public abstract void commit();
	
	public abstract void deleteAll() throws IOException;
	
	public abstract void  reopen() throws IOException;
	
}