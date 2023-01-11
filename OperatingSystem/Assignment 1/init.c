/*
 *  Subject : Operating System
 *  Date    : 2022.10.02
 *	File	: init.c
 *  Todo    : Initialize process, ready queues, sleep-state process array
 *  Author  : 2020315798 ChoiJinWoo
 */

#include "common.h"

void initPrcs(char* fileName){
	FILE *fp = fopen(fileName, "r");									// open input file
	if(fp == NULL){
		fileOpenError();
	}

	int lineCnt = 0;
	int colCnt = 0;
	char str[BUFFER];
	int* PIDptr;

	while(!feof(fp)){
		fgets(str, sizeof(str), fp);
		char* ptr = strtok(str, " ");

		if(lineCnt == 0){												// # of processes in the input file
			prcs_cnt = atoi(ptr);
			prcsList = (prcs_t**)malloc(sizeof(prcs_t)*prcs_cnt);		// allocate memory for list of processes
			PIDptr = (int*)malloc(sizeof(int)*prcs_cnt);				// allocate memory for PID duplication check
			memoryAllocationError(prcsList);
			memoryAllocationError(PIDptr);
		}
		else{															// information of processes in the input file
			prcs_t *prcs = (prcs_t*)malloc(sizeof(prcs_t));				// allocate memory for new empty process
			memoryAllocationError(prcs);
			
			prcs->allocateTime = -1;
			prcs->CPUburstTime = 0;
			prcs->exitTime = -1;
			prcs->currQueue = -1;
			prcs->burstIdx = 0;

			colCnt = 0;
			while(ptr != NULL){											// assign process information in each process
				if(colCnt == 0){
					prcs->pId = atoi(ptr);

					if(prcs->pId > prcs_cnt){
						fileSyntaxError("Invalid process Id");
					}
					if(isPIDDupl(PIDptr, prcs->pId, lineCnt)){
						fileSyntaxError("Duplicated process Id");
					}
					*(PIDptr+lineCnt-1) = prcs->pId;					// temporary save PID
				}
				else if(colCnt == 1){
					prcs->arrivalTime = atoi(ptr);
				}
				else if(colCnt == 2){
					prcs->prevQueue = atoi(ptr);

					if(prcs->prevQueue > IDX_RQ2){
						fileSyntaxError("Maximum queue exceeded");
					}
				}
				else if(colCnt == 3){
					prcs->burstCycle = atoi(ptr);	
					prcs->CPUburstList = (int*)malloc(sizeof(int)*(prcs->burstCycle));
					prcs->IOburstList = (int*)malloc(sizeof(int)*(prcs->burstCycle-1));
					memoryAllocationError(prcs->CPUburstList);
					memoryAllocationError(prcs->IOburstList);
				}
				else{
					if(colCnt % 2 == 0){
						prcs->CPUburstList[(colCnt-4)/2] = atoi(ptr);
					}
					else{
						prcs->IOburstList[(colCnt-5)/2] = atoi(ptr);
					}
				}
				colCnt++;
				ptr = strtok(NULL, " ");
			}
			prcsList[lineCnt-1] = prcs;									// register to process list
		}
		lineCnt++;
	}
	if(lineCnt-1 != prcs_cnt){
		fileSyntaxError("Process count mismatch");
	}
	free(PIDptr);
	fclose(fp);
}

void initQueue(){														// allocate memory & initialize
	rq0 = (prcs_t**)malloc(sizeof(prcs_t)*prcs_cnt);
	rq1 = (prcs_t**)malloc(sizeof(prcs_t)*prcs_cnt);
	rq2 = (prcs_t**)malloc(sizeof(prcs_t)*prcs_cnt);
	sq = (prcs_t**)malloc(sizeof(prcs_t)*prcs_cnt);

	memoryAllocationError(rq0);		
	memoryAllocationError(rq1);
	memoryAllocationError(rq2);		
	memoryAllocationError(sq);
	
	for(int i=0;i<prcs_cnt;i++){
		rq0[i] = NULL;
		rq1[i] = NULL;
		rq2[i] = NULL;
		sq[i] = NULL;
	}
}

int isPIDDupl(int *ptr, int val, int index){
	int result = 0;
	
	for(int i=0;i<index;i++){
		if(*(ptr+i) == val){
			result = 1;
		}
	}

	return result;
}