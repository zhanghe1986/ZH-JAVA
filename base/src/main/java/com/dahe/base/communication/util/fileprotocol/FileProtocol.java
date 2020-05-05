package com.dahe.base.communication.util.fileprotocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

/**
 * 上传文件依赖于apache服务的﻿WebDav协议(apache方面的配置在github上)
 * */
public class FileProtocol {
	private final static int BUFFER_SIZE = 1024;
	private static HttpClient httpClient;
	
	//-----------------------------------upload file depends on Webdav protocol-----------------------------------------
	protected static HttpClient getClient(Credentials creds, String httpUrl, int sslPort) {
		if(httpClient == null) {
			httpClient = initClient(creds, httpUrl, sslPort);
		}
		return httpClient;
	}
	
	protected static HttpClient initClient(Credentials creds, String httpUrl, int sslPort) {
		HttpClient tempClient = new HttpClient();
		//remote credential
		//Credentials creds = new UsernamePasswordCredentials("admin", "password");
		tempClient.getState().setCredentials(AuthScope.ANY, creds);
		/*if((httpUrl != null) && (httpUrl != "") && (httpUrl.trim().startsWith("https"))) {
			Protocol httpsProtocol = new Protocol("https", new IgnoreSSLProtocolSocketFactory(), sslPort);
			Protocol.registerProtocol("https", httpsProtocol);
		}*/
		return tempClient;
	}
	
	public static int uploadFile(String targetUrl, String sourceFile, Credentials creds, int sslPort) {
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(sourceFile);
			PutMethod putMethod = new PutMethod(targetUrl);
			RequestEntity requestEntity = new InputStreamRequestEntity(fileInputStream);
			putMethod.setRequestEntity(requestEntity);
			return getClient(creds, targetUrl, sslPort).executeMethod(putMethod);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}
	//-----------------------------------------------------------------------------------------------------
	
	//--------------------------------------------download file--------------------------------------------
	protected static FileOutputStream openOutputStream(File file) throws IOException {
		if(file.exists()) {
			if(file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if(file.canWrite() == false) {
				throw new IOException("File '" + file + "' can not be written");
			}
		} else {
			File parent = file.getParentFile();
			if(parent != null && parent.exists() == false) {
				if(parent.mkdirs() == false) {
					throw new IOException("File '" + file + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file);
	}
	
	protected static void copy(InputStream inputStream, OutputStream outputStream) {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int n = 0;
			while(-1 != (n = inputStream.read(buffer))) {
				outputStream.write(buffer, 0, n);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadFile(String sourceHttpUrl, String targetDir) {
		InputStream is = null;
		FileOutputStream fileOutputStream = null;
		
		try {
			URL httpUrl = new URL(sourceHttpUrl);
			File file = new File(targetDir);
			is = httpUrl.openStream();
			fileOutputStream = openOutputStream(file);
			copy(is, fileOutputStream);
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	//-----------------------------------------------------------------------------------------------------
}
