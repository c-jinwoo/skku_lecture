#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>

long point_in_circle;
long point_total;
pthread_mutex_t m = PTHREAD_MUTEX_INITIALIZER;

void *MonteCarlo(void *arg){
	srand((double)time(NULL));
	long index = (long)arg+1;
	long count = 0;
	double x = 0;
	double y = 0;

	for(long i=0;i<point_total;i++){
		x = index * (double)rand()/(double)RAND_MAX;
		y = index * (double)rand()/(double)RAND_MAX;
		if((x*x)+(y*y) <= (index*index)){
			pthread_mutex_lock(&m);
			point_in_circle++;
			pthread_mutex_unlock(&m);
			count++;
		}
	}
	pthread_exit((void*)count);
}


int main(int argc, char *argv[]){
	long thread_cnt = atoi(argv[1]);
	point_total = atoi(argv[2]);
	point_in_circle = 0;

	printf("=======================================\n");
	pthread_t *tid = (pthread_t*)malloc(sizeof(pthread_t)*thread_cnt);
	for(long i=0;i<thread_cnt;i++){
		if(pthread_create(&tid[i], NULL, &MonteCarlo, (void*)i)){
			printf("ERROR: pthread creation failed.\n");
			exit(1);
		}

		long result;
		pthread_join(tid[i], (void**)&result);

		printf("Number of Points in circle #%ld : %ld\n", i+1, result);
	}
	printf("=======================================\n");
	printf("Number of threads : %ld\n", thread_cnt);
	printf("Number of random points : %ld\n", point_total);
	printf("PI : %lf\n", 4 * (double)point_in_circle / (point_total * thread_cnt));

	free(tid);
	
	return 0;
}