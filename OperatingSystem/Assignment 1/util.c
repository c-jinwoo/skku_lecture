/*
 *  Subject : Operating System
 *  Date    : 2022.10.02
 *	File	: util.c
 *  Todo    : Set of utilization functions
 *  Author  : 2020315798 ChoiJinWoo
 */

#include "common.h"

void fileOpenError(){
	printf("Failed to open input file.\n");
	exit(1);
}

void fileSyntaxError(char *s){
	printf("Invalid input file - %s\n", s);
	exit(1);
}

void memoryAllocationError(void *pt){
	if(pt == NULL){
		printf("Failed to allocate memory.\n");
		exit(1);
	}
}

void quit(){
	free(prcsList);
	free(rq0);
	free(rq1);
	free(rq2);
	free(sq);
}