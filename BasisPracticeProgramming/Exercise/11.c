
#include <stdio.h>
#define SIZE 20

int main(void){
	int score[SIZE] = {0};											// an array for storing score
	int i = 0;														// for indexing
	FILE *fPtr = fopen("score.dat", "wb");							// for file input
	
	for(i=0;i<SIZE;i++){
		printf("Enter the score for student %d : ", i+1);
		scanf("%d", &score[i]);
	}
	
	fwrite(&score, sizeof(int), SIZE, fPtr);						// write in binary form
	
	fclose(fPtr);													// close file
		
	return 0;
}
