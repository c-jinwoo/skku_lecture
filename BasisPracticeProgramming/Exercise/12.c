
#include <stdio.h>
#define SIZE 80

struct StuInfo {
	char Name[10];
	int IDNo;
	int Exam[2];
};

int main(void){
	FILE *fPtr = fopen("lab12_datafile.txt", "r");			// open file
	struct StuInfo student[SIZE];							// struct object with size 80 array
	int i = 0;												// for iteration
	int input = 0;											// for user input index
	
	if(fPtr != NULL){
		for(i=0;i<SIZE;i++){		
			fscanf(fPtr, "%s %d %d %d\n", &student[i].Name, &student[i].IDNo, &student[i].Exam[0], &student[i].Exam[1]);		
		}
	}
	
	while(1){
		printf("Enter a student no (-1 to quit): ");
		scanf("%d", &input);
		
		if(input == -1){
			break;
		}
		
		printf("Name: %s, Stu#: %d, Exam1: %d, Exam2: %d\n", student[input-1].Name, student[input-1].IDNo, student[input-1].Exam[0], student[input-1].Exam[1]);
	}
	
	close(fPtr);											// close file
	
	return 0;
}
