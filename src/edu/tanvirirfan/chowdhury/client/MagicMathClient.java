package edu.tanvirirfan.chowdhury.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import edu.tanvirirfan.chowdhury.utility.Constant;
import edu.tanvirirfan.chowdhury.utility.Utility;

public class MagicMathClient {

	String hostname;
	int port = Constant.DEFAULT_PORT;
	Socket[] sockets; // need one socket per Thread. Gave me hard time finding
						// the DEAD LOCK HERE.
	int totalRemainingRequest = Constant.NUMBER_OF_OPERATION_PER_CLIENT;

	File file;

	public MagicMathClient(String hostname, int port) {
		if (hostname != null)
			this.hostname = hostname;
		else
			// this.hostname = "129.115.3.10";
			this.hostname = Constant.DEFAULT_HOST;
		if (port != Constant.INVALID_PORT) {
			this.port = port;
		} else {
			this.port = Constant.DEFAULT_PORT;
		}
		sockets = new Socket[Constant.NUMBER_OF_THREAD_PER_CLIENT];
		for (int i = 1; i <= Constant.NUMBER_OF_THREAD_PER_CLIENT; i++) {
			new RequestThread("CLIENT THREAD # " + i, i).start();
		}
	}

	public static boolean isServerOnline = true;
	
	public boolean startRequestingServices(int threadId) {
		
		try {
			synchronized ((Boolean)MagicMathClient.isServerOnline) {
				if(!MagicMathClient.isServerOnline) {
					System.out.println("Server Offline!");
					return false;
				}
			}
			String req = "";
			synchronized ((Boolean)MagicMathClient.isServerOnline) {
				// create a socket
				sockets[threadId - 1] = new Socket(this.hostname, this.port);
				sockets[threadId - 1].setKeepAlive(true);
				// perform a simple math operation
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
						sockets[threadId - 1].getOutputStream()));
			
				req = Utility.getRandomRequest();
				writer.write(req);
				writer.newLine();
				writer.flush();
			}
			synchronized ((Boolean)MagicMathClient.isServerOnline) {
				// get the result from the server
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								sockets[threadId - 1].getInputStream()));
				String result = "";
				result += reader.readLine();
				//TODO commenting out the print to save some time.
				System.out
						.println("######################### REQUEST STARTED ################################");
				System.out.println(req);
				System.out.println(result);
				System.out
						.println("######################### REQUEST ENDED ################################");
			}
			return true;
		} catch (UnknownHostException e) { // unknown host
			MagicMathClient.isServerOnline =  false;
		} catch (SecurityException e) { // 
			MagicMathClient.isServerOnline =  false;
		} catch (IllegalArgumentException e) { // 
			MagicMathClient.isServerOnline =  false;
		} catch (SocketException e) { // 
			MagicMathClient.isServerOnline =  false;
		} catch (IOException e) { // io exception, service probably not running
			MagicMathClient.isServerOnline =  false;
		} catch (NullPointerException e) {
			MagicMathClient.isServerOnline =  false;
		} catch (Exception e) {
			MagicMathClient.isServerOnline =  false;
		} finally{
			if (!MagicMathClient.isServerOnline) {
				System.out.println("Server Offline!!");
				return false;
			}		
		}
		return true;
	}

	class RequestThread extends Thread {
		private String name;
		private int curCount;
		int tID;

		RequestThread(String str, int id) {
			this.name = str;
			this.curCount = 0;
			this.tID = id;
		}

		@Override
		public void run() {
			while (isAnyRequestLeft(name) /*&& hostAvailabilityCheck()*/) {
				boolean wasSuccessful = startRequestingServices(this.tID);
				if(!wasSuccessful) break;
			}
		}
	}

	private boolean isAnyRequestLeft(String name) {
		synchronized (this) {
			return totalRemainingRequest-- > 0;
		}
	}
	
	public static void main(String[] args) {

		// args[0] = java file name, args[1] = hostname, args[2] = port number
		MagicMathClient mC;
		if (args.length != 3) {
			System.out.println("Use the default setting...");
			mC = new MagicMathClient(null, Constant.INVALID_PORT);
		} else {
			mC = new MagicMathClient(args[1], Integer.parseInt(args[2]));
			System.out.println("Use the user's setting...");
		}
	}
}