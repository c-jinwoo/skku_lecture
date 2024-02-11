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
int nPageRefStr = 0;															// 페이지 참조열 길이

int main(int argc, char* argv[]){
	FILE *fp = fopen(argv[1], "r");
	if(fp == NULL){
		printf("Failed to open input file.\n");						
		return -1;
	}

	char str[BUFFER];															// 입력 버퍼
	int lineCnt = 0;															// 입력 라인수 카운트
	int wordCnt = 0;															// 입력 라인 별 단어수 카운트
	int prsCnt = 0;																// 페이지 참조열 길이 확인용
	int nPage = 0;																// 페이지 갯수
	int nPageFrame = 0;															// 페이지 프레임 갯수
	int nWindowSize = 0;														// 윈도우 사이즈
	int* prsArr;																// 페이지 참조열 배열
	srand(time(NULL));
	/*** 입력 데이터 세팅 진행 ***/
	while(!feof(fp)){		
		fgets(str, sizeof(str), fp);
		char* ptr = strtok(str, " ");
		
		while(ptr != NULL){
			if(lineCnt == 0){
				if(wordCnt == 0){
					nPage = atoi(ptr);											// 페이지 갯수 설정
					if(nPage <= 0 || nPage > MAX_PAGE_NUM){
						printf("Invalid input : Page number\n");
						return -1;
					}
				}
				else if(wordCnt == 1){
					nPageFrame = atoi(ptr);										// 페이지 프레임 갯수 설정
					if(nPageFrame <= 0 || nPageFrame > MAX_PAGEFRAME_NUM){
						printf("Invalid input : Page frame number\n");
						return -1;
					}
				}
				else if(wordCnt == 2){
					nWindowSize = atoi(ptr);									// 윈도우 사이즈 설정
					if(nWindowSize < 0 || nWindowSize > MAX_WINDOWSIZE){
						printf("Invalid input : Window Size\n");
						return -1;
					}
				}
				else{
					nPageRefStr = atoi(ptr);									// 페이지 참조열 설정
					if(nPageRefStr < 0 || nPageRefStr > MAX_PAGEREFSTR){
						printf("Invalid input : Page Ref Str number\n");
						return -1;
					}
					prsArr = (int*)malloc(sizeof(int)*nPageRefStr);				// 페이지 참조열 동적할당
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
					prsArr[wordCnt] = atoi(ptr);								// 페이지 참조열에 페이지 번호 할당
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
	
	int time = 0;																// 진행 시간
	int pageFaultCnt = 0;														// Page fault 횟수
	int *pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// 페이지 프레임 배열
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// 페이지 프레임 배열 -1로 초기화
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// 이미 할당 돼있는지 확인
			}
		}
		if(!isDupl){															// 중복 할당이 아닌 경우
			int isAllocated = 0;												// 빈 페이지 프레임에 페이지 할당 여부

			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i] < 0){
					pfArr[i] = prsArr[time];									// 빈 페이지 프레임에 현재 페이지 할당
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// 중복 페이지도 아니고 빈 페이지 프레임도 없는 경우
				int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);			// 예정 시간 저장 배열
				for(int i=0;i<nPageFrame;i++){
					timeStamp[i] = nPageRefStr + 1;								// 향후 사용이 없을 것 고려해서 최대 시간+1로 초기화

					for(int j=time+1;j<nPageRefStr;j++){
						if(pfArr[i] == prsArr[j]){
							timeStamp[i] = j;									// 예정 시간 중 가장 가까운 시간 저장
							break;
						}
					}
				}				
				int targetIdx = 0;												// 예정 시간이 제일 긴 페이지의 인덱스 저장용
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(timeStamp[targetIdx] < timeStamp[i]){
							targetIdx = i;										// 뒤 페이지 프레임의 예정 시간이 더 클 경우에만 타겟으로 설정
						}
					}
				}				
				pfArr[targetIdx] = prsArr[time];								// 예정 시간이 제일 긴 페이지 프레임에 현재 페이지 할당
				free(timeStamp);
			}
			isPageFault = 1;													// Page fault 발생
			pageFaultCnt++;
		}
		// 결과 출력
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
	
	int time = 0;																// 진행 시간
	int pageFaultCnt = 0;														// Page fault 횟수
	int* pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// 페이지 프레임 배열
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// 페이지 프레임 배열 -1로 초기화
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// 이미 할당 돼있는지 확인
			}
		}
		if(!isDupl){															// 중복 할당이 아닌 경우
			int isAllocated = 0;												// 빈 페이지 프레임에 페이지 할당 여부

			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i] < 0){
					pfArr[i] = prsArr[time];									// 빈 페이지 프레임에 현재 페이지 할당
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// 중복 페이지도 아니고 빈 페이지 프레임도 없는 경우
				int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);			// 과거 시간 저장 배열
				for(int i=0;i<nPageFrame;i++){
					timeStamp[i] = 0;

					for(int j=time-1;j>=0;j--){
						if(pfArr[i] == prsArr[j]){
							timeStamp[i] = j;									// 과거 시간 중 가장 가까운 시간 저장
							break;
						}
					}
				}				
				int targetIdx = 0;												// 과거 시간이 가장 오래된 페이지의 인덱스 저장용
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(timeStamp[targetIdx] > timeStamp[i]){
							targetIdx = i;										// 뒤 페이지 프레임의 과거 시간이 더 작은 경우에만 타겟으로 설정
						}
					}
				}				
				pfArr[targetIdx] = prsArr[time];								// 과거 시간이 가장 오래된 페이지 프레임에 현재 페이지 할당
				free(timeStamp);
			}
			isPageFault = 1;													// Page fault 발생
			pageFaultCnt++;
		}
		// 결과 출력
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
	
	int time = 0;																// 진행 시간
	int pageFaultCnt = 0;														// Page fault 횟수
	int *pfArr = (int*)malloc(sizeof(int)*nPageFrame);							// 페이지 프레임 배열
	int *refCntArr = (int*)malloc(sizeof(int)*nPageFrame);						// 참조 횟수 저장 배열
	int *refCheckArr = (int*)malloc(sizeof(int)*nPageFrame);					// 참조 횟수 Tie break 체크용 배열(0/1)
	
	for(int i=0;i<nPageFrame;i++){
		pfArr[i] = -1;															// 페이지 프레임 배열 -1로 초기화
		refCntArr[i] = 0;														// 페이지 참조 횟수 0으로 초기화
	}
	while(time < nPageRefStr){
		int isPageFault = 0;
		int isDupl = 0;

		for(int i=0;i<nPageFrame;i++){
			refCheckArr[i] = 0;													// Tie break 체크용 배열 초기화
			if(pfArr[i] == prsArr[time]){
				isDupl = 1;														// 이미 할당 돼있는지 확인
				refCntArr[i]++;													// 참조 횟수 증가
			}
		}
		if(!isDupl){															// 중복 할당이 아닌 경우
			int isAllocated = 0;												// 빈 페이지 프레임에 페이지 할당 여부
			for(int i=0;i<nPageFrame;i++){
				if(pfArr[i]< 0){
					pfArr[i] = prsArr[time];									// 빈 페이지 프레임에 현재 페이지 할당
					refCntArr[i]++;												// 참조 횟수 증가
					isAllocated = 1;
					break;
				}
			}			
			if(!isAllocated){													// 중복 페이지도 아니고 빈 페이지 프레임도 없는 경우
				int targetCnt = 0;
				for(int i=0;i<nPageFrame;i++){
					if(i > 0){
						if(targetCnt > refCntArr[i]){
							targetCnt = refCntArr[i];							// 최저 참조 횟수 구하기
						}
					}
					else{
						targetCnt = refCntArr[i];
					}
				}
				int targetIdx = 0;												// 최저 참조 횟수 및 tie break 시 lru 타켓 인덱스
				int targetDuplCnt = 0;											// Tie break 발생 확인용
				for(int i=0;i<nPageFrame;i++){
					if(refCntArr[i] == targetCnt){
						if(!targetDuplCnt){
							targetIdx = i;										// 최저인덱스 우선 저장
						}							
						refCheckArr[i] = 1;										// 최저 참조 횟수와 같은 경우 해당 페이지 프레임 번호의 참조 체크 배열값 1로 변경
						targetDuplCnt++;										// 최저 참조 횟수 중복 횟수 증가
					}
				}
				if(targetDuplCnt > 1){											// Tie break 발생 시 LRU
					int *timeStamp = (int*)malloc(sizeof(int)*nPageFrame);		// 과거 시간 저장 배열
					for(int i=targetIdx;i<nPageFrame;i++){
						timeStamp[i] = 0;

						for(int j=time-1;j>=0;j--){
							if(pfArr[i] == prsArr[j]){
								timeStamp[i] = j;								// 과거 시간 중 가장 가까운 시간 저장
								break;
							}
						}
					}					
					for(int i=targetIdx+1;i<nPageFrame;i++){
						if(refCheckArr[i]){										// Tie break가 발생한 인덱스에 한해서만 진행
							if(timeStamp[targetIdx] > timeStamp[i]){			// 더 오래된 페이지인지 검사
								targetIdx = i;									// 뒤 페이지 프레임의 과거 시간이 더 작은 경우에만 타겟으로 설정
							}
						}
					}
					free(timeStamp);
				}
				pfArr[targetIdx] = prsArr[time];								// 과거 시간이 가장 오래된 페이지 프레임에 현재 페이지 할당
				refCntArr[targetIdx] = 1;										// 바뀐 페이지 프레임의 참조 횟수 1로 초기화
			}
			isPageFault = 1;													// Page fault 발생
			pageFaultCnt++;
		}
		// 결과 출력
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

	int time = 0;																// 진행 시간
	int pageFaultCnt = 0;														// Page fault 횟수
	int *pageArr = (int*)malloc(sizeof(int)*nPage);								// 페이지 할당 유무 저장 배열
	int *pageAllocArr = (int*)malloc(sizeof(int)*nPageRefStr);					// 할당 중인 페이지 프레임 저장 배열
	int pageAllocNum = 0;														// 할당 중인 페이지 프레임 갯수
	int totalAllocNum = 0;														// 전체 할당 페이지 프레임 갯수(평균 계산용)

	for(int i=0;i<nPage;i++){
		pageArr[i] = 0;															// 페이지 1 : 할당, 0 : 미할당
	}

	while(time < nPageRefStr){
		int oldPage = 0;														// Window size 직전 페이지 번호
		int newPage = prsArr[time];												// 신규 페이지 번호
		int isPageFault = 1;													// Page fault 발생 유무
		int isQws = 1;															// Qws 발생 유무
		
		if(pageArr[newPage]){
			isPageFault = 0;													// 현재 기준 있는 페이지면 Page fault 미발생
		}
		else{
			pageArr[newPage] = 1;												// 현재 기준 없는 페이지면 해당 페이지 상태 1로 변환
		}

		if(time <= nWindowSize){
			isQws = 0;															// 초기 Window Size까지는 탈락되는 페이지 없음
		}
		else{
			oldPage = prsArr[time - nWindowSize - 1];							// Window Size 직전 페이지 번호
			for(int i=time-nWindowSize;i<=time;i++){
				if(prsArr[i] == oldPage){
					isQws = 0;													// 중복되는 페이지가 있다면 탈락되는 페이지 없음
					break;
				}
			}
		}

		if(isPageFault){
			pageFaultCnt++;														// Page fault 발생 시 카운트 증가
		}
		if(isQws){
			pageArr[oldPage] = 0;												// Qws 발생 시 해당 페이지 할당해제
		}

		pageAllocNum = 0;	
		for(int i=0;i<nPage;i++){
			if(pageArr[i]){
				pageAllocNum++;													// 할당 페이지 프레임 갯수 계산
			}
		}
		totalAllocNum += pageAllocNum;

		// 결과 출력
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