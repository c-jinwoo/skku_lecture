/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 5
 *	About	 	: random (Sic Bo game)
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(void){
	const int DICE_SIZE = 3;							// define size of the array
	int dice_arr[DICE_SIZE];							// initialize array
	int sum = 0;										// for saving summation
	
	srand(time(NULL));									// generate random seed
	
	int i = 0;
	for(i = 0;i < DICE_SIZE; i++){
		dice_arr[i] = rand() % 6 + 1;					// assign range 1 to 6
		
		printf("%d ", dice_arr[i]);	
		
		sum += dice_arr[i];								// add up
	}
	
	printf("\n");
	
	if((dice_arr[0] == dice_arr[1]) && (dice_arr[1] == dice_arr[2])){
		printf("TRIPLE!\n");
	}
	else if((3 <= sum) && (sum <= 10)){
		printf("SMALL!\n");		
	}
	else if((11 <= sum) && (sum <= 17)){
		printf("BIG!\n");		
	}
	
	return 0;
}
