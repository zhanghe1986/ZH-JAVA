package com.dahe.base.communication.entity.mina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.dahe.base.communication.entity.mina.Request;

public class ClientHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		String content = message.toString();
		System.out.println("[ClientHandler]client receive a message is:"
				+ content);
	}

	public void messageSent(IoSession session, Object message) throws Exception {
		if (message instanceof Request) {
			Request request = (Request) message;
			System.out.println("[ClientHandler]messageSent -> :"
					+ request.getProtocol());
		} else {
			System.out.println("[ClientHandler]messageSent -> :"
					+ message.toString());
		}
	}
}
