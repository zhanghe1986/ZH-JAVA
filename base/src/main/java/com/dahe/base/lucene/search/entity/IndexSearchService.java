package com.dahe.base.lucene.search.entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dahe.base.lucene.search.service.impl.PageDocumentResult;


public class IndexSearchService {

	private SearchService searchService = new SearchService();
	private Analyzer stantardAnalyzer = new StandardAnalyzer(Version.LUCENE_40);
	private Analyzer analyzer = new PinyinAnalyzer(Version.LUCENE_40);

	/*public void search(String indexType, String keyword, int startPageNo, int pageSize) {
		search(indexType, keyword, startPageNo, pageSize);
	}*/

	/**
	 * ���������ͼ��ؼ�������
	 * @param indexType ��������
	 * @param keyword �����ؼ���
	 * @param roleIds ��ɫID����
	 * @param startPageNo ��ʼҳ
	 * @param pageSize ÿҳ��С
	 * @param userId ��ǰ�û�ID
	 * @param operations �������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void search(String indexType, String keyword, int startPageNo, int pageSize) {
		try {
			BooleanQuery mergedQuery = new BooleanQuery();
			/*String transferredKeyword = transferKeyword(keyword);*/
			MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_40,
					getSearchFieldByIndexType(indexType), stantardAnalyzer);
			queryParser.setDefaultOperator(MultiFieldQueryParser.AND_OPERATOR);
			Query query = queryParser.parse(keyword);
			mergedQuery.add(query, Occur.MUST);
		
			PageDocumentResult result = searchService.pageSearch(indexType, mergedQuery, startPageNo, pageSize);
			
			while (result.hasNext()) {
				Document doc = result.next();
				Map<String, Object> docMap = getInfoFromDoc(doc, keyword, indexType);
				System.out.println(docMap.toString()); 
			}

		} catch (ParseException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * ͨ���������ͻ�ȡҪ���ҵ�field��Χ
	 * @param indexType
	 * @return
	 */
	private String[] getSearchFieldByIndexType(String indexType) {
		String[] fields = null;
		if ("camera".equals(indexType)) {
			/*fields = new String[] { "name", "indexCode", "pinyinCode", "pinyinCodeAll", "address", "cameraType", "pixel",
					"deviceName", "deviceIp", "orgName", "videoType" };*/
			fields = new String[] { "indexCode" };
		} else if ("device".equals(indexType)) {
			fields = new String[] { "name", "indexCode", "pinyinCode", "address", "ip" };
		} else if ("otherResource".equals(indexType)) {
			fields = new String[] { "name", "indexCode", "pinyinCode", "resourceTypeName", "orgName" };
		} else if ("video".equals(indexType)) {
			fields = new String[] { "name", "key", "comment", "cameraNames" };
		} else if ("extraResource".equals(indexType)) {
			fields = new String[] { "name", "indexCode", "resourceTypeName", "orgName" };
		}
		return fields;
	}
	
	private Map<String, Object> getInfoFromDoc(Document doc, String keyword, String indexType)
			throws IOException {
		Map<String, Object> docMap = new HashMap<String, Object>();
		List<IndexableField> fieldList = doc.getFields();
		for (IndexableField field : fieldList) {
			if ("id".equals(field.name())) {
				docMap.put(field.name(), doc.get(field.name()));
			} else {
				/*String text = getHighlightText(keyword, doc.get(field.name()));*/
				docMap.put(field.name(), doc.get(field.name()));
			}
		}
		return docMap;
	}
	
	
	/*private String transferKeyword(String keyword) {
		String[] specialStrings = new String[] { "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*",
				"?", ":", "/" };
		for (String specialString : specialStrings) {
			keyword = StringUtils.replace(keyword, specialString, "\\" + specialString);
		}
		return keyword;
	}*/

}