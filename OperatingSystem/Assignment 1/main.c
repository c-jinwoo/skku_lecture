/*
 *  Subject : Operating System
 *  Date    : 2022.10.02
 *	File	: main.c
 *  Todo    : Control overall procedure
 *  Author  : 2020315798 ChoiJinWoo
 */

#include "common.h"

int main(int argc, char* argv[]){
	initPrcs(argv[1]);
	initQueue();
	exec();
	quit();
 
	return 0;
}