#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#define BUFFER 4096
#define MAX_PAGE_NUM 100
#define MAX_PAGEFRAME_NUM 20
#define MAX_WINDOWSIZE 100
#define MAX_PAGEREFSTR 1000

void min(int *prsArr, int nPageFrame);
void lru(int *prsArr, int nPageFrame);
void lfu(int *prsArr, int nPageFrame);
void ws(int *prsArr, int nPage, int nWindowSize);
int nPageRefStr = 0;															// ������ ������ ����

int main(int argc, char* argv[]){
	FILE *fp = fopen(argv[1], "r");
	if(fp == NULL){
		printf("Failed to open input file.\n");						
		return -1;
	}

	char str[BUFFER];															// �Է� ����
	int lineCnt = 0;															// �Է� ���μ� ī��Ʈ
	int wordCnt = 0;															// �Է� ���� �� �ܾ�� ī��Ʈ
	int prsCnt = 0;																// ������ ������ ���� Ȯ�ο�
	int nPage = 0;																// ������ ����
	int nPageFrame = 0;															// ������ ������ ����
	int nWindowSize = 0;														// ������ ������
	int* prsArr;																// ������ ������ �迭
	srand(time(NULL));
	/*** �Է� ������ ���� ���� ***/
	while(!feof(fp)){		
		fgets(str, sizeof(str), fp);
		char* ptr = strtok(str, " ");
		
		while(ptr != NULL){
			if(lineCnt == 0){
				if(wordCnt == 0){
					nPage = atoi(ptr);											// ������ ���� ����
					if(nPage <= 0 || nPage > MAX_PAGE_NUM){
						printf("Invalid input : Page number\n");
						return -1;
					}
				}
				else if(wordCnt == 1){
					nPageFrame = atoi(ptr);										// ������ ������ ���� ����
					if(nPageFrame <= 0 || nPageFrame > MAX_PAGEFRAME_NUM){
						printf("Invalid input : Page frame number\n");
						return -1;
					}
				}
				else if(wordCnt == 2){
					nWindowSize = atoi(ptr);									// ������ ������ ����
					if(nWindowSize < 0 || nWindowSize > MAX_WINDOWSIZE){
						printf("Invalid input : Window Size\n");
						return -1;
					}
				}
				else{
					nPageRefStr = atoi(ptr);									// ������ ������ ����
					if(nPageRefStr < 0 || nPageRefStr > MAX_PAGEREFSTR){
						printf("Invalid input : Page Ref Str number\n");
						return -1;
					}
					prsArr = (int*)malloc(sizeof(int)*nPageRefStr);				// ������ ������ �����Ҵ�
				}
			}
			else{
				if(wordCnt < nPageRefStr){
					if(strlen(ptr)){
						prsCnt++;
					}
					int pageIdx = atoi(ptr);
					if(pageIdx < 0 || pageIdx > nPage - 1){
						printf("Invalid input : Page Ref Str\n");
						return -1;
					}
					prsArr[wordCnt] = atoi(ptr);								// ������ �������� ������ ��ȣ �Ҵ�
				}
			}
			wordCnt++;
			ptr = strtok(NULL, " ");
		}
		wordCnt = 0;
		lineCnt++;
	}
	
	if(prsCnt < nPageRefStr){
		printf("Invalid input : Page Ref Str mismatch\n");
		return -1;
	}
	
	min(prsArr, nPageFrame);
	lru(prsArr, nPageFrame);
	lfu(prsArr, nPageFrame);
	ws(prsArr, nPage, nWindowSize);
	
	free(prsArr);
	close(fp);
	return 0;
}

