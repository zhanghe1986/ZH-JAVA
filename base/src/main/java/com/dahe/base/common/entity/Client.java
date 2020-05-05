package com.dahe.base.common.entity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Config")
public class Client {
	/* listened port */
	@XmlElement(name = "ListenedPort")
	private int listenedPort;

	public void setListenedPort(int listenedPort) {
		this.listenedPort = listenedPort;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/* server ip */
	@XmlElement(name = "ServerIp")
	private String serverIp;
	/* client socket object */
	@XmlElement(name = "Socket")
	private Socket socket;
	
	/**
	 * client constructure function
	 * @param serverIp
	 * @param listenedPort
	 * */
	/*public Client(String serverIp, int listenedPort, Socket socket) {
		this.listenedPort = listenedPort;
		this.serverIp = serverIp;	
		this.socket = socket;
	}*/
	
	/**
	 * client sends messages to server
	 * */
	public void send() {
		PrintWriter pw = null;
		try {
			socket = new Socket(this.serverIp, this.listenedPort);
			pw = new PrintWriter(
					socket.getOutputStream(), true); //pw is sender to server socket
			pw.println("[client send]:Hello!");
			pw.println("[client send]:I am client.");
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(socket != null) {
					socket.close();
				}
				if(pw != null) {
					pw.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
