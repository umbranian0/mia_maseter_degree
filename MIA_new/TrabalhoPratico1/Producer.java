package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.Spring;

public class Producer implements Runnable {
	// each 100 miliseconds
	public static final int RUNNING_TIME_MS = 100;

	private static final String[][] TYPE_PRODUCERS = {{"CPU"}, {"RAM"}, {"DISK_SPACE"}};
	
	private BlockingQueue<OutputSpec_v2> queue;
	private int id;
	private String type;
	private boolean started;
	private ResourceMonitorGUI gui;

	public Producer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}

	public Producer(int id, BlockingQueue<OutputSpec_v2> queue, boolean start, ResourceMonitorGUI gui, String type) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
		this.type = type;
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
	public void interruptThread() {
		Thread.currentThread().interrupt();
	}
	private synchronized double getRam() {
		double ram = 0;
		if (ram != 0) {
			return ram;
		}
		try {
			ram = ResourceMonitorUtils.getFreeRAM();
		} catch (Exception e) {
			//System.out.println("error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());
			if (e.getMessage() == "Invalid free RAM percentage.") {
				ram = getRam();
			}
		}
		return ram;
	}

	private synchronized double getCpu() {
		double cpu = 0;

		if (cpu != 0) {
			return cpu;
		}
		try {
			cpu = ResourceMonitorUtils.getCpuLoad();
		} catch (Exception e) {
			//System.out.println("error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());
			if (e.getMessage() == "Invalid free RAM percentage.") {
				cpu = getCpu();
			}
		}
		return cpu;
	}

	private synchronized double getFreeDisk() {
		double freeDiskSpace = 0;
		if (freeDiskSpace != 0) {
			return freeDiskSpace;
		}
		try {
			freeDiskSpace = ResourceMonitorUtils.getFreeDiskSpace();
		} catch (Exception e) {
			//System.out.println("error : " + e.getMessage() + " , cause: e.getCause()" + e.getCause());

			if (e.getMessage() == "Invalid free RAM percentage.") {
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
				if (isStartProducer()) {
					// each 100 miliseconds
					Thread.sleep(RUNNING_TIME_MS);
					// version 1 object
					// OutputSpec out = new OutputSpec(getCpu(), getRam(), getFreeDisk());
					OutputSpec_v2 out= new OutputSpec_v2(getValue(getType()), getType());
					queue.put(out);
					System.out.println("Producer  id: " + this.getId() + " , out: " + out.toString());

					
				} else {
					gui.addAlert("Closing program");
					setStart(false);
					interruptThread();
				}

			}
		} catch (InterruptedException e) {
			System.out.println("error: " + e.toString());
		}
	}



	@Override
	public String toString() {

		return "Producer [queue=" + getQueue() + ", id=" + id + "] \n";

	}

}
