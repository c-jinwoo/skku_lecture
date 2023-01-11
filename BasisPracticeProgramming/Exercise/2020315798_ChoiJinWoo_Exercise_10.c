/*
 *	Lecture 	: Basic and Practice in Programming
 *	Content		: Lab Exercise 10
 *	About	 	: Password Valid Check with ctype.h
 *	Student		: 2020315798 Choi Jin Woo(ÃÖÁø¿ì)
 */

#include <stdio.h>
#include <ctype.h>
#define VALID_CNT 5

int checkIfLower(char *ptr){
	int i = 0;
	
	for(i=0;i<strlen(ptr);i++){
		if(islower(ptr[i]) != 0){
			return 1;
		}	
	}	
	return 0;
}

int checkIfUpper(char *ptr){
	int i = 0;
	
	for(i=0;i<strlen(ptr);i++){
		if(isupper(ptr[i]) != 0){
			return 1;
		}	
	}	
	return 0;
}

int checkIfDigit(char *ptr){
	int i = 0;
	
	for(i=0;i<strlen(ptr);i++){
		if(isdigit(ptr[i]) != 0){
			return 1;
		}	
	}	
	return 0;
}

int checkIfPunct(char *ptr){
	int i = 0;
	
	for(i=0;i<strlen(ptr);i++){
		if(ispunct(ptr[i]) != 0){
			return 1;
		}	
	}	
	return 0;
}

int checkIfMinLen(char *ptr){
	return (strlen(ptr) >= 10) ? 1 : 0;
}

int main(void){
	char pw[100];									// create reasonalble amount size of char array to store string
	int result = 0;									// to add 1 for every validation check
	
	printf("Please enter a password : ");
	scanf("%s", pw);
	
	/* check if the pw contains a lower case */	
	result += checkIfLower(pw);
	
	/* check if the pw contains an upper case */	
	result += checkIfUpper(pw);
	
	/* check if the pw contains a digit */	
	result += checkIfDigit(pw);
	
	/* check if the pw contains a punct */	
	result += checkIfPunct(pw);
	
	/* check if the pw len is at least 10 */	
	result += checkIfMinLen(pw);
	
	if(result < VALID_CNT){
		printf("This is NOT a valid password\n");		
	}
	else{
		printf("This is a valid password\n");		
	}
	
	return 0;
}
