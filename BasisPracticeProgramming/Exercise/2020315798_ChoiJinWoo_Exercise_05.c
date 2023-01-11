/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 5
 *	About	 	: get GCD using recursive call
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>

/**
 *	Function 	: swap()
 *	Todo		: swap two integers by using pointer variables
 */
void swap(int *x, int *y){
	int tmp = *y;
	*y = *x;
	*x = tmp;
}

/**
 *	Function 	: getGCD()
 *	Todo		: get GCD by using Euclid's algorithm 
 */
int getGCD(int x, int y){
	if(y == 0){
		return x;
	}
	else{
		int temp = y;
		y = x % y;
		x = temp;
		
		return getGCD(x, y);				// recursive call
	}
}

int main(void){
	int M = 0,
		N = 0;	
		
	printf("M = ");
	scanf("%d", &M);
	
	printf("N = ");
	scanf("%d", &N);
	
	if(M < N){
		swap(&M, &N);						// if first one is less than latter one, swap 
	}
	
	printf("GCD = %d\n", getGCD(M, N));
	
	return 0;
}
