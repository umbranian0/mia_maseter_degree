package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Producer implements Runnable {
	// each 100 miliseconds
	public static final int RUNNING_TIME_MS = 100;
	// public static final int RUNNING_TIME_MS = 10000;
	private static final String[][] TYPE_PRODUCERS = { { "CPU" }, { "RAM" }, { "DISK_SPACE" } };

	private BlockingQueue<OutputSpec_v2> queue;
	private int id;
	private String type;
	private boolean started;
	private ResourceMonitorGUI gui;

	private ConcurrentHashMap<Runnable, Long> lastExecutionTimes;

	public Producer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}

	public Producer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start, ResourceMonitorGUI gui, String type,
			ConcurrentHashMap<Runnable, Long> lastExecutionTimes) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
		this.type = type;

		this.lastExecutionTimes = lastExecutionTimes;
	}

	public ResourceMonitorGUI getGui() {
		return gui;
	}

	public void setGui(ResourceMonitorGUI gui) {
		this.gui = gui;
	}

	public BlockingQueue<OutputSpec_v2> getQueue() {
		return queue;
	}

	public int getId() {
		return id;
	}

	public boolean isStartProducer() {
		return started;
	}

	public void setStart(boolean start) {
		this.started = start;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private double getRam() {
		double ram = 0;

		try {
			ram = ResourceMonitorUtils.getFreeRAM();
		} catch (Exception e) {

			gui.addAlert("Stopping Producer error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());
			if (e.getMessage() == "Invalid free RAM percentage.") {
				gui.addAlert("Producer : "+e.getMessage() +"  .Trying again..");
				ram = getRam();
			}
		}

		return ram;
	}

	private double getCpu() {
		double cpu = 0;

		try {
			cpu = ResourceMonitorUtils.getCpuLoad();

		} catch (Exception e) {

			gui.addAlert("Stopping Producer error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());
			if (e.getMessage() == "Invalid free RAM percentage.") {
				gui.addAlert("Producer : "+e.getMessage() +"  .Trying again..");
				cpu = getCpu();
			}
		}

		return  cpu;
	}

	private double getFreeDisk() {
		double freeDiskSpace = 0;

		try {
			freeDiskSpace = ResourceMonitorUtils.getFreeDiskSpace();
		} catch (Exception e) {

			gui.addAlert("Stopping Producer error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());
			if (e.getMessage() == "Invalid free RAM percentage.") {
				gui.addAlert("Producer : " + e.getMessage() + "  .Trying again..");
				freeDiskSpace = getFreeDisk();
			}
		}

		return freeDiskSpace;
	}

	private double getValue(String type) {

		switch (type) {
		case "CPU": {
			return getCpu();

		}
		case "RAM": {
			return getRam();

		}
		case "DISK_SPACE": {
			return getFreeDisk();

		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}

	}

	@Override
	public void run() {
		try {

			while (isStartProducer()) {
				long sysTime = System.currentTimeMillis();
				lastExecutionTimes.put(this, sysTime);
				OutputSpec_v2 out = new OutputSpec_v2(getValue(getType()), getType(), getId(), -1, sysTime);
				//System.out.println("Producer  id : " + id + " , produced object: " + out.toString());
				queue.offer(out, 1, TimeUnit.SECONDS);
				// each 100 miliseconds
				Thread.sleep(RUNNING_TIME_MS);
			}
		} catch (InterruptedException e) {
			
			System.out.println("Producer exception: " + e.toString());
		}
	}

	@Override
	public String toString() {
		return "Producer [queue=" + getQueue() + ", id=" + id + "] \n";

	}

}
