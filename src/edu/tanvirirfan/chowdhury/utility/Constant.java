package edu.tanvirirfan.chowdhury.utility;

public final class Constant {
	public static final int TASK_MAGIC_ADD = 0;
	public static final int TASK_MAGIC_SUB = TASK_MAGIC_ADD+1;
	public static final int TASK_MAGIC_MUL = TASK_MAGIC_SUB+1;
	public static final int TASK_MAGIC_SORT = TASK_MAGIC_MUL+1;
	
	public static final int WORKER_TYPE_ADD = 1;
	public static final int WORKER_TYPE_SUB = WORKER_TYPE_ADD+1;
	public static final int WORKER_TYPE_MUL = WORKER_TYPE_SUB+1;
	public static final int WORKER_TYPE_SORT = WORKER_TYPE_MUL+1;
	public static final int WORKER_TYPE_REQUEST_HANDLER = WORKER_TYPE_SORT+1;
	
	public static final int DEFAULT_PORT = 10000;
	public static final String DEFAULT_HOST = "localhost";
	public static final int INVALID_PORT = -1;
	
	public static final int NUMBER_OF_OPERATION_PER_CLIENT = 1000;
	public static final int NUMBER_OF_THREAD_PER_CLIENT = 5;
	
//	public static final float FLOAT_MIN = Float.MIN_VALUE;
//	public static final float FLOAT_MAX = Float.MAX_VALUE;
	
	public static final float FLOAT_MIN = -1000000;
	public static final float FLOAT_MAX = 1000000;
	
	public static final int K_MIN = 500;
	public static final int K_MAX = 1000;
	
	public static final int SORT_K_MIN = 100;
	public static final int SORT_K_MAX = 1000;
	
	public static final int SORT_INT_MIN = 1;
	public static final int SORT_INT_MAX = 1000000;
	
	public static final int INDEX_OPERATION_TYPE = 0;
	public static final int INDEX_FIRST_DATA = 1;
	public static final int INDEX_SECOND_DATA = 2;
	public static final int INDEX_K = 3;
	
	public static final int INDEX_SORT_K = 1;
	public static final int INDEX_SORT_INTEGERS = 2;
	
	public static final String INPUT_SPLITTER = "#";
	public static final String SORT_INTEGER_SPLITTER = " ";
	
	public static final int SERVER_REPORTING_INTERVAL = 1000;
	
	public static final int CONNECTION_CHECK = 1;
}
