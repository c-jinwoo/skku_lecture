/*
 *  Subject : Algorithms
 *  Date    : 2022.11.25
 *  Todo    : Airline reservation system
 *  Author  : 2020315798 ChoiJinWoo
 */

#pragma warning(disable : 4996)
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#define ASCII_FIRST 97
#define ASCII_LAST 122
#define CITY_CNT 26
#define CITY_PATH_CNT 10
#define MAX_NODE_SIZE 20
#define REZ_NAME_SIZE 100
#define PATH_STR_LEN 100 

enum COLOR { Red, Black };
int inorderArr[MAX_NODE_SIZE] = { 0 };
int inorderIdx = 0;

typedef struct City {
	char name;
	struct City* next;
} City;

typedef struct Node {
	char name[REZ_NAME_SIZE];
	char path[PATH_STR_LEN];
	char src;
	char dest;
	int id;
	int depth;
	int active;
	struct Node* left;
	struct Node* right;
	struct Node* p;
	enum COLOR color;
} Node;

typedef struct RBT {
	Node* root;
	Node* nil;
} RBT;

Node* createNode(int id, char* name, char src, char dest, char* path) {
	Node* node = (Node*)malloc(sizeof(Node));
	strcpy(node->name, name);
	strcpy(node->path, path);
	node->src = src;
	node->dest = dest;

	node->id = id;
	node->depth = 0;
	node->active = 1;
	node->left = NULL;
	node->right = NULL;
	node->p = NULL;
	node->color = Red;

	return node;
}
RBT* createRBT() {
	Node* nil = (Node*)malloc(sizeof(Node));
	RBT* rbt = (RBT*)malloc(sizeof(RBT));
	nil->left = NULL;
	nil->right = NULL;
	nil->p = NULL;
	nil->color = Black;
	nil->id = 0;
	nil->active = 0;
	rbt->nil = nil;
	rbt->root = rbt->nil;

	return rbt;
}
void leftRotate(RBT* T, Node* x) {
	Node* y = x->right;
	x->right = y->left;

	if (y->left != T->nil) {
		y->left->p = x;
	}
	y->p = x->p;

	if (x->p == T->nil) {
		T->root = y;
	}
	else if (x == x->p->left) {
		x->p->left = y;
	}
	else {
		x->p->right = y;
	}

	y->left = x;
	x->p = y;
}
void rightRotate(RBT* T, Node* x) {
	Node* y = x->left;
	x->left = y->right;

	if (y->right != T->nil) {
		y->right->p = x;
	}
	y->p = x->p;

	if (x->p == T->nil) {
		T->root = y;
	}
	else if (x == x->p->right) {
		x->p->right = y;
	}
	else {
		x->p->left = y;
	}
	y->right = x;
	x->p = y;
}
void RB_INSERT_FIXUP(RBT* T, Node* z) {
	while (z->p->color == Red) {
		if (z->p == z->p->p->left) {
			Node* y = z->p->p->right;

			if (y->color == Red) {
				y->color = Black;
				z->p->color = Black;
				z->p->p->color = Red;
				z = z->p->p;
			}
			else {
				if (z == z->p->right) {
					z = z->p;
					leftRotate(T, z);
				}
				z->p->color = Black;
				z->p->p->color = Red;
				rightRotate(T, z->p->p);
			}
		}
		else {
			Node* y = z->p->p->left;

			if (y->color == Red) {
				y->color = Black;
				z->p->color = Black;
				z->p->p->color = Red;
				z = z->p->p;
			}
			else {
				if (z == z->p->left) {
					z = z->p;
					rightRotate(T, z);
				}
				z->p->color = Black;
				z->p->p->color = Red;
				leftRotate(T, z->p->p);
			}
		}
	}
	T->root->color = Black;
}
void RB_INSERT(RBT* T, Node* z) {
	Node* y = T->nil;
	Node* x = T->root;

	while (x != T->nil) {
		y = x;
		if (z->id < x->id) {
			x = x->left;
		}
		else {
			x = x->right;
		}
	}
	z->p = y;

	if (y == T->nil) {
		T->root = z;
	}
	else if (z->id < y->id) {
		y->left = z;
	}
	else {
		y->right = z;
	}
	z->left = T->nil;
	z->right = T->nil;

	RB_INSERT_FIXUP(T, z);
}
Node* TREE_SUCCESSOR(RBT* T, Node* z) {
	while (z->left != T->nil) {
		z = z->left;
	}
	return z;
}
void RB_TRANSPLANT(RBT* T, Node* from, Node* to) {
	if (from->p == T->nil) {
		T->root = to;
	}
	else if (from == from->p->left) {
		from->p->left = to;
	}
	else {
		from->p->right = to;
	}
	to->p = from->p;
}
void RB_DELETE_FIXUP(RBT* T, Node* x) {
	while (x != T->root && x->color == Black) {
		if (x == x->p->left) {
			Node* w = x->p->right;

			if (w->color == Red) {
				w->color = Black;
				x->p->color = Red;
				leftRotate(T, x->p);
				w = x->p->right;
			}
			if (w->left->color == Black && w->right->color == Black) {
				w->color = Red;
				x = x->p;
			}
			else {
				if (w->right->color == Black) {
					w->left->color = Black;
					w->color = Red;
					rightRotate(T, w);
					w = x->p->right;
				}
				w->color = x->p->color;
				x->p->color = Black;
				w->right->color = Black;
				leftRotate(T, x->p);
				x = T->root;
			}
		}
		else {
			Node* w = x->p->left;

			if (w->color == Red) {
				w->color = Black;
				x->p->color = Red;
				rightRotate(T, x->p);
				w = x->p->left;
			}
			if (w->right->color == Black && w->left->color == Black) {
				w->color = Red;
				x = x->p;
			}
			else {
				if (w->left->color == Black) {
					w->right->color = Black;
					w->color = Red;
					leftRotate(T, w);
					w = x->p->left;
				}
				w->color = x->p->color;
				x->p->color = Black;
				w->left->color = Black;
				rightRotate(T, x->p);
				x = T->root;
			}
		}
	}
	x->color = Black;
}
void RB_DELETE(RBT* T, Node* z) {
	Node *x;
	Node *y = z;
	enum COLOR y_color = y->color;
	z->active = 0;

	if (z->left == T->nil) {
		x = z->right;
		RB_TRANSPLANT(T, z, z->right);
	}
	else if (z->right == T->nil) {
		x = z->left;
		RB_TRANSPLANT(T, z, z->left);
	}
	else {
		y = TREE_SUCCESSOR(T, z->right);
		y_color = y->color;
		x = y->right;
		if (y->p == z) {
			x->p = y;
		}
		else {
			RB_TRANSPLANT(T, y, y->right);
			y->right = z->right;
			y->right->p = y;
		}
		RB_TRANSPLANT(T, z, y);
		y->left = z->left;
		y->left->p = y;
		y->color = z->color;
	}
	if (y_color == Black) {
		RB_DELETE_FIXUP(T, x);
	}
}

