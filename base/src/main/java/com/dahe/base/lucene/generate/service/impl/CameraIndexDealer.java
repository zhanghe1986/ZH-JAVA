package com.dahe.base.lucene.generate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;

import com.dahe.base.lucene.generate.entity.IndexerFactory;
import com.dahe.base.lucene.generate.entity.TransactionIndexServiceWrapper;

/*import com.hikvision.core.search.document.DocumentCreator;*/

public class CameraIndexDealer extends AbstractResourceDealer{
    
    public String getIndexType(){
        return "camera";
    }
    
    /* (non-Javadoc)
     * @see com.hikvision.ivms6.module.search.listener.dealer.IResourceNotifyDealer#index()
     */
    @Override
    public void index() {
        TransactionIndexServiceWrapper wrapper = IndexerFactory.getTransactionIndexService(getIndexType());
        writeIndex(wrapper);
    }

    @Override
    protected List<Document> buildDc() {
        
    	List<Document> docList = new ArrayList<Document>();
        Document doc = new Document();
        doc.add(new LongField("id", 1, Store.YES));
        doc.add(new TextField("name", "haha", Store.YES));
        doc.add(new TextField("indexCode", "123456", Store.YES));
        docList.add(doc);
        
        return docList;
    }

	@Override
	public void dealResource(List<Long> idList, String type) {
		// TODO Auto-generated method stub
		
	}
    
}