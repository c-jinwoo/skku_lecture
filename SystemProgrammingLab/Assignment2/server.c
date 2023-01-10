/*
 *	Subject : System Programming Lab
 *	Lecture : Programming Assignment 2
 *	Author	: Choi Jin Woo
 *	SID		: 2020315798
 *	Date	: 2022.11.25
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <pthread.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>

#define MAX_CLIENT_NUM 1024
#define MAX_SEAT_NUM 256

typedef struct _query{
	int user;
	int action;
	int data;
	int fd;
} query;

typedef struct _user{
	int client;
	int passcode;
	int seat_num;
	int isLogin;
	int isRegistered;
	int isReserved;
} user;
	
pthread_mutex_t lock = PTHREAD_MUTEX_INITIALIZER;
user u[MAX_CLIENT_NUM];
int seat_arr[MAX_SEAT_NUM];

void *funct_thread(void *arg){
	pthread_detach(pthread_self());
	query qu = *(query*)arg;
	int success = 1;
	int fail = -1;
	int result = 0;

	if(qu.user < 0 || qu.user > 1023){
		printf("Unavailable user id.\n");
		send(qu.fd, &fail, sizeof(int), 0);
		return NULL;
	}

	if(qu.user == 0 && qu.action == 0 && qu.data == 0){
		send(qu.fd, seat_arr, sizeof(seat_arr), 0);
	}
	else{
		if(qu.action == 1){
			if(u[qu.user].isRegistered){
				if(u[qu.user].isLogin){
					printf("User %d is already logged in.\n", qu.user);
					result = 0;
				}
				else{
					if(u[qu.user].passcode == qu.data){
						pthread_mutex_lock(&lock);
						u[qu.user].client = qu.fd;
						u[qu.user].isLogin = 1;
						pthread_mutex_unlock(&lock);
					
						printf("User %d login.\n", qu.user);
						result = 1;
					}
					else{
						printf("User %d login info incorrect.\n", qu.user);
						result = 0;
					}
				}
			}
			else{
				pthread_mutex_lock(&lock);
				u[qu.user].client = qu.fd;
				u[qu.user].passcode = qu.data;
				u[qu.user].isRegistered = 1;
				u[qu.user].isLogin = 1;
				pthread_mutex_unlock(&lock);
				
				printf("User %d registered.\n", qu.user);
				result = 1;
			}
			result ? send(qu.fd, &success, sizeof(int), 0) : send(qu.fd, &fail, sizeof(int), 0);
		}
		else if(qu.action == 2){
			if(qu.data < 0 || qu.data > 255){
				printf("Seat number out of range.\n");
				result = 0;
			}
			else{
				if(u[qu.user].client == qu.fd && u[qu.user].isLogin){
					if(u[qu.user].isReserved){
						printf("User already reserved a seat.\n");
						result = 0;
					}
					else{
						if(seat_arr[qu.data] == -1){
							pthread_mutex_lock(&lock);
							u[qu.user].isReserved = 1;					
							u[qu.user].seat_num = qu.data;
							seat_arr[qu.data] = qu.user;
							pthread_mutex_unlock(&lock);			
							
							printf("User %d registered seat %d.\n", qu.user, qu.data);
							result = 1;
						}
						else{
							printf("Seat number %d already reserved.\n", qu.data);
							result = 0;
						}
					}
				}
				else{
					printf("Reservation failed.\n");
					result = 0;
				}
			}
			result ? send(qu.fd, &(qu.data), sizeof(qu.data), 0) : send(qu.fd, &fail, sizeof(int), 0);
		}
		else if(qu.action == 3){
			if(qu.data < 0 || qu.data > 255){
				printf("Seat number out of range.\n");
				result = 0;
			}
			else{
				if(u[qu.user].client == qu.fd && u[qu.user].isLogin){
					if(u[qu.user].seat_num == qu.data){
						printf("Reservation for user %d seat %d checked.\n", qu.user, qu.data);
						result = 1;
					}
					else{
						printf("Reservation check failed.\n");
						result = 0;
					}
				}
				else{
					printf("Reservation check failed.\n");
					result = 0;
				}
			}
			result ? send(qu.fd, &(qu.data), sizeof(qu.data), 0) : send(qu.fd, &fail, sizeof(int), 0);
		}
		else if(qu.action == 4){
			if(qu.data < 0 || qu.data > 255){
				printf("Seat number out of range.\n");
				result = 0;
			}
			else{
				if(u[qu.user].client == qu.fd && u[qu.user].isLogin){
					if(u[qu.user].seat_num == qu.data){
						pthread_mutex_lock(&lock);
						u[qu.user].isReserved = 0;
						u[qu.user].seat_num = -1;
						seat_arr[qu.data] = -1;
						pthread_mutex_unlock(&lock);
					
						printf("User %d cancelled seat %d.\n", qu.user, qu.data);
						result = 1;
					}
					else{
						printf("Reservation cancellation failed.\n");
						result = 0;
					}
				}
				else{
					printf("Reservation cancellation failed.\n");
					result = 0;
				}
			}
			result ? send(qu.fd, &(qu.data), sizeof(qu.data), 0) : send(qu.fd, &fail, sizeof(int), 0);
		}
		else if(qu.action == 5){
			if(u[qu.user].client == qu.fd && u[qu.user].isLogin){
				pthread_mutex_lock(&lock);
				u[qu.user].client = 0;
				u[qu.user].isLogin = 0;
				pthread_mutex_unlock(&lock);
				
				result = 1;
				printf("User %d logged out.\n", qu.user);
			}
			else{
				result = 0;
				printf("Logout failed.\n");
			}
			result ? send(qu.fd, &success, sizeof(int), 0) : send(qu.fd, &fail, sizeof(int), 0);
		}
		else{
			printf("Unavailable query.\n");
			send(qu.fd, &fail, sizeof(int), 0);
		}
	}
}

int main(int argc, char *argv[]){
	int listenfd, connfd, caddrlen;
	struct hostent *h;
	struct sockaddr_in saddr, caddr;
	pthread_t thread;
	if(argc < 3){
		printf("argv[1]: server address, argv[2]: port number\n");
		exit(1);
	}

	for(int i=0;i<MAX_CLIENT_NUM;i++){
		u[i].client = 0;
		u[i].passcode = 0;
		u[i].seat_num = -1;
		u[i].isLogin = 0;
		u[i].isRegistered = 0;
		u[i].isReserved = 0;
	}	
	for(int i=0;i<MAX_SEAT_NUM;i++){
		seat_arr[i] = -1;
	}

	// Server side setting
	memset((char *)&saddr, 0, sizeof(saddr));
	saddr.sin_family = AF_INET;
	saddr.sin_addr.s_addr = htonl(INADDR_ANY);
	saddr.sin_port = htons(atoi(argv[2]));

	if((listenfd = socket(PF_INET, SOCK_STREAM, 0)) < 0){
		printf("socket() failed.\n");
		exit(1);
	}
	if(bind(listenfd, (struct sockaddr *)&saddr, sizeof(saddr)) < 0){
		printf("bind() failed.\n");
		exit(1);
	}
	if(listen(listenfd, MAX_CLIENT_NUM) < 0){
		printf("listen() failed.\n");
		exit(1);
	}

	// I/O Multiplexing
	fd_set readset, copyset;
	FD_ZERO(&readset);
	FD_SET(listenfd, &readset);
	int fdmax = listenfd, fdnum;

	while(1){
		copyset = readset;
		
		if((fdnum = select(fdmax+1, &copyset, NULL, NULL, NULL)) < 0){
			printf("select() failed.\n");
			exit(0);
		}

		for(int i=0;i<fdmax+1;i++){
			if(FD_ISSET(i, &copyset)){
				if(i == listenfd){
					if((connfd = accept(listenfd, (struct sockaddr *)&caddr, (socklen_t *)&caddrlen)) < 0){
						printf("accept() failed.\n");
						continue;
					}
					FD_SET(connfd, &readset);
					if(fdmax < connfd){
						fdmax = connfd;
					}
					printf("Conncected to a new client!\n");
				}
				else{
					query qu;
					if(recv(i, &qu, sizeof(qu), 0) > 0){
						qu.fd = i;
						pthread_create(&thread, NULL, &funct_thread, &qu);
					}
					else{
						FD_CLR(i, &readset);
						printf("Connection from client %d has been terminated.\n", i-3);
						close(i);
					}
				}
			}
		}
	}
	printf("Server Connection terminated.\n");
	close(listenfd);
	return 0;
}
