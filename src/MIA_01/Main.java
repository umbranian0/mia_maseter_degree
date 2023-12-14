package MIA_01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");
        //thread_example();

        //calculator();
        dead_locks();
    }
    public static void dead_locks() throws InterruptedException{
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

    public static void calculator() throws InterruptedException{
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

    public static void thread_example() throws InterruptedException{
        class ThreadExample extends Thread{
            public void run(){
                System.out.println("hello from new thread");
            }
        }
        ThreadExample t1 = new ThreadExample();
        //deamon = tarefas/threads a auxiliar threads (usadas no fundo por um utilizador)
        t1.setDaemon(true);
        t1.start();
        Thread.yield();
        //join espera todas as threads acabarem para fazer um processo sequencial
        t1.join();
        System.out.println("hello from main thread");
    }
}