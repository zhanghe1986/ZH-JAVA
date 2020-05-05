package com.dahe.base.communication.entity.mina.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServer {
	private static final int PORT = 6588;
	private static final int BUFFERSIZE = 2048;
	private static MinaServer minaServerInstance = new MinaServer();

	public MinaServer() {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(BUFFERSIZE);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		acceptor.setHandler(new ServerHandler());

		try {
			acceptor.bind(new InetSocketAddress(PORT));
			acceptor.bind();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static MinaServer getMinaServerInstance() {
		return minaServerInstance;
	}
}
