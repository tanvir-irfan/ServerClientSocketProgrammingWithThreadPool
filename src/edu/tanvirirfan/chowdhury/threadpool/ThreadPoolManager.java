package edu.tanvirirfan.chowdhury.threadpool;

import edu.tanvirirfan.chowdhury.utility.Constant;

public class ThreadPoolManager {

	private final int CAPACITY_MAGIC_ADD;
	private final int CAPACITY_MAGIC_SUB;
	private final int CAPACITY_MAGIC_MUL;
	private final int CAPACITY_MAGIC_SORT;
	private final int CAPACITY_GENERAL;
	
	private MyQueue<TaskOfMagic> qAdd = new MyQueue<TaskOfMagic>();
	private MyQueue<TaskOfMagic> qSub = new MyQueue<TaskOfMagic>();
	private MyQueue<TaskOfMagic> qMul = new MyQueue<TaskOfMagic>();
	private MyQueue<TaskOfMagic> qSort = new MyQueue<TaskOfMagic>();
	private MyQueue<TaskOfMagic> allRequestQ = new MyQueue<TaskOfMagic>();
	
	private static ThreadPoolManager threadPoolManager = null;
	
	private static ThreadPoolManager getThreadPoolInstance(int capacity) {
		if(threadPoolManager == null) {
			threadPoolManager = new ThreadPoolManager(capacity);
		}
		
		return threadPoolManager;
	}

	private ThreadPoolManager(int capacity) {
		this(capacity,capacity,capacity,capacity,capacity);
	}
	
	private ThreadPoolManager(int cAdd, int cSub, int cMUL, int cSort, int cGeneral) {
		this.CAPACITY_MAGIC_ADD = cAdd;
		this.CAPACITY_MAGIC_SUB = cSub;
		this.CAPACITY_MAGIC_MUL = cMUL;
		this.CAPACITY_MAGIC_SORT = cSort;
		this.CAPACITY_GENERAL = cGeneral;
		initAllThreads();
	}
	
	public static ThreadPoolManager getThreadPoolmanager() {
		return getThreadPoolInstance(5);
	}
	
	private void initAllThreads() {
		for (Integer i = 0; i < CAPACITY_MAGIC_ADD; i++) {
			Thread thread = new Thread(new Worker<TaskOfMagic>(qAdd, "MAGIC ADD WORKER # " + i, Constant.WORKER_TYPE_ADD));
			thread.start();
		}
		for (Integer i = 0; i < CAPACITY_MAGIC_SUB; i++) {
			Thread thread = new Thread(new Worker<TaskOfMagic>(qSub, "MAGIC SUB WORKER # " + i, Constant.WORKER_TYPE_SUB));
			thread.start();
		}
		for (Integer i = 0; i < CAPACITY_MAGIC_MUL; i++) {
			Thread thread = new Thread(new Worker<TaskOfMagic>(qMul, "MAGIC MUL WORKER # " + i, Constant.WORKER_TYPE_MUL));
			thread.start();
		}
		for (Integer i = 0; i < CAPACITY_MAGIC_SORT; i++) {
			Thread thread = new Thread(new Worker<TaskOfMagic>(qSort, "MAGIC SORT WORKER # " + i, Constant.WORKER_TYPE_SORT));
			thread.start();
		}
		for (Integer i = 0; i < CAPACITY_GENERAL; i++) {
			Thread thread = new Thread(new Worker<TaskOfMagic>(allRequestQ, "ALL REQUEST HANDLER WORKER # " + i, Constant.WORKER_TYPE_REQUEST_HANDLER));
			thread.start();
		}
	}
	
	public void submitTask(TaskOfMagic r) {
		switch(r.gettType()) {
		case Constant.TASK_MAGIC_ADD:
			qAdd.enqueue(r);
			break;
		case Constant.TASK_MAGIC_SUB:
			qSub.enqueue(r);
			break;
		case Constant.TASK_MAGIC_MUL:
			qMul.enqueue(r);
			break;
		case Constant.TASK_MAGIC_SORT:
			qSort.enqueue(r);
			break;
		// when MagicMathServer calls this method, we always come to this place
		case Constant.WORKER_TYPE_REQUEST_HANDLER:
			allRequestQ.enqueue(r);
			break;
		}
	}

}
