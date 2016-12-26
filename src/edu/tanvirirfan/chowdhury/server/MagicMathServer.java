package edu.tanvirirfan.chowdhury.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import edu.tanvirirfan.chowdhury.threadpool.TaskOfMagic;
import edu.tanvirirfan.chowdhury.threadpool.ThreadPoolManager;
import edu.tanvirirfan.chowdhury.utility.Constant;

public class MagicMathServer {
	static ServerSocket serverSocket;
	static Socket socket;

	private ThreadPoolManager poolManager;
	String threadName = "MagicMathServer";
	static final Integer port = Constant.DEFAULT_PORT;

	// TODO new implementation for the counter and the timer
	public static int numberOfRequestHandled = 0;
	public static int[] totalRequestOfSpecificType = new int[4];
	public static int[] totalTimeOfSpecificType = new int[4];

	public MagicMathServer() throws IOException {
		poolManager = ThreadPoolManager.getThreadPoolmanager();

		// Register service on port Constant.PORT;
		MagicMathServer.serverSocket = new ServerSocket(port);
		// Wait and accept connection!
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						MagicMathServer.socket = serverSocket.accept();
						long startTime = System.currentTimeMillis();

						BufferedReader reader = new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
						// read the message from client and parse the execution
						String line = reader.readLine();
						if(line.equalsIgnoreCase(""+Constant.CONNECTION_CHECK)) {
							//TODO do nothing bcause this is just a connection check request
						} else {
							poolManager.submitTask(new TaskOfMagic(
									Constant.WORKER_TYPE_REQUEST_HANDLER, line,
									MagicMathServer.socket, startTime));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}, threadName).start();
	}

	public ThreadPoolManager getPoolManager() {
		return poolManager;
	}

	public static void main(String[] args) throws Exception {
		MagicMathServer mMS = new MagicMathServer();
		System.out.println("Math Server is up and running...");
	}
}