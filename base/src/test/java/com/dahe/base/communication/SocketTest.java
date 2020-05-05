package com.dahe.base.communication;

import com.dahe.base.communication.entity.socket.Client;
import com.dahe.base.communication.entity.socket.Server;

public class SocketTest {
	private static Server server;
	private static Client client;
	
	public static void main(String[] args) {
		server = new Server(8081);
		client = new Client("127.0.0.1", 8081);
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		client.send();
		server.listen();
		server.stop();
	}
}
