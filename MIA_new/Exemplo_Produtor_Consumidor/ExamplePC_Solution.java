package Exemplo_Produtor_Consumidor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class ExamplePC_Solution
{
    public static void main(String[] args) throws InterruptedException
    {
        class Producer extends Thread {
            private BlockingQueue<Integer> queue;

            public Producer(BlockingQueue<Integer> queue) {
                this.queue = queue;
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        int rNumber = ThreadLocalRandom.current().nextInt(-1, 101);
                        System.out.println("P: " + rNumber);
                        queue.put(rNumber);
                        if (rNumber == -1) break;
                    }
                } catch (InterruptedException e) { }
            }
        }

        class Consumer extends Thread {
            private BlockingQueue<Integer> queue;

            public Consumer(BlockingQueue<Integer> queue) {
                this.queue = queue;
            }

            @Override
            public void run() {
                try {
                    while (true) {
                        int number = queue.take();
                        
                        System.out.println("C: " + number);
                        if (number == -1) break;
                    }
                } catch (InterruptedException e) { }
            }
        }

        @SuppressWarnings("rawtypes")
		BlockingQueue queue = new ArrayBlockingQueue<Integer>(5);
        @SuppressWarnings("unchecked")
		Thread producer = new Producer(queue);
        @SuppressWarnings("unchecked")
		Thread consumer = new Consumer(queue);
        producer.start(); consumer.start();
        producer.join(); consumer.join();
    }
}