const char* printColor(enum COLOR color) {
	if (color == Red) {
		return "Red";
	}
	else {
		return "Black";
	}
}
void RB_PRINT(Node* root, int size) {
	int currIdx = 0;
	int lastIdx = 0;
	int depth = -1;
	int depthNodeCnt = 0;

	printf("\n[Current Reservation List]\n");

	Node** queue = (Node**)malloc(sizeof(Node*) * size);

	if (root->active) {
		root->depth = 0;
		queue[lastIdx++] = root;

		while (currIdx != lastIdx) {
			Node* currentNode = queue[currIdx];
			if (currentNode->active) {
				if (currentNode == root) {
					printf("Depth %d >> No:%d, Name:%s, Departure:%c, Arrival:%c, Path:%s (%s, root)\n", currentNode->depth, currentNode->id, currentNode->name, currentNode->src, currentNode->dest, currentNode->path, printColor(currentNode->color));
				}
				else {
					if (currentNode == currentNode->p->left) {
						printf("Depth %d >> No:%d, Name:%s, Departure:%c, Arrival:%c, Path:%s (%s, left of %d)\n", currentNode->depth, currentNode->id, currentNode->name, currentNode->src, currentNode->dest, currentNode->path, printColor(currentNode->color), currentNode->p->id);
					}
					else {
						printf("Depth %d >> No:%d, Name:%s, Departure:%c, Arrival:%c, Path:%s (%s, right of %d)\n", currentNode->depth, currentNode->id, currentNode->name, currentNode->src, currentNode->dest, currentNode->path, printColor(currentNode->color), currentNode->p->id);
					}
				}
				if (currentNode->left->active) {
					currentNode->left->depth = currentNode->depth + 1;
					queue[lastIdx++] = currentNode->left;
				}
				if (currentNode->right->active) {
					currentNode->right->depth = currentNode->depth + 1;
					queue[lastIdx++] = currentNode->right;
				}
				currIdx++;
			}
			else {
				printf("Error : Current node not active\n");
				break;
			}
		}
	}
	printf("\n");
	free(queue);
}

