package com.dahe.base.communication.util.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {
	private static final int CONNECTION_TIME_OUT = 10000;
	private static final int READ_TIME_OUT = 10000;
	private static final int STATUS_CODE_OF_OK = 200;

	/**
	 * @author He Zhang
	 * @param httpUrl
	 *            http://ip:port/vms/GetResourceType
	 * @param param
	 * @return String
	 * */
	public static String doPost(String httpUrl, String param) {
		OutputStream os = null;
		InputStream is = null;
		OutputStreamWriter out = null;
		BufferedReader br = null;
		HttpURLConnection connection = null;

		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(CONNECTION_TIME_OUT);
			connection.setReadTimeout(READ_TIME_OUT);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Authorization",
					"Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
			connection.connect();
			if (connection.getResponseCode() == STATUS_CODE_OF_OK) {
				os = connection.getOutputStream();
				out = new OutputStreamWriter(os, "UTF-8");
				out.write(param);
				out.flush();
				is = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String data;
				while ((data = br.readLine()) != null) {
					result.append(data);
					result.append("\r\n");
				}
				return result.toString();
			} else {
				System.out.println("[doPost]Connection is fail:" + httpUrl);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			connection.disconnect();
		}
		return result.toString();
	}

	/**
	 * @author He Zhang
	 * @param httpUrl
	 *            http://ip:port/vms/GetResourceType
	 * */
	public static String doGet(String httpUrl) {
		InputStream is = null;
		BufferedReader br = null;
		HttpURLConnection connection = null;

		StringBuilder result = new StringBuilder();

		try {
			URL url = new URL(httpUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(CONNECTION_TIME_OUT);
			connection.setReadTimeout(READ_TIME_OUT);
			connection.connect();
			if (connection.getResponseCode() == STATUS_CODE_OF_OK) {
				is = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String data;
				while ((data = br.readLine()) != null) {
					result.append(data);
					result.append("\r\n");
				}
				return result.toString();
			} else {
				System.out.println("[doGet]Connection is fail:" + httpUrl);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			connection.disconnect();
		}
		return result.toString();
	}
}
