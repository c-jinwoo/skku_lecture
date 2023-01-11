/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 6
 *	About	 	: assign random value to 2D array, print out several values
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define ROW 10
#define COL 8

int main(void){
	srand(time(NULL));
	
	int arr[ROW][COL] = {0};
	int i = 0;
	int j = 0;
	int searchValue = 0;
		
	// print out array with assign
	for(i = 0;i < ROW;i++){
		for(j = 0;j < COL;j++){
			arr[i][j] = rand() % 91 + 10;
			
			if(arr[i][j] == 100){
				printf("%d ", arr[i][j]);
			}
			else{
				printf("%d  ", arr[i][j]);	
			}
		}
		printf("\n");
	}
	printf("\n");

	
	// print out smallest value
	searchValue = arr[0][0];
	
	for(i = 0;i < ROW;i++){
		for(j = 0;j < COL-1;j++){
			if(searchValue > arr[i][j+1]){
				searchValue = arr[i][j+1];
			}
		}	
	}	
	printf("Smallest value is : %d\n", searchValue);


	// print out largest value
	searchValue = arr[0][0];
	
	for(i = 0;i < ROW;i++){
		for(j = 0;j < COL-1;j++){
			if(searchValue < arr[i][j+1]){
				searchValue = arr[i][j+1];
			}
		}	
	}
	printf("Largest value is : %d\n", searchValue);


	// print out sum
	float sum = 0;
	
	for(i = 0;i < ROW;i++){
		for(j = 0;j < COL;j++){
			sum += arr[i][j];
		}
	}
	printf("Average is : %f\n", sum/80);

	return 0;
}
