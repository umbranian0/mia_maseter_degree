package TrabalhoPratico1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonitoringThread extends Thread {

	private final ExecutorService producerExecutor;
	private final ExecutorService consumerExecutor;
	private final ConcurrentHashMap<Runnable, Long> lastExecutionTimes;

	private final AtomicBoolean isActive;
	final ResourceMonitorGUI gui;
	private static final int PRODUCER_THRESHOLD_SECONDS = 1;
	private static final int CONSUMER_THRESHOLD_SECONDS = 3;

	public MonitoringThread(ExecutorService producerExecutor, ExecutorService consumerExecutor, ResourceMonitorGUI gui,
			ConcurrentHashMap<Runnable, Long> lastExecutionTimes) {
		this.producerExecutor = producerExecutor;
		this.consumerExecutor = consumerExecutor;
		this.lastExecutionTimes = lastExecutionTimes;
		this.gui = gui;
		isActive = new AtomicBoolean(true);
		setDaemon(true);
		start();
	}

	public void shutdown() {
		isActive.set(false);
	}

	@Override
	public void run() {
		while (isActive.get() && !producerExecutor.isShutdown() || !consumerExecutor.isShutdown()) {
			checkAndRestartProducers();
			checkAndRestartConsumers();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

	private void checkAndRestartProducers() {
		checkAndRestartThreads(producerExecutor, PRODUCER_THRESHOLD_SECONDS);
	}

	private void checkAndRestartConsumers() {
		checkAndRestartThreads(consumerExecutor, CONSUMER_THRESHOLD_SECONDS);
	}

	private void checkAndRestartThreads(ExecutorService executorService, int secondsThreshold) {
		gui.addAlert("Running monitoring thread run ");
		
		long currentTime = System.currentTimeMillis();
		
		for (Runnable task : ((ThreadPoolExecutor) executorService).getQueue()) {
			long lastExecutionTime = lastExecutionTimes.getOrDefault(task, 0L);

			if (lastExecutionTime != 0 && currentTime - lastExecutionTime > secondsThreshold * 1000) {
				// The task has been inactive for more than the threshold (in milliseconds)
				// Restart the task (replace it with a new one)
				executorService.execute(task);
				lastExecutionTimes.put(task, currentTime); // Update last execution time
				gui.addAlert("Executing new task - " + task.toString());
				System.out.println("Executing new task - " + task.toString());
			}
		}
	}
}
