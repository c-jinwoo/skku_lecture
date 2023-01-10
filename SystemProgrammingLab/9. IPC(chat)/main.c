/*
 * Subject  : System Programming Lab
 * Lecture  : Week9 - IPC(Chatting program)
 * Author   : Choi Jin Woo
 * SID      : 2020315798
 */

#include <sys/types.h>
#include <sys/wait.h>
#include <sys/msg.h>
#include <sys/ipc.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>

typedef struct msgbuf{
	long msgtype;
	char text[256];
} msgbuf;

int main(void){
	char *myId = NULL;
	char *yourId = NULL;
	size_t size = 0;
	ssize_t myIdLen = 0;
	ssize_t yourIdLen = 0;
	int my_qid = 0;
	int your_qid = 0;
	pid_t my_pid, your_pid;

	printf("My id is : ");
	myIdLen = getline(&myId, &size, stdin);
	printf("Who to send : ");
	yourIdLen = getline(&yourId, &size, stdin);
	myId[myIdLen-1] = '\0';
	yourId[yourIdLen-1] = '\0';
	
	key_t my_k = ftok(".", atoi(myId));
	key_t your_k = ftok(".", atoi(yourId));

	if((my_qid = msgget(my_k, IPC_CREAT | 0660)) == -1){
		perror("my msgget error");
		exit(0);
	}
	if((your_qid = msgget(your_k, IPC_CREAT | 0660)) == -1){
		perror("your msgget error");
		exit(0);
	}

	// Send : Child process
	if((your_pid = fork()) == 0){
		while(1){
			msgbuf *my_msg = malloc(sizeof(msgbuf));
            my_msg->msgtype = 1;
 
            char *ment = NULL;
            ssize_t myMentLen = getline(&ment, &size, stdin);
            ment[myMentLen-1] = '\0';
            strcpy(my_msg->text, ment);

            if(msgsnd(your_qid, (void*)my_msg, sizeof(msgbuf)-sizeof(long), IPC_NOWAIT) == -1){
                perror("msgsnd send error");
                exit(0);
            }
            free(ment);
            free(my_msg);

            msgbuf *check_msg = malloc(sizeof(msgbuf));
            if(msgrcv(your_qid, (void*)check_msg, sizeof(msgbuf)-sizeof(long), 2, 0) == -1){
                perror("checktime receive error");
                exit(0);
            }
            printf("%s\n", check_msg->text);
            free(check_msg);
		}
	}
	// Receive : Parent process
	else{
		while(1){
			msgbuf *your_msg = malloc(sizeof(msgbuf));
  
	        if(msgrcv(my_qid, (void*)your_msg, sizeof(msgbuf)-sizeof(long), 1, 0) == -1){
			       perror("msgrcv receive error");
				   exit(0);
            }
            printf("RECEIVED %s\n\n", your_msg->text);          
            free(your_msg);
                  
            msgbuf *check_msg = malloc(sizeof(msgbuf));
            check_msg->msgtype = 2;
  
			time_t ltime;
			time(&ltime);
            char temp[100] ="\0";
			char curTime[26];
            char *str = " read message at:\t";
            strcat(temp, myId);
            strcat(temp, str);
			strcat(temp, ctime_r(&ltime, curTime));
            strcat(check_msg->text, temp);
                  
            if(msgsnd(my_qid, (void*)check_msg, sizeof(msgbuf)-sizeof(long), IPC_NOWAIT) == -1){
                perror("checktime send error");
                exit(0);
            }
            free(check_msg);
		}
	}
	free(myId);
	free(yourId);

	return 0;
}
