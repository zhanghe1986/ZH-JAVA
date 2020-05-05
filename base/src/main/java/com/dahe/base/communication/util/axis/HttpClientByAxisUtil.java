package com.dahe.base.communication.util.axis;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

/**
 * 通过axis方式通信，调第三方webService接口，并获取接口返回值
 * @author lenovo
 * */
public class HttpClientByAxisUtil {
	private static ThreadLocal<RPCServiceClient> rpcServiceClients = new ThreadLocal<RPCServiceClient>();

	/**
	 * @author He Zhang
	 * @param url
	 *            http://ip:port/vms/services/Alarm?wsdl
	 * @param method
	 *            getSource
	 * @param namespaceURI
	 *            http://ws.vms.ivms6.hikvision.com
	 * @param args
	 *            new Object[] {"1"}
	 * @param returnType
	 *            String.class
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public static <T> T callWs(String url, String method, String namespaceURI,
			Object args[], Class<T> returnType) {
		RPCServiceClient rpcServiceClient = null;
		try {
			rpcServiceClient = getRPCServiceClient(url, method);
			QName name = new QName(namespaceURI, method);
			/* Class<?>[] returnTypes = new Class[] { returnType }; */
			OMElement element = rpcServiceClient.invokeBlocking(name, args);
			T result = (T) element.getFirstElement().getText();
			return result;
		} catch (AxisFault e) {
			e.printStackTrace();
		} finally {
			if (rpcServiceClient != null) {
				try {
					rpcServiceClient.cleanupTransport();
				} catch (AxisFault e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static RPCServiceClient getRPCServiceClient(String url,
			String method) {
		RPCServiceClient rpcServiceClient = rpcServiceClients.get();
		if (rpcServiceClient == null) {
			try {
				rpcServiceClient = new RPCServiceClient();
			} catch (AxisFault e) {
				e.printStackTrace();
			}
			rpcServiceClients.set(rpcServiceClient);
		}
		Options option = rpcServiceClient.getOptions();
		EndpointReference erf = new EndpointReference(url);
		option.setTo(erf);
		option.setProperty(HTTPConstants.CHUNKED, Boolean.FALSE);
		option.setAction("urn:" + method);
		return rpcServiceClient;
	}

}
