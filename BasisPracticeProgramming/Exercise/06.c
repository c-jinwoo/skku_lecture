
#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define SIZE 25

void getArray(int *arr){
	int i = 0;
	
	printf("Array is : ");
	for(i = 0;i < SIZE;i++){
		printf("%d ",arr[i]);	
	}
	printf("\n");
}

void getSmallestValue(int *arr){
	int i = 0;
	int searchValue = arr[0];
	
	// sequential search
	for(i = 0;i < SIZE-1;i++){
		if(searchValue > arr[i+1]){
			searchValue = arr[i+1];	
		}	
	}
	
	printf("Smallest value is : %d\n", searchValue);
}

void getLargestValue(int *arr){
	int i = 0;
	int searchValue = arr[0];
	
	// sequential search
	for(i = 0;i < SIZE-1;i++){
		if(searchValue < arr[i+1]){
			searchValue = arr[i+1];
		}
	}
	
	printf("Largest value is : %d\n", searchValue);
}

void getAverageValue(int *arr){
	int i = 0;	
	float sum = 0;
	
	for(i = 0;i < SIZE;i++){
		sum += arr[i];
	}
	
	printf("Average is : %f\n", sum/25);
}

void getOddValue(int *arr){
	int i = 0;
	int count = 0;
	
	printf("Odd value is : ");
	
	for(i = 0;i < SIZE;i++){
		int abs = (arr[i] >= 0) ? arr[i] : -1 * arr[i];		// make the value absolute one to use mod operator
		
		if(abs % 2 == 1){
			printf("%d ", arr[i]);							// if the absolute value is odd, print out the original one
			count++;										// to check if there is no odd number
		}		
	}	
	if(count == 0){
		printf("There is no odd value.\n");	
	}
}

int main(void){
	srand(time(NULL));
	
	int randArr[SIZE] = {0};				// SIZE is defined as 25
	int i = 0;
	
	for(i = 0;i < SIZE;i++){
		randArr[i] = rand() % 126 - 50;		// assign random value from -50 to 75 for 25 times
	}	
		
	// each task is implemented seperately on each function
	getArray(randArr);
	getSmallestValue(randArr);
	getLargestValue(randArr);
	getAverageValue(randArr);
	getOddValue(randArr);	

	return 0;
}
 
