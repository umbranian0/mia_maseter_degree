package MIA_01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example9 extends Thread{
	private boolean hasFirstThreadEnded = false;
	private Lock lock = new ReentrantLock();
	private Condition cond = lock.newCondition();
	//fixed thread pool, é um limite maximo 
	//ExecutorService s = Executors.newFixedThreadPool(15);
	//codigo executado na threead
	//s.execute(new Thread(i));
	
	public void run() {
		new Thread()
		{
			@Override
			public void run() {
				while(true)
					first_step();
			}

			private void first_step() {
				// TODO Auto-generated method stub
				try {
					lock.lock();
					Thread.sleep(2000);
					hasFirstThreadEnded = true;
					//evento concretizado
					cond.signal();	
				}catch(InterruptedException ex) {
					
				}finally {
					lock.unlock();
					System.out.println("first step done");
				}
				
			}
		}.start();
		
		new Thread()
		{
			@Override
			public void run() {
				while(true)
					second_step();
			}

			private void second_step() {
				// TODO Auto-generated method stub
				try {
					lock.lock();
					//não podemos ter o acknolage
					while(!hasFirstThreadEnded)
					Thread.sleep(2000);
					
					hasFirstThreadEnded = false;
						
				}catch(InterruptedException ex) {
					
				}finally {
					lock.unlock();
					System.out.println("second step done");
				}
				
				
			}
		}.start();
		
	}

}
