package com.dahe.base.lucene.generate.entity;

public class IndexServiceWrapper {

	private String indexType;
	private IndexService indexService = new IndexService();
	
	public IndexServiceWrapper(String indexType){
		this.indexType = indexType;
/*		indexService = ServiceLocator.findService("indexService");*/
	}
	
	/*public void addIndex(DocumentCreator dc) {
		indexService.addIndex(indexType, dc);
	}*/
	
	/*public void delete(Query query) {
		indexService.delete()
	}*/
}
