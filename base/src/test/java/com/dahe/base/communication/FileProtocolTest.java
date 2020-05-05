package com.dahe.base.communication;

import com.dahe.base.communication.util.fileprotocol.FileProtocol;

//import org.apache.commons.io.FileUtils;

public class FileProtocolTest {

	public static void main(String args[]) {
		String targetHttpUrl = "http://10.33.47.29:80/uploads/test.txt";
		String sourceFile = "E:/test11/text.txt";
		FileProtocol.uploadFile(targetHttpUrl, sourceFile, null, 80);
		
		
		String sourceHttpUrl = "http://10.33.47.29:80/uploads/test.txt";
		String targetFile = "E:/test11/text.txt";
		FileProtocol.downloadFile(sourceHttpUrl, targetFile);
		//FileUtils.copyURLToFile(Url, file); //local server
	}
}
