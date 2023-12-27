package TrabalhoPratico1;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

public class MonitoringThread implements Runnable {
	private CopyOnWriteArrayList<ExecutorService> executorsArrayList;
	// timmer to run, in seconds
	private int timmer = 10;
	private ResourceMonitorGUI gui;

	// constructors
	public MonitoringThread(CopyOnWriteArrayList<ExecutorService> executorsArrayList, ResourceMonitorGUI gui) {
		this.executorsArrayList = executorsArrayList;
		this.gui = gui;
	}

	// assessors
	public CopyOnWriteArrayList<ExecutorService> getExecutorsArrayList() {
		return executorsArrayList;
	}

	public void setExecutorsArrayList(CopyOnWriteArrayList<ExecutorService> executorsArrayList) {
		this.executorsArrayList = executorsArrayList;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			for (int i = 1; i <= executorsArrayList.size(); i++) {

				if (getExecutorsArrayList().get(i).isShutdown()) {
					Object o = getExecutorsArrayList().clone();
					executorsArrayList.get(i).execute((Runnable) getExecutorsArrayList().clone());
					gui.addAlert("replace with new consumer " + o.toString());
				}
			}
			try {
				// Aguarda 1 segundo antes de verificar novamente
				Thread.sleep(timmer * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	// get Deamon status
	public boolean deamonThreadIsAlive() {
		return Thread.currentThread().isAlive();
	}

	public void terminateThread() {
		Thread.currentThread().interrupt();
	}

	// attributes for my deamon thread
	public void setDaemon(boolean b) {
		// TODO Auto-generated method stub
		Thread.currentThread().setDaemon(b);
		Thread.currentThread().start();
	}

}
