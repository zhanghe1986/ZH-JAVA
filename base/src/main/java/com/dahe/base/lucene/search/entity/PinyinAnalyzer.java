package com.dahe.base.lucene.search.entity;
import java.io.Reader;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

public class PinyinAnalyzer extends StopwordAnalyzerBase {  //StopwordAnalyzerBase����lucene-analyzers-common jar

	public PinyinAnalyzer(Version version) {
		super(version);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName, Reader reader) {

		// ���ｫ�����ַ�ָ�ɴʻ��Tokenizer��Ҫʹ���Զ����Tokenizer
		PinyinTokenizer tokenizer = new PinyinTokenizer(reader);
		return new TokenStreamComponents(tokenizer, new LowerCaseFilter(matchVersion, tokenizer));    //TokenStreamComponents ����lucene-core jar
	}

}