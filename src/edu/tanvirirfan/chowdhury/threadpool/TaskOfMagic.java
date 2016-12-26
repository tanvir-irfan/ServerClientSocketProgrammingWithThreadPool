package edu.tanvirirfan.chowdhury.threadpool;

import java.net.Socket;

import edu.tanvirirfan.chowdhury.utility.Constant;

public class TaskOfMagic {
	private int tType;
	private Float dataOne;
	private Float dataTwo;
	private Integer iterationOrNumberOfItem;
	private Integer[] numberToSort;
	Socket socket;
	String requestString;
	long startTime;
	
	public TaskOfMagic(int tType, String str, Socket socket, long startTime) {
		this.tType = tType;
		this.socket = socket;
		this.requestString = str;
		this.startTime = startTime;
		
	}
	
	public TaskOfMagic(int tType, Float dataOne, Float dataTwo, Integer iterationOrNumberOfItem, Integer[] numberToSort, Socket socket, long startTime) {
		this.tType = tType;
		this.socket = socket;
		this.iterationOrNumberOfItem = iterationOrNumberOfItem;
		this.startTime = startTime;
		switch(tType) {
		case Constant.TASK_MAGIC_ADD:
		case Constant.TASK_MAGIC_SUB:
		case Constant.TASK_MAGIC_MUL:
			this.dataOne = dataOne;
			this.dataTwo = dataTwo;
			break;
		case Constant.TASK_MAGIC_SORT:
			this.numberToSort = new Integer[iterationOrNumberOfItem];
			break;
		}
	}

	public int gettType() {
		return this.tType;
	}
	
	public Float getDataOne() {
		return dataOne;
	}

	public Float getDataTwo() {
		return dataTwo;
	}

	public Integer getIterationOrNumberOfItem() {
		return iterationOrNumberOfItem;
	}

	public Integer[] getNumberToSort() {
		return numberToSort;
	}
	
	public String getRequestString() {
		return requestString;
	}
	
	public boolean setNumToSortB(String strN) {
		boolean result = true;
		String[] elements = strN.split(Constant.SORT_INTEGER_SPLITTER);
		if(elements.length != this.iterationOrNumberOfItem) {
			return false;
		}
		int count = 0;
		for(String e: elements) {
			numberToSort[count++] = Integer.parseInt(e);
		}
		
		return result;
	}
}