void shuffleTimeTable(int* arr, int num) {
	int temp;
	int random;
	for (int i = 0; i < num - 1; i++) {
		random = rand() % (num - i) + i;
		temp = arr[i];
		arr[i] = arr[random];
		arr[random] = temp;
	}
}

int main() {
	srand(time(NULL));
	int duplCheck[CITY_CNT][CITY_CNT] = { 0 };						// 인접 도시 중복 여부 체크
	int pathCnt[CITY_CNT] = { 0 };									// 인접 도시 전체 갯수 저장용
	int* cityList2Int[CITY_CNT];									// 각 도시별 운항 리스트(숫자)
	City* cityList2Char[CITY_CNT];									// 각 도시별 운항 리스트(리스트)
	int directPathCnt = 0;											// 인접 도시 중 각 단계에서 생성한 도시 갯수 저장용

	/*** 각 도시별 이동 가능 루트 생성 ***/
	for (int i = 0; i < CITY_CNT; i++) {
		duplCheck[i][i] = i + 1;										// 자기자신 미리 중복처리
		directPathCnt = 0;

		while (directPathCnt < CITY_PATH_CNT) {						// i번째에서 직항의 갯수가 10이하면 반복
			int dest_city_idx = rand() % CITY_CNT;					// 랜덤으로 생성
			if (!duplCheck[i][dest_city_idx]) {
				int isPathable = 0;									// 해당 도시에 경로 생성 가능여부체크
				if (i < dest_city_idx) {
					if (pathCnt[dest_city_idx] < CITY_PATH_CNT) {
						isPathable = 1;
					}
				}
				else {
					isPathable = 1;
				}
				if (isPathable) {
					duplCheck[i][dest_city_idx] = dest_city_idx + 1;	// 양방향 중복처리
					duplCheck[dest_city_idx][i] = i + 1;				// 양방향 중복처리
					directPathCnt++;								// 각 단계별 인접 도시 갯수 추가
					pathCnt[dest_city_idx]++;						// 인접 도시 갯수에 추가
				}
			}
		}
	}

	/*** 각 도시별 이동 가능 루트 중 이동하는 도시에 대해 리스트 생성 ***/
	for (int i = 0; i < CITY_CNT; i++) {
		pathCnt[i] += CITY_PATH_CNT;								// 각 도시별 전체 운항 도시 수 저장		
		cityList2Int[i] = (int*)malloc(sizeof(int) * pathCnt[i]);		// 각 도시별 운항 도시 수만큼 동적할당

		int tempIdx = 0;
		for (int j = 0; j < CITY_CNT; j++) {
			if (i != j) {
				if (duplCheck[i][j]) {
					cityList2Int[i][tempIdx] = duplCheck[i][j];		// 0이 아닌 원소(이동 가능 경로) 복사
					tempIdx++;
				}
			}
		}
		shuffleTimeTable(cityList2Int[i], pathCnt[i]);				// 정렬된 배열을 임의로 섞어 시간표 생성

		cityList2Char[i] = (City*)malloc(sizeof(City));				// 인접 리스트 생성
		cityList2Char[i]->name = ASCII_FIRST + i;					// 아스키코드로 도시 이름 삽입
		cityList2Char[i]->next = NULL;
		City* src_city = cityList2Char[i];
		for (int j = 0; j < pathCnt[i]; j++) {
			City* dest_city = (City*)malloc(sizeof(City));
			dest_city->name = cityList2Int[i][j] + ASCII_FIRST - 1;
			src_city->next = dest_city;
			src_city = src_city->next;
		}
	}
	printf("\n");
	printf("+==============================================================================================================================+\n");
	printf("|                                                 Airline Reservation System                                                   |\n");
	printf("+==============================================================================================================================+\n");
	printf("|                                                  Today's Flight Time Table                                                   |\n");
	printf("+------------------------------------------------------------------------------------------------------------------------------+\n");
	printf("|D|0900|0905|0910|0915|0920|0925|0930|0935|0940|0945|0950|0955|1000|1005|1010|1015|1020|1025|1030|1035|1040|1045|1050|1055|1100|\n");
	// 인접도시 출력(리스트)
	for (int i = 0; i < CITY_CNT; i++) {
		for (int j = 0; j < pathCnt[i]; j++) {
			if (j == 0) {
				printf("|%c|", i+ASCII_FIRST);
			}
			printf("%4c|", cityList2Int[i][j] - 1 + ASCII_FIRST);
		}
		printf("\n");
	}
	printf("+------------------------------------------------------------------------------------------------------------------------------+\n");

	/*** 사용자 입력 ***/
	RBT* T = createRBT();
	Node* nodeList[MAX_NODE_SIZE];
	int job_type = 0;												// 1:삽입,2:삭제,3:종료
	int rez_cnt = 0;												// 예약 번호

	while (1) {
		printf("+==============================================================================================================================+\n");
		printf("Please enter the number of task (1: Insertion, 2: Deletion, 3: Quit) >> ");
		scanf("%d", &job_type);

		if (job_type == 1) {
			char rez_name[REZ_NAME_SIZE];									// 예약 이름
			char rez_src;													// 출발 도시
			char rez_dest;													// 도착 도시
			printf("Please enter the reservation name, departure city, arrival city >> ");
			scanf("%s %c %c", rez_name, &rez_src, &rez_dest);				// 사용자 입력

			if (rez_src < ASCII_FIRST || rez_src > ASCII_LAST || rez_dest < ASCII_FIRST || rez_dest > ASCII_LAST) {
				printf("Unavailable City Name\n\n");						// 예외 처리
				continue;
			}
			if (rez_src == rez_dest) {
				printf("Departure and Arrival should be different.\n\n");	// 예외 처리
				continue;
			}

			int current_src_idx = rez_src - ASCII_FIRST;					// 출발 도시 인덱스
			int isPathExist = 0;											// 경로 존재 여부
			char rez_path[PATH_STR_LEN];									// 경로 문자열

			// 1차 검색 : 직항 검색
			for (int i = 0; i < pathCnt[current_src_idx]; i++) {
				if (cityList2Int[current_src_idx][i] == (rez_dest - ASCII_FIRST + 1)) {
					int t_h = 9 + i / 12;
					int t_m = (i % 12) * 5;

					if (t_h < 12) {
						if (t_h + 1 < 12) {
							sprintf(rez_path, "%c(%02d:%02dam)-%c(%02d:%02dam)", rez_src, t_h, t_m, rez_dest, t_h + 1, t_m);
						}
						else {
							sprintf(rez_path, "%c(%02d:%02dam)-%c(%02d:%02dpm)", rez_src, t_h, t_m, rez_dest, t_h + 1, t_m);
						}
					}
					else {
						sprintf(rez_path, "%c(%02d:%02dpm)-%c(%02d:%02dpm)", rez_src, t_h, t_m, rez_dest, t_h + 1, t_m);
					}
					isPathExist = 1;
					break;
				}
			}
			// 2차 검색 : 경유 검색
			if (!isPathExist) {
				for (int i = 0; i < pathCnt[current_src_idx]; i++) {
					int mid_src_idx = cityList2Int[current_src_idx][i] - 1;

					for (int j = i + 12; j < pathCnt[mid_src_idx]; j++) {
						if (cityList2Int[mid_src_idx][j] == (rez_dest - ASCII_FIRST + 1)) {
							int t_src_h = 9 + i / 12;
							int t_src_m = (i % 12) * 5;
							int t_mid_h = 9 + j / 12;
							int t_mid_m = (j % 12) * 5;

							if (t_mid_h < 12) {
								if (t_mid_h + 1 < 12) {
									sprintf(rez_path, "%c(%02d:%02dam)-%c(%02d:%02dam)-%c(%02d:%02dam)", rez_src, t_src_h, t_src_m, mid_src_idx + ASCII_FIRST, t_mid_h, t_mid_m, rez_dest, t_mid_h + 1, t_mid_m);
								}
								else {
									sprintf(rez_path, "%c(%02d:%02dam)-%c(%02d:%02dam)-%c(%02d:%02dpm)", rez_src, t_src_h, t_src_m, mid_src_idx + ASCII_FIRST, t_mid_h, t_mid_m, rez_dest, t_mid_h + 1, t_mid_m);
								}
							}
							else {
								sprintf(rez_path, "%c(%02d:%02dam)-%c(%02d:%02dpm)-%c(%02d:%02dpm)", rez_src, t_src_h, t_src_m, mid_src_idx + ASCII_FIRST, t_mid_h, t_mid_m, rez_dest, t_mid_h + 1, t_mid_m);
							}
							isPathExist = 1;
							break;
						}
					}

					if (isPathExist) {
						break;
					}
				}
			}

			if (isPathExist) {
				rez_cnt++;											// 예약 번호 증가 및 노드 생성
				nodeList[rez_cnt - 1] = createNode(rez_cnt, rez_name, rez_src, rez_dest, rez_path);
				RB_INSERT(T, nodeList[rez_cnt - 1]);					// RB트리 삽입
			}
			else {
				printf("\n>> There is no flight available from %c to %c today\n", rez_src, rez_dest);
			}
			RB_PRINT(T->root, MAX_NODE_SIZE);
		}
		else if (job_type == 2) {
			int rez_id = 0;
			printf("Please enter the reservation id >> ");
			scanf("%d", &rez_id);

			if (nodeList[rez_id - 1] == NULL) {
				printf("\n>> There is no reservation id for %d\n", rez_id);
			}
			else {
				if (nodeList[rez_id - 1]->active) {
					RB_DELETE(T, nodeList[rez_id - 1]);
					printf("\n>> Successfully deleted reservation id %d\n", rez_id);
				}
				else {
					printf("\n>> There is no reservation id for %d\n", rez_id);
				}
			}
			RB_PRINT(T->root, MAX_NODE_SIZE);
		}
		else if (job_type == 3) {
			printf("\n");
			return 0;
		}
		else {
			printf("Unavailable option. Please choose again.\n\n");
		}
	}

	for (int i = 0; i < CITY_CNT; i++) {
		free(cityList2Int[i]);
		free(cityList2Char[i]);
	}
	return 0;
}
