/*
 *  Subject : Operating System
 *  Date    : 2022.10.02
 *	File	: common.h
 *  Todo    : Define global vars
 *  Author  : 2020315798 ChoiJinWoo
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define BUFFER 1024				// Bytes of buffer when reading line from input txt
#define SPACE 32				// ASCII code for Space
#define RQ0TQ 2					// Time quantum of Ready queue 0 
#define RQ1TQ 4					// Time quantum of Ready queue 1 				
#define IDX_RQ0 0				// Index number of Ready queue 0 
#define IDX_RQ1 1				// Index number of Ready queue 1 
#define IDX_RQ2 2				// Index number of Ready queue 2 
#define IDX_SQ 3				// Index number of Sleep queue
#define MAXTIME 10000			// Maximum time limit of whole process

typedef struct prcs_s{
	int pId;					// Process ID
	int arrivalTime;			// Initial arrival time to queue
	int allocateTime;			// CPU allocation time of the process
	int CPUburstTime;			// CPU burst time of the process
	int exitTime;				// exit time of the process
	int prevQueue;				// Initial arrival queue & Previous queue of the process
	int currQueue;				// Current queue of the process
	int burstIdx;				// Current index of the burst array of the process
	int burstCycle;				// Total index of the burst array of the process
	int *CPUburstList;			// CPU burst array of the process
	int *IOburstList;			// IO burst array of the process
} prcs_t;

prcs_t **rq0;					// Array of processes in queue 0	: RR	(Queue index 0)
prcs_t **rq1;					// Array of processes in queue 1	: RR	(Queue index 1)
prcs_t **rq2;					// Array of processes in queue 2	: SRTN	(Queue index 2)
prcs_t **sq;					// Array of processes in asleep				(Queue index 3)
prcs_t **prcsList;				// Array of processes in total
int prcs_cnt;					// Number of process
int time;						// Current running time of CPU
int prevPrcs;					// Previous process-in-CPUburst index 
int currPrcs;					// Current process-in-CPUburst index