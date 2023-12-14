package MIA_01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example_02 {

	public static void main(String[] args)  throws InterruptedException{
		// TODO Auto-generated method stub
		
		        class Counter {
		            private int count = 0;
		            private final Lock lock = new ReentrantLock();

		            //dado que o codigo trabalha com o lock
		            //entao o synchronized sai dos metods
		            //public synchronized void increment(){
		            public void increment(){
		                try{
		                    lock.lock();
		                    this.count ++;
		                }finally {
		                    lock.unlock();
		                }
		            }
		            public int getCount(){
		                //dado que o codigo trabalha com o lock
		                //entao o synchronized sai dos metods
		                //synchronized (this){
		                    try{
		                        lock.lock();
		                        return count;
		                    }finally {
		                        lock.unlock();
		                    }
		                //}
		            }
		        }

		        final Counter counter = new Counter();
		        class CountingThread extends Thread{
		            @Override
		            public void run(){
		               for ( int i = 0 ; i< 10000 ; i ++)
		                   counter.increment();
		            }

		        }
		        CountingThread t1 = new CountingThread();
		        CountingThread t2 = new CountingThread();
		        t1.start();
		        t2.start();
		        t1.join();
		        t2.join();

		        System.out.println(counter.getCount());
		    }

	

}
