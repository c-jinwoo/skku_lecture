/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 11
 *	About	 	: read and write w/read in binary
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */

#include <stdio.h>
#define SIZE 20

int main(void){ 
	int score[SIZE] = {0};												// an array for storing score
	int i = 0;															// for indexing
	int option = 0;														// for option input
	FILE *fPtr;	
	
	while(1){
		printf("1. Read and display all data from \"score.dat\".\n");
		printf("2. Multiply all the numbers in \"score.dat\" by 2.\n");
		printf("3. Quit\n");
		
		printf("Enter your option: ");
		scanf("%d", &option);
		
		if(option == 1){
			fPtr = fopen("score.dat", "rb");							// open in binary mode
			fread(&score, sizeof(int), SIZE, fPtr);						// read file
			
			for(i=0;i<SIZE;i++){
				printf("%d ", score[i]);	
			}
			rewind(fPtr);
		}
		else if(option == 2){
			fPtr = fopen("score.dat", "wb+");							// write and read
			
			for(i=0;i<SIZE;i++){
				score[i] *= 2;											// multiply by 2
			}
			
			fwrite(&score, sizeof(int), SIZE, fPtr);					// write in file
			
			for(i=0;i<SIZE;i++){
				printf("%d ", score[i]);	
			}
			rewind(fPtr);
		}
		else if(option == 3){
			break;														// break for input 3
		}
		else{
			printf("Invalid Option");									// exception
		}
		printf("\n");
	}
	fclose(fPtr);														// close file
	
	return 0;
}
