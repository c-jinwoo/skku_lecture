#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netdb.h>
#include <arpa/inet.h>

#define MAXSNDLINE 128
#define MAXRCVLINE 80
#define MAXUSER 5
#define MAXNAME 30

int main(int argc, char *argv[]){
	int n;						// number of characters from client
	int listenfd;				// server socket
	int connfd;					// client connection descriptor
	int caddrlen;				// client address length
	char buf[MAXRCVLINE];		// buffer of line from client
	int port = atoi(argv[1]);	// connection port
	int fdmax, fdnum;			// number of fdi
	struct sockaddr_in saddr;	// server address
	struct sockaddr_in caddr;	// client address

	int clientMsgCnt[MAXUSER] = {0};
	int currentUserNum = 0;
	char clientName[MAXUSER][MAXNAME];

	// Server side setting
	memset((char *)&saddr, 0, sizeof(saddr));
	saddr.sin_family = AF_INET;
	saddr.sin_addr.s_addr = htonl(INADDR_ANY);
	saddr.sin_port = htons(port);

	// socket(), bind(), listen()
	if((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
		printf("socket() failed.\n");
		exit(1);
	}
	if((bind(listenfd, (struct sockaddr *)&saddr, sizeof(saddr)) < 0)){
		printf("bind() failed.\n");
		exit(2);
	}
	if((listen(listenfd, MAXUSER) < 0)){
		printf("listen() failed.\n");
		exit(3);
	}

	fd_set readset, copyset;
	FD_ZERO(&readset);			// initialize readset to 0
	FD_SET(listenfd, &readset);	// register socket descriptor info
	fdmax = listenfd;			// save socket number

	while(1){
		copyset = readset;
		struct timeval timeout;
		timeout.tv_sec = 1;
		timeout.tv_usec = 0;

		//if((fdnum = select(fdmax+1, &copyset, NULL, NULL, &timeout)) < 0){
		if((fdnum = select(fdmax+1, &copyset, NULL, NULL, NULL)) < 0){
			printf("select() failed\n");
			exit(0);
		}

		/*
		if(fdnum == 0){
			printf("Time out\n");
			continue;
		}
		*/
		
		for(int i=0;i<fdmax+1;i++){											// i : client fd number
			if(FD_ISSET(i, &copyset)){										// when event occurs
				if(i == listenfd){											// when connection occurs
					if((connfd = accept(listenfd, (struct sockaddr*)&caddr, (socklen_t *)&caddrlen)) < 0){
						printf("accept() failed\n");
						continue;
					}

					FD_SET(connfd, &readset);
					if(fdmax < connfd){
						fdmax = connfd;										// reassign fdmax to new connfd
					}

					currentUserNum++;										// increase user number
				}
				else{
					if((n = read(i, buf, MAXRCVLINE)) > 0){
						if(clientMsgCnt[i-4] == 0){							// initial message
							char digit[2];
							sprintf(digit, "%d", currentUserNum);			// stringfy current user count
							strncpy(clientName[i-4], buf, n-1);				// copy user name
							clientName[i-4][n-1] = '\0';
							for(int j=4;j<fdmax+1;j++){
								if(i != j){
									write(j, clientName[i-4], strlen(clientName[i-4]));
									write(j, " joined. ", strlen(" joined. "));
									write(j, &digit, strlen(digit));
									write(j, " current users.\n", strlen(" current users.\n"));
								}
							}
						}
						else{
							printf("got %d bytes from user %s.\n", n, clientName[i-4]);
							for(int j=4;j<fdmax+1;j++){
								if(i != j){									// send to everyone but sender
									write(j, clientName[i-4], strlen(clientName[i-4]));
									write(j, ":", strlen(":"));
									write(j, buf, n);
								}
							}
						}
						clientMsgCnt[i-4]++;
					}
					else{
						char digit[2];
						sprintf(digit, "%d", --currentUserNum);				// stringfy current user count
						clientMsgCnt[i-4] = 0;								// reset msg cnt of the user
						for(int j=4;j<fdmax+1;j++){
							if(i != j){
								write(j, clientName[i-4], strlen(clientName[i-4]));
								write(j, " leaved. ", strlen(" leaved. "));
								write(j, &digit, strlen(digit));
								write(j, " current users.\n", strlen(" current users.\n"));
							}
						}
						FD_CLR(i, &readset);
						close(i);
					}				
				}
			}
		}
	}
	printf("connection terminated.\n");
	close(listenfd);
	return 0;
}
