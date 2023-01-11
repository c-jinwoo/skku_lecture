#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define BUFFER 4096

void printOverallStatus(int processNum, int resourceTypeNum, int** maxClaimArr, int** curAllocArr, int** addiNeedArr){
	printf("+---------------------------------------------------+\n");
	printf("| Process-ID | Max. Claim | Cur. Alloc. | Add. Need |\n");
	printf("+---------------------------------------------------+\n");
	for(int i=0;i<processNum;i++){
		printf("| Process %2d |", i+1);
		for(int j=0;j<resourceTypeNum;j++){
			printf("%3d", maxClaimArr[i][j]);
		}
		printf("   |");
		for(int j=0;j<resourceTypeNum;j++){
			printf("%3d", curAllocArr[i][j]);
		}
		printf("    |");
		for(int j=0;j<resourceTypeNum;j++){
			printf("%3d", addiNeedArr[i][j]);
		}
		printf("  |");
		printf("\n");
	}
}
void printResourceSummary(int resourceTypeNum, int* totalUnitArr, int* availUnitArr){
	printf("+---------------------------------------------------+\n");
	printf("| Total		Unit #	: ");
	for(int i=0;i<resourceTypeNum;i++){
		printf("%3d", totalUnitArr[i]);
	}
	printf("\n+---------------------------------------------------+\n");
	printf("| Available	Unit #	: ");
	for(int i=0;i<resourceTypeNum;i++){
		printf("%3d", availUnitArr[i]);
	}
	printf("\n+---------------------------------------------------+\n");
}

