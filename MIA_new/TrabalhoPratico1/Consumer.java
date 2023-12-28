package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
	public static final int TIMEOUT_SEC = 5;
	public static final int RUNNING_TIME_MS = 12000000;
	// alarms
	public static final double CPU_ALARM_PERCENTAGE = 0.80;
	public static final double RAM_ALARM_PERCENTAGE = 0.10;
	public static final double DISK_SPACE_ALARM_PERCENTAGE = 0.20;

	private static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };
	private BlockingQueue<OutputSpec_v2> queue;
	private int id;
	private boolean started;
	private ResourceMonitorGUI gui;

	// constructors
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
		return value < RAM_ALARM_PERCENTAGE;
	}

	private synchronized boolean isDiskSpaceAlarm(double value) {
		return value < DISK_SPACE_ALARM_PERCENTAGE;
	}

	private synchronized void alertFlow(OutputSpec_v2 obj, int consumerId) {
		String alert = "Consumer id :" + consumerId + " !!ALERT!! " + obj.getType() + " , is too HIGH! ALERT .."
				+ obj.getValue();
		boolean ringAlarm = false;
		switch (obj.type) {
		case "CPU": {
			if (isCpuAlarm(obj.getValue()))
				ringAlarm = true;
			break;
		}
		case "RAM": {
			if (isRamAlarm(obj.getValue()))
				ringAlarm = true;
			break;
		}
		case "DISK_SPACE": {
			if (isDiskSpaceAlarm(obj.getValue()))
				ringAlarm = true;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + obj.type);
		}
		// ring the alarm in gUI
		if (ringAlarm) {
			gui.addAlert(alert.toString());
			//System.out.println("consumer ALERT Check " + alert.toString());
		}
	}

	public void interruptThread() {
		Thread.currentThread().interrupt();
	}

	@Override
	public synchronized void run() {
		try {
			while (isStarted()) {

				// Thread.sleep(RUNNING_TIME_MS);
				OutputSpec_v2 obj = queue.poll(TIMEOUT_SEC, TimeUnit.SECONDS);
				if (obj != null) {
					//System.out.println("Consumer  id : " + id + " , consumed object: " + obj.toString());
					alertFlow(obj, id);
					
				}
				// Thread.sleep(RUNNING_TIME_MS);
			}
		} catch (InterruptedException e) {
			System.out.println("Consumer exception: " + e.toString());
			interruptThread();
		}
	}

	@Override
	public String toString() {
		return "Consumer [queue=" + queue + ", id=" + id + "]";
	}

}
