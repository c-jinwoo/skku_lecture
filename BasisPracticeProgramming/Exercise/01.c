
#include <stdio.h>

int main(void){
	int Number_1, Number_2, Sum;						// declare integer Number_1, Number_2, Sum 
	
	printf("Please enter a number to Number_1 : ");		// prompt user to enter an integer
	scanf("%d", &Number_1);								// assign value to Number_1
	
	printf("Please enter a number to Number_2 : ");		// prompt user to enter an integer
	scanf("%d", &Number_2);								// assign value to Number_2
	
	Sum = Number_1 + Number_2;							// assign the value of Number_1 + Number_2 to Sum 
	printf("The sum is %d.\n", Sum);					// print out the value of Sum
	
	return 0;											// end of function
}
