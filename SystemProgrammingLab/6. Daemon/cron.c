/*
 * Subject  : System Programming Lab
 * Lecture  : Week6 - Daemon
 * Author   : Choi Jin Woo
 * SID      : 2020315798
 */

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <errno.h>
#include <limits.h>
#include <signal.h>
#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <syslog.h>
#include <time.h>
#include <unistd.h>
#include <fcntl.h>

int main(void){
	unsigned int pid;
	time_t t;
	struct tm *tm;	
	int fd;
	char *argv[3];
	char buf[512];
	int fd0, fd1, fd2;

    fd = open("/crontab", O_RDWR);

	// read from crontab of size of buf with error check
	if(read(fd, buf, sizeof(buf)) < 0){
	    perror("read");
	    exit(1);
    }

	pid = fork();
	
	if(pid == -1) return -1;
	if(pid != 0)
		exit(0);
	if(setsid() < 0)
		exit(0);
	if(chdir("/") < 0)
		exit(0);
	
	umask(0);

	close(0);
	close(1);
	close(2);
	
	fd0 = open("/dev/null", O_RDWR);
	fd1 = open("/dev/null", O_RDWR);
	fd2 = open("/dev/null", O_RDWR);
	
	t = time(NULL);
	tm = localtime(&t);
	
	while (1)
	{
		// assign each tokenized buf values to argv
		char *token;
		char *pos = buf;
		int tokenIdx = 0;

		while((token = strtok_r(pos, " ", &pos))){
			argv[tokenIdx] = token;
			tokenIdx++;
		}

		// assign path+filename to path array, concatenate ".sh" : to avoid gabage value in array		
		char path[1000] = " ";
		char *npos = argv[2];
		char *ntoken = strtok_r(npos, ".", &npos);
		
		strcat(path, ntoken);
		strcat(path, ".sh");

		// execute sh file
		int format_m = atoi(argv[0]);
		int format_h = atoi(argv[1]);
		if((*argv[0] == '*' || tm->tm_min == format_m) && (*argv[1] == '*' || tm->tm_hour == format_h)){
			if((pid = fork()) == 0){
				execl("/bin/sh", "/bin/sh", "-c", path, (char*)NULL);
			}
		}

        // logging
		setlogmask(LOG_MASK(LOG_DEBUG));
        openlog("cron", LOG_PID, LOG_LPR);
		syslog(LOG_DEBUG, "Hello?");
		closelog();
		
		t = time(NULL);
		tm = localtime(&t);
		sleep(60 - tm->tm_sec % 60);
	}

	return 0;
}
