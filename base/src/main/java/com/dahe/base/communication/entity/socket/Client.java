package com.dahe.base.communication.entity.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	/* listened port */
	private int listenedPort;
	/* server ip */
	private String serverIp;
	/* client socket object */
	private Socket socket;

	/**
	 * client constructure function
	 * 
	 * @param serverIp
	 * @param listenedPort
	 * */
	public Client(String serverIp, int listenedPort) {
		this.listenedPort = listenedPort;
		this.serverIp = serverIp;
	}

	/**
	 * client sends messages to server
	 * */
	public void send() {
		PrintWriter pw = null;
		try {
			socket = new Socket(this.serverIp, this.listenedPort);
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println("[client send]:Hello!");
			pw.println("[client send]:I am client.");
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
				if (pw != null) {
					pw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
