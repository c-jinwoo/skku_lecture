#include <stdio.h>
#include <stdlib.h>
#pragma warning(disable: 4996)	// Ignore this command if you are not on Microsoft Visual Studio
#define N 100

int n, m, x[N];					// n : # of elements, m : Reverse time
int seq[N][2];					// Reverse log
int calcReverse(int *arr, int idx1, int idx2);
int getScore(int *arr);
void excuteReverse(int *arr, int idx1, int idx2);

int main() {
	scanf("%d %d", &n, &m);
	for(int i=0;i<n;i++){
		scanf("%d", &x[i]);									// Get sequence from user
	}
	for(int i=0;i<m;i++){
		seq[i][0] = seq[i][1] = 0;							// Initialize to 0
	}
	int rIndex = 0;											// Current reverse index
	int rCnt = 1 + n*(n-1)/2;								// Total number of reverse
	int* rResultArr = (int*)malloc(sizeof(int)*rCnt);		// for saving score log
	int** rHistArr = (int**)malloc(sizeof(int*)*rCnt);		// for saving index log

	for(int i=0;i<rCnt;i++){
		rHistArr[i] = (int*)malloc(sizeof(int)*2);
	}
	
	for(int t=0;t<m;t++){
		// Set initial condition
		rIndex = 0;
		rResultArr[rIndex] = getScore(x);
		rHistArr[rIndex][0] = 0;
		rHistArr[rIndex][1] = 0;
		rIndex++;

		// Save history
		for(int i=0;i<n-1;i++){
			for(int j=i+1;j<n;j++){
				int result = calcReverse(x, i, j);
				rResultArr[rIndex] = result;
				rHistArr[rIndex][0] = i;
				rHistArr[rIndex][1] = j;
				rIndex++;
			}
		}
		
		// Find minimal value
		int tempIdx = 0;
		int tempSum = rResultArr[0];
		for(int i=1;i<rCnt;i++){
			if(tempSum > rResultArr[i]){
				tempIdx = i;
				tempSum = rResultArr[i];
			}
		}
		
		// Save to seq array
		seq[t][0] = rHistArr[tempIdx][0];
		seq[t][1] = rHistArr[tempIdx][1];

		// Reverse original sequence
		excuteReverse(x, rHistArr[tempIdx][0], rHistArr[tempIdx][1]);
		
		// Quit loop if it reached the minimum score
		if((seq[t][0] == 0) && (seq[t][1] == 0)){
			break;
		}
	}

	free(rHistArr);
	free(rResultArr);

	for(int i=0;i<m;i++){
		printf("%d %d\n", seq[i][0], seq[i][1]);
	}

	return 0;
}
int calcReverse(int *arr, int idx1, int idx2){
	int result = 0;
	int *arr_cp = (int*)malloc(sizeof(int)*n);

	for(int i=0;i<n;i++){
		arr_cp[i] = arr[i];									// Copy the array
	}

	excuteReverse(arr_cp, idx1, idx2);						// Excute reverse to the copied array
	result = getScore(arr_cp);								// Get result score from the array
	
	free(arr_cp);											// Free malloc
	arr_cp = NULL;

	return result;
}
void excuteReverse(int *arr, int idx1, int idx2){
	int count = idx2 - idx1 + 1;							// Calculate index count
	int *subarr = (int*)malloc(sizeof(int)*count);			// Malloc temporary array

	for(int i=0;i<count;i++){
		subarr[i] = arr[i+idx1];							// Copy the amount to the temp array
	}
	for(int i=0;i<count;i++){
		arr[i+idx1] = subarr[count - 1 - i];				// Reallocate original array
	}

	free(subarr);											// Free malloc
	subarr = NULL;
}
int getScore(int *arr){
	int sum = 0;											// For saving the result
	int previousNum = 0;									// Save previous number to check if repeating
	int repeatCnt = 1;										// Count repeated times for the same number

	for(int i=0;i<n;i++){
		if(i > 0){
			int currentNum = arr[i];

			if(currentNum == previousNum){
				repeatCnt++;								// Increase count if the number repeats
			}
			if((currentNum != previousNum) || (i == n-1)){
				if(repeatCnt > 1){
					sum += (arr[i-1] * repeatCnt);			// add to the result
				}
				repeatCnt = 1;								// Reset repeat count
			}
		}
		previousNum = arr[i];								// Save current value to use as previous one
	}
	return sum;
}