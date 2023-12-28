package TrabalhoPratico1;

import java.io.InterruptedIOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class Main {
	/*
	 * Requirments
	 *
	 * 1 - Base program : publisher <-> consumer 1.1- 3 producers , each 100
	 * milliseconds DONE 1- CPU Load 2- Free RAM 3- Free Disk Space
	 * 
	 * 1.2 - N consumers , N == Config Variable 1.2.1- Consume Producer Data
	 * 1.2.1.2- Alarm - CPU over > than 80% 1.2.1.3- Alarm - Free RAM less < than
	 * 10% 1.2.1.4- Alarm - Storage less < than 20% 1.3- Case Alarm == true 1.3.1
	 * Alert to GUI Interface about the Alarm
	 * 
	 * TODO 2 - Fault Tolerance daemon Thread Verify every 1 second -->
	 * ThreadProducer.isDown ? ThreadConsumer.isDown ?
	 *
	 * TODO 2.1 - Verify if any ThreadProducer.isDown 2.1.1- If ThreadProducer don't
	 * produce values for 10 or more seconds
	 * 
	 * TODO 2.2 - Verify if any ThreadConsumer.isDown 2.2.1- If ThreadConsumer don't
	 * consume values for 30 or more seconds TODO 2.3 - Case Producer or Consumer
	 * down 2.3.1 - Instance new Thread to replace the down Thread
	 * 
	 * 
	 * 3 - The system should be configured to end the system safely There must be a
	 * button or a string command line to stop the program all threads must be
	 * finished before system shut down
	 * 
	 * 
	 */
	public static final ResourceMonitorGUI GUI = new ResourceMonitorGUI();

	public static final int NUMBER_CACHED_THREAD_POOLS_CONSUMERS = 1;
	public static final int NUMBER_CACHED_THREAD_POOLS_PRODUCERS = 1;
	public static final int NUMBER_MONITOR_THREAD_POOLS = 1;

	public static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };

	public static final int NUMBER_PRODUCERS = 3;
	public static final int NUMBER_CONSUMERS = 5;

	// Variable to dynamic start / stop program
	public static boolean isRunning = true;

	// Queue for the producers and consuemrs
	public static BlockingQueue<OutputSpec_v2> queue = new ArrayBlockingQueue<OutputSpec_v2>(100);

	// checking for any down thread
	// ARRAY TO store all my executors and will be checked by a deamon thread //
	public static CopyOnWriteArrayList<ExecutorService> executorsArrayList = new CopyOnWriteArrayList<>();
	public static CopyOnWriteArrayList<Thread> threadArrayList = new CopyOnWriteArrayList<>();
	public static ExecutorService monitorExecutor;
	// Cached thread pools, 1 for each, producers and consumers
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;

	public static void main(String[] args) throws InterruptedException {
		GUI.addAlert("Starting Program by default" + isRunning);
		if (!isRunning) {
			stopProgram();
		}
		if (isRunning) {
			if (executorsArrayList.isEmpty()) {
				startThreadPools();
				monitoringDeamonThread();
			}

		}

	}

	/*
	 * USE Case 2 - Monitoring threads that are stopped Replacing the stopped
	 * Threads by new ones
	 */
	public static void monitoringDeamonThread() {
		if(!monitorExecutor.isShutdown()) {
			MonitoringThread myThread = new MonitoringThread(producerExecutor, consumerExecutor);
			myThread.setDaemon(true);
			try {
			monitorExecutor.execute(myThread);
			myThread.start();
			Thread.yield();
			System.out.println("Stard monitoring thread");

			
				myThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			threadArrayList.add(myThread);
		}
		
	}

	/*
	 * USE CASE 3 - Safety when closing the program Stop program, stoppiung all
	 * executors and services removing all the executors from the array This is the
	 * safety method that closes the program
	 */

	public static void stopProgram() {
		// terminate Daemon Thread
		isRunning = false;

		// shutdown services executors
		for (int j = 0; j < executorsArrayList.size(); j++) {
			if (!executorsArrayList.get(j).isShutdown()) {
				executorsArrayList.get(j).shutdown();
				try {
					
					executorsArrayList.get(j).awaitTermination(5, TimeUnit.MILLISECONDS);
					executorsArrayList.get(j).shutdownNow();
				} catch (InterruptedException e) {

					executorsArrayList.get(j).shutdownNow();
					e.printStackTrace();
				}
			}

		}
		//close all threads
		for (int i = 0; i < threadArrayList.size(); i++) {
			try {
				threadArrayList.get(i).join();
				threadArrayList.get(i).interrupt();
				threadArrayList.remove(i);
			} catch (InterruptedException e) {
				threadArrayList.get(i).interrupt();
				e.printStackTrace();
			}
		}
		System.out.println(executorsArrayList.toString() + threadArrayList.toString());
	}

	/*
	 * USE CASE - 1 Private method to start thread pools with the logic of pools
	 * creation and executros
	 * 
	 */
	private synchronized static void startThreadPools() {

		// Starting 2 thread pools, 1 for Producers , 1 for Consumers
		if (executorsArrayList.isEmpty()) {
			for (int x = 0; x < NUMBER_CACHED_THREAD_POOLS_PRODUCERS; x++) {
				producerExecutor = Executors.newCachedThreadPool();
				executorsArrayList.add(producerExecutor);
			}
			for (int y = 0; y < NUMBER_CACHED_THREAD_POOLS_CONSUMERS; y++) {
				consumerExecutor = Executors.newCachedThreadPool();
				executorsArrayList.add(consumerExecutor);
			}
			for (int k = 0; k < NUMBER_MONITOR_THREAD_POOLS; k++) {
				monitorExecutor = Executors.newSingleThreadExecutor();
				executorsArrayList.add(monitorExecutor);
			}
		}

		while (isRunning) {

			// instance Consumers in Consumer Thread Pool
			for (int i = 1; i <= NUMBER_CONSUMERS; i++) {
				Thread c = new Thread(new Consumer(i, queue, isRunning, GUI));
				try {
					// execute task
					if (!consumerExecutor.isShutdown()) {
						consumerExecutor.execute(c);
					}

					consumerExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
					threadArrayList.add(c);
				} catch (InterruptedException e) {
					consumerExecutor.shutdownNow();
					e.printStackTrace();
				}

			}
			// instance 3 producers
			// dynamic based on global variable
			for (int j = 1; j <= NUMBER_PRODUCERS; j++) {

				// case solution
				// create executors process for each type of output
				Thread p;
				switch (j) {
				case 1: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "CPU"));
					break;
				}
				case 2: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "RAM"));
					break;
				}
				case 3: {
					p = new Thread(new Producer(j, queue, isRunning, GUI, "DISK_SPACE"));
					;
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + j);
				}

				try {
					// execute task
					if (!producerExecutor.isShutdown()) {
						producerExecutor.execute(p);
						threadArrayList.add(p);
					}
					producerExecutor.awaitTermination(10, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					producerExecutor.shutdownNow();
					e.printStackTrace();
				}

			}

		}

	}
	// old stop program???
	/*
	 * if (!threadArrayList.isEmpty()) { for (int i = 0; i < threadArrayList.size();
	 * i++) { if (threadArrayList.get(i).isAlive()) { try {
	 * threadArrayList.get(i).join();
	 * 
	 * } catch (InterruptedException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } threadArrayList.get(i).interrupt(); }
	 * 
	 * } }
	 * 
	 * if (monitoringThread != null && monitoringThread.deamonThreadIsAlive()) {
	 * monitoringThread.terminateThread(); } // stop thread pools if
	 * (!executorsArrayList.isEmpty()) { for (ExecutorService executorService :
	 * executorsArrayList) { executorService.shutdown(); try { if
	 * (!executorService.awaitTermination(60, TimeUnit.MILLISECONDS))
	 * executorService.shutdown(); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block executorService.shutdown();
	 * Thread.currentThread().interrupt(); e.printStackTrace(); }
	 * executorsArrayList.remove(executorService);
	 * 
	 * System.out.println( "removing " + executorService.toString() +
	 * " terminated? " + executorService.isShutdown()); }
	 * System.out.println("conusmer" + consumerExecutor.toString());
	 * System.out.println("producer" + producerExecutor.toString()); }
	 * 
	 * // terminate program isRunning = false; System.out.println("stop program");
	 */
}
