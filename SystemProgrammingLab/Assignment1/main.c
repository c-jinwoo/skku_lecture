/*
 * Subject	: System Programming Lab
 * Lecture	: Assigment 1
 * Author	: Choi Jin Woo
 * SID		: 2020315798
 */

#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <string.h>
#include <stdlib.h>
#include <grp.h>
#include <pwd.h>
#define MAX_CMD_LEN 200
#define ARGC 10
#define STDIN 0
#define STDOUT 1
#define STDERR 2
#define CHAR2INT 48
#define PERMIT_RW 0666
#define PERMIT_FOLDER 0755
#define PERMIT_MKDIR 0777

int ls(char *dir_path, char *option);
int head(char *file_path, char *line);
int tail(char *file_path, char *line);
int mv(char *file_path1, char* file_path2);
int mk_dir(char *dir_path);
int rm_dir(char *dir_path);

char* getFilePermission(mode_t st_mode);
int isPathValid(char *path, char file_mode);
char* int2str(int val);
int str2int(char *val);
int power(int val, int pow);

char *NEWLINE = "\n";
char *SPACE = " ";
char *TAB = "\t";

int main(){
	while(1){
		int i = 0, cmdret;
		char cmd[MAX_CMD_LEN] = " ";
		char *argument[ARGC];
	    size_t size;
		
        // Input commands
		char *arrow = ">> ";
		write(STDOUT, arrow, strlen(arrow));				// print out ">> "
		read(STDIN, cmd, MAX_CMD_LEN);						// read cmd from keyboard
        
        // Tokenize commands (Use strtok function)
		for(int j=0;j<ARGC;j++){
			argument[j] = NULL;								// initialize to NULL
		}

		char *ptr = strtok(cmd, " ");						// strtok and save to ptr
		
		while(ptr != NULL){
			argument[i++] = ptr;
			ptr = strtok(NULL, " ");
		}
		argument[i-1][strlen(argument[i-1])-1] = '\0';		// replace newline character with null character
		        
		// Command cases
		if(!strcmp("ls", argument[0])){
			cmdret = ls(argument[1], argument[2]);		
		}
		else if(!strcmp("head", argument[0])){
			cmdret = head(argument[3], argument[2]);
		}
		else if(!strcmp("tail", argument[0])){
			cmdret = tail(argument[3], argument[2]);
		}
		else if(!strcmp("mv", argument[0])){
			cmdret = mv(argument[1], argument[2]);
		}
		else if(!strcmp("mkdir", argument[0])){
			cmdret = mk_dir(argument[1]);
		}
		else if(!strcmp("rmdir", argument[0])){
			cmdret = rm_dir(argument[1]);
		}
		else if(!strcmp("quit", argument[0])){
			write(STDOUT, NEWLINE, strlen(NEWLINE));
			break;
		}
		else{
			char err_msg[] = "ERROR: Invalid commands";
			write(STDERR, err_msg, strlen(err_msg));
            continue;
		}

		if(cmdret == -1){
            char err_msg[] = "ERROR: The command is executed abnormally\n";
			write(STDERR, err_msg, strlen(err_msg));
		}

		write(STDOUT, NEWLINE, strlen(NEWLINE));
	}
	return 0;
}

