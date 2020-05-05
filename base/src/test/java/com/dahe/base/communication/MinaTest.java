package com.dahe.base.communication;

import com.dahe.base.communication.entity.mina.client.MinaClient;
import com.dahe.base.communication.entity.mina.server.MinaServer;

public class MinaTest {

	public static void main(String args[]) {
		String serverIp = "127.0.0.1";
		int serverListenPort = 6588;

		MinaServer.getMinaServerInstance();

		MinaClient client_1 = new MinaClient();
		MinaClient client_2 = new MinaClient();
		MinaClient client_3 = new MinaClient();

		client_1.connectToServer(serverIp, serverListenPort);
		client_2.connectToServer(serverIp, serverListenPort);
		client_3.connectToServer(serverIp, serverListenPort);
	}
}
