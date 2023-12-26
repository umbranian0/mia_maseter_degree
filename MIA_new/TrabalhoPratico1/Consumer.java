package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Consumer implements Runnable {
	public static final int TIMEOUT_SEC = 5;
	//alarms
	public static final double CPU_ALARM_PERCENTAGE = 0.8;
	public static final double RAM_ALARM_PERCENTAGE = 0.9;
	public static final double DISK_SPACE_ALARM_PERCENTAGE = 0.8;

	private BlockingQueue<OutputSpec> queue;
	private int id;
	private boolean started;
	private ResourceMonitorGUI gui;
	
	//constructors
	public Consumer(int id, BlockingQueue<OutputSpec> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}
	
	public Consumer(int id, BlockingQueue<OutputSpec> queue, boolean start, ResourceMonitorGUI gui) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
	}

	public BlockingQueue<OutputSpec> getQueue() {
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
		if (this.started == false)
			interrupt();
	}

	// can be private method
	public void interrupt() {
		this.interrupt();
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

	private synchronized void alertFlow(OutputSpec obj) {
		if (isCpuAlarm(obj.getCpu())) {
			gui.addAlert("ALERT CPU is too HIGH! ALERT .."+ obj.getCpu());
		}
			
		if (isRamAlarm(obj.getRam())){
			gui.addAlert("ALERT RAM is too HIGH! ALERT .."+ obj.getRam());
		}
		if (isDiskSpaceAlarm(obj.getDiskSpace())){
			gui.addAlert("ALERT DISTO SPACE\" is too HIGH! ALERT .."+ obj.getDiskSpace());
		}
			
	}

	@Override
	public void run() {
		try {
			while (true) {
				OutputSpec obj = queue.poll(TIMEOUT_SEC, TimeUnit.SECONDS);
				if (obj != null) {
					System.out.println("Consumer  id : " + id + " , consumed object: " + obj.toString());

					alertFlow(obj);
				}

			}
		} catch (InterruptedException e) {
		}
	}
	// TODO - Alerts

	@Override
	public String toString() {
		return "Consumer [queue=" + queue + ", id=" + id + "]";
	}

}
