package com.dahe.base.lucene.generate.entity;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import com.dahe.base.lucene.generate.service.impl.TransactionIndexService;

/*import com.hikvision.core.search.TransactionIndexService;
import com.hikvision.core.search.document.DocumentCreator;
import com.hikvision.swdf.core.ServiceLocator;
import com.ivms6.core.util.CollectionUtils;*/


/**
 * <p></p>
 * @version V1.0 
 */
public class TransactionIndexServiceWrapper {
	
	private String indexType;
	private static TransactionIndexService transactionIndexService = new TransactionIndexService();
	
	public TransactionIndexServiceWrapper(String indexType){
		this.indexType = indexType;
	}

    public void addIndex(List<Document> docs) {
	    transactionIndexService.addIndex(indexType, docs);
    }

    /*public void delete(Query query) {
	    transactionIndexService.delete(indexType, query);
    }

    public void delete(String field, String value) {
	    transactionIndexService.delete(indexType, field, value);
    }

    public void delete(String[] fields, Object[] values) {
	    transactionIndexService.delete(indexType, fields, values);
    }*/

    public <T> void delete(String field, List<T> values) {
    	if(values==null || values.isEmpty()){
    		return;
    	}
    	for(T value : values){
    	    if (value instanceof Long) {
    	        transactionIndexService.delete(indexType, field, (Long)value);
            } else {
                transactionIndexService.delete(indexType, field, String.valueOf(value));
            }
    	}
    }

    /*public void deleteAll() {
	    transactionIndexService.delete(indexType);
    }

    public void open() {
	    transactionIndexService.open(indexType);
    }
    
    public void close(){
        transactionIndexService.close(indexType);
    }

    public void rollback() {
	    transactionIndexService.rollback(indexType);
    }

    public void commit() {
        transactionIndexService.commit(indexType);
    }*/

    /*public void update(String deleteField, String deleteValue, DocumentCreator dc) {
	    transactionIndexService.update(indexType, deleteField, deleteValue, dc);
    }*/
    public void open() {
	    transactionIndexService.open(indexType);
    }
    
    public void close(){
        transactionIndexService.close(indexType);
    }
}
