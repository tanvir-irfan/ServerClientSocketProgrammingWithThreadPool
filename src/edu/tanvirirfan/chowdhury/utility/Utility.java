package edu.tanvirirfan.chowdhury.utility;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Utility {
	public synchronized static String getRandomRequest() {
		String newRequest = "";
		int whichOperation = (int) Utility.genRandomFloat(0, 3);
		int k = 0;;
		
		switch (whichOperation) {
		case Constant.TASK_MAGIC_ADD:
		case Constant.TASK_MAGIC_SUB:
		case Constant.TASK_MAGIC_MUL:
			float firstNumber = Utility.genRandomFloat(Constant.FLOAT_MIN,
					Constant.FLOAT_MAX);
			float secondNumber = Utility.genRandomFloat(Constant.FLOAT_MIN,
					Constant.FLOAT_MAX);
			k = (int) Utility.genRandomFloat(Constant.K_MIN,
					Constant.K_MAX);
			newRequest = "" + whichOperation + Constant.INPUT_SPLITTER
					+ firstNumber + Constant.INPUT_SPLITTER + secondNumber
					+ Constant.INPUT_SPLITTER + k;
			break;
		case Constant.TASK_MAGIC_SORT:
			k = (int) Utility.genRandomFloat(Constant.K_MIN,
					Constant.K_MAX);
			newRequest = "" + whichOperation + Constant.INPUT_SPLITTER + k + Constant.INPUT_SPLITTER;
			for(int i=1; i<= k; i++) {
				int j = (int)Utility.genRandomFloat(Constant.SORT_INT_MIN,
						Constant.SORT_INT_MAX);
				newRequest += j + Constant.SORT_INTEGER_SPLITTER;
			}
			break;
		}
		return newRequest;
	}

	private static float genRandomFloat(float aStart, float aEnd) {
		if (aStart > aEnd) {
			throw new IllegalArgumentException("Start cannot exceed End.");
		}

		Random aRandom = new Random();
		// get the range, casting to long to avoid overflow problems
		float range = (float) aEnd - (float) aStart + 1;
		// compute a fraction of the range, 0 <= frac < range
		float fraction = (float) (range * aRandom.nextFloat());
		float randomNumber = (float) (fraction + aStart);
		return randomNumber;
	}
	
	static int totalRemainingRequest = 10;
	
	public static boolean isAnyRequestLeft() {
		synchronized (Utility.class) {
			return totalRemainingRequest-- > 0;
		}
	}
	
	public boolean hostAvailabilityCheck(String hostName, int port) {
		Socket s = null;
		boolean available = true;
		try {
			s = new Socket(hostName, port);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					s.getOutputStream()));

			String req = "" + Constant.CONNECTION_CHECK;
			writer.write(req);
			writer.newLine();
			writer.flush();
			
			if (s.isConnected()) {
				s.close();
			}
		} catch (UnknownHostException e) { // unknown host
			available = false;
			s = null;
		} catch (IOException e) { // io exception, service probably not running
			available = false;
			s = null;
		} catch (NullPointerException e) {
			available = false;
			s = null;
		} catch (Exception e) {
			available = false;
			s = null;
		}

		return available;
	}
	
	public static void main(String [] atgs) {
//		int []array = new int[6];
//		for(int i = 0; i<1000000; i++) {
//			int k = (int) genRandomFloat(0, 3);
//			array[k] = array[k]+1;
//		}
//		
//		for(int i = 0; i<6; i++) {
//			System.out.println( i + " = " +array[i]);
//		}
//		
//		for(int i = 0; i<100; i++) {
//			System.out.println( getRandomRequest() );
//		}
		
		for(int i = 0; i<15; i++) {
			System.out.println( isAnyRequestLeft() );
		}
	}
}
