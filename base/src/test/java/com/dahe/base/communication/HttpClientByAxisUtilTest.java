package com.dahe.base.communication;

import org.junit.Assert;
import org.junit.Test;

import com.dahe.base.communication.util.axis.HttpClientByAxisUtil;

public class HttpClientByAxisUtilTest {

	@Test
	public void callWsTest() {
		String url = "http://ip:port/vms/services/Alarm?wsdl";
		String method = "getSource";
		String namespaceURI = "http://ws.vms.ivms6.hikvision.com";
		Object args[] = new Object[] {"1"};
		
		Assert.assertNotNull(HttpClientByAxisUtil.callWs(url, method, namespaceURI, args, String.class));
	}
}
