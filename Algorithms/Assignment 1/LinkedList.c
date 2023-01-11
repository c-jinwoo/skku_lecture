/*
 *  Subject : Algorithms
 *  Date    : 2022.09.25
 *  Todo    : Linked List
 *  Author  : 2020315798 ChoiJinWoo
 */
  
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define MAX_SIZE 60

typedef struct{
	int arr[MAX_SIZE];
	int size;
} ArrayList;

// Initialize Linked List
void init(ArrayList* L){
	L->size = 0;
}

// Insert to Linked List
void insertList(ArrayList* L, int val){
	if(L->size >= MAX_SIZE){
		printf("Insertion Overflow!\n");
		return;
	}
	L->arr[L->size++] = val;
}

// Print Linked List
void printList(ArrayList* L){	
	printf("[1st third of the list]\t: ");
	for(int i=0;i<L->size/3;i++){
		printf("%d ",L->arr[i]);
	}
	printf("\n[2nd third of the list]\t: ");
	for(int i=L->size/3;i<2*L->size/3;i++){
		printf("%d ",L->arr[i]);
	}
	printf("\n[3rd third of the list]\t: ");
	for(int i=2*L->size/3;i<L->size;i++){
		printf("%d ",L->arr[i]);
	}
	printf("\n\n");
}

// Delete 3rd last integer
void deleteList(ArrayList* L){	
	int position = L->size-3;
	for(int i=position;i<L->size-1;i++){
		L->arr[i] = L->arr[i+1];
	}
	L->size--;
}

// Check if there's duplicated element in Linked List
int isDuplList(ArrayList* L, int val){
	int result = 0;
	
	for(int i=0;i<L->size;i++){
		if(L->arr[i] == val){
			result = 1;
		}
	}
	
	return result;
}

int main(void){
	ArrayList list;	
	init(&list);
	
	for(int i=0;i<MAX_SIZE;i++){
		int num = 0;
		while(1){
			num = rand()%1000;					
			int dupl = isDuplList(&list, num);
			
			if(!dupl){
				break;
			}
		}
		insertList(&list, num);
	}
	
	for(int i=0;i<3;i++){
		deleteList(&list);
		printList(&list);
	}
	
	return 0;	
}
