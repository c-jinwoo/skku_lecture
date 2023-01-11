/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 7
 *	About	 	: search array by passing by reference
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
#include <time.h>
#define SIZE 10

int searchArray(int *input, int *arr){
	int i = 0;
	int result = 0;
	
	for(i=0;i<SIZE;i++){
		if(*input == arr[i]){
			result = 1;							// if there is an element, assign 1 to result
		}
	}	
	
	return result;								// return result 0 or 1
}

int main(void){
	int array[SIZE] = {0};						// initialize the array
	int i = 0,
		input = 0;
	
	srand(time(NULL));							// time seed
	
	for(i=0;i<SIZE;i++){
		array[i] = (rand() % 99) + 1;			// assign from 1 to 99
		
		(i < SIZE - 1) ? printf("A[%d]: %d, ", i, array[i]) : printf("A[%d]: %d\n", i, array[i]);
	}
	
	printf("Enter the number you want to search: ");
	scanf("%d", &input);
	
	int result = searchArray(&input, array);	// result gets 1 or 0
	
	if(result){
		printf("The number you entered is in the array.\n");	
	}
	else{
		printf("The number you entered is not in the array.\n");			
	}
}
 
