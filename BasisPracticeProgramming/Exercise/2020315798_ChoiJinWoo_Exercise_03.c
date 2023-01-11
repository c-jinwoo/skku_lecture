/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 3
 *	About	 	: Sum up 1 to user input using for-loop
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
 int main(void){ 
 	char checkIfContinue = 'y';												// for storing the value if the user runs the program again
	int userInput = 0;														// for storing the value user inputs to calculate the sum
	
	/**
	 *	For-Loop : infinite loop until the check value is eqaul to 'n'
	 */	
 	for(;checkIfContinue != 'n';){	 
 		printf("Enter a positive integer value : ");						// prompt user to input the value
 		scanf("%d", &userInput);
 		
 		/**
 		 *	If-Else Statement : if userInput is a negative, prompt user to enter a positive one.
 		 */
 		if(userInput > 0){ 		
	 		int i = 0;														// for storing initial value of for-loop and calculation for sum
	 		int sum = 0;													// for storing cascade sum value from userInput to 1
	 		
	 		/**
	 		 * For-Loop : calculate sum from userInput to 1
			 */
	 		for(i = userInput;i > 0;i--){
	 			sum += i;					
	 		}
	 		printf("The result is %d.\n", sum);								// print out the result
	 		
			printf("Do you want to run again?(y/n) : ");					// ask if user wants to run the program again
	 		scanf(" %c", &checkIfContinue); 
	 		
	 		while(checkIfContinue != 'y' && checkIfContinue != 'n'){
		 		printf("Do you want to run again?(y/n) : ");		 		// if the user inputs but y/n, prompt user to ask again 
	 			scanf(" %c", &checkIfContinue);
		 	}
		 	
		 	if(checkIfContinue == 'y'){
		 		printf("\n");												// just to distinguish next loop knitly
		 	}
		}
		else{
			printf("You should enter a positive integer value.\n\n");
		}
 	}
 }
