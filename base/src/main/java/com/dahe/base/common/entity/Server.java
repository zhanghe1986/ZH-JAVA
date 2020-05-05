package com.dahe.base.common.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The server is able to receive messages from client.
 * Listen the port in whole process.
 * */
public class Server {
	/* listened port */
	private int listenedPort;
	/* server socket to bind listened port */
	private ServerSocket serverSocket;
	/* thread pool executor */
	private ExecutorService executor;
	
	/**
	 * server constructure function
	 * @param listenedPort
	 * */
	public Server(int listenedPort) {
		try {
			this.listenedPort = listenedPort;
			this.serverSocket = new ServerSocket(this.listenedPort);
			this.executor = Executors.newFixedThreadPool(10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * listen messages from client
	 * */
	public void listen() {
		while(true) {
			try {
				Socket socket = serverSocket.accept();
				executor.execute(new MessageHandler(socket));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * server sends messages to client
	 * */
	public void send(String clientIp) {
		PrintWriter pw = null;
		Socket socket = null;
		try {
			socket = new Socket(clientIp, this.listenedPort);
			pw = new PrintWriter(
					socket.getOutputStream(), true); //pw is sender to server socket
			pw.println("[server send]:hello!");
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(pw != null) {
					pw.close();
				}
				if(socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * stop socket and server socket
	 * */
	public void stop() {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		} catch(IOException e) {
			
		}
	}

}


/**
 * the message handler is able to handle messages that from clients
 * */
class MessageHandler implements Runnable{
	private Socket localSocket;
	
	/**
	 *MessageHandler constructor
	 *@param socket
	 * */
	public MessageHandler(Socket socket) {
		this.localSocket = socket;
	}
	
	public void run() {
		BufferedReader br = null;
		try {
			System.out.println("[server listen]:Accept messages from client: ip " + localSocket.getInetAddress().getHostAddress() 
					+ " name " + localSocket.getInetAddress().getHostName());
			br = new BufferedReader(new InputStreamReader(
					this.localSocket.getInputStream())); //br is datasource from client socket
			String content = null;
			while((content = br.readLine()) != null) {
				System.out.println("[server listen]:" + content);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();
				}
				if(localSocket != null) {
					localSocket.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}


