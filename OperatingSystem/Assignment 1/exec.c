/*
 *  Subject : Operating System
 *  Date    : 2022.10.02
 *	File	: exec.c
 *  Todo    : Procede overall MFQ workflow
 *  Author  : 2020315798 ChoiJinWoo
 */
 
#include "common.h"

void exec(){
	printf("+-------+--------+\n");
	printf("|   GanttChart   |\n");
	printf("+-------+--------+\n");
	printf("|  PID  |  Time  |\n");
	
	while(1){															// for every 1 second
		prevPrcs = currPrcs;											// store prev process id
		execArrival();		
		calcIOburstTime();
		calcCPUburstTime();
		execPreemption();
		allocateCPU();
		printGanttChart();

		if(isFinished()){
			printTimeResult();
			break;
		}
		else{
			time++;
			if(time > MAXTIME){
				printf("Scheduling ended with time overrun.\n");
				break;
			}
		}
	}
}

void execArrival(){
	for(int i=0;i<prcs_cnt;i++){		
		if(time == prcsList[i]->arrivalTime){
			push(prcsList[i], prcsList[i]->prevQueue);					// execute initial queue insertion at each AT
		}
	}
}

void calcIOburstTime(){
	for(int i=0;i<prcs_cnt;i++){
		if(sq[i] != NULL){
			int pId = sq[i]->pId;
			prcs_t *currPrcsPtr = prcsList[pId-1];

			currPrcsPtr->IOburstList[currPrcsPtr->burstIdx]--;			// reduce IO BT in the burst array of each process in sleep queue 

			if(currPrcsPtr->IOburstList[currPrcsPtr->burstIdx] == 0){	// if IO BT became 0,
				pop(currPrcsPtr);										// remove from sleep queue
				currPrcsPtr->burstIdx++;								// increase burst index of each process
				
				// RQ0 process return to RQ0, else return to RQi-1
				(currPrcsPtr->prevQueue == IDX_RQ0) ? push(currPrcsPtr, IDX_RQ0) : push(currPrcsPtr, currPrcsPtr->prevQueue - 1);
			}
		}
	}
}

void calcCPUburstTime(){		
	if(currPrcs > 0){													// if there is a current process,
		prcs_t *currPrcsPtr = prcsList[currPrcs-1];												

		currPrcsPtr->CPUburstList[currPrcsPtr->burstIdx]--;				// reduce CPU BT in the burst array of the process
		currPrcsPtr->CPUburstTime++;									// increase CPU BT of the process

		if(currPrcsPtr->CPUburstList[currPrcsPtr->burstIdx] == 0){		// if CPU BT is 0,
			pop(currPrcsPtr);											// remove from current Queue
			
			if(currPrcsPtr->CPUburstList[currPrcsPtr->burstCycle-1] != 0){	// if it was not the last CPU burst cycle,
				push(currPrcsPtr, IDX_SQ);								// push to sleep queue
			}
			else{
				currPrcsPtr->exitTime = time;
			}
			currPrcs = 0;
		}	
	}
}

void execPreemption(){
	if(currPrcs > 0){													// if there is a current process
		prcs_t *currPrcsPtr = prcsList[currPrcs-1];
		
		if(currPrcsPtr->currQueue == IDX_RQ0){
			if(time - currPrcsPtr->allocateTime >= RQ0TQ){
				pop(currPrcsPtr);
				push(currPrcsPtr, IDX_RQ1);
				currPrcs = 0;
			}
		}
		else if(currPrcsPtr->currQueue == IDX_RQ1){
			if(time - currPrcsPtr->allocateTime >= RQ1TQ){
				pop(currPrcsPtr);
				push(currPrcsPtr, IDX_RQ2);
				currPrcs = 0;
			}
		}
		else if(currPrcsPtr->currQueue == IDX_RQ2){
			int srt = currPrcsPtr->CPUburstList[currPrcsPtr->burstIdx];
			int result = currPrcsPtr->pId;
			for(int i=0;i<prcs_cnt;i++){
				if(rq2[i] != NULL){
					if(srt > rq2[i]->CPUburstList[rq2[i]->burstIdx]){
						srt = rq2[i]->CPUburstList[rq2[i]->burstIdx];
						result = rq2[i]->pId;
					}
				}
			}
			currPrcs = result;
		}
	}
}

