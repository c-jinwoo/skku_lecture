/*
 *  Subject : Algorithms
 *  Date    : 2022.09.25
 *  Todo    : Selection Sort
 *  Author  : 2020315798 ChoiJinWoo
 */
  
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define SIZE 100

void selectionSort(int* ptr){
	
	int compCnt = 0;						// for storing computation count
	
	for(int i=0;i<SIZE-1;i++){
		int max = i;
		
		for(int j=i+1;j<SIZE;j++){
			if(ptr[max] < ptr[j]){
				max = j;					// save the bigger index
			}
			compCnt++;						// increase computation count
		}
		
		int tmp = ptr[i];					// swap
		ptr[i] = ptr[max];
		ptr[max] = tmp;		
	}
		
	printf("\n\n[A after sorting]\n");
	for(int i=0;i<SIZE;i++){
		printf("%d ", ptr[i]);
	}
	printf("\n\n[Number of comparisons]\n%d\n", compCnt);
	printf("---------------------------------------------------------------------\n");
}

int main(void){
	int A[SIZE] = {0};
	int type = 0;
	
	while(1){
		printf("Choose the type of integer inputs(0:break, 1:random, 2:already sorted, 3:reversely sorted): ");
		scanf("%d", &type);
		srand(time(NULL));	
		
		if(type == 0){
			break;		
		}
		else if(type == 1){
			printf("\n[A before sorting]\n");
			for(int i=0;i<SIZE;i++){
				while(1){
					int dupl = 0;
					A[i] = rand()%1000;					
					
					for(int j=0;j<i;j++){
						if(A[j] == A[i]){
							dupl = 1;		// prevent duplicated value
						}
					}
					
					if(!dupl){
						break;
					}
				}
				printf("%d ", A[i]);
			}
			selectionSort(A);		
		}		
		else if(type == 2){
			printf("\n[A before sorting]\n");
			for(int i=0;i<SIZE;i++){
				A[i] = 100-i;
				printf("%d ", A[i]);
			}
			selectionSort(A);
		}
		else if(type == 3){
			printf("\n[A before sorting]\n");
			for(int i=0;i<SIZE;i++){
				A[i] = i+1;
				printf("%d ", A[i]);
			}
			selectionSort(A);		
		}
		else{
			printf("Select proper option.\n\n");
		}
	}
	
	return 0;	
}
