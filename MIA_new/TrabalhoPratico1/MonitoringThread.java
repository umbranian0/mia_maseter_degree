package TrabalhoPratico1;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonitoringThread extends Thread {

	private final ExecutorService producerExecutor;
	private final ExecutorService consumerExecutor;
	private CopyOnWriteArrayList<Runnable> listProducerAndConsumer;
	private ExecutorService monitorExecutor;
	private final AtomicBoolean isActive;
	final ResourceMonitorGUI gui;
	private static final int PRODUCER_THRESHOLD_SECONDS = 10;
	private static final int CONSUMER_THRESHOLD_SECONDS = 30;

	public MonitoringThread(ExecutorService producerExecutor, 
			ExecutorService consumerExecutor,
			ResourceMonitorGUI gui,
			CopyOnWriteArrayList<Runnable> listProducerAndConsumer2, 
			ExecutorService monitorExecutor) {
		
		this.producerExecutor = producerExecutor;
		this.consumerExecutor = consumerExecutor;
		this.gui = gui;
		this.listProducerAndConsumer = listProducerAndConsumer2;
		this.monitorExecutor = monitorExecutor;
		this.isActive = new AtomicBoolean(true);
		
		setDaemon(true);
	}

	public void shutdown() {
		isActive.set(false);
	}

	@Override
	public void run() {
		while (isActive.get()) {
			if(main.isRunning) {
				checkAndRestartProducers();
				checkAndRestartConsumers();
				gui.addAlert("Running monitoring thread run ");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else
			{
				//not sure if neeeded
				monitorExecutor.shutdown();
				break;
			}
			
		}
	}

	private void checkAndRestartProducers() {
		checkAndRestartThreads(producerExecutor, PRODUCER_THRESHOLD_SECONDS);
	}

	private void checkAndRestartConsumers() {
		checkAndRestartThreads(consumerExecutor, CONSUMER_THRESHOLD_SECONDS);
	}

	private synchronized void checkAndRestartThreads(ExecutorService executorService, int secondsThreshold) {
		
		for (Runnable out : this.listProducerAndConsumer ) {
			
			Runnable object = out;
			listProducerAndConsumer.remove(object);
			
			long lastExecutionTime ;
			
			if(object instanceof Producer) {
				lastExecutionTime = ((Producer) object).getLastExecutionTimes();
				object =  ((Producer) object);
			}else {
				lastExecutionTime = ((Consumer) object).getLastExecutionTimes();
				object =  ((Consumer) object);
			}
			
			//check if is over time 
			if( isOverLastExecutionTime(lastExecutionTime, secondsThreshold)) {
				executorService.execute(object);
				//System.out.println("executed new object : " + object.toString());
				gui.addAlert("Restarted executor Object : " + object.toString());
				executorService.toString();
			}		
		}
	}
	
	private boolean isOverLastExecutionTime(long lastExecutionTime,int secondsThreshold ) {
		long currentTime = System.currentTimeMillis();
		
		if (lastExecutionTime != 0 && currentTime - lastExecutionTime > secondsThreshold * 1000) {
			// The task has been inactive for more than the threshold (in milliseconds)
			return true;
		}
		return false;
	}
}