/*** MIN algorithm ***/
void min(int *prsArr, int nPageFrame){
	printf("+------------------------------+\n");
	printf("|         MIN algorithm        |\n");
	printf("+-------------------------------\n");
	printf("| Time | Ref. str | Page fault |\n");
	printf("+------------------------------+\n");
	
	int time = 0;																// ���� �ð�
	int pageFaultCnt = 0;														// Page fault Ƚ��
	int *pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// ������ ������ �迭
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// ������ ������ �迭 -1�� �ʱ�ȭ
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// �̹� �Ҵ� ���ִ��� Ȯ��
			}
		}
		if(!isDupl){															// �ߺ� �Ҵ��� �ƴ� ���
			int isAllocated = 0;												// �� ������ �����ӿ� ������ �Ҵ� ����

			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i] < 0){
					pfArr[i] = prsArr[time];									// �� ������ �����ӿ� ���� ������ �Ҵ�
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// �ߺ� �������� �ƴϰ� �� ������ �����ӵ� ���� ���
				int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);			// ���� �ð� ���� �迭
				for(int i=0;i<nPageFrame;i++){
					timeStamp[i] = nPageRefStr + 1;								// ���� ����� ���� �� ����ؼ� �ִ� �ð�+1�� �ʱ�ȭ

					for(int j=time+1;j<nPageRefStr;j++){
						if(pfArr[i] == prsArr[j]){
							timeStamp[i] = j;									// ���� �ð� �� ���� ����� �ð� ����
							break;
						}
					}
				}				
				int targetIdx = 0;												// ���� �ð��� ���� �� �������� �ε��� �����
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(timeStamp[targetIdx] < timeStamp[i]){
							targetIdx = i;										// �� ������ �������� ���� �ð��� �� Ŭ ��쿡�� Ÿ������ ����
						}
					}
				}				
				pfArr[targetIdx] = prsArr[time];								// ���� �ð��� ���� �� ������ �����ӿ� ���� ������ �Ҵ�
				free(timeStamp);
			}
			isPageFault = 1;													// Page fault �߻�
			pageFaultCnt++;
		}
		// ��� ���
		printf("| %4d |  %4d    |  %4c      |", time+1, prsArr[time], isPageFault ? 'F' : ' ');
		for(int i=0;i<nPageFrame;i++){
			(pfArr[i] == -1) ? printf("   -", pfArr[i]) : printf("%4d", pfArr[i]);
		}
		printf("\n");
		time++;
	}
	printf("+------------------------------+\n");
	printf("|  # of Page fault :  %4d     |\n", pageFaultCnt);
	printf("+------------------------------+\n\n");

	free(pfArr);
}

/*** LRU algorithm ***/
void lru(int *prsArr, int nPageFrame){
	printf("+------------------------------+\n");
	printf("|         LRU algorithm        |\n");
	printf("+-------------------------------\n");
	printf("| Time | Ref. str | Page fault |\n");
	printf("+------------------------------+\n");
	
	int time = 0;																// ���� �ð�
	int pageFaultCnt = 0;														// Page fault Ƚ��
	int* pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// ������ ������ �迭
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// ������ ������ �迭 -1�� �ʱ�ȭ
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// �̹� �Ҵ� ���ִ��� Ȯ��
			}
		}
		if(!isDupl){															// �ߺ� �Ҵ��� �ƴ� ���
			int isAllocated = 0;												// �� ������ �����ӿ� ������ �Ҵ� ����

			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i] < 0){
					pfArr[i] = prsArr[time];									// �� ������ �����ӿ� ���� ������ �Ҵ�
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// �ߺ� �������� �ƴϰ� �� ������ �����ӵ� ���� ���
				int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);			// ���� �ð� ���� �迭
				for(int i=0;i<nPageFrame;i++){
					timeStamp[i] = 0;

					for(int j=time-1;j>=0;j--){
						if(pfArr[i] == prsArr[j]){
							timeStamp[i] = j;									// ���� �ð� �� ���� ����� �ð� ����
							break;
						}
					}
				}				
				int targetIdx = 0;												// ���� �ð��� ���� ������ �������� �ε��� �����
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(timeStamp[targetIdx] > timeStamp[i]){
							targetIdx = i;										// �� ������ �������� ���� �ð��� �� ���� ��쿡�� Ÿ������ ����
						}
					}
				}				
				pfArr[targetIdx] = prsArr[time];								// ���� �ð��� ���� ������ ������ �����ӿ� ���� ������ �Ҵ�
				free(timeStamp);
			}
			isPageFault = 1;													// Page fault �߻�
			pageFaultCnt++;
		}
		// ��� ���
		printf("| %4d |  %4d    |  %4c      |", time+1, prsArr[time], isPageFault ? 'F' : ' ');
		for(int i=0;i<nPageFrame;i++){
			(pfArr[i] == -1) ? printf("   -", pfArr[i]) : printf("%4d", pfArr[i]);
		}
		printf("\n");
		time++;
	}
	printf("+------------------------------+\n");
	printf("|  # of Page fault :  %4d     |\n", pageFaultCnt);
	printf("+------------------------------+\n\n");

	free(pfArr);
}

