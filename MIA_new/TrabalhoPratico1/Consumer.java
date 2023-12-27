package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
	public static final int TIMEOUT_SEC = 5;
	public static final int RUNNING_TIME_MS = 10000;
	//alarms
	public static final double CPU_ALARM_PERCENTAGE = 0.8;
	public static final double RAM_ALARM_PERCENTAGE = 0.9;
	public static final double DISK_SPACE_ALARM_PERCENTAGE = 0.8;

	private static final String[][] TYPE_PRODUCERS = {{"CPU"}, {"RAM"}, {"DISK_SPACE"}};
	private BlockingQueue<OutputSpec_v2> queue;
	private int id;
	private boolean started;
	private ResourceMonitorGUI gui;
	
	//constructors
	public Consumer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}
	
	public Consumer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start, ResourceMonitorGUI gui) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
	}

	public BlockingQueue<OutputSpec_v2> getQueue() {
		return queue;
	}

	public int getId() {
		return id;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
		
	}


	// methods to initialize alarm
	private synchronized boolean isCpuAlarm(double value) {
		return value > CPU_ALARM_PERCENTAGE;
	}

	private synchronized boolean isRamAlarm(double value) {
		return value > RAM_ALARM_PERCENTAGE;
	}

	private synchronized boolean isDiskSpaceAlarm(double value) {
		return value > DISK_SPACE_ALARM_PERCENTAGE;
	}

	private synchronized void alertFlow(OutputSpec_v2 obj) {
		if (obj.type == "CPU") {
			this.isCpuAlarm(obj.getValue());
		}
		else if (obj.type == "RAM") {
			this.isRamAlarm(obj.getValue());
		}
		else if (obj.type == "DISK_SPACE") {
			this.isDiskSpaceAlarm(obj.getValue());
		}
		gui.addAlert("ALERT!! "+obj.getType() +" , is too HIGH! ALERT .."+ obj.getValue());
		
	}
	public void interruptThread() {
		Thread.currentThread().interrupt();
	}
	@Override
	public void run() {
		try {
			while (!queue.isEmpty()) {

				//Thread.sleep(RUNNING_TIME_MS);
				OutputSpec_v2 obj = queue.poll(TIMEOUT_SEC, TimeUnit.SECONDS);
				if (obj != null) {
					System.out.println("Consumer  id : " + id + " , consumed object: " + obj.toString());
					alertFlow(obj);
				}

			}
		} catch (InterruptedException e) {
		}
	}
	
	@Override
	public String toString() {
		return "Consumer [queue=" + queue + ", id=" + id + "]";
	}

}
