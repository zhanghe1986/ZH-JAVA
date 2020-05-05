package com.dahe.base.lucene.generate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.generate.entity.TransactionIndexServiceWrapper;
import com.dahe.base.lucene.generate.service.IResourceDealer;

/**
 * Resource handler base class
 */
public abstract class AbstractResourceDealer implements IResourceDealer {

    protected List<Long> addIdsList = new ArrayList<Long>();
    
    protected List<Long> deleteIdsList = new ArrayList<Long>();

    protected Analyzer analyzer;    //����lucene-core jar
    
    /*protected static IQueryInfoForIndexDao queryInfoForIndexDao;*/

    /*private static Logger log = LoggerFactory.getLogger(AbstractResourceDealer.class);*/
    
    private String resourceType;
    

	/*
     * (non-Javadoc)
     * 
     * @see com.hikvision.ivms6.module.search.listener.dealer.IResourceNotifyDealer#addResource()
     */
    @Override
    public void addResource(long id) {
        addIdsList.add(id);
    }

    @Override
	public void addResource(long id, String resourceType) {
		addResource(id);
		this.resourceType = resourceType;
	}

	public long generateIndexId(long id){
        return id;
    }
    
    /*
     * �����¼Ϊ�����������͡���ɾ������
     */
    /*public void dealResource(List<Long> idList, DealType type) {
        switch (type) {
            case ADD:
                for (Long id : idList) {
                    addIdsList.add(id);
                }
                break;
            case DELETE:
                for (Long id : idList) {
                    deleteIdsList.add(generateIndexId(id));
                }
                break;
            case MODIFY:
                for (Long id : idList) {
                    addIdsList.add(id);
                    deleteIdsList.add(generateIndexId(id));
                }
                break;
        }
    }*/

    /*
     * (non-Javadoc)
     * 
     * @see com.hikvision.ivms6.module.search.listener.dealer.IResourceNotifyDealer#index()
     */
    @Override
    public abstract void index();
    
    /*
     * ����ʵ��
     */
    protected abstract List<Document> buildDc();

    protected void writeIndex(TransactionIndexServiceWrapper wrapper) {
        if (wrapper == null) {
            /*log.warn("TransactionIndexServiceWrapper is null,index type:" + getIndexType());*/
            return;
        }
        boolean indexChanged = false;
        wrapper.open();
        try {
            /*if (deleteIdsList != null && !deleteIdsList.isEmpty()) {
                wrapper.delete("id", deleteIdsList);
                indexChanged = true;
            }*/
            /*if (addIdsList != null && !addIdsList.isEmpty()) {*/
            	List<Document> docs = buildDc();
                if (docs != null && !docs.isEmpty()) {
                    wrapper.addIndex(docs);
                    indexChanged = true;
                }
            /*}*/
        } finally {
            wrapper.close();
        }
        /*if (indexChanged) {
            IndexManager.reopenIndex(getIndexType());
        }*/
    }
    
    /*protected static IQueryInfoForIndexDao getQueryInfoForIndexDao() {
        if (queryInfoForIndexDao == null) {
            queryInfoForIndexDao = ServiceLocator.findService("queryInfoForIndexDao");
        }
        return queryInfoForIndexDao;
    }*/

}