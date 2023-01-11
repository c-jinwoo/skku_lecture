/** 
 * Java Programming
 * Assignment 1-02
 * 2020315798
 * Choi Jin Woo
 */

import java.util.Scanner;

public class Main{
	public static void main(String[] args){		
		System.out.print("Enter number of days: ");		
		Scanner input = new Scanner(System.in);	
		
		int dDay = input.nextInt();			// for storing whole number of days					
		int entireWeeks = dDay / 7;			// for counting entire weeks
		int remainderDays = dDay % 7;		// for counting remaining days
		double moneyForWeeks = 0;			// for storing money saved during entire weeks
		double moneyForDays = 0;			// for storing money saved during remaining days
		double totalMoney = 0;				// for storing both kinds of money

		// Kilo unit for the money is omitted, calculated later
		// Iterating to sum from 2 to (entire week + 1) because the amount is (week + 1) per every week
		for(int i = 2;i <= entireWeeks + 1;i++){
			moneyForWeeks += i;
		}
		moneyForWeeks *= 7;									// multiply 7 : there are 7 days for the every entire weeks
		
		moneyForDays = (entireWeeks + 2) * remainderDays;	// for the remaining days, the amount is (week + 2). Multiply by remaing days
		
		totalMoney = moneyForWeeks + moneyForDays;
		
		System.out.println("==================");
		System.out.printf("Jin's total savings after %d days: %.2f won", dDay, totalMoney * 1000);
	}
}