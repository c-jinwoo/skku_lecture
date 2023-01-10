/*
 * Subject	: System Programming Lab
 * Lecture	: Week5 - Process
 * Author	: Choi Jin Woo
 * SID		: 2020315798
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>

int main(void){
	while(1){
		char *cmd = NULL;
		size_t size = 0;
		int i = 0;

		getline(&cmd, &size, stdin);
		cmd[strlen(cmd)-1] = '\0';

		char *arg[100];
		char *ptr = strtok(cmd, " ");
		while(ptr != NULL){
			arg[i++] = ptr;
			ptr = strtok(NULL, " ");
		}

		arg[i] = NULL;

		if(strcmp(arg[0], "quit") == 0){
			break;
		}

		char path[100];
		sprintf(path, "/bin/%s", arg[0]);

		if(fork() == 0){
			execv(path, arg);
		}

		wait(NULL);
	}	
}
