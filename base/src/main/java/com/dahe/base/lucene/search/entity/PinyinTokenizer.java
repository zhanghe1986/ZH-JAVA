package com.dahe.base.lucene.search.entity;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class PinyinTokenizer extends Tokenizer {      //Tokenizer����lucene-core jar

	private static final String SPLITTER_REGX = "[\\s]";
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);     //CharTermAttribute����lucene-core jar
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);       //OffsetAttribute����lucene-core jar
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);       //PositionIncrementAttribute����lucene-core jar

	private char currentTokenBuff[] = new char[4096];
	private LinkedList<TokenWithOffset> tokens = new LinkedList<TokenWithOffset>();
	private TokenWithOffset currentToken = null;

	private static final int TOKEN_MAX_LENGTH = 2;// ��Ԫ��󳤶�

	private static class TokenWithOffset {

		private String token = null;
		private int offset = 0;

		public TokenWithOffset(String token, int offset) {
			this.token = token;
			this.offset = offset;
		}

		public String getToken() {
			return token;
		}

		public int getOffset() {
			return offset;
		}

	}

	public PinyinTokenizer(Reader input) {
		super(input);
	}

	private boolean getNextToken() throws IOException {
		if (tokens.isEmpty()) {
			int currentBufferLen = this.input.read(currentTokenBuff, 0, currentTokenBuff.length);
			if (currentBufferLen == -1) {
				return false;
			}
			getTokens(splitBuff(currentTokenBuff, 0, currentBufferLen));
		}

		if (!tokens.isEmpty()) {
			currentToken = tokens.poll();
			return true;
		}

		return false;
	}

	private void getTokens(Collection<TokenWithOffset> bufferStr) {
		for (TokenWithOffset s : bufferStr) {
			getTokens(s, TOKEN_MAX_LENGTH);
		}
	}

	private void getTokens(TokenWithOffset buffer, int tokenMaxLength) {
		String token = buffer.getToken();
		List<TokenWithOffset> tempList = new ArrayList<TokenWithOffset>();
		for (int i = 1; i <= tokenMaxLength; i++) {
			for (int j = 0; j < token.length() - i + 1; j++) {
				tempList.add(new TokenWithOffset(token.substring(j, j + i), buffer.getOffset() + j));
			}
		}
		
		tokens.addAll(tempList);
	}

	/**
	 * ���β��,���ָ�����в��
	 * @param buf
	 * @param offset
	 * @param len
	 * @return
	 */
	private Collection<TokenWithOffset> splitBuff(char[] buf, int offset, int len) {
		List<TokenWithOffset> buffers = new ArrayList<TokenWithOffset>();
		int lastPoint = offset;
		boolean isLastSplitter = new String(new char[] { buf[offset] }).matches(SPLITTER_REGX);
		for (int i = offset + 1; i < len; i++) {
			boolean isSplitter = new String(new char[] { buf[i] }).matches(SPLITTER_REGX);
			if (isSplitter) {
				if (!isLastSplitter) {
					buffers.add(new TokenWithOffset(new String(buf, lastPoint, i - lastPoint), lastPoint));
					lastPoint = i;
				}
			}

			if (isLastSplitter) {
				lastPoint++;
			}

			isLastSplitter = isSplitter;
		}

		if (lastPoint != len && !isLastSplitter) {
			buffers.add(new TokenWithOffset(new String(buf, lastPoint, len - lastPoint), lastPoint));
		}

		return buffers;
	}

	private void addToken(CharTermAttribute term, OffsetAttribute offsetAtt) {
		if (currentToken != null) {
			char[] buff = currentToken.getToken().toCharArray();
			termAtt.copyBuffer(buff, 0, buff.length);
			offsetAtt.setOffset(correctOffset(currentToken.getOffset()),
					correctOffset(currentToken.getOffset() + termAtt.length()));
		}
	}

	@Override
	public final boolean incrementToken() throws IOException {
		clearAttributes();
		int posIncr = 1;
		while (getNextToken()) {
			posIncrAtt.setPositionIncrement(posIncr);
			addToken(termAtt, offsetAtt);
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object object) {
		return super.equals(object);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}