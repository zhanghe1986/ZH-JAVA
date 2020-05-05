package com.dahe.base.lucene.generate.entity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.entity.IndexBaseService;
import com.dahe.base.lucene.entity.IndexConfig;
import com.dahe.base.lucene.generate.service.Indexable;
import com.dahe.base.lucene.generate.service.impl.DefaultIndexer;

/*import com.hikvision.core.search.document.DocumentCreator;*/


public class IndexService extends IndexBaseService{
	/*private static Logger log = LoggerFactory.getLogger(IndexService.class);*/
	
	protected Map<String, Indexable> indexerMap = new HashMap<String, Indexable>();
	
	protected Indexable getIndexer(String indexType){
		Set<IndexConfig> indexConfigs = getIndexConfigs();
		if(indexConfigs == null){
			return null;
		}
		Indexable indexer = indexerMap.get(indexType);
		if(indexer != null){
			return indexer;
		}
		synchronized(indexType.intern()){
			indexer = indexerMap.get(indexType);
			if(indexer != null){
				return indexer;
			}
			for(IndexConfig config : indexConfigs){
				if(!config.getName().equals(indexType)){
					continue;
				}
				indexer = new DefaultIndexer();
				indexer.initIndexer(config.getPath(), OpenMode.CREATE_OR_APPEND);
				indexerMap.put(config.getName(), indexer);
				indexer.close();
				/*log.info("indexer [" + config.getName() + "] for path[" + config.getPath() + "] is created.");*/
			}
		}
		return indexer;
	}
	
	/**
	 * 
	 * @param indexType
	 * @param dc
	 * @throws IndexerFailureException
	 */
	/*public void addIndex(String indexType,DocumentCreator dc){
		if(dc == null){
			return;
		}
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.open();
		        Analyzer analyzer = dc.getAnalyzer();
				while(dc.hasNext()){
					Document doc = dc.next();
					indexer.index(doc,analyzer);
				}
	        } catch (IOException e) {
	        	log.info("open index[" + indexType + "] error.");
	        	throw new IndexerFailureException("open index error.",e);
	        }finally{
	        	indexer.close();
	        }
		}
	}*/

}