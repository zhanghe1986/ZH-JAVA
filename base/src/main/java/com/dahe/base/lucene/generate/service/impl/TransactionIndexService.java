package com.dahe.base.lucene.generate.service.impl;
import java.io.IOException;
import java.util.List;
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

import com.dahe.base.lucene.generate.service.Indexable;

/*import com.hikvision.core.search.document.DocumentCreator;*/

/**
 * <p></p>
 * @version V1.0   
 */
public class TransactionIndexService extends IndexService{
	/*private static Logger log = LoggerFactory.getLogger(TransactionIndexService.class);*/
	
	protected Indexable getIndexer(String indexType){
		/*Set<IndexConfig> indexConfigs = getIndexConfigs();
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
				log.info("indexer [" + config.getName() + "] for path[" + config.getPath() + "] is created.");
			}
		}*/
		Indexable indexer = indexerMap.get(indexType);
		if(indexer==null) {
			indexer = new DefaultIndexer();
			indexer.initIndexer("D:/lucene_file", OpenMode.CREATE_OR_APPEND);
			indexerMap.put(indexType, indexer);
		}
		
		return indexer;
	}
	
	public void open(String indexType){
		Indexable indexer = getIndexer(indexType);
		try {
	        indexer.open();
        } catch (IOException e) {
        	/*log.info("open index[" + indexType + "] error.");*/
        	/*throw new IndexerFailureException("open index error.",e);*/
        }
	}
	
	/**
	 * ���������������
	 * @param indexType
	 * @param dc
	 * @throws IndexerFailureException ������ʧ���׳����쳣
	 */
	public void addIndex(String indexType,List<Document> docs){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			/*throw new IndexerNotExistException(indexType);*/
		}

