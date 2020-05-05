package com.dahe.base.lucene.search.entity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.entity.IndexBaseService;
import com.dahe.base.lucene.entity.IndexConfig;
import com.dahe.base.lucene.search.service.Readerable;
import com.dahe.base.lucene.search.service.impl.DefaultReader;
import com.dahe.base.lucene.search.service.impl.PageDocumentResult;

public class SearchService extends IndexBaseService{

	private Map<String, Readerable> readerMap = new HashMap<String, Readerable>();
	
	private Readerable getReader(String indexType){
		/*Set<IndexConfig> indexConfigs = getIndexConfigs();
		if(indexConfigs == null){
			return null;
		}
		Readerable reader = indexerMap.get(indexType);
		if(reader != null){
			return reader;
		}
		synchronized(indexType.intern()){
			reader = indexerMap.get(indexType);//�ٴβ鿴�Ƿ��Ѿ���ʼ��
			if(reader != null){
				return reader;
			}
			for(IndexConfig config : indexConfigs){
				if(!config.getName().equals(indexType)){
					continue;
				}
				reader = new DefaultReader();
				reader.setIndexPath(config.getPath());
				indexerMap.put(config.getName(), reader);
			}
		}
		return reader;*/
		Readerable reader = readerMap.get(indexType);
		if(reader==null) {
			reader = new DefaultReader();
			reader.setIndexPath("D:/lucene_file");
			readerMap.put(indexType, reader);
		}
		return reader;
	}
	
	/**
	 * ����������ͺͲ�ѯ�����������
	 * @param indexType  ��������
	 * @param query      ��ѯ��������Ҫ����ƴװ
	 * @param num        ��ѯ�Ľ�������0��ʼ
	 * @return
	 */
	public PageDocumentResult search(String indexType,Query query, int num){
		return search(indexType,query,null,num,null);
	}
	
	/**
	 * ����������ͺͲ�ѯ����������������ķ�Χ����ͨ��filter��ȷ����Χ
	 * @param indexType  ��������
	 * @param query      ��ѯ��������Ҫ����ƴװ
	 * @param num        ��ѯ�Ľ�������0��ʼ
	 * @param filter     ������
	 * @return
	 */
	public PageDocumentResult search(String indexType,Query query, Filter filter, int num){
		return search(indexType,query,filter,num,null);
	} 
	
	/**
	 * ����������ͺͲ�ѯ����������������ķ�Χ����ͨ��filter��ȷ����Χ
	 * @param indexType  ��������
	 * @param query      ��ѯ��������Ҫ����ƴװ
	 * @param num        ��ѯ�Ľ�������0��ʼ
	 * @param filter     ������
	 * @param sort       �������
	 * @return
	 */
	public PageDocumentResult search(String indexType,Query query, Filter filter, int num,Sort sort){
		Readerable reader = getReader(indexType);
		/*if(reader == null){
			throw new ReaderNotExistException(indexType);
		}*/
		return pageSearch(query, filter, 0,num, sort, reader.getReader());
	}

	/**
	 * ���һ���������ͷ�ҳ�������е����
	 * @param indexType   ��������
	 * @param query       ��ѯ��������Ҫ����ƴװ
	 * @param startPageNo ��ʼҳ�룬��0��ʼ
	 * @param pageSize    һҳ��ݰ���������
	 * @return
	 */
	public PageDocumentResult pageSearch(String indexType,Query query, int startPageNo,int pageSize){
		return pageSearch(indexType,query,null,startPageNo,pageSize,null);
	}
	
	/**
	 * ���һ���������ͷ�ҳ�������е����
	 * @param indexType   ��������
	 * @param query       ��ѯ��������Ҫ����ƴװ
	 * @param startPageNo ��ʼҳ�룬��0��ʼ
	 * @param pageSize    һҳ��ݰ���������
	 * @param filter   ������
	 * @return
	 */
	public PageDocumentResult pageSearch(String indexType,Query query, Filter filter, int startPageNo,int pageSize){
		return pageSearch(indexType,query,filter,startPageNo,pageSize,null);
	} 
	
	/**
	 * ���һ���������ͷ�ҳ�������е����
	 * @param indexType   ��������
	 * @param query       ��ѯ��������Ҫ����ƴװ
	 * @param startPageNo ��ʼҳ�룬��0��ʼ
	 * @param pageSize    һҳ��ݰ���������
	 * @param filter      ������
	 * @param sort        �������
	 * @return
	 */
	public PageDocumentResult pageSearch(String indexType,Query query, Filter filter, int startPageNo,int pageSize,Sort sort){
		Readerable reader = getReader(indexType);
		return pageSearch(query, filter, startPageNo,pageSize, sort, reader.getReader());
	}
	
	
	/**
     * @param query
     * @param filter
     * @param num
     * @param sort
     * @param reader
     * @return
     */
    private PageDocumentResult pageSearch(Query query, Filter filter, int startPageNo,int pageSize, Sort sort, IndexReader reader) {
    	if(query == null){
			return null;
		}
	    IndexSearcher searcher = new IndexSearcher(reader);
		try {
			if(sort == null){
				sort = new Sort();
			}
	        /*TopDocs tds = searcher.search(query,filter, (startPageNo+1)*pageSize,sort);*/
	        TopDocs tds = searcher.search(query,filter,1);
	        ScoreDoc[] hits = tds.scoreDocs;
	        PageDocumentResult result = new PageDocumentResult(searcher, tds);
	        result.setStart(startPageNo * pageSize);
	        result.setPageSize(pageSize);
	        return result;
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
		return null;
    }
    
    /**
     * ���¼�����������
     */
	public void reopenReaders() {
		for(IndexConfig config : getIndexConfigs()) {
			getReader(config.getName()).reopen();
		}
	}
    
    /**
     * ���¼���ָ������
     * @param indexType
     */
	public void reopenReader(String indexType) {
		for(IndexConfig config : getIndexConfigs())  {
			if(config.getName().equals(indexType)){
				getReader(indexType).reopen();
			}
		}
	}
	
}