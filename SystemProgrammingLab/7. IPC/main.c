/*
 * Subject  : System Programming Lab
 * Lecture  : Week7 - IPC
 * Author   : Choi Jin Woo
 * SID      : 2020315798
 */

#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>

int count = 0;
void handler(int sig){
	printf("BEEP\n");
	count++;
	if(count < 5){
		sleep(1);
		handler(sig);
	}
	else{
		printf("I'm Alive!\n");
		exit(0);
	}
}

int main(void){
	signal(SIGINT, handler);
	while(1);
	return 0;
}
