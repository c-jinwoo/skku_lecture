/*
 *  Subject : Algorithms
 *  Date    : 2022.10.27
 *  Todo    : Red Black Tree
 *  Author  : 2020315798 ChoiJinWoo
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#define SIZE 10
#define RAND_SIZE 50
#define MAX_SIZE 20
#define TABLE_SIZE 15
#define INSERT_SIZE 5
#define DELETE_SIZE 4

enum COLOR { Red, Black };
int inorderArr[MAX_SIZE] = { 0 };
int inorderIdx = 0;

typedef struct Node {
	int key;
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

Node* createNode(int key) {
	Node* node = (Node*)malloc(sizeof(Node));
	node->left = NULL;
	node->right = NULL;
	node->p = NULL;
	node->color = Red;
	node->key = key;
	node->depth = 0;
	node->active = 1;

	return node;
}

RBT* createRBT() {
	Node* nil = (Node*)malloc(sizeof(Node));
	RBT* rbt = (RBT*)malloc(sizeof(RBT));
	nil->left = NULL;
	nil->right = NULL;
	nil->p = NULL;
	nil->color = Black;
	nil->key = 0;
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
		if (z->key < x->key) {
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
	else if (z->key < y->key) {
		y->left = z;
	}
	else {
		y->right = z;
	}
	z->left = T->nil;
	z->right = T->nil;
	z->color = Red;

	RB_INSERT_FIXUP(T, z);
}

Node* TREE_SUCCESSOR(RBT* T, Node* z) {
	while (z->left != T->nil) {
		z = z->left;
	}
	return z;
}

void nodeChange(RBT* T, Node* from, Node* to) {
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
	Node* x, * y = z;
	enum COLOR y_color = z->color;
	z->active = 0;

	if (z->left == T->nil) {
		x = z->right;
		nodeChange(T, z, z->right);
	}
	else if (z->right == T->nil) {
		x = z->left;
		nodeChange(T, z, z->left);
	}
	else {
		y = TREE_SUCCESSOR(T, z->right);
		y_color = y->color;
		x = y->right;
		if (y->p == z) {
			x->p = z;
		}
		else {
			nodeChange(T, y, y->right);
			y->right = z->right;
			y->right->p = y;
		}
		nodeChange(T, z, y);
		y->left = z->left;
		y->left->p = y;
		y->color = z->color;
	}
	if (y_color == Black) {
		RB_DELETE_FIXUP(T, x);
	}
}

void inorder(Node* node) {
	if (node->key != NULL) {
		inorder(node->left);
		inorderArr[inorderIdx++] = node->key;
		inorder(node->right);
	}
}

void NEAREST_NEIGHBOR(RBT* T, int val) {
	for (int i = 0; i < MAX_SIZE; i++) {
		inorderArr[i] = 0;
	}
	inorderIdx = 0;
	inorder(T->root);

	int idx = 0;
	int result = 0;

	for (int i = 0; i < inorderIdx; i++) {
		if (inorderArr[i] >= val) {
			idx = i;
			break;
		}
	}
	if (idx == 0) {
		result = inorderArr[idx];
	}
	else {
		int gapWithSmall = val - inorderArr[idx - 1];
		int gapWithBig = inorderArr[idx] - val;

		result = (gapWithBig > gapWithSmall) ? inorderArr[idx - 1] : inorderArr[idx];

	}

	printf(">>> Nearest Neighbor of %d\n%d\n", val, result);
}

const char* printColor(enum COLOR color) {
	if (color == Red) {
		return "Red";
	}
	else {
		return "Black";
	}
}

void bfs(Node* root, int size) {
	int currIdx = 0;
	int lastIdx = 0;
	int depth = -1;
	int depthNodeCnt = 0;

	Node** queue = (Node**)malloc(sizeof(Node*) * size);

	if (root->active) {
		root->depth = 0;
		queue[lastIdx++] = root;

		while (currIdx != lastIdx) {
			Node* currentNode = queue[currIdx];

			if (depth != currentNode->depth) {
				depth = currentNode->depth;
				(depth == 0) ? printf("Depth %d : ", depth) : printf("\nDepth %d : ", depth);
			}
			if (currentNode->active) {
				if (currentNode == root) {
					printf("%d(%s, root) ", currentNode->key, printColor(currentNode->color));
				}
				else {
					if (currentNode == currentNode->p->left) {
						printf("%d(%s, left of %d) ", currentNode->key, printColor(currentNode->color), currentNode->p->key);
					}
					else{
						printf("%d(%s, right of %d) ", currentNode->key, printColor(currentNode->color), currentNode->p->key);
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
	free(queue);
	printf("\n\n");
}

int main(void) {
	int A[TABLE_SIZE] = { 0 };
	int duplCheck[RAND_SIZE] = { 0 };
	RBT* T = createRBT();
	Node* nodeList[MAX_SIZE];
	srand(time(NULL));

	// Question 2 : Random Insertion
	printf("\n===================== Question 2 : Random Insertion ===================\n");
	for (int i = 0; i < TABLE_SIZE; i++) {
		while (1) {
			int temp = rand() % RAND_SIZE;
			if (!duplCheck[temp]) {
				A[i] = temp;
				duplCheck[temp]++;
				break;
			}
		}
	}
	printf(">>> After inserting A[15]\n");
	for (int i = 0; i < TABLE_SIZE; i++) {
		nodeList[i] = createNode(A[i]);
		RB_INSERT(T, nodeList[i]);
	}
	bfs(T->root, TABLE_SIZE);

	// Question 3 : Designated Insertion
	printf("\n=================== Question 3 : Designated Insertion =================\n");
	int A_insert[INSERT_SIZE] = { 13, 22, 21, 11, 45 };
	int duplCnt = 0;
	for (int i = 0; i < INSERT_SIZE; i++) {
		if (duplCheck[A_insert[i]]) {
			duplCnt++;
		}
	}

	int newIdx = 0;
	int newSize = MAX_SIZE - duplCnt;
	int* newA = (int*)malloc(sizeof(int) * newSize);
	for (int i = 0; i < TABLE_SIZE; i++) {
		newA[i] = A[i];
	}
	for (int i = 0; i < INSERT_SIZE; i++) {
		if (!duplCheck[A_insert[i]]) {
			newA[TABLE_SIZE + newIdx] = A_insert[i];

			printf(">>> Insert %d \n", A_insert[i]);
			nodeList[TABLE_SIZE + newIdx] = createNode(newA[TABLE_SIZE + newIdx]);
			RB_INSERT(T, nodeList[TABLE_SIZE + newIdx]);
			bfs(T->root, TABLE_SIZE + newIdx + 1);
			newIdx++;
		}
		else {
			printf(">>> %d is already in the tree\n\n", A_insert[i]);
		}
	}

	// Question 4 : Nearest Neighbor
	printf("\n==================== Question 4 : Nearest Neighbor ====================\n");
	int nearArr[3] = { 5, 23, 38 };
	for (int i = 0; i < 3; i++) {
		NEAREST_NEIGHBOR(T, nearArr[i]);
	}
	printf("\n");

	// Question 5 : Deletion
	printf("\n========================= Question 5 : Deletion =======================\n");
	int deleteNum = 0;
	int deleteArr[DELETE_SIZE] = { 13, 22, 45, 11 };
	for (int i = 0; i < DELETE_SIZE; i++) {
		for (int j = 0; j < MAX_SIZE; j++) {
			if (nodeList[j] != NULL) {
				if (deleteArr[i] == nodeList[j]->key) {
					printf(">>> Delete %d \n", deleteArr[i]);
					RB_DELETE(T, nodeList[j]);
					deleteNum++;
					bfs(T->root, newSize - deleteNum);
					break;
				}
			}
		}

		if (deleteNum == DELETE_SIZE) {
			break;
		}
	}
	printf(">>> Delete Root \n");
	RB_DELETE(T, T->root);
	deleteNum++;
	bfs(T->root, newSize - deleteNum);

	free(newA);
	return 0;
}
