package com.dahe.base.lucene;

import com.dahe.base.lucene.search.entity.IndexSearchService;

public class SearchLuceneTest {

	public static void main(String args[]) {
		IndexSearchService indexSearch = new IndexSearchService();
		indexSearch.search("camera", "123456", 1, 100);
	}
	
}