/*** LFU algorithm ***/
void lfu(int *prsArr, int nPageFrame){
	printf("+------------------------------+\n");
	printf("|         LFU algorithm        |\n");
	printf("+-------------------------------\n");
	printf("| Time | Ref. str | Page fault |\n");
	printf("+------------------------------+\n");
	
	int time = 0;																// ���� �ð�
	int pageFaultCnt = 0;														// Page fault Ƚ��
	int *pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// ������ ������ �迭
	int *refCntArr = (int*)malloc(sizeof(int)*nPageFrame);						// ���� Ƚ�� ���� �迭
	int *refCheckArr = (int*)malloc(sizeof(int)*nPageFrame);					// ���� Ƚ�� Tie break üũ�� �迭(0/1)
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// ������ ������ �迭 -1�� �ʱ�ȭ
		refCntArr[i] = 0;														// ������ ���� Ƚ�� 0���� �ʱ�ȭ
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			refCheckArr[i] = 0;													// Tie break üũ�� �迭 �ʱ�ȭ
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// �̹� �Ҵ� ���ִ��� Ȯ��
				refCntArr[i]++;													// ���� Ƚ�� ����
			}
		}
		if(!isDupl){															// �ߺ� �Ҵ��� �ƴ� ���
			int isAllocated = 0;												// �� ������ �����ӿ� ������ �Ҵ� ����
			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i]< 0){
					pfArr[i] = prsArr[time];									// �� ������ �����ӿ� ���� ������ �Ҵ�
					refCntArr[i]++;												// ���� Ƚ�� ����
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// �ߺ� �������� �ƴϰ� �� ������ �����ӵ� ���� ���
				int targetCnt = 0;
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(targetCnt > refCntArr[i]){
							targetCnt = refCntArr[i];							// ���� ���� Ƚ�� ���ϱ�
						}
					}
					else{
						targetCnt = refCntArr[i];
					}
				}
				int targetIdx = 0;												// ���� ���� Ƚ�� �� tie break �� lru Ÿ�� �ε���
				int targetDuplCnt = 0;											// Tie break �߻� Ȯ�ο�
				for(int i=0;i<nPageFrame;i++){
					if(refCntArr[i] == targetCnt){
						if(!targetDuplCnt){
							targetIdx = i;										// �����ε��� �켱 ����
						}							
						refCheckArr[i] = 1;										// ���� ���� Ƚ���� ���� ��� �ش� ������ ������ ��ȣ�� ���� üũ �迭�� 1�� ����
						targetDuplCnt++;										// ���� ���� Ƚ�� �ߺ� Ƚ�� ����
					}
				}
				if(targetDuplCnt > 1){											// Tie break �߻� �� LRU
					int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);		// ���� �ð� ���� �迭
					for(int i=targetIdx;i<nPageFrame;i++){
						timeStamp[i] = 0;

						for(int j=time-1;j>=0;j--){
							if(pfArr[i] == prsArr[j]){
								timeStamp[i] = j;								// ���� �ð� �� ���� ����� �ð� ����
								break;
							}
						}
					}					
					for(int i=targetIdx+1;i<nPageFrame;i++){
						if(refCheckArr[i]){										// Tie break�� �߻��� �ε����� ���ؼ��� ����
							if(timeStamp[targetIdx] > timeStamp[i]){			// �� ������ ���������� �˻�
								targetIdx = i;									// �� ������ �������� ���� �ð��� �� ���� ��쿡�� Ÿ������ ����
							}
						}
					}
					free(timeStamp);
				}
				pfArr[targetIdx] = prsArr[time];								// ���� �ð��� ���� ������ ������ �����ӿ� ���� ������ �Ҵ�
				refCntArr[targetIdx] = 1;										// �ٲ� ������ �������� ���� Ƚ�� 1�� �ʱ�ȭ
			}
			isPageFault = 1;													// Page fault �߻�
			pageFaultCnt++;
		}
		// ��� ���
		printf("| %4d |  %4d    |  %4c      |", time+1, prsArr[time], isPageFault ? 'F' : ' ');
		for(int i=0;i<nPageFrame;i++){
			(pfArr[i] == -1) ? printf("   -", pfArr[i]) : printf("%4d", pfArr[i]);
		}
		printf("\n");
		time++;
	}
	printf("+------------------------------+\n");
	printf("|  # of Page fault :  %4d     |\n", pageFaultCnt);
	printf("+------------------------------+\n\n");

	free(pfArr);
	free(refCntArr);
	free(refCheckArr);
}

