/*
 *	Basic and Practice in Programming
 *	Lab Assignment 1
 *	2020315798 Choi Jin Woo(ÃÖÁø¿ì) 
 *
 */

#include <stdio.h>

int main(void){
	int score_John, score_Mary, score_Peter, score_Jane;				// declare integer score_John, score_Mary, score_Peter, score_Jane
	float sum, avg;														// declare float sum, average
	
	printf("Input John's score\t: ");									// prompt user to enter an integer
	scanf("%d", &score_John);											// assign value to score_John
	
	printf("Input Mary's score\t: ");									// prompt user to enter an integer
	scanf("%d", &score_Mary);											// assign value to score_Mary
	
	printf("Input Peter's score\t: ");									// prompt user to enter an integer
	scanf("%d", &score_Peter);											// assign value to score_Peter
	
	printf("Input Jane's score\t: ");									// prompt user to enter an integer
	scanf("%d", &score_Jane);											// assign value to score_Jane
	
	sum = score_John + score_Mary + score_Peter + score_Jane;			// calculate the summation of the score
	avg = sum / 4;														// calculate the average of the summation		
	printf("Average : %0.6f\n", avg);									// display the average score
	
	return 0;															// end of function
}
