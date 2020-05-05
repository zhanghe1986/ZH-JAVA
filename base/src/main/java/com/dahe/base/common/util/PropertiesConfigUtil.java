package com.dahe.base.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesConfigUtil {

	/**
	 * @describe read value from .properties file by key
	 * */
	public static String readValue(String filePath, String key) {
		String filePathBase = PropertiesConfigUtil.class.getResource("/"+filePath).toString();
		//delete "file:" prefix
		filePath = filePathBase.substring(6);
		Properties props = new Properties();
		InputStream in = null;
		String value = "";
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
			value = props.getProperty(key);
		} catch (Exception e) {
			e.printStackTrace();
			return value;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	/**
	 * @describe add or update key and value into .properties file
	 * */
	public static void writeKeyAndValue(String tempFilePath, String key, String value) {
		String filePathBase = PropertiesConfigUtil.class.getResource("/").toString();
		//delete "file:/" prefix
		String filePath = filePathBase.substring(6) + tempFilePath;
		Properties props = new Properties();
		InputStream in = null;
		File file = new File(filePath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			in = new FileInputStream(file);
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		OutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			props.setProperty(key, value);
			props.store(os, "Update '"+key+"' value");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
