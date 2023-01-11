/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Assignment 9
 *	About	 	: Palindrome Check
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */
 
#include <stdio.h>
#define SIZE 10

int checkPalindrome(int *arr){
	int *head = &arr[0],
		*tail = &arr[9],
		i = 0,
		result = 1;
	
	for(i=0;i<SIZE/2;i++){
		if(*head != *tail){
			result = 0;					// if the values do not match, result will be assigned 0
			break;
		}
		head += 1;						// increase pointer head to next index
		tail -= 1;						// decrease pointer tail to prev index
	}
	
	return result;
}

int main(void){
	int i = 0,
		arr[SIZE] = {0};
	
	/* Prompt user to input the values */	
	for(i=0;i<SIZE;i++){
		printf("Enter number %d: ", i);
		scanf("%d", &arr[i]);
	}
	
	/* Print out the array */
	printf("User entered:");
	for(i=0;i<SIZE;i++){
		printf(" %d", arr[i]);
	}
	printf("\n");
	
	/* Palindrome check */
	int result = checkPalindrome(arr);
	
	/* Print out result */
	result ? printf("YES!!! It is a palindrome!\n") : printf("NO!!! It is not a palindrome!\n");
		
	return 0;
}
