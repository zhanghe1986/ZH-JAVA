package com.dahe.base.lucene.generate.entity;

public class IndexerFactory {

	public static IndexServiceWrapper getIndexService(String indexType) {
		return new IndexServiceWrapper(indexType);
	}
	
	public static TransactionIndexServiceWrapper getTransactionIndexService(String indexType) {
		return new TransactionIndexServiceWrapper(indexType);
	}
}
