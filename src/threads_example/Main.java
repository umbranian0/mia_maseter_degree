/**
 * 
 */
package threads_example;

/**
 * 
 */
public class Main {

	/*
	 * This main will be used to demonstrate some examples for: Threads Objects
	 * Executors - high-level concurrency objects
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("im in main to threads_example pakcage");

		ExampleRunnable exampleRunnable = new ExampleRunnable(false);
		ExampleThread exampleThread = new ExampleThread(false);
		// first lets define and stage a Thread
		exampleRunnable.main(args);
		exampleThread.main(args);
		//(new ExampleThread()).start();
		//(new Thread(new ExampleRunnable())).start();

	}

}