		synchronized(indexType.intern()){
	        /*Analyzer analyzer = dc.getAnalyzer();*/
			/*while(dc.hasNext()){
				Document doc = dc.next();
				indexer.index(doc,analyzer);
			}*/
			for(Document doc : docs) {
				indexer.index(doc,null);
			}
		}
	}
	
	/**
	 * ɾ�����е�����
	 * @throws IndexerFailureException
	 */
	/*public void deleteAll(){
		Set<IndexConfig> indexConfigs = getIndexConfigs();
		if(indexConfigs == null){
			return;
		}
		for(IndexConfig config : indexConfigs){
			try{
				delete(config.getName());
			}catch(IndexerNotExistException e){
				log.info("delete all index[" + config.getName() + "] error.");
			}
		}
	}*/
	
	/**
	 * ɾ���ƶ��������͵���������
	 * @param indexType
	 * @throws IndexerFailureException
	 */
	/*public void delete(String indexType){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.deleteAll();
	        } catch (IOException e) {
	        	log.info("delete index[" + indexType + "] error.");
	        	throw new IndexerFailureException("open index error[" + indexType + "].",e);
	        }
		}
	}*/
	
	/**
	 * ɾ��ָ�����������У�ƥ������ֵ������ȷƥ��
	 * ֻ��ɾ��ͨ��StringField����ɵ�����
	 * @param indexType ��������
	 * @param field     ����
	 * @param value     ��ȷƥ���ֵ
	 */
	public void delete(String indexType,String field,String value){
		if(field.isEmpty() || value.isEmpty()){
			throw new IllegalArgumentException("field or value is null");
		}
		Term term = new Term(field, value);
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			//throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.getWriter().deleteDocuments(term);
	        } catch (IOException e) {
	        	//log.info("delete index [indexType:" + indexType + ",field:" + field + ",value:" + value + "] error.");
	        	//throw new IndexerFailureException("open index error[indexType:" + indexType + ",field:" + field + ",value:" + value + "].",e);
	        }
		}
	}
	
	/**
	 * ɾ��ָ�����������У�ƥ������ֵ������ȷƥ��
	 * ֻ��ɾ��ͨ��LongField����ɵ�����
	 * @param indexType ��������
	 * @param field     ����
	 * @param value     ��ȷƥ���ֵ
	 */
	public void delete(String indexType,String field,Long value){
		if(field.isEmpty() || value == null || value < 0){
			throw new IllegalArgumentException("field or value is null,value must great than 0");
		}
		Query query = NumericRangeQuery.newLongRange(field, value - 1, value + 1, false, false);
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			//throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.getWriter().deleteDocuments(query);
	        } catch (IOException e) {
	        	//log.info("delete index [indexType:" + indexType + ",field:" + field + ",value:" + value + "] error.");
	        	//throw new IndexerFailureException("open index error[indexType:" + indexType + ",field:" + field + ",value:" + value + "].",e);
	        }
		}
	}
	
	/**
	 * ɾ��ָ�����������У�ƥ��query�ж������������
	 * @param indexType ��������
	 * @param query     ƥ��ɾ�����������
	 */
	/*public void delete(String indexType,Query query){
		if(query == null){
			throw new IllegalArgumentException("query is null");
		}
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.getWriter().deleteDocuments(query);
	        } catch (IOException e) {
	        	log.info("delete index  error.");
	        	throw new IndexerFailureException("open index error.",e);
	        }
		}
	}*/
	
	/**
	 * ɾ��ָ�����������У�ƥ������ֵ������ȷƥ�䣬�൱�ڿ���ʹ�ö�����������������and�Ĺ�ϵ��
	 * ֻ��ɾ��ͨ��LongField��StringField����ɵ�����
	 * @param indexType ��������
	 * @param fields    ����
	 * @param values    ��ȷƥ���ֵ ֻ����String  long  Long�е�����һ��
	 */
	/*public void delete(String indexType,String[] fields,Object[] values){
		if(fields == null || values == null 
				|| fields.length == 0 || values.length == 0
				|| fields.length != values.length){
			throw new IllegalArgumentException();
		}
		StringBuilder sb = new StringBuilder();
		BooleanQuery query = new BooleanQuery();
		for(int i = 0; i < fields.length; i++){
			sb.append("field:").append(fields[i]).append(",value:").append(values[i]).append("  ");
			if(StringUtils.isEmpty(fields[i]) || values[i] == null){
				continue;
			}
			Query tempQuery = null;
			if(values[i] instanceof String){
				tempQuery= new TermQuery(new Term(fields[i], values[i].toString()));
			}else if( values[i] instanceof Long){
				tempQuery = NumericRangeQuery.newLongRange(fields[i], (Long)values[i] - 1, (Long)values[i] + 1, false, false);
			}
			if(tempQuery == null){
				continue;
			}
			query.add(tempQuery, Occur.MUST);
		}
		
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			throw new IndexerNotExistException(indexType);
		}
		synchronized(indexType.intern()){
			try {
		        indexer.getWriter().deleteDocuments(query);
	        } catch (IOException e) {
	        	log.info("delete index [" + sb.toString() + "] error.");
	        	throw new IndexerFailureException("open index error[" + sb.toString() + "].",e);
	        }
		}
	}*/

	/**
	 * ����������ʵ���ǽ����������ɾ��Ȼ�������Ҫ���������ͨ��delete��addIndex������
	 * @param indexType
	 * @param deleteField
	 * @param deleteValue
	 * @param dc
	 */
	/*public void update	(String indexType,String deleteField,String deleteValue,DocumentCreator dc){
		delete(indexType,deleteField,deleteValue);
		addIndex(indexType, dc);
	}

	public void close(String indexType){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			return;
		}
		indexer.close();
	}

	public void rollback(String indexType){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			return;
		}
		indexer.rollback();
	}
	
	public void commit(String indexType){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			return;
		}
		indexer.commit();
	}*/
	public void close(String indexType){
		Indexable indexer = getIndexer(indexType);
		if(indexer == null){
			return;
		}
		indexer.close();
	}
}
