package com.InterHJ.HJ.Codee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Crunchify.com
 * 
 */

public class ExecutorServiceClass {
	private static final int MYTHREADS = 50;
	private static ExecutorService executor;

	public ExecutorServiceClass() {
		executor = Executors.newFixedThreadPool(MYTHREADS);
	}

	public void shutdown() {
		executor.shutdown();
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");
	}

	public void startNewThread(Runnable element) {
		Runnable worker = element;
		executor.execute(worker);
	}
}
