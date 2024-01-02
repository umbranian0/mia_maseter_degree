package TrabalhoPratico1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Requirements
 *
 * 1 - Base program : publisher <-> consumer 1.1- 3 producers , each 100
 * milliseconds DONE 1- CPU Load 2- Free RAM 3- Free Disk Space
 * 
 * 1.2 - N consumers , N == Config Variable 1.2.1- Consume Producer Data
 * 1.2.1.2- Alarm - CPU over > than 80% 1.2.1.3- Alarm - Free RAM less < than
 * 10% 1.2.1.4- Alarm - Storage less < than 20% 1.3- Case Alarm == true 1.3.1
 * Alert to GUI Interface about the Alarm
 * 
 * 2 - Fault Tolerance daemon Thread Verify every 1 second -->
 * ThreadProducer.isDown ? ThreadConsumer.isDown ?
 *
 * 2.1 - Verify if any ThreadProducer.isDown 2.1.1- If ThreadProducer don't
 * produce values for 10 or more seconds
 * 
 * 2.2 - Verify if any ThreadConsumer.isDown 2.2.1- If ThreadConsumer don't
 * consume values for 30 or more seconds 2.3 - Case Producer or Consumer down
 * 2.3.1 - Instance new Thread to replace the down Thread
 * 
 * 
 * 3 - The system should be configured to end the system safely There must be a
 * button or a string command line to stop the program all threads must be
 * finished before system shut down
 * 
 * 
 */
public class main {
	public static final ResourceMonitorGUI GUI = new ResourceMonitorGUI();

	public static final int NUMBER_CACHED_THREAD_POOLS_CONSUMERS = 1;
	public static final int NUMBER_CACHED_THREAD_POOLS_PRODUCERS = 1;
	public static final int NUMBER_MONITOR_THREAD_POOLS = 1;

	public static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };

	public static final int NUMBER_PRODUCERS = 3;
	public static int numConsumers = -1;
	// last consumer times concurrent hash map
	public static ConcurrentHashMap<Runnable, Long> lastExecutionTimes = new ConcurrentHashMap<Runnable, Long>();
	// Queue for the producers and consuemrs
	public static BlockingQueue<OutputSpec_v2> queue = new ArrayBlockingQueue<OutputSpec_v2>(100);

	// Daemon thread
	public static MonitoringThread monitorinDeamonThread;
	// checking for any down thread
	// ARRAY TO store all my executors and will be checked by a deamon thread //
	public static CopyOnWriteArrayList<ExecutorService> executorsArrayList = new CopyOnWriteArrayList<>();

	// executors
	public static ExecutorService monitorExecutor;
	// Cached thread pools, 1 for each, producers and consumers
	public static ExecutorService producerExecutor;
	public static ExecutorService consumerExecutor;

	// Variable to dynamic start / stop program
	public static boolean isRunning = false;

	public static void main(String[] args) throws InterruptedException {

		// update variable based on UI
		while (numConsumers <= 0) {
			// Get the updated numConsumers value from the GUI
			Thread.sleep(1000); // Adjust the sleep time as needed
		}
		while (isRunning) {

			GUI.addAlert("Starting Program " + isRunning);
			if (isRunning) {
				startThreadPools(isRunning, numConsumers);

			} else {
				stopProgram(isRunning);
			}

		}

	}

	/*
	 * USE Case 2 - Monitoring threads that are stopped Replacing the stopped
	 * Threads by new ones
	 */
	public synchronized static void monitoringDeamonThread(boolean isRunning, ExecutorService consumerExecutor,
			ExecutorService producerExecutor) {
		if (monitorinDeamonThread == null) {
			monitorinDeamonThread = new MonitoringThread(producerExecutor, consumerExecutor, GUI, lastExecutionTimes);
			monitorExecutor.execute(monitorinDeamonThread);
		}
	}

	/*
	 * USE CASE - 1 Private method to start thread pools with the logic of pools
	 * creation and executors
	 * 
	 */
	private synchronized static void startThreadPools(boolean isRunning, int numConsumers) {

		// Starting 2 thread pools, 1 for Producers , 1 for Consumers
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

		while (isRunning) {

			// instance Consumers in Consumer Thread Pool
			for (int i = 1; i <= numConsumers; i++) {
				Consumer c = new Consumer(i, queue, isRunning, GUI, lastExecutionTimes);
				try {
					// execute task
					if (!consumerExecutor.isShutdown()) {
						lastExecutionTimes.put(c, System.currentTimeMillis());
						consumerExecutor.execute(c);
						consumerExecutor.awaitTermination(1, TimeUnit.SECONDS);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// use case 2 - restart threads that are down
			if (consumerExecutor != null && producerExecutor != null)
				monitoringDeamonThread(isRunning, consumerExecutor, producerExecutor);
			// instance 3 producers
			// dynamic based on global variable
			for (int j = 1; j <= NUMBER_PRODUCERS; j++) {

				// case solution
				// create executors process for each type of output
				Producer p;
				switch (j) {
				case 1: {
					p = new Producer(j, queue, isRunning, GUI, "CPU", lastExecutionTimes);
					break;
				}
				case 2: {
					p = new Producer(j, queue, isRunning, GUI, "RAM", lastExecutionTimes);
					break;
				}
				case 3: {
					p = new Producer(j, queue, isRunning, GUI, "DISK_SPACE", lastExecutionTimes);
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + j);
				}

				// execute task
				if (!producerExecutor.isShutdown()) {
					lastExecutionTimes.put(p, System.currentTimeMillis());
					producerExecutor.execute(p);
				}

			}

		}

	}

	/*
	 * USE CASE 3 - Safety when closing the program Stop program, stoppiung all
	 * executors and services removing all the executors from the array This is the
	 * safety method that closes the program
	 */

	public static void stopProgram(boolean isRunning) {
		// terminate Daemon Thread
		isRunning = false;
		// shutdown services executors
		for (int j = 0; j < executorsArrayList.size(); j++) {
			try {
				while (!executorsArrayList.isEmpty()) {
					if (!executorsArrayList.get(j).isShutdown())
						executorsArrayList.get(j).shutdown();

					executorsArrayList.get(j).awaitTermination(1, TimeUnit.SECONDS);

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
