#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <arpa/inet.h>
#define MAXLINE 80
#define FILE_CONTENT_SIZE 200000

int main (int argc, char *argv[]) {
	int n, listenfd, connfd, caddrlen;
	struct hostent *h;
	struct sockaddr_in saddr, caddr;
	char buf[MAXLINE] = "";
	int port = atoi(argv[1]);

	if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		printf("socket() failed.\n");
		exit(1);
	}

	memset((char *)&saddr, 0, sizeof(saddr));
	saddr.sin_family = AF_INET;
	saddr.sin_addr.s_addr = htonl(INADDR_ANY);
	saddr.sin_port = htons(port); 
	if (bind(listenfd, (struct sockaddr *)&saddr, sizeof(saddr)) < 0) {
		printf("bind() failed.\n");
		exit(2);
	}
	if (listen(listenfd, 5) < 0) {
		printf("listen() failed.\n");
		exit(3);
	}

	char cmdLine[] = "File name : ";
	while (1) {
		caddrlen = sizeof(caddr);
		if ((connfd = accept(listenfd, (struct sockaddr *)&caddr,
			(socklen_t *)&caddrlen)) < 0) {
			printf ("accept() failed.\n");
			continue;
		}
		// echo
		while ((n = read(connfd, buf, MAXLINE)) > 0) {

			write(1, cmdLine, strlen(cmdLine));
			write(1, buf, strlen(buf));
			write(1, "\n", 1);

			if(!strcmp(buf, "quit")){
				exit(1);
			}

			printf("got %d bytes from client.\n", n);


			int newFileLen = strlen(buf) + 6;
			char *newFileName = (char*)malloc(newFileLen);
			strcpy(newFileName, buf);
			strcat(newFileName, "_copy\0");

			// Open source fd to read text, dest fd to write text
			int sfd, dfd;
			off_t endPos;
			char content[FILE_CONTENT_SIZE] = " ";
			if((sfd = open(buf, O_RDONLY)) < 0){
				perror("File open error\n");
				exit(1);
			}
			if((dfd = open(newFileName, O_WRONLY|O_CREAT, 0644)) < 0){
				perror("New file open error\n");
				exit(1);
			}
			
			// get total size of the text and copy
			endPos = lseek(sfd, 0, SEEK_END);
			lseek(sfd, 0, SEEK_SET);
			read(sfd, content, endPos);
			write(dfd, content, endPos);

			close(sfd);
			close(dfd);
			free(newFileName);
			for(int i=0;i<MAXLINE;i++){
				buf[i] = '\0';
			}
		}
		printf("connection terminated.\n");
		close(connfd);
	}
}	