int main(int argc, char* argv[]){
	FILE *fp = fopen(argv[1], "r");
	if(fp == NULL){
		printf("Failed to open input file.\n");						
		return -1;
	}

	char str[BUFFER];																		// 입력 버퍼
	int lineCnt = 0;																		// 입력 라인수 카운트
	int wordCnt = 0;																		// 입력 라인 별 단어수 카운트
	int processNum = 0;																		// 프로세수 갯수(행)
	int resourceTypeNum = 0;																// 자원 타입 수(열)
	int safeCnt = 0;																		// Safe 상태 카운트
	int isSafe = 0;																			// Safe 결과 저장
	int **maxClaimArr;																		// 전체 요청 갯수 이차원 배열(1회 저장 후 고정)
	int **curAllocArr;																		// 현재 할당 갯수 이차원 배열
	int **addiNeedArr;																		// 필요 유닛 갯수 이차원 배열
	int *totalUnitArr;																		// 전체 유닛 갯수 배열(1회 저장 후 고정)
	int *availUnitArr;																		// 여분 유닛 갯수 배열
	int *safeSeqArr;																		// Safe Sequence 저장 배열
	int *safeStateArr;																		// Safe State 저장 배열

	// 입력 데이터 세팅 진행
	while(!feof(fp)){		
		fgets(str, sizeof(str), fp);
		char* ptr = strtok(str, " ");
		
		while(ptr != NULL){
			if(lineCnt == 0){
				if(wordCnt == 0){
					if((processNum = atoi(ptr)) < 0){
						printf("Exception : Number of process cannot be a negative value.\n");
						return -1;
					}

					maxClaimArr = (int**)malloc(sizeof(int*)*processNum);					// 전체 요청 갯수 이차원 배열(2차) 동적할당
					curAllocArr = (int**)malloc(sizeof(int*)*processNum);					// 현재 할당 갯수 이차원 배열(2차) 동적할당
					addiNeedArr = (int**)malloc(sizeof(int*)*processNum);					// 필요 유닛 갯수 이차원 배열(2차) 동적할당
				}
				else if(wordCnt == 1){
					if((resourceTypeNum = atoi(ptr)) < 0){
						printf("Exception : Number of resource type cannot be a negative value.\n");
						return -1;
					}

					for(int i=0;i<processNum;i++){
						maxClaimArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// 전체 요청 갯수 이차원 배열(1차) 동적할당
						curAllocArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// 현재 할당 갯수 이차원 배열(1차) 동적할당
						addiNeedArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// 필요 유닛 갯수 이차원 배열(1차) 동적할당
					}
					totalUnitArr = (int*)malloc(sizeof(int)*resourceTypeNum);				// 전체 유닛 갯수 배열 동적할당
					availUnitArr = (int*)malloc(sizeof(int)*resourceTypeNum);				// 여분 유닛 갯수 배열 동적할당
					safeSeqArr = (int*)malloc(sizeof(int)*processNum);						// Safe Sequence 저장 배열 동적할당
					safeStateArr = (int*)malloc(sizeof(int)*processNum);					// Safe State 저장 배열 동적할당

					// 초기화
					for(int i=0;i<resourceTypeNum;i++){
						totalUnitArr[i] = availUnitArr[i] = safeSeqArr[i] = safeStateArr[i] = 0;								

						for(int j=0;j<processNum;j++){
							maxClaimArr[j][i] = curAllocArr[j][i] = addiNeedArr[j][i] = 0;
						}
					}
				}
				else{
					if(wordCnt-2 < resourceTypeNum){
						if((totalUnitArr[wordCnt-2] = atoi(ptr)) < 0){
							printf("Exception : Number of resource unit cannot be a negative value.\n");
							return -1;
						}
					}
				}
			}
			else{
				int lineIdx = lineCnt - 1;
				if(lineIdx < 2 * processNum){
					if(wordCnt < resourceTypeNum){
						if(lineIdx / processNum < 1){
							if((maxClaimArr[lineIdx % processNum][wordCnt] = atoi(ptr)) < 0){
								printf("Exception : Number of max claim cannot be a negative value.\n");
								return -1;
							}
						}
						else{
							if((curAllocArr[lineIdx % processNum][wordCnt] = atoi(ptr)) < 0){
								printf("Exception : Number of current allocation cannot be a negative value.\n");
								return -1;
							}
						}
					}
				}
			}
			wordCnt++;
			ptr = strtok(NULL, " ");
		}
		wordCnt = 0;
		lineCnt++;
	}

	if(lineCnt < processNum * 2 + 1){
		printf("Exception : Input value is incorrect.\n");
		return -1;
	}

	// 초기 상태 세팅 및 출력 진행
	for(int i=0;i<resourceTypeNum;i++){
		int sum = 0;

		for(int j=0;j<processNum;j++){
			sum += curAllocArr[j][i];

			if((addiNeedArr[j][i] = maxClaimArr[j][i] - curAllocArr[j][i]) < 0){
				printf("Exception : Number of additional need cannot be a negative value.\n");
				return -1;
			}
		}
		if((availUnitArr[i] = totalUnitArr[i] - sum) < 0){
			printf("Exception : Number of avaiable unit cannot be a negative value.\n");
			return -1;
		}
	}
	printf(">> Initial State\n");
	printOverallStatus(processNum, resourceTypeNum, maxClaimArr, curAllocArr, addiNeedArr);
	printResourceSummary(resourceTypeNum, totalUnitArr, availUnitArr);
	printf("\n");
	
	// Deadlock Avoidance 체크 루프 진행
	while(1){
		int isBecameSafe = 0;

		for(int i=0;i<processNum;i++){
			int availableCnt = 0;									// Safe condition 여부 체크
			int totalNeed = 0;										// 현재 프로세스 이미 Safe 여부 체크

			for(int j=0;j<resourceTypeNum;j++){
				if(availUnitArr[j] >= addiNeedArr[i][j]){
					availableCnt++;									// 자원 별 여유 유닛 > 필요 유닛 일 경우 카운트 증가
				}
				totalNeed += addiNeedArr[i][j];						// 프로세스 별 필요 유닛 수 합산
			}

			if(totalNeed > 0){										// 이미 Safe 한 프로세스 제외
				if(availableCnt == resourceTypeNum){				// 모든 자원에 대해 할당 가능할 경우
					for(int j=0;j<resourceTypeNum;j++){
						availUnitArr[j] += curAllocArr[i][j];		// 현재 할당 자원 수 여유분에 추가
						curAllocArr[i][j] = addiNeedArr[i][j] = 0;	// 현재 프로세스의 현재 할당 자원 수, 필요 자원 수 초기화
					}
					safeSeqArr[safeCnt] = i+1;						// Safe Sequence 삽입
					safeStateArr[i] = 1;							// Safe 상태 저장
					isBecameSafe = 1;								// 현재 프로세스 Safe 결과 저장
					break;
				}
			}
		}


		if(isBecameSafe){
			// Safe 된 프로세스가 있다면 상태 출력
			safeCnt++;
			printf(">> Loop %d : Process %d became safe\n", safeCnt, safeSeqArr[safeCnt-1]);
			printOverallStatus(processNum, resourceTypeNum, maxClaimArr, curAllocArr, addiNeedArr);
			printResourceSummary(resourceTypeNum, totalUnitArr, availUnitArr);
			printf("\n");

			int count = 0;
			for(int i=0;i<resourceTypeNum;i++){
				if(totalUnitArr[i] == availUnitArr[i]){
					count++;										// 자원 별 조건 만족 여부 체크
				}
			}

			if(count == resourceTypeNum){
				isSafe = 1;											// 모든 프로세스가 Safe일 경우 루프 종료
				break;												
			}
		}
		else{
			break;													// Unsafe 상태로 종료
		}
	}

	// 결과 출력
	printf(">> Result\n");
	if(isSafe){
		printf("SAFE CONDITION\n\n");
		printf(">> Safe Sequence\n");
		for(int i=0;i<processNum;i++){
			printf("Process %d", safeSeqArr[i]);
			if(i < processNum - 1){
				printf(" -> ");
			}
		}
		printf("\n\n");
	}
	else{
		printf("UNSAFE CONDITION\n\n");
		int totalSafeProcessCnt = 0;
		for(int i=0;i<processNum;i++){
			if(safeStateArr[i]){
				totalSafeProcessCnt++;
			}
		}		
		if(totalSafeProcessCnt > 0){
			printf(">> Safe Process List\n");
			for(int i=0;i<processNum;i++){
				if(safeStateArr[i]){
					printf("Process %d\n", i+1);
				}
			}
			printf("\n");
		}
		printf(">> Unsafe Process List\n");
		for(int i=0;i<processNum;i++){
			if(!safeStateArr[i]){
				printf("Process %d\n", i+1);
			}
		}
		printf("\n");
	}

	// 동적 할당 및 파일 포인터해제
	free(maxClaimArr);
	free(curAllocArr);
	free(addiNeedArr);
	free(totalUnitArr);
	free(availUnitArr);
	free(safeSeqArr);
	free(safeStateArr);
	fclose(fp);
	return 0;
}