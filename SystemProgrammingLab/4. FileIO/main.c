/*
 * Subject	: System Programming Lab
 * Lecture	: Week3 - FileIO
 * Author	: Choi Jin Woo
 * SID		: 2020315798
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>

void itoa(int num, char *str){
	int i = 0;
	int radix = 10;
	int deg = 1;
	int cnt = 0;

	while(1){
		if((num/deg) > 0){
			cnt++;
		}
		else{
			break;
		}
		deg *= radix;
	}
	deg /= radix;

	for(i=0;i<cnt;i++){
		*(str+i) = num/deg + '0';
		num -= ((num/deg) * deg);
		deg /= radix;
	}
	*(str+i) = '\0';
}
int main(int argc, char* argv[]){	
	//char srcFileName[] = "Aladdin.txt";				// Source File Name
	char dstFileName[] = "Aladdin_num.txt";				// Destination File Name
	int srcFd = open(argv[1], O_RDONLY);				// Source File Descriptor
	int dstFd = open(dstFileName, O_WRONLY | O_CREAT);	// Destination File Descriptor
	int lineIdx = 0;									// to store current line index
	int wordIdx = 0;									// to store current word index at current line
	off_t curPos;										// to store current pointer location
	off_t endPos;										// to store end of file pointer location
	
	if((srcFd < 0) || (dstFd < 0)){
		perror("open");
		exit(1);
	}

	endPos = lseek(srcFd, 0, SEEK_END);					// save last index of the file
	lseek(srcFd, 0, SEEK_SET);							// move pointer to the beginning of the file

	while(1){
		char buffer[200];								// create enough buffer to store by lines
		read(srcFd, buffer+wordIdx, 1);					// read 1 byte and save to buffer with offset of wordIdx
		curPos = lseek(srcFd, 0, SEEK_CUR);				// save current pointer info
		wordIdx++;										// increase wordIdx to get next word
		
		if((buffer[wordIdx-1] == '\n') || (curPos >= endPos)){	// check if the line ends or if the file ends	
			char line[] = "";							// to store new line with 
			lineIdx++;									// increase line index
			itoa(lineIdx, line);						// save stingfied line index
			strcat(line, " | ");						// concatenate '|'
			strncat(line, buffer, wordIdx);				// concatenate buffer string for the amount of wordIdx
			write(dstFd, line, strlen(line));			// write each line to destination file
			wordIdx = 0;								// initialize wordIdx to 0 for the next loop
			
			if(curPos >= endPos){
				break;									// break if current position exceeds maximum position
			}	
		}
	}

	close(srcFd);
	close(dstFd);

	return 0;
}
