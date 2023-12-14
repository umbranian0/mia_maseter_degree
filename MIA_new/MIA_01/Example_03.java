package MIA_01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example_03 {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		        //final Object o1 = new Object();
		        //final Object o2 = new Object();

		        final Lock o1 = new ReentrantLock();
		        final Lock o2 = new ReentrantLock();
		        Thread t1 = new Thread(){
		            @Override
		            public void run(){
		                try {
		                    //synchronized (o1)
		                    o1.lockInterruptibly();
		                    Thread.sleep(1000);
		                    //synchronized (o2){}
		                    o2.lockInterruptibly();
		                } catch (InterruptedException e) {
		                    System.out.println("t1 interrupted");
		                }
		            }
		        };

		        Thread t2 = new Thread(){
		            @Override
		            public void run(){

		                try {
		                    //synchronized (o2){
		                    o2.lockInterruptibly();
		                        Thread.sleep(1000);
		                    o1.lockInterruptibly();
		                        //synchronized (o1){}
		                   // }
		                } catch (InterruptedException e) {
		                    System.out.println("t2 interrupted");
		                }
		            }
		        };

		        t1.start();t2.start();

		        Thread.sleep(2000);
		        t1.interrupt();t2.interrupt();
		        t1.join();t2.join();
		    

		 
	}

}
