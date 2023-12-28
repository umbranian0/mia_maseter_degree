package TrabalhoPratico1;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class MonitoringThread extends Thread implements Runnable {
	private final ExecutorService producerExecutor;
	private final ExecutorService consumerExecutor;
	private final Map<Runnable, Long> lastExecutionTimes = new ConcurrentHashMap<>();
	private final static int PRODUCER_TIMMER = 1;
	private final static int CONSUMER_TIMMER = 3;
	private boolean isActive;
	private CopyOnWriteArrayList<Thread> threadArrayList;

	// constructors
	public MonitoringThread(ExecutorService producerExecutor, ExecutorService consumerExecutor) {
		this.producerExecutor = producerExecutor;
		this.consumerExecutor = consumerExecutor;
		this.isActive = true;
	}

	public MonitoringThread(ExecutorService producerExecutor, ExecutorService consumerExecutor, boolean isActive,
			CopyOnWriteArrayList<Thread> threadArrayList) {
		this.producerExecutor = producerExecutor;
		this.consumerExecutor = consumerExecutor;
		this.isActive = isActive;
		this.threadArrayList = threadArrayList;
	}

	// accessors
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public ExecutorService getProducerExecutor() {
		return producerExecutor;
	}

	public ExecutorService getConsumerExecutor() {
		return consumerExecutor;
	}

	public Map<Runnable, Long> getLastExecutionTimes() {
		return lastExecutionTimes;
	}

	public static int getProducerTimmer() {
		return PRODUCER_TIMMER;
	}

	public static int getConsumerTimmer() {
		return CONSUMER_TIMMER;
	}

	// methods
	@Override
	public void run() {
		while (isActive()) {

			// Verifica se algum produtor ou consumidor está inativo e reinicia
			if (!Thread.currentThread().isAlive()) {
				setActive(false);
			}
			checkAndRestartThreads_v2(producerExecutor, PRODUCER_TIMMER);
			checkAndRestartThreads_v2(consumerExecutor, CONSUMER_TIMMER);

			// System.out.println("run deamon monitoring thread");
			try {
				// Sleep for a short duration to avoid tight-looping
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				currentThread().interrupt();
				// e.printStackTrace();
			}
		}
	}

	private void checkAndRestartThreads_v2(ExecutorService executorService, int secondsThreshold) {
		if (threadArrayList == null) {
			return;
		}
		for (int i = 0; i < threadArrayList.size(); i++) {
			Thread thread = (Thread) threadArrayList.get(i);
			if (!thread.isAlive()) {
				executorService.execute(thread);
			}

		}
		try {
			// Sleep for a short duration to avoid tight-looping
			Thread.sleep(secondsThreshold * 1000);
		} catch (InterruptedException e) {
			currentThread().interrupt();
			// e.printStackTrace();
		}
	}

	

}
