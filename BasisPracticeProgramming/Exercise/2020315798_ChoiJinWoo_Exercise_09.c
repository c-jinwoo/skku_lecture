/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 9
 *	About	 	: Pointer random select
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */

#include <stdio.h>
#include <time.h>

int main(void){
	int numbers[45] = {0},
		*pick[6] = {0},
		i = 0;
		
	for(i=0;i<45;i++){
		numbers[i] = i + 1;
	}
	
	/* Implementation */
	srand(time(NULL));
	
	for(i=0;i<6;i++){
		int num = rand() % 45;
		pick[i] = &numbers[num];		// assign random index of array numbers to pick pointer array
	}
	
	
	printf("%d %d %d %d %d %d\n", *pick[0], *pick[1], *pick[2], *pick[3], *pick[4], *pick[5]);
	
	return 0;	
}
