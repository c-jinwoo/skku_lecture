
 int main(void){
 	int userSelect = 0;																			// for storing userSelect value(1~5)
	
	/**
	 *	For-Loop : infinite loop until the user inputs 5
	 */	
	for(;userSelect != 5;){
		printf("Please choose which currency you want to convert:\n");
		printf("1 - Korean Won to US Dollar (Exchange Rate: 0.000905)\n");
		printf("2 - Korean Won to Euro (Exchange Rate: 0.000807350908)\n");
		printf("3 - Korean Won to Japanese Yen (Exchange Rate: 0.0919061643)\n");
		printf("4 - Korean Won to Chinese RMB (Exchange Rate: 0.00603703605)\n");
		printf("5 - Quit\n");
		
		printf("Enter your option: ");															// prompt user to choose between 1 to 5
		scanf("%d", &userSelect);

		int userAmount = 0;																		// for storing user's input amount in Won
		double rate = 0;																		// for storing currency rate with given value
		
		/**
 		 *	Switch Statement : prompt user to enter the amount in Won, calculate with rate and then print with double type value for each cases
 		 */
		switch(userSelect){
			case 1 :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.000905;
				printf("%d Won equals to %f USD\n\n", userAmount, userAmount * rate);
				break;
			case 2 :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.000807350908;
				printf("%d Won equals to %f Euro\n\n", userAmount, userAmount * rate);
				break;
			case 3 :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.0919061643;
				printf("%d Won equals to %f Yen\n\n", userAmount, userAmount * rate);
				break;
			case 4 :
				printf("Enter the amount in Korean Won: ");
				scanf("%d", &userAmount);
				rate = 0.00603703605;
				printf("%d Won equals to %f RMB\n\n", userAmount, userAmount * rate);
				break;
			case 5 :
				break;
			default :
				printf("You entered an invalid input.\n\n"); 
		}	
	}	
 }
