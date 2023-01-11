#include <stdio.h>
#include <stdlib.h>
#pragma warning(disable: 4996)	// Ignore this command if you are not on Microsoft Visual Studio
#define N 100

int n, m, x[N];					// n : # of elements, m : Reverse time
int seq[N][2];					// Reverse log
int freq[N] = {0};				// # of each element
int score[N] = {0};				// score of each element
int reverseCnt = 0;				// # of reverse count

void reverse(int *arr);
void setElementScore();
void excuteReverse(int *arr, int idx1, int idx2, int save);
int getTarget();
int getScore(int *arr);
int getMaxScore();

int main() {
	scanf("%d %d", &n, &m);
	for(int i=0;i<n;i++){
		scanf("%d", &x[i]);
		freq[x[i]-1]++;
	}

	reverse(x);

	for(int i=0;i<m;i++){
		printf("%d %d\n", seq[i][0], seq[i][1]);
	}
	return 0;
}
void reverse(int *arr){
	setElementScore();

	if(m == reverseCnt){
		return;
	}
	else{
		int currentScore = getScore(arr);
		int maxScore = getMaxScore();

		if(currentScore == maxScore){
			excuteReverse(arr, 0, 0, 1);
		}
		else{
			int *arr_cp1 = (int*)malloc(sizeof(int)*n);
			int *arr_cp2 = (int*)malloc(sizeof(int)*n);
			int leftP = 0, rightP = 0, 
				target = getTarget();

			for(int i=0;i<n;i++){
				arr_cp1[i] = arr_cp2[i] = arr[i];				// copy to 2 extra arrays
			}
			for(int i=0;i<n;i++){
				if(arr[i] == target){
					leftP = i;
					break;
				}
			}
			for(int i=n-1;i>=0;i--){
				if(arr[i] == target){
					if(arr[i-1] != target){
						rightP = i;
						break;
					}
				}
			}
			excuteReverse(arr_cp1, leftP, rightP-1, 0);			// reverse copy1 with the option
			excuteReverse(arr_cp2, leftP+1, rightP, 0);			// reverse copy2 with the option

			if(getScore(arr_cp1) >= getScore(arr_cp2)){
				excuteReverse(arr, leftP, rightP-1, 1);			// apply to the original array after comparing the sum
			}
			else{
				excuteReverse(arr, leftP+1, rightP, 1);
			}	

			free(arr_cp1);
			free(arr_cp2);
		}		
		reverseCnt++;											// Increase reverse count
		reverse(arr);
	}
}
void setElementScore(){
	int previousNum = 0;										// Save previous number to check if repeating
	int repeatCnt = 1;											// Count repeated times for the same number
	
	for(int i=0;i<N;i++){
		score[i] = 0;											// initialize every step
	}

	for(int i=0;i<n;i++){
		if(i > 0){
			int currentNum = x[i];

			if(currentNum == previousNum){
				repeatCnt++;									// Increase count if the number repeats
			}
			if((currentNum != previousNum) || (i == n-1)){
				if(repeatCnt > 1){
					score[previousNum-1] += (x[i-1] * repeatCnt);// add to the result
				}
				repeatCnt = 1;									// Reset repeat count
			}
		}
		previousNum = x[i];										// Save current value to use as previous one
	}
}
int getTarget(){
	int targetIdx = 0;

	for(int i=0;i<N;i++){
		if(i > 0){
			int elementCurrentSum = score[i];					// 현재값
			int elementMaxSum = (i+1) * freq[i];				// 최대값
			
			if(elementMaxSum != elementCurrentSum){
				if(freq[i] > 1){
					if((elementMaxSum - elementCurrentSum) > ((targetIdx+1) * freq[targetIdx] - score[targetIdx])){
						targetIdx = i;
					}
				}
			}
		}
		else{
			targetIdx = i;
		}
	}
	return targetIdx+1;
}
void excuteReverse(int *arr, int idx1, int idx2, int save){
	int count = idx2 - idx1 + 1;								// Calculate index count
	int *subarr = (int*)malloc(sizeof(int)*count);				// Malloc temporary array

	for(int i=0;i<count;i++){
		subarr[i] = arr[i+idx1];								// Copy the amount to the temp array
	}
	for(int i=0;i<count;i++){
		arr[i+idx1] = subarr[count - 1 - i];					// Reallocate original array
	}

	if(save){
		seq[reverseCnt][0] = idx1;								// Save reverse log
		seq[reverseCnt][1] = idx2;								// Save reverse log
	}

	free(subarr);												// Free malloc
}
int getScore(int *arr){
	int sum = 0;												// For saving the result
	int previousNum = 0;										// Save previous number to check if repeating
	int repeatCnt = 1;											// Count repeated times for the same number

	for(int i=0;i<n;i++){
		if(i > 0){
			int currentNum = arr[i];

			if(currentNum == previousNum){
				repeatCnt++;									// Increase count if the number repeats
			}
			if((currentNum != previousNum) || (i == n-1)){
				if(repeatCnt > 1){
					sum += (arr[i-1] * repeatCnt);				// add to the result
				}
				repeatCnt = 1;									// Reset repeat count
			}
		}
		previousNum = arr[i];									// Save current value to use as previous one
	}
	return sum;
}
int getMaxScore(){
	int sum = 0;
	
	for(int i=0;i<N;i++){
		if(freq[i] > 1){
			sum += freq[i] * (i+1);
		}
	}
	return sum;
}