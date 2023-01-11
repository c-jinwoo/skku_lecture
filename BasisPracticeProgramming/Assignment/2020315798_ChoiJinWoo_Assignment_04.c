/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 4
 *	About	 	: Currency exchange with function
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
 
/**
 *	Function	: CurrencyConversion()
 *	Todo		: return converted amount by multiplying amount in Won and the rate
 */	
float CurrencyConversion(float AmountInWon, float rate){
	return AmountInWon * rate; 	
}
 
int main(void){
 	char userSelect = 0;																		// for storing userSelect value(A~E)
	
	/**
	 *	For-Loop : infinite loop until the user inputs 5
	 */	
	for(;userSelect != 'E';){
		printf("Please choose which currency you want to convert:\n");
		printf("A - Korean Won to US Dollar (Exchange Rate: 0.000905)\n");
		printf("B - Korean Won to Euro (Exchange Rate: 0.000807350908)\n");
		printf("C - Korean Won to Japanese Yen (Exchange Rate: 0.0919061643)\n");
		printf("D - Korean Won to Chinese RMB (Exchange Rate: 0.00603703605)\n");
		printf("E - Quit\n");
		
		printf("Enter your option: ");															// prompt user to choose between 1 to 5
		scanf(" %c", &userSelect);

		int userAmount = 0;																		// for storing user's input amount in Won
		float rate = 0;																			// for storing currency rate with given value

		/**
 		 *	Switch Statement : prompt user to enter the amount in Won, calculate with rate and then print with double type value for each cases
 		 */
		switch(userSelect){
			case 'A' :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.000905;				
				printf("%d Won equals to %f USD\n\n", userAmount, CurrencyConversion((float)userAmount, rate));
				break;
			case 'B' :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.000807350908;
				printf("%d Won equals to %f Euro\n\n", userAmount, CurrencyConversion((float)userAmount, rate));
				break;
			case 'C' :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.0919061643;
				printf("%d Won equals to %f Yen\n\n", userAmount, CurrencyConversion((float)userAmount, rate));
				break;
			case 'D' :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.00603703605;
				printf("%d Won equals to %f RMB\n\n", userAmount, CurrencyConversion((float)userAmount, rate));
				break;
			case 'E' :
				break;
			default :
				printf("You entered an invalid input.\n\n"); 
		}	
	}	
 }
