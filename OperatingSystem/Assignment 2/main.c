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

	char str[BUFFER];																		// �Է� ����
	int lineCnt = 0;																		// �Է� ���μ� ī��Ʈ
	int wordCnt = 0;																		// �Է� ���� �� �ܾ�� ī��Ʈ
	int processNum = 0;																		// ���μ��� ����(��)
	int resourceTypeNum = 0;																// �ڿ� Ÿ�� ��(��)
	int safeCnt = 0;																		// Safe ���� ī��Ʈ
	int isSafe = 0;																			// Safe ��� ����
	int **maxClaimArr;																		// ��ü ��û ���� ������ �迭(1ȸ ���� �� ����)
	int **curAllocArr;																		// ���� �Ҵ� ���� ������ �迭
	int **addiNeedArr;																		// �ʿ� ���� ���� ������ �迭
	int *totalUnitArr;																		// ��ü ���� ���� �迭(1ȸ ���� �� ����)
	int *availUnitArr;																		// ���� ���� ���� �迭
	int *safeSeqArr;																		// Safe Sequence ���� �迭
	int *safeStateArr;																		// Safe State ���� �迭

	// �Է� ������ ���� ����
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

					maxClaimArr = (int**)malloc(sizeof(int*)*processNum);					// ��ü ��û ���� ������ �迭(2��) �����Ҵ�
					curAllocArr = (int**)malloc(sizeof(int*)*processNum);					// ���� �Ҵ� ���� ������ �迭(2��) �����Ҵ�
					addiNeedArr = (int**)malloc(sizeof(int*)*processNum);					// �ʿ� ���� ���� ������ �迭(2��) �����Ҵ�
				}
				else if(wordCnt == 1){
					if((resourceTypeNum = atoi(ptr)) < 0){
						printf("Exception : Number of resource type cannot be a negative value.\n");
						return -1;
					}

					for(int i=0;i<processNum;i++){
						maxClaimArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// ��ü ��û ���� ������ �迭(1��) �����Ҵ�
						curAllocArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// ���� �Ҵ� ���� ������ �迭(1��) �����Ҵ�
						addiNeedArr[i] = (int*)malloc(sizeof(int)*resourceTypeNum);			// �ʿ� ���� ���� ������ �迭(1��) �����Ҵ�
					}
					totalUnitArr = (int*)malloc(sizeof(int)*resourceTypeNum);				// ��ü ���� ���� �迭 �����Ҵ�
					availUnitArr = (int*)malloc(sizeof(int)*resourceTypeNum);				// ���� ���� ���� �迭 �����Ҵ�
					safeSeqArr = (int*)malloc(sizeof(int)*processNum);						// Safe Sequence ���� �迭 �����Ҵ�
					safeStateArr = (int*)malloc(sizeof(int)*processNum);					// Safe State ���� �迭 �����Ҵ�

					// �ʱ�ȭ
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

	// �ʱ� ���� ���� �� ��� ����
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
	
	// Deadlock Avoidance üũ ���� ����
	while(1){
		int isBecameSafe = 0;

		for(int i=0;i<processNum;i++){
			int availableCnt = 0;									// Safe condition ���� üũ
			int totalNeed = 0;										// ���� ���μ��� �̹� Safe ���� üũ

			for(int j=0;j<resourceTypeNum;j++){
				if(availUnitArr[j] >= addiNeedArr[i][j]){
					availableCnt++;									// �ڿ� �� ���� ���� > �ʿ� ���� �� ��� ī��Ʈ ����
				}
				totalNeed += addiNeedArr[i][j];						// ���μ��� �� �ʿ� ���� �� �ջ�
			}

			if(totalNeed > 0){										// �̹� Safe �� ���μ��� ����
				if(availableCnt == resourceTypeNum){				// ��� �ڿ��� ���� �Ҵ� ������ ���
					for(int j=0;j<resourceTypeNum;j++){
						availUnitArr[j] += curAllocArr[i][j];		// ���� �Ҵ� �ڿ� �� �����п� �߰�
						curAllocArr[i][j] = addiNeedArr[i][j] = 0;	// ���� ���μ����� ���� �Ҵ� �ڿ� ��, �ʿ� �ڿ� �� �ʱ�ȭ
					}
					safeSeqArr[safeCnt] = i+1;						// Safe Sequence ����
					safeStateArr[i] = 1;							// Safe ���� ����
					isBecameSafe = 1;								// ���� ���μ��� Safe ��� ����
					break;
				}
			}
		}


		if(isBecameSafe){
			// Safe �� ���μ����� �ִٸ� ���� ���
			safeCnt++;
			printf(">> Loop %d : Process %d became safe\n", safeCnt, safeSeqArr[safeCnt-1]);
			printOverallStatus(processNum, resourceTypeNum, maxClaimArr, curAllocArr, addiNeedArr);
			printResourceSummary(resourceTypeNum, totalUnitArr, availUnitArr);
			printf("\n");

			int count = 0;
			for(int i=0;i<resourceTypeNum;i++){
				if(totalUnitArr[i] == availUnitArr[i]){
					count++;										// �ڿ� �� ���� ���� ���� üũ
				}
			}

			if(count == resourceTypeNum){
				isSafe = 1;											// ��� ���μ����� Safe�� ��� ���� ����
				break;												
			}
		}
		else{
			break;													// Unsafe ���·� ����
		}
	}

	// ��� ���
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

	// ���� �Ҵ� �� ���� ����������
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