package Mia_01_exercicion;

import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int inputUser;
		//requirmen
		//ask a limit
		System.out.println("Press X to Stop program");
		try (Scanner s = new Scanner(System.in)) {
			inputUser = s.nextInt();
			System.out.println("Select Value:"+ inputUser);
			//sum all prime values  until a limit
		}
		
		SumPrimeTask task = new SumPrimeTask(inputUser,4);
		System.out.println("total: " +task.getTotalSum());
		System.out.println("Number of threads? " + task.getNumThreads());
		System.out.println("Sum of prime numbers up to " + task.getLimit() + ": " + task.getTotalSum());
		System.out.println("Time taken: " + (task.getEndTime() - task.getStartTime()) + " milliseconds");

		
		
		//test Constructor with thread
		SumPrimeTask task2 = new SumPrimeTask(inputUser,15);
		System.out.println("total: " +task2.getTotalSum());
		System.out.println("Number of threads? " + task2.getNumThreads());
		System.out.println("Sum of prime numbers up to " + task2.getLimit() + ": " + task2.getTotalSum());
		System.out.println("Time taken: " + (task2.getEndTime() - task2.getStartTime()) + " milliseconds");

	}
	
}
