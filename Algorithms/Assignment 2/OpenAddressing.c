/*
 *  Subject : Algorithms
 *  Date    : 2022.10.27
 *  Todo    : Hash Table(Open Addressing)
 *  Author  : 2020315798 ChoiJinWoo
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define KEY_SIZE 30
#define RAND_SIZE 1000
#define TABLE_SIZE 37

typedef struct Node {
	int key;
	int index;
} Node;
	
Node* linearProbing[TABLE_SIZE];
Node* quadraticProbing[TABLE_SIZE];
Node* doubleHashing[TABLE_SIZE];
int primaryCluster[3] = { 0 };		// [0] : Linear Probing, [1] : Quadratic Probing, [2] : Double Hashing	

void insertChain(Node** hashTable, int type, int key, int index) {
	int hash_value = 0;
	int i = 0;
	int primaryClusterTemp = 0;

	while (1) {
		if (type == 1) {
			hash_value = ((key % TABLE_SIZE) + i) % TABLE_SIZE;
		}
		else if (type == 2) {
			hash_value = ((key % TABLE_SIZE) + i + 3 * (i ^ 2)) % TABLE_SIZE;
		}
		else if (type == 3) {
			hash_value = ((key % TABLE_SIZE) + i * (1 + (key % (TABLE_SIZE - 1)))) % TABLE_SIZE;
		}

		if (hashTable[hash_value] == NULL) {
			Node* node = (Node*)malloc(sizeof(Node));
			node->key = key;
			node->index = index + 1;
			hashTable[hash_value] = node;
			primaryClusterTemp = 0;

			break;
		}
		else {
			i = (i + 1) % TABLE_SIZE;
			primaryClusterTemp++;			
			if (primaryCluster[type - 1] < primaryClusterTemp) {
				primaryCluster[type - 1] = primaryClusterTemp;
			}
		}
	}
}

void printChain(Node** hashTable, int type) {
	for (int i = 0; i < TABLE_SIZE; i++) {
		if (hashTable[i] == NULL) {
			printf("[%d] -> \n", i);
		}
		else {
			printf("[%d] -> %d\t[Insertion number : %d, Avg number of probes :%.2lf]\n", i, hashTable[i]->key, hashTable[i]->index, 1 / (1 - (hashTable[i]->index / 30.0)));
		}
	}
	printf("Largest Primary Cluster Length : %d\n\n", primaryCluster[type - 1]);
}

int main(void) {
	int key[KEY_SIZE] = { 0 };
	int duplCheck[RAND_SIZE] = { 0 };
	srand(time(NULL));
			
	for (int i = 0; i < KEY_SIZE; i++) {
		while (1) {
			int temp = rand() % RAND_SIZE;
			if (!duplCheck[temp]) {
				key[i] = temp;
				duplCheck[temp]++;
				break;
			}
		}
	}
	for (int i = 0; i < KEY_SIZE; i++) {
		insertChain(linearProbing, 1, key[i], i);
		insertChain(quadraticProbing, 2, key[i], i);
		insertChain(doubleHashing, 3, key[i], i);
	}

	printf("[Table a : Linear Probing]\n");
	printChain(linearProbing, 1);

	printf("[Table b : Quadratic Probing]\n");
	printChain(quadraticProbing, 2);

	printf("[Table c : Double Hashing]\n");
	printChain(doubleHashing, 3);

	for (int i = 0; i < TABLE_SIZE; i++) {
		free(linearProbing[i]);
		free(quadraticProbing[i]);
		free(doubleHashing[i]);
	}
	return 0;
}

