/*
 *  Subject : Problem Solving Techniques
 *  Date    : 2022.10.07
 *  Todo    : Assignment1 - SKKU-SEQ
 *  Author  : 2020315798 ChoiJinWoo
 */
 
#include <stdio.h>
#include <string.h>
#pragma warning(disable : 4996)
#define SIZE 300

int* seq[SIZE];
int seqSize[SIZE];

void funct_rseq(int seqIdx, int *N){
	for(int i=seqIdx;i<*N;i++){
		seq[i-1] = seq[i];
		seqSize[i-1] = seqSize[i];
	}

	seq[*N-1] = NULL;
	seqSize[*N-1] = 0;
	*N -= 1;
}

int main(){
	char cmd_type[11];
	int N = 0;

	while(1){
		scanf("%s", cmd_type);

		if(!strcmp(cmd_type, "new")){
			int first, diff, m;
			scanf("%d %d %d", &first, &diff, &m);

			int *ptr = (int*)malloc(sizeof(int)*m);

			for(int i=0;i<m;i++){
				*(ptr+i) = first + diff * i;
			}

			seq[N] = ptr;
			seqSize[N] = m;
			N++;
		}
		else if (!strcmp(cmd_type, "insert")){
			int seqIdx, tIdx, num;
			scanf("%d %d %d", &seqIdx, &tIdx, &num);

			int *ptr = seq[seqIdx-1];
			int *nptr = (int*)malloc(sizeof(int)*(seqSize[seqIdx-1]+1));	
			
			for(int i=0;i<tIdx;i++){
				*(nptr+i) = *(ptr+i);
			}
			for(int i=tIdx;i<seqSize[seqIdx-1];i++){
				*(nptr+i+1) = *(ptr+i);
			}
			*(nptr+tIdx) = num;

			seq[seqIdx-1] = nptr;
			seqSize[seqIdx-1]++;
		}
		else if (!strcmp(cmd_type, "rseq")){
			int seqIdx;
			scanf("%d", &seqIdx);
			
			funct_rseq(seqIdx, &N);
		}
		else if (!strcmp(cmd_type, "rrange")){
			int seqIdx, sIdx, eIdx;
			scanf("%d %d %d", &seqIdx, &sIdx, &eIdx);
			
			int rsize = eIdx - sIdx + 1;
			int nsize = seqSize[seqIdx-1] - rsize;
			
			if(nsize == 0){
				funct_rseq(seqIdx, &N);
			}
			else{
				int *ptr = seq[seqIdx-1];
				int *nptr = (int*)malloc(sizeof(int)*nsize);
				
				for(int i=0;i<sIdx-1;i++){
					*(nptr+i) = *(ptr+i);
				}
				for(int i=eIdx;i<seqSize[seqIdx-1];i++){
					*(nptr+i-rsize) = *(ptr+i);
				}
				seq[seqIdx-1] = nptr;
				seqSize[seqIdx-1] -= rsize;
			}
		}
		else if (!strcmp(cmd_type, "rone")){
			int num;
			scanf("%d", &num);

			for(int i=0;i<N;i++){
				int count = 0;
				for(int j=0;j<seqSize[i];j++){
					if(seq[i][j] == num){
						count++;
					}
				}
				
				if(count < seqSize[i]){
					int idx = 0;
					int *ptr = seq[i];
					int *nptr = (int*)malloc(sizeof(int)*(seqSize[i] - count));

					for(int j=0;j<seqSize[i];j++){
						if(seq[i][j] != num){
							*(nptr+idx) = seq[i][j];
							idx++;
						}
					}
					seq[i] = nptr;
					seqSize[i] = seqSize[i] - count;
				}
				else{
					funct_rseq(i+1, &N);
				}
			}
		}
		else if (!strcmp(cmd_type, "sum")){
			for(int i=0;i<N;i++){
				int sum = 0;

				for(int j=0;j<seqSize[i];j++){
					sum += seq[i][j];
				}

				printf("%d\n", sum);
			}
		}
		else if (!strcmp(cmd_type, "exit")){
			for(int i=0;i<N;i++){
				free(seq[i]);
			}
			break;
		}
	}
	return 0;
}