int ls(char *dir_path, char *option){
	if(dir_path == NULL){
		dir_path = ".";
	}
	if(!strcmp(dir_path, "-al")){
		dir_path = ".";
		option = "-al";
	}

	DIR *directory;
	struct dirent *de;
	struct stat st;
	struct passwd *pwd;
	struct group *grp;
	char final_path[300] = "";
	char* isTilda = strchr(dir_path, '~');

	if(isTilda != NULL){
		directory = opendir(".");
		de = readdir(directory);
		stat(de->d_name, &st);
		pwd = getpwuid(st.st_uid);
		
		strcat(final_path, "/home/");
		strcat(final_path, pwd->pw_name);
		strcat(final_path, isTilda+1);
	}
	else{
		strcpy(final_path, dir_path);
	}

	if(!isPathValid(final_path, 'd')){
		return -1;
	}

	// Result	
	directory = opendir(final_path);
	if(!directory){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}

	while(de = readdir(directory)){
		if(option == NULL){
			if(de->d_name[0] != '.'){
				char *dir = strcat(de->d_name, " ");

				write(STDOUT, dir, strlen(dir));
			}
		}
		else{
			if(!strcmp(option, "-al")){
				stat(de->d_name, &st);
				pwd = getpwuid(st.st_uid);
				grp = getgrgid(st.st_gid);
				
				char *permission = strcat(getFilePermission(st.st_mode), SPACE);
				char *hardlink = strcat(int2str(st.st_nlink), SPACE);
				char *filesize = strcat(int2str(st.st_size), TAB);
			
				write(STDOUT, permission, strlen(permission));
				write(STDOUT, hardlink, strlen(hardlink));
				write(STDOUT, strcat(pwd->pw_name, SPACE), strlen(strcat(pwd->pw_name, SPACE)));
				write(STDOUT, strcat(grp->gr_name, SPACE), strlen(strcat(grp->gr_name, SPACE)));
				write(STDOUT, filesize, strlen(filesize));
				write(STDOUT, de->d_name, strlen(de->d_name));
				write(STDOUT, NEWLINE, strlen(NEWLINE));
				
				free(permission);
				free(hardlink);
				free(filesize);
			}
			else{
				return -1;
			}
		}
	}
	if(option == NULL){
		write(STDOUT, NEWLINE, strlen(NEWLINE));
	}

	closedir(directory);

	return 0;
}

