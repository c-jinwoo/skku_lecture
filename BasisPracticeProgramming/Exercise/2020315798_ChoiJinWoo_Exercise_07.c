/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 7
 *	About	 	: check array by passing by reference
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
#include <time.h>
#define SIZE 15

void checkArray(int *arr, int *max, int *min, int *sum){
	int i = 0;
		
	// assign max value : sequential search
	*max = arr[0];
	for(i=1;i<SIZE;i++){
		if(*max < arr[i]){
			*max = arr[i];
		}
	}
	
	// assign min value : sequential search
	*min = arr[0];
	for(i=1;i<SIZE;i++){
		if(*min > arr[i]){
			*min = arr[i];
		}
	}
	
	// assign sum
	for(i=0;i<SIZE;i++){
		*sum += arr[i];
	}
}

int main(void){	
	int array[SIZE] = {0};						// initialize the array
	int max = 0,
		min = 0,
		sum = 0,
		i = 0;
	
	srand(time(NULL));							// time seed
	
	for(i=0;i<SIZE;i++){
		array[i] = (rand() % 101) - 50;			// assign from -50 to 50
		
		(i < SIZE - 1) ? printf("A[%d]: %d, ", i, array[i]) : printf("A[%d]: %d\n", i, array[i]);
	}
	printf("\n");
	
	checkArray(array, &max, &min, &sum);		// pass by reference
		
	printf("Max : %d\nMin : %d\nSum : %d\n", max, min, sum);
}
