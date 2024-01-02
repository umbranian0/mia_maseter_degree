package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
	public static final int TIMEOUT_SEC = 5;
	public static final int RUNNING_TIME_MS = 100000;
	// alarms
	public static final double CPU_ALARM_PERCENTAGE = 0.8;
	public static final double RAM_ALARM_PERCENTAGE = 0.10;
	public static final double DISK_SPACE_ALARM_PERCENTAGE = 0.20;

	private static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };
	private BlockingQueue<OutputSpec_v2> queue;
	private int id;
	private boolean started;
	private ResourceMonitorGUI gui;
	private ConcurrentHashMap<Runnable, Long> lastExecutionTimes;

	// constructors
	public Consumer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}

	public Consumer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start, ResourceMonitorGUI gui,
			ConcurrentHashMap<Runnable, Long> lastExecutionTimes) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
		this.lastExecutionTimes = lastExecutionTimes;

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
	private boolean isCpuAlarm(double value) {
		return value > CPU_ALARM_PERCENTAGE;
	}

	private boolean isRamAlarm(double value) {
		return value < RAM_ALARM_PERCENTAGE;
	}

	private boolean isDiskSpaceAlarm(double value) {
		return value < DISK_SPACE_ALARM_PERCENTAGE;
	}

	private void alertFlow(OutputSpec_v2 obj, int consumerId) {
		String alert = "Consumer id :" + consumerId + " ,producer id :" + obj.getproducer_id() + " !!ALERT!! "
				+ obj.getType() + " ! ALERT .." + obj.getValue();

		boolean ringAlarm = false;

		switch (obj.getType()) {
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
			throw new IllegalArgumentException("Unexpected value: " + obj.getType());
		}

		// ring the alarm in gUI
		if (ringAlarm) {
			gui.addAlert(alert.toString());
		}
	}

	public void interruptThread() {
		Thread.currentThread().interrupt();
	}

	@Override
	public void run() {
		try {
			while (isStarted()) {

				long sysTime = System.currentTimeMillis();
				lastExecutionTimes.put(this, sysTime);
				OutputSpec_v2 obj = queue.poll(TIMEOUT_SEC, TimeUnit.SECONDS);

				if (obj != null) {
					obj.setConsumer_id(getId());
					alertFlow(obj, getId());
				}
				//Thread.sleep(RUNNING_TIME_MS);

			}
		} catch (InterruptedException e) {

			System.out.println("Consumer exception: " + e.toString());

		}
	}

	@Override
	public String toString() {
		return "Consumer [queue=" + getQueue() + ", id=" + getId() + "]";
	}

}
