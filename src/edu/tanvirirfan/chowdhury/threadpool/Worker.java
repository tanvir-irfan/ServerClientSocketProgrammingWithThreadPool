package edu.tanvirirfan.chowdhury.threadpool;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import edu.tanvirirfan.chowdhury.server.MagicMathServer;
import edu.tanvirirfan.chowdhury.utility.Constant;

public class Worker<T> implements Runnable {

	private MyQueue<T> myQueue;

	public Worker(MyQueue<T> myQueue, String name, int workerType) {
		this.myQueue = myQueue;
	}

	@Override
	public void run() {
		String result = "";
		while (true) {
			TaskOfMagic r = (TaskOfMagic) myQueue.dequeue();
			switch (r.gettType()) {
			case Constant.TASK_MAGIC_ADD:
				synchronized (result) {
					result += magicAdd(r.getDataOne(), r.getDataTwo(),
							r.getIterationOrNumberOfItem());
					sendResultToClient(r, result);
					result = "";
					printStatistics(r);
					//Now I do not need the TaskOfMagic instance
					r = null;
				}
				break;
			case Constant.TASK_MAGIC_SUB:
				synchronized (result) {
					result += magicSub(r.getDataOne(), r.getDataTwo(),
							r.getIterationOrNumberOfItem());
					sendResultToClient(r, result);
					result = "";
					printStatistics(r);
					//Now I do not need the TaskOfMagic instance
					r = null;
				}
				break;
			case Constant.TASK_MAGIC_MUL:
				synchronized (result) {
					result += magicMul(r.getDataOne(), r.getDataTwo(),
							r.getIterationOrNumberOfItem());
					sendResultToClient(r, result);
					result = "";
					printStatistics(r);
					//Now I do not need the TaskOfMagic instance
					r = null;
				}
				break;
			case Constant.TASK_MAGIC_SORT:
				synchronized (result) {
					result += magicSort(r.getNumberToSort());
					sendResultToClient(r, result);
					result = "";
					printStatistics(r);
					//Now I do not need the TaskOfMagic instance
					r = null;
				}
				break;
			case Constant.WORKER_TYPE_REQUEST_HANDLER:
				parseAndExecute(r);
				break;
			}
		}
	}

	private void printStatistics(TaskOfMagic r) {
		MagicMathServer.numberOfRequestHandled++;
		MagicMathServer.totalRequestOfSpecificType[r.gettType()]++;
		MagicMathServer.totalTimeOfSpecificType[r.gettType()] += (int) (System
				.currentTimeMillis() - r.startTime);

		if ((MagicMathServer.numberOfRequestHandled % Constant.SERVER_REPORTING_INTERVAL) == 0) {
			System.out
					.println("############################ STATISTICS START #################################");
			System.out.printf("%15s\t%15s\t%15s\t%15s\t%15s", "OPERATION_TYPE",
					"MAGIC_ADD", "MAGIC_SUB", "MAGIC_MUL", "MAGIC_SORT");
			System.out.println();
			System.out
					.printf("%15s\t%15d\t%15d\t%15d\t%15d",
							"SERVICE_COUNT",
							MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_ADD],
							MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_SUB],
							MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_MUL],
							MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_SORT]);
			System.out.println();
			System.out
			.printf("%15s\t%15.3f\t%15.3f\t%15.3f\t%15.3f",
					"AVERAGE_TIME",
					((float) MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_ADD] / (float) MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_ADD]),
					((float) MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_SUB] / (float) MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_SUB]),
					((float) MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_MUL] / (float) MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_MUL]),
					((float) MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_SORT] / (float) MagicMathServer.totalRequestOfSpecificType[Constant.TASK_MAGIC_SORT]));
			System.out.println();
			System.out
			.printf("%15s\t%15d\t%15d\t%15d\t%15d",
					"TOTAL_TIME",
					MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_ADD],
					MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_SUB],
					MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_MUL],
					MagicMathServer.totalTimeOfSpecificType[Constant.TASK_MAGIC_SORT]);
			System.out.println();
		}
		//TODO I added this code to avoid "java.net.SocketException : too many open file"
		try {
			r.socket.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	protected void parseAndExecute(TaskOfMagic tM)
			throws IllegalArgumentException {
		String[] elements = tM.getRequestString()
				.split(Constant.INPUT_SPLITTER);
		if (elements.length < 3 || elements.length > 4) {
			throw new IllegalArgumentException("parsing error!");
		}

		float firstValue = 0;
		float secondValue = 0;
		int iterationOrNumberOfInteger = 0;
		int tType;

		try {
			tType = Integer.parseInt(elements[Constant.INDEX_OPERATION_TYPE]);
			switch (tType) {
			case Constant.TASK_MAGIC_ADD:
			case Constant.TASK_MAGIC_SUB:
			case Constant.TASK_MAGIC_MUL:
				// parsing the data for the first three operations
				firstValue = Float
						.parseFloat(elements[Constant.INDEX_FIRST_DATA]);
				secondValue = Float
						.parseFloat(elements[Constant.INDEX_SECOND_DATA]);
				iterationOrNumberOfInteger = Integer
						.parseInt(elements[Constant.INDEX_K]);

				ThreadPoolManager.getThreadPoolmanager().submitTask(
						new TaskOfMagic(tType, firstValue, secondValue,
								iterationOrNumberOfInteger, null, tM.socket,
								tM.startTime));
				break;
			case Constant.TASK_MAGIC_SORT:
				iterationOrNumberOfInteger = Integer
						.parseInt(elements[Constant.INDEX_SORT_K]);
				TaskOfMagic t = new TaskOfMagic(tType, null, null,
						iterationOrNumberOfInteger, null, tM.socket,
						tM.startTime);

				t.setNumToSortB(elements[Constant.INDEX_SORT_INTEGERS]);
				ThreadPoolManager.getThreadPoolmanager().submitTask(t);
				break;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid arguments!");
		}
	}

	private void sendResultToClient(TaskOfMagic r, String result) {
		// write the result back to the client
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					r.socket.getOutputStream()));
			writer.write(result);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Float magicAdd(Float dataOne, Float dataTwo,
			Integer iterationOrNumberOfItem) {
		Float result = 0.0f;
		for (int i = iterationOrNumberOfItem; i >= 1; i--) {
			result += dataOne / i + dataTwo / i;
		}
		return result;
	}

	private Float magicSub(Float dataOne, Float dataTwo,
			Integer iterationOrNumberOfItem) {
		Float result = 0.0f;
		for (int i = iterationOrNumberOfItem; i >= 1; i--) {
			result += dataOne / i - dataTwo / i;
		}
		return result;
	}

	private Float magicMul(Float dataOne, Float dataTwo,
			Integer iterationOrNumberOfItem) {
		Float result = 0.0f;
		for (int i = iterationOrNumberOfItem; i >= 1; i--) {
			result += dataOne / i * dataTwo / i;
		}
		return result;
	}

	private String magicSort(Integer[] integers) {
		String result = "";
		Arrays.sort(integers);
		for (int number : integers) {
			result += number + " ";
		}
		return result;
	}
}