int head(char *file_path, char *line){	
	if(file_path == NULL){
		return -1;
	}
	if(!isPathValid(file_path, '-')){
		return -1;
	}

	int fd = open(file_path, O_RDONLY);
	if(fd < 0){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	
	int line_idx = 0;	
	int line_input = str2int(line);
	off_t prev_pos = 0;
	off_t curr_pos = 0;
	off_t last_pos = lseek(fd, 0, SEEK_END);
	lseek(fd, 0, SEEK_SET);

	while(1){
		char letter;
		read(fd, &letter, 1);
		curr_pos = lseek(fd, 0, SEEK_CUR);

		if(letter == '\n'){
			int word_byte = curr_pos - prev_pos;
			char *buf = (char*)malloc(word_byte*sizeof(char));
			
			// return to the first letter of the last new line
			lseek(fd, -word_byte, SEEK_CUR);
			read(fd, buf, word_byte);
			write(STDOUT, buf, word_byte*sizeof(char));
			
			// save current pos to previous pos
			prev_pos = curr_pos;
			
			line_idx++;
			if(line_idx >= line_input){
				free(buf);
				break;
			}
		}
	}
	close(fd);

	return 0;
}

int tail(char *file_path, char *line){
	if(file_path == NULL){
		return -1;
	}
	if(!isPathValid(file_path, '-')){
		return -1;
	}	

	int fd = open(file_path, O_RDONLY);
	if(fd < 0){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}

	int line_idx = 0;	
	int line_input = str2int(line);
	int word_byte = 0;

	lseek(fd, 0, SEEK_END);

	while(1){
		off_t curr_pos = lseek(fd, -2, SEEK_CUR);

		char letter;
		read(fd, &letter, 1);
		
		// count new line character until input line number exceeds
		if(letter == '\n'){
			line_idx++;
			if(line_idx >= line_input){
				break;
			}
		}
		if(curr_pos == 0){
			lseek(fd, -1, SEEK_CUR);
			word_byte++;
			break;
		}
		word_byte++;
	}
	
	// print whole text from lseek
	char *buf = (char*)malloc((word_byte+1)*sizeof(char));
	read(fd, buf, (word_byte+1));
	write(STDOUT, buf, (word_byte+1)*sizeof(char));

	free(buf);
	close(fd);
	
	return 0;
}

int mv(char *file_path1, char *file_path2){
	if(file_path1 == NULL || file_path2 == NULL){
		return -1;
	}
	if(!isPathValid(file_path1, '-') || !isPathValid(file_path2, '-')){
		return -1;
	}

	int sfd, dfd;
	if((sfd = open(file_path1, O_RDONLY))< 0){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	if((dfd = open(file_path2, O_WRONLY | O_CREAT, PERMIT_RW)) < 0){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	unlink(file_path2);
	dfd = open(file_path2, O_WRONLY | O_CREAT, PERMIT_RW);

	off_t last_pos = lseek(sfd, 0, SEEK_END);
	lseek(sfd, 0, SEEK_SET);
	
	// count number of text in source file and copy all text to destination file
	char *content = (char*)malloc(last_pos*sizeof(char));
	read(sfd, content, last_pos);
	write(dfd, content, last_pos);
	free(content);	
	close(sfd);
	close(dfd);

	// delete original file
	unlink(file_path1);

	return 0;
}

int mk_dir(char *dir_path){
	if(dir_path == NULL){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	if(!isPathValid(dir_path, ' ')){
		return -1;
	}
    if(mkdir(dir_path, PERMIT_MKDIR) == -1){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
    }
    return 0;
}

int rm_dir(char *dir_path){
	if(dir_path == NULL){
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	if(!isPathValid(dir_path, 'd')){
		return -1;
	}

	DIR *dir_ptr = opendir(dir_path);
    struct dirent *de = NULL;
	int fileCnt = 0;

    while((de = readdir(dir_ptr)) != NULL) {
        if(!strcmp(de->d_name, ".") || !strcmp(de->d_name, "..")){
             continue;
        }
		fileCnt++;
    }	
    closedir(dir_ptr);

	if(fileCnt > 0){
		char err_msg[] = "rmdir: failed to remove 'dir_path': Directory is not empty\n";
		write(STDERR, err_msg, strlen(err_msg));
		return -1;
	}
	else{
		rmdir(dir_path);
	}    

    return 0;
}

char* getFilePermission(mode_t st_mode){
	char *permission = (char*)malloc(11*sizeof(char));

	if(S_ISREG(st_mode)){
		*permission = '-';
	}
	else if(S_ISDIR(st_mode)){
		*permission = 'd';
	}
	else if(S_ISBLK(st_mode)){
		*permission = 'b';
	}
	else if(S_ISCHR(st_mode)){
		*permission = 'c';
	}
	else if(S_ISLNK(st_mode)){
		*permission = 'l';
	}	
	*(permission+1) = (st_mode & S_IRUSR) ? 'r' : '-';
	*(permission+2) = (st_mode & S_IWUSR) ? 'w' : '-';
	*(permission+3) = (st_mode & S_IXUSR) ? 'x' : '-';
	*(permission+4) = (st_mode & S_IRGRP) ? 'r' : '-';
	*(permission+5) = (st_mode & S_IWGRP) ? 'w' : '-';
	*(permission+6) = (st_mode & S_IXGRP) ? 'x' : '-';
	*(permission+7) = (st_mode & S_IROTH) ? 'r' : '-';
	*(permission+8) = (st_mode & S_IWOTH) ? 'w' : '-';
	*(permission+9) = (st_mode & S_IXOTH) ? 'x' : '-';
	*(permission+10) = '\0';

	return permission;
}

int isPathValid(char *path, char file_mode){
	int result = 1;
	struct stat st;
	stat(path, &st);

	if(file_mode == 'd'){
		if(S_ISREG(st.st_mode)){
			result = 0;
		}
	}
	else if(file_mode == '-'){
		if(S_ISDIR(st.st_mode)){
			result = 0;
		}
	}

	if(!result){		
		char err_msg[] = "ERROR: invalid path\n";
		write(STDERR, err_msg, strlen(err_msg));
	}

	return result;
}

char* int2str(int val){
	int temp = val;
	int digit = 1;

	while(temp / 10 > 0){
		temp /= 10;
		digit++;
	}

	char *result = (char*)malloc(digit*sizeof(char));
	
	for(int i=0;i<digit;i++){
		int pow = power(10, digit-i-1);
		char num = val / pow;
		val %= pow;
		*(result+i) = num + CHAR2INT;
	}
	*(result+digit) = '\0';

	return result;
}

int str2int(char *val){
	int result = 0;

	for(unsigned int i=0;i<strlen(val);i++){
		int num = *(val+i) - CHAR2INT;
		int digit = power(10, strlen(val)-i-1);
		result += (num * digit);
	}

	return result;
}

int power(int val, int pow){
	int result = 1;

	if(pow > 0){
		for(int i=0;i<pow;i++){
			result *= val;
		}
	}

	return result;
}