void allocateCPU(){
	if(currPrcs == 0){
		if(len(rq0) > 0){
			currPrcs = rq0[0]->pId;
			prcsList[currPrcs-1]->allocateTime = time;
		}
		else if(len(rq1) > 0){
			currPrcs = rq1[0]->pId;
			prcsList[currPrcs-1]->allocateTime = time;
		}
		else if(len(rq2) > 0){
			int srt = rq2[0]->CPUburstList[rq2[0]->burstIdx];
			int result = rq2[0]->pId;

			for(int i=1;i<prcs_cnt;i++){
				if(rq2[i] != NULL){
					if(srt > rq2[i]->CPUburstList[rq2[i]->burstIdx]){
						srt = rq2[i]->CPUburstList[rq2[i]->burstIdx];
						result = rq2[i]->pId;
					}
				}
			}
			currPrcs = result;
			prcsList[currPrcs-1]->allocateTime = time;
		}
		else{
			currPrcs = 0;
		}
	}
}

void printGanttChart(){
	if(prevPrcs != currPrcs || ((prevPrcs == currPrcs) && (time == 0))){
		printf("+--------%3c%-5d|\n", SPACE, time);
		if(currPrcs > 0){
			printf("|%3c%-4d|%8c|\n", SPACE, currPrcs, SPACE);			// when the process changes to other process
		}
		else{
			if(!isFinished()){
				printf("|  Not Allocated |\n");							// when the process changed but no process to be allocated
			}
		}
	}
	if(isFinished()){
		printf("+----------------+\n");
	}
}

void printTimeResult(){
	double tt_avg = 0.0;
	double wt_avg = 0.0;

	printf("+---------------------+\n");
	printf("|     Result Table    |\n");
	printf("+---------------------+\n");
	printf("|  PID  |  TT  |  WT  |\n");
	printf("+---------------------+\n");	

	for(int i=0;i<prcs_cnt;i++){
		int tt = prcsList[i]->exitTime - prcsList[i]->arrivalTime;
		int wt = tt - prcsList[i]->CPUburstTime;

		tt_avg += tt;
		wt_avg += wt;

		printf("|   %-4d|  %-4d|  %-4d|\n", prcsList[i]->pId, tt, wt);
		printf("+---------------------+\n");
	}
	printf("| Average TT:  %-7.2lf|\n", tt_avg / prcs_cnt);
	printf("+---------------------+\n");
	printf("| Average WT:  %-7.2lf|\n", wt_avg / prcs_cnt);
	printf("+---------------------+\n");
}

int isFinished(){
	int result = 0;

	for(int i=0;i<prcs_cnt;i++){
		result += prcsList[i]->CPUburstList[prcsList[i]->burstCycle-1];	// sum up all the last CPU BT of processes
	}
	return (result > 0) ? 0 : 1;										// if it's 0, the overall workload is done
}

prcs_t** getQueuePtr(int queueIdx){
	prcs_t **queuePtr = NULL;
	
	switch(queueIdx){
		case 0:
			queuePtr = rq0;
			break;
		case 1:
			queuePtr = rq1;
			break;
		case 2:
			queuePtr = rq2;
			break;
		case 3:
			queuePtr = sq;
			break;
	}
	return queuePtr;
}

void push(prcs_t* prcs, int queueIdx){
	prcs_t **queuePtr = getQueuePtr(queueIdx);
	int arrLen = len(queuePtr);

	queuePtr[arrLen] = prcs;
	prcs->prevQueue = prcs->currQueue;
	prcs->currQueue = queueIdx;
}

void pop(prcs_t* prcs){
	prcs_t **queuePtr = getQueuePtr(prcs->currQueue);
	int queueIdx = -1;

	for(int i=0;i<prcs_cnt;i++){
		if(queuePtr[i] != NULL){
			if(queuePtr[i]->pId == prcs->pId){
				queueIdx = i;
				queuePtr[i] = NULL;
				break;
			}
		}
	}
	for(int i=queueIdx;i<prcs_cnt;i++){
		queuePtr[i] = (i < prcs_cnt-1) ? queuePtr[i+1] : NULL;		
	}
}

int len(prcs_t** prcsPtr){
	int count = 0;	

	for(int i=0;i<prcs_cnt;i++){
		if(prcsPtr[i] != NULL){
			count++;													// count processes in the given queue
		}
	}
	return count;
}