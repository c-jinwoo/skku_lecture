/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 8
 *	About	 	: Swapping using Pointer 
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>

void ArrayReverse(int *InputPtr,int size){
	int i = 0;
	
	for(i=0;i<size/2;i++){
		int tmp = InputPtr[i];						// store tmp for swaping
		InputPtr[i] = *(InputPtr+size-1-i);			// assign InputPtr[i] for its counterpart
		InputPtr[size-1-i] = tmp;					// assign the counterpart with the temp value
	}
}
int main(void){
	int Test1[10] = {10,20,30,40,50,60,70,80,90,100};
	int Test2[6] = {112,110,108,106,14,12};
	int Test3[7] = {18,35,1024,23,68,51,51};
	int i = 0;
	
	printf("Test1 contains %d items: ",sizeof(Test1)/sizeof(int));
	for(i=0;i<sizeof(Test1)/sizeof(int);i++){
		printf("%d ",Test1[i]);
	}
	printf("\n");// Print Array Test1
	
	printf("Test2 contains %d items: ",sizeof(Test2)/sizeof(int));
	for(i=0;i<sizeof(Test2)/sizeof(int);i++){
		printf("%d ",Test2[i]);
	}
	printf("\n");// Print Array Test2
	
	printf("Test3 contains %d items: ",sizeof(Test3)/sizeof(int));
	for(i=0;i<sizeof(Test3)/sizeof(int);i++){
		printf("%d ",Test3[i]);
	}
	printf("\n\n");// Print Array Test3

	ArrayReverse(Test1,sizeof(Test1)/sizeof(int));
	ArrayReverse(Test2,sizeof(Test2)/sizeof(int));
	ArrayReverse(Test3,sizeof(Test3)/sizeof(int));
	
	printf("Test1 after Reverse: ");
	for(i=0;i<sizeof(Test1)/sizeof(int);i++){
		printf("%d ",Test1[i]);
	}
	printf("\n");// Print Array Test1
	
	printf("Test2 after Reverse: ");
	for(i=0;i<sizeof(Test2)/sizeof(int);i++){
		printf("%d ",Test2[i]);
	}
	printf("\n");// Print Array Test2
	
	printf("Test3 after Reverse: ");
	for(i=0;i<sizeof(Test3)/sizeof(int);i++){
		printf("%d ",Test3[i]);
	}
	printf("\n");// Print Array Test3
	
	return 0;
}
