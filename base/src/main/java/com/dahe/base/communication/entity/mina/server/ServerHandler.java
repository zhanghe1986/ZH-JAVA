package com.dahe.base.communication.entity.mina.server;

import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.dahe.base.communication.entity.mina.Request;

public class ServerHandler extends IoHandlerAdapter {
	@Override
	public void sessionCreated(IoSession session) {
		System.out.println(session.getRemoteAddress().toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof Request) {
			Request request = (Request) message;
			Map<String, String> params = request.getParams();
			for (Map.Entry<String, String> msgEntry : params.entrySet()) {
				System.out.println("[ServerHandler]message:"
						+ msgEntry.getKey() + " " + msgEntry.getValue());
			}
		}
		String strMsg = message.toString();
		System.out.println("[ServerHandler]message written:" + strMsg);
		if (strMsg.trim().equalsIgnoreCase("quit")) {
			session.close(true);
			return;
		}
		session.write("Hi Client!");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		System.out.println("[ServerHandler]idle:"
				+ session.getIdleCount(status));
	}
}
