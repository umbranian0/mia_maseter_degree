package TrabalhoPratico1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Producer implements Runnable {
	// each 100 miliseconds
	public static final int RUNNING_TIME_MS = 100000;

	private BlockingQueue<OutputSpec> queue;
	private int id;
	private boolean started;
	private ResourceMonitorGUI gui;

	public Producer(int id, BlockingQueue<OutputSpec> queue, boolean start) {
		this.queue = queue;
		this.id = id;
		this.started = start;
	}

	public Producer(int id, BlockingQueue<OutputSpec> queue, boolean start, ResourceMonitorGUI gui) {
		this.queue = queue;
		this.id = id;
		this.started = start;
		this.gui = gui;
	}

	public ResourceMonitorGUI getGui() {
		return gui;
	}

	public void setGui(ResourceMonitorGUI gui) {
		this.gui = gui;
	}

	public BlockingQueue<OutputSpec> getQueue() {
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



	private synchronized double getRam() {
		double ram = 0;
		if (ram != 0) {
			return ram;
		}
		try {
			ram = ResourceMonitorUtils.getFreeRAM();
		} catch (Exception e) {
			System.out.println("error : " + e.getMessage() + "\n , cause: e.getCause()" + e.getCause());
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
			System.out.println("error : " + e.getMessage() + "\n , cause: e.getCause()" + e.getCause());
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
			System.out.println("error : " + e.getMessage() + "\n , cause: e.getCause()" + e.getCause());

			if (e.getMessage() == "Invalid free RAM percentage.") {
				freeDiskSpace = getFreeDisk();
			}
		}
		return freeDiskSpace;
	}


	@Override
	public void run() {
		try {
			while (this.isStartProducer()) {
				if (isStartProducer()) {
					OutputSpec out = new OutputSpec(getCpu(), getRam(), getFreeDisk());
					queue.put(out);
					System.out.println("Producer  id: " + this.getId() + " , out: " + out.toString());

					// each 100 miliseconds
					Thread.sleep(RUNNING_TIME_MS);
				} else {
					gui.addAlert("Closing program");
					setStart(false);
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
