/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 12
 *	About	 	: Linked List with FILE IO
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */

#include <stdio.h>
#define SIZE 80

int sCnt = 0;											// for counting student

struct StuInfo {
	char Name[10];
	int IDNo;
	int Exam[2];
	struct StuInfo *nextPtr;
};

void PrintAllName(struct StuInfo node){
	struct StuInfo *curPtr = &node;
	
	while(curPtr != NULL){
		printf("Name : %s\n", curPtr->Name);
		curPtr = curPtr->nextPtr;
	}
}

float AverageScore(struct StuInfo node){
	struct StuInfo *curPtr = &node;
	int sum = 0;
	float average = 0.0;
	
	while(curPtr != NULL){
		sum += (curPtr->Exam[0] + curPtr->Exam[1]);
		curPtr = curPtr->nextPtr;		
	}
	
	average = sum / (sCnt * 2);
	
	return average;	
}
struct StuInfo *BestStudent(struct StuInfo node){
	struct StuInfo *curPtr = &node;
	struct StuInfo *nextPtr = curPtr->nextPtr;
	
	while(nextPtr != NULL){
		if(curPtr->Exam[0] + curPtr->Exam[1] < nextPtr->Exam[0] + nextPtr->Exam[1]){
			curPtr = nextPtr;								// move curPtr node to nextPtr node if nextPtr node has better grade
		}
		
		nextPtr = nextPtr->nextPtr;							// move nextPtr node to next node
	}
	
	return curPtr;
}

int main(void){
	FILE *fPtr = fopen("lab12_datafile.txt", "r");			// open file
	struct StuInfo node[SIZE];								// struct object with size 80 array
	int i = 0;												// for iteration
	int length = 0;											// for amount of the student
		
	if(fPtr != NULL){	
		while(!feof(fPtr)){		
			fscanf(fPtr, "%s %d %d %d\n", &node[sCnt].Name, &node[sCnt].IDNo, &node[sCnt].Exam[0], &node[sCnt].Exam[1]);
			node[sCnt].nextPtr =  &node[sCnt+1];
			sCnt += 1;
		}
		
		node[sCnt-1].nextPtr = NULL;						// set last node Ptr to NULL
		
		PrintAllName(node[0]);	
		printf("Average is : %f\n", AverageScore(node[0]));
		printf("Best Student is : %s\n", BestStudent(node[0])->Name);
	
	}
	
	fclose(fPtr);
	
	return 0;
}
