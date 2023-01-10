#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#define MAXSNDLINE 80
#define MAXRCVLINE 128
#define MAXNAME 30

int main(int argc, char *argv[]){
	int n;						// number of character from STDIN
	int clientfd;				// client socket
	int port = atoi(argv[2]);	// port number
	char *host = argv[1];		// host address
	char sndbuf[MAXSNDLINE];	// send buffer
	char rcvbuf[MAXRCVLINE];	// receive buffer
	struct hostent *h;			
	struct sockaddr_in saddr;	// server address
	pid_t pid;

	// socket(), gethostbyname(), connect()
	if((clientfd = socket(AF_INET, SOCK_STREAM, 0)) < 0){
		printf("socket() failed.\n");
		exit(1);
	}
	if((h = gethostbyname(host)) == NULL){
		printf("invalid hostname %s\n", host);
		exit(2);
	}

	// Server address setting
	memset((char *)&saddr, 0, sizeof(saddr));
	saddr.sin_family = AF_INET;
	memcpy((char *)&saddr.sin_addr.s_addr, (char *)h->h_addr, h->h_length);
	saddr.sin_port = htons(port);
	
	char name[MAXNAME];
	char welcomeMent[] = "Insert your name : ";
	write(1, welcomeMent, strlen(welcomeMent)); 
	read(0, name, MAXNAME);
	
	if(connect(clientfd, (struct sockaddr *)&saddr, sizeof(saddr)) < 0){
		printf("connect() failed.\n");
		exit(3);
	}

	write(clientfd, name, strlen(name));				// send client name
	if((pid = fork()) == 0){							// Child process : read
		while(1){
			int len = read(clientfd, rcvbuf, MAXRCVLINE);
			rcvbuf[len] = '\0';
			fputs(rcvbuf, stdout);
		}
	}
	else{												// Parent process : write
		while(1){
			fgets(sndbuf, MAXSNDLINE, stdin);			// read message from stdin
			write(clientfd, sndbuf, strlen(sndbuf));	// send to server

			if(!strcmp(sndbuf, "quit\n")){
				kill(0, SIGINT);						// kill child process and break
				break;
			}
		}
	}	
	/*
	while((n = read(0, buf, MAXLINE)) > 0){
		write(clientfd, buf, n);
		n = read(clientfd, buf, MAXLINE);
		write(1, buf, n);
	}
	*/
	close(clientfd);
	return 0;
}

