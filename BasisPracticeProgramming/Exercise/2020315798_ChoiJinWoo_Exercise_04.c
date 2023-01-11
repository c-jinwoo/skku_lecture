/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 4
 *	About	 	: Get cubic root of the user input by using functions
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
 #include <stdio.h>
 #include <math.h>
 
 /**
  *	Function 	: PrintMenu()
  *	Todo		: print out the objective of the program
  */
 void PrintMenu(){
 	printf("This program asks the user to print out an integer, ");
	printf("and displays the cubic value of this integer.\n\n");
 }
 
 /**
  *	Function 	: AskUserInput()
  *	Todo		: prompt user to enter an integer value, return it
  */
 int AskUserInput(){
 	int input = 0;
 	
 	printf("Please input an integer: ");
 	scanf("%d", &input);
 	
 	return input;
 }
 
 /**
  *	Function 	: DisplayResult()
  *	Todo		: print out the cubic root and the original value of the parameter result
  */
 void DisplayResult(int result){
 	int cubicRoot = pow((float)result, (float)1/3);						// get cubic root of the result by powering with 1/3, because pow() uses float value
 	
 	printf("You entered: %d\n", cubicRoot);								// print out the cubic root
 	printf("The result is: %d\n", result);								// print out the result
 }
 
 int main(void){
 	PrintMenu();														// call PrintMenu() function
 	int input = AskUserInput();											// store user input by calling AskUserInput() function
 	int result = pow(input,3);											// store result by using pow() function in Math.h library
 	DisplayResult(result);												// call DisplayResult(), using parameter result
 	
 	return 0;															// end of Main function
 }
