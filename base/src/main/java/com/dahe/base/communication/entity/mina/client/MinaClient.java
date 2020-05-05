package com.dahe.base.communication.entity.mina.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.dahe.base.communication.entity.mina.Request;

public class MinaClient {

	public void connectToServer(String serverIp, int serverListenPort) {
		NioSocketConnector connector = new NioSocketConnector();
		connector.setDefaultRemoteAddress(new InetSocketAddress(serverIp,
				serverListenPort));
		connector.setConnectTimeoutCheckInterval(30);
		connector.setHandler(new ClientHandler());

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));

		/*
		 * connector.getFilterChain().addLast("codec", new
		 * ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		 * connector.getFilterChain().addLast("reconnection", new
		 * IoFilterAdapter() {
		 * 
		 * @Override public void sessionClosed(NextFilter nextFilter, IoSession
		 * ioSession) { while(true) { try { Thread.sleep(3000); ConnectFuture
		 * future = connector.connect(); future.awaitUninterruptibly(); //wait
		 * for successing to connect ioSession = future.getSession(); //get
		 * obtain if(ioSession.isConnected()) { ioSession.write("Hi Server!");
		 * ioSession.write("quit");
		 * ioSession.getCloseFuture().awaitUninterruptibly();
		 * connector.dispose(); break; } }catch(Exception e) {
		 * System.out.println(e.getMessage()); } } } });
		 */
		try {
			ConnectFuture cf = connector.connect(new InetSocketAddress(
					serverIp, serverListenPort));
			cf.awaitUninterruptibly();
			IoSession ioSession = cf.getSession();

			Request request = new Request();
			request.setProtocol("https");
			Map<String, String> params = new HashMap<String, String>();
			params.put("1", "one");
			params.put("2", "tow");
			request.setParams(params);

			ioSession.write("Hi Server!");
			ioSession.write(request);
			ioSession.write("quit");

			cf.getSession().getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
