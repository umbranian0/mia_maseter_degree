package TrabalhoPratico1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class tests_class {
	// test to Producer Class
		public static void test_Consumer() {

			BlockingQueue<OutputSpec> queue = new ArrayBlockingQueue<OutputSpec>(5);
			queue.add(new OutputSpec(0.5, 0.2, 0.6));
			queue.add(new OutputSpec(0.4, 0.2, 0.1));
			queue.add(new OutputSpec(0.6, 0.4, 0.5));
			Consumer consumer = new Consumer(1, queue, true);

			System.out.println("consumer:  " + consumer.toString());
		}

		// test to Producer Class
		public static void test_Producer() {

			BlockingQueue<OutputSpec> queue = new ArrayBlockingQueue<OutputSpec>(5);
			queue.add(new OutputSpec(0.5, 0.2, 0.6));
			queue.add(new OutputSpec(0.4, 0.2, 0.1));
			queue.add(new OutputSpec(0.6, 0.4, 0.5));
			Producer producer = new Producer(1, queue, true);

			System.out.println("producer:  " + producer.toString());
		}

		// This main is just to test the GUI. Please delete it!
		public static void test_ResourceMonitorGUI() {
			ResourceMonitorGUI resourceMonitorGUI = new ResourceMonitorGUI();
			resourceMonitorGUI.addAlert("CPU load is over 80%!");
			resourceMonitorGUI.addAlert("Free RAM is less than 10%!");
		}

		// This main is just to test the methods. Please delete it!
		public static void test_ResourceMonitorUtils() throws InterruptedException {
			while (true) {
				System.out.println("CPU Load (%): " + ResourceMonitorUtils.getCpuLoad());
				System.out.println("Free RAM (%): " + ResourceMonitorUtils.getFreeRAM());
				System.out.println("Free Disk Space (%): " + ResourceMonitorUtils.getFreeDiskSpace());
				System.out.println();
				Thread.sleep(2000);
			}
		}
}
