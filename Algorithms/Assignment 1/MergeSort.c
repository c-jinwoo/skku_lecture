/*
 *  Subject : Algorithms
 *  Date    : 2022.09.25
 *  Todo    : Merge Sort
 *  Author  : 2020315798 ChoiJinWoo
 */
  
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define SIZE 100

int compCnt = 0;
void merge(int *ptr, int left, int mid, int right){
	int buffer[SIZE] = {0};
	
	int x = left,
		y = mid + 1,
		z = left;
		
	while(x <= mid && y <= right){
		buffer[z++] = (ptr[x] <= ptr[y]) ? ptr[x++] : ptr[y++];		
		compCnt++;
	}
	
	if(x > mid){
		for(int i=y;i<=right;i++){
			buffer[z++] = ptr[i];
		}
	}
	else{
		for(int i=x;i<=mid;i++){
			buffer[z++] = ptr[i];
		}
	}
	
	for(int i=left;i<=right;i++){
		ptr[i] = buffer[i];
	}	
}

void mergeSort(int* ptr, int left, int right){
	int mid;
	
	if(left < right){
		mid = (left + right) / 2;
		mergeSort(ptr, left, mid);
		mergeSort(ptr, mid+1, right);
		merge(ptr, left, mid, right);
	}	
}

int main(void){
	int A[SIZE] = {0};
	int type = 0;
	
	while(1){
		printf("Choose the type of integer inputs(0:break, 1:random, 2:already sorted, 3:reversely sorted): ");
		scanf("%d", &type);
		srand(time(NULL));	
		compCnt = 0;
		
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
							dupl = 1;
						}
					}
					
					if(!dupl){
						break;
					}
				}
				printf("%d ", A[i]);
			}	
			mergeSort(A, 0, SIZE-1);
			printf("\n\n[A after sorting]\n");
			for(int i=0;i<SIZE;i++){
				printf("%d ", A[i]);
			}
			printf("\n\n[Number of comparisons]\n%d\n", compCnt);
			printf("---------------------------------------------------------------------\n");
		}		
		else if(type == 2){
			printf("\n[A before sorting]\n");
			for(int i=0;i<SIZE;i++){
				A[i] = 100-i;
				printf("%d ", A[i]);
			}
			mergeSort(A, 0, SIZE-1);
			printf("\n\n[A after sorting]\n");
			for(int i=0;i<SIZE;i++){
				printf("%d ", A[i]);
			}
			printf("\n\n[Number of comparisons]\n%d\n", compCnt);
			printf("---------------------------------------------------------------------\n");
		}
		else if(type == 3){
			printf("\n[A before sorting]\n");
			for(int i=0;i<SIZE;i++){
				A[i] = i+1;
				printf("%d ", A[i]);
			}
			mergeSort(A, 0, SIZE-1);
			printf("\n\n[A after sorting]\n");
			for(int i=0;i<SIZE;i++){
				printf("%d ", A[i]);
			}		
			printf("\n\n[Number of comparisons]\n%d\n", compCnt);
			printf("---------------------------------------------------------------------\n");
		}
		else{
			printf("Select proper option.\n\n");
		}
	}
	
	return 0;	
}
