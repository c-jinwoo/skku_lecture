/*
 * Subject  : System Programming Lab
 * Lecture  : Week11 - PThread
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
#include <pthread.h>

typedef struct {
	long index;
	int sum;
} thread_value;

int M;
int N;
int **matrix;
int *vector;

void *thread(void *arg){
	long index = (long)arg;

	thread_value *tval = (thread_value*)malloc(sizeof(thread_value));

	int result = 0;
	int sum = 0;

	for(int i=0;i<N;i++){
		result = matrix[index][i] * vector[i];
		sum += result;
	}
	tval->index = index;
	tval->sum = sum;
	pthread_exit((void*)tval);
}

int main(int argc, char* argv[]){
	srand(time(NULL));
	M = atoi(argv[1]);
	N = atoi(argv[2]);
	matrix = (int**)malloc(sizeof(int*)*M);
	vector = (int*)malloc(sizeof(int)*M);

	for(int i=0;i<M;i++){
		matrix[i] = (int*)malloc(sizeof(int)*N);
		for(int j=0;j<N;j++){
			matrix[i][j] = rand()%10;
			vector[j] = rand()%10;
		}
	}
	
	// Thread °è»ê
	pthread_t *tid = (pthread_t*)malloc(sizeof(pthread_t)*M);
	long t;
	for(t=0;t<M;t++){
		if(pthread_create(&tid[t], NULL, thread, (void*)t)){
			printf("ERROR: pthread creation failed.\n");
			exit(1);
		}
	}

	printf("*** Matrix ***\n");
	for(int i=0;i<M;i++){
		for(int j=0;j<N;j++){
			printf("[ %d ] ", matrix[i][j]);
		}
		printf("\n");
	}

	printf("\n*** Vector ***\n");
	for(int i=0;i<N;i++){
		printf("[ %d ]\n", vector[i]);
	}

	printf("\n*** Result ***\n");
	for(t=0;t<M;t++){
		thread_value *data;
		pthread_join(tid[t], (void**)&data);
		printf("[ %d ]\n", data->sum);
	}

	free(tid);

	return 0;
}