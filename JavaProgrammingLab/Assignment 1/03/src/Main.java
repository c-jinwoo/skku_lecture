/**
 * Java Programming
 * Assignment 1-03
 * 2020315798
 * Choi Jin Woo
 */

import java.util.Scanner;

public class Main{
	public static void main(String[] args){
		int powerNum = 0;			// for storing input value of guessed power of the number
		int baseNum = 0;			// for storing input value of base number
		int resultNum = 1;			// to calculate power of input base number
		int power = 0;				// to print power later if the guessed number is a power number
		boolean result = false;		// for escaping iterator 
				
		System.out.print("Enter the number: ");
		Scanner input = new Scanner(System.in);
		baseNum = input.nextInt();

		System.out.print("Enter your guessed power of above number: ");
		powerNum = input.nextInt();
		
		/**
		 * Iterator : power the base number until if the number is greater than or equal to the guessed one
		 */
		while(!result){
			power++;											// add 1 for every cycle to get exact power
			resultNum *= baseNum;								// multiply base number for every cycle
			
			// If the result is equal to guessed one, stop the while loop and print the result
			if(resultNum == powerNum){
				System.out.println("===================");
				System.out.println("power = " + power);
				System.out.println("Output : true");
				result = true;
			}
			else if(resultNum > powerNum){
				System.out.println("===================");
				System.out.println("Output : false");
				result = true;
			}					
		}
	}
}