/*** WS algorithm ***/
void ws(int *prsArr, int nPage, int nWindowSize){
	printf("+----------------------------------------------------------------+\n");
	printf("|                          WS algorithm                          |\n");
	printf("+----------------------------------------------------------------+\n");
	printf("| Time | Ref. str | Page fault |  Pws  |  Qws  | alloc frame cnt |\n");
	printf("+----------------------------------------------------------------+\n");

	int time = 0;																// ���� �ð�
	int pageFaultCnt = 0;														// Page fault Ƚ��
	int *pageArr = (int*)malloc(sizeof(int)*nPage);								// ������ �Ҵ� ���� ���� �迭
	int *pageAllocArr = (int*)malloc(sizeof(int)*nPageRefStr);					// �Ҵ� ���� ������ ������ ���� �迭
	int pageAllocNum = 0;														// �Ҵ� ���� ������ ������ ����
	int totalAllocNum = 0;														// ��ü �Ҵ� ������ ������ ����(��� ����)

	for(int i=0;i<nPage;i++){
		pageArr[i] = 0;															// ������ 1 : �Ҵ�, 0 : ���Ҵ�
	}

	while(time < nPageRefStr){
		int oldPage = 0;														// Window size ���� ������ ��ȣ
		int newPage = prsArr[time];												// �ű� ������ ��ȣ
		int isPageFault = 1;													// Page fault �߻� ����
		int isQws = 1;															// Qws �߻� ����
		
		if(pageArr[newPage]){
			isPageFault = 0;													// ���� ���� �ִ� �������� Page fault �̹߻�
		}
		else{
			pageArr[newPage] = 1;												// ���� ���� ���� �������� �ش� ������ ���� 1�� ��ȯ
		}

		if(time <= nWindowSize){
			isQws = 0;															// �ʱ� Window Size������ Ż���Ǵ� ������ ����
		}
		else{
			oldPage = prsArr[time - nWindowSize - 1];							// Window Size ���� ������ ��ȣ
			for(int i=time-nWindowSize;i<=time;i++){
				if(prsArr[i] == oldPage){
					isQws = 0;													// �ߺ��Ǵ� �������� �ִٸ� Ż���Ǵ� ������ ����
					break;
				}
			}
		}

		if(isPageFault){
			pageFaultCnt++;														// Page fault �߻� �� ī��Ʈ ����
		}
		if(isQws){
			pageArr[oldPage] = 0;												// Qws �߻� �� �ش� ������ �Ҵ�����
		}

		pageAllocNum = 0;	
		for(int i=0;i<nPage;i++){
			if(pageArr[i]){
				pageAllocNum++;													// �Ҵ� ������ ������ ���� ���
			}
		}
		totalAllocNum += pageAllocNum;

		// ��� ���
		printf("| %4d |  %4d    |  %4c      |", time+1, newPage, isPageFault ? 'F' : ' ');
		isPageFault ? printf("%4d   |", newPage) : printf("       |");
		isQws		? printf("%4d   |", oldPage) : printf("       |");
		printf("     %4d        |", pageAllocNum);
		
		for(int i=0;i<nPage;i++){
			if(pageArr[i]){
				printf("%4d", i);
			}
			else{
				printf("   -");
			}
		}
		printf("\n");
		time++;
	}	
	printf("+----------------------------------------------------------------+\n");
	printf("|  %4d page fault with %.2f page frames allocated in average    |\n", pageFaultCnt, (float)totalAllocNum / nPageRefStr);
	printf("+----------------------------------------------------------------+\n\n");

	free(pageArr);
	free(pageAllocArr);
}