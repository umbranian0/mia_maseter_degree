package Mia_01_exercicion;

import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		int inputUser;
		//requirmen
		//ask a limit
		System.out.println("Define a Limit to calcule sum of prime numbers:");
		try (Scanner s = new Scanner(System.in)) {
			inputUser = s.nextInt();
			System.out.println("Select Value:"+ inputUser);
			//sum all prime values  until a limit
		}
		
		SumPrimeTask.main(args,inputUser);
		


	}

}
