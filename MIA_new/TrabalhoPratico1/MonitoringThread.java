package TrabalhoPratico1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;


class MonitoringThread extends Thread implements Runnable {
	private final ExecutorService executor;
	private final Map<Runnable, Long> lastExecutionTimes = new ConcurrentHashMap<>();

	private boolean isActive;
	private CopyOnWriteArrayList<Thread> threadArrayList;
	private ResourceMonitorGUI gui;

	// constructors
	public MonitoringThread(ExecutorService executor) {
		this.executor = executor;
		this.isActive = true;
	}

	public MonitoringThread(ExecutorService executor, boolean isActive,
			CopyOnWriteArrayList<Thread> threadArrayList, ResourceMonitorGUI gui) {
		this.executor = executor;
		this.isActive = true;
		this.threadArrayList = threadArrayList;
		this.gui = gui;
	}

	// accessors
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


	public Map<Runnable, Long> getLastExecutionTimes() {
		return lastExecutionTimes;
	}



	// methods
	@Override
	public void run() {
		while (isActive()) {

			// Verifica se algum produtor ou consumidor está inativo e reinicia
			if (!Thread.currentThread().isAlive()) {
				setActive(false);
			}

			// Sleep for a short duration to avoid tight-looping
			if (!executor.isShutdown()) {
				checkAndRestartThreads_v2(executor);
				
			}
			
		}
	}


	private synchronized void checkAndRestartThreads_v2(ExecutorService executorService) {
		if (threadArrayList == null) {
			return;
		}

		for (int i = 0; i < threadArrayList.size(); i++) {
			Thread thread = (Thread) threadArrayList.get(i);

			// Task has been inactive for the specified threshold, restart it
			if (!thread.isAlive()) {
				threadArrayList.get(i).interrupt();
				executorService.execute(thread);
				//System.out.println("Thread: " + thread.getClass() + " Restarting task: " + thread.toString());
				gui.addAlert(" Restarting task: " + thread.toString());
				
			}

		}

	}

}
