/*
 *  Subject : Algorithms
 *  Date    : 2022.10.27
 *  Todo    : Hash Table(Seperate Chaining)
 *  Author  : 2020315798 ChoiJinWoo
 */
  
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define KEY_SIZE 50
#define RAND_SIZE 1000
#define TABLE_1_SIZE 5
#define TABLE_2_SIZE 7
#define TABLE_3_SIZE 13

typedef struct Node{
	int key;
	struct Node *next;
} Node;

Node *hashTable1[TABLE_1_SIZE];
Node *hashTable2[TABLE_2_SIZE];
Node *hashTable3[TABLE_3_SIZE];

void insertChain(int key, Node **hashTable, int type){
	int hash_value = 0;
	
	if(type == 1){
		hash_value = key % TABLE_1_SIZE;
	}
	else if(type == 2){
		hash_value = key % TABLE_2_SIZE;
	}
	else if(type == 3){
		hash_value = key % TABLE_3_SIZE;
	}
	Node *node = (Node*)malloc(sizeof(Node));
	node->key = key;
	node->next = NULL;
	
	if(hashTable[hash_value] == NULL){
		hashTable[hash_value] = node;
	}
	else{
		node->next = hashTable[hash_value];
		hashTable[hash_value] = node;
	}
}

void printChain(Node **hashTable, int type){
	Node *node;
	int length = 0;
	double average = 0;
	int info[3] = {0};										// [0] : Shortest Length, [1] : Longest Length, [2] : Average Length	
	
	if(type == 1){
		length = TABLE_1_SIZE;
	}
	else if(type == 2){
		length = TABLE_2_SIZE;
	}
	else if(type == 3){
		length = TABLE_3_SIZE;
	}
		
	for(int i=0;i<length;i++){
		printf("[%d]->", i);
		
		int count = 0;
		for(node=hashTable[i];node;node=node->next){
			printf("%d->", node->key);
			count++;
		}		
		
		if(i == 0){
			for(int j=0;j<3;j++){
				info[j] = count;
			}
		}
		else{
			if(info[0] > count){
				info[0] = count;
			}
			if(info[1] < count){
				info[1] = count;
			}
			info[2] += count;
		}
		printf("\n");
	}
	
	printf("Shortest length : %d\n", info[0]);
	printf("Longest length : %d\n", info[1]);	
	printf("Average length : %.2lf\n\n", (double)info[2] / length);			
}

int main(void){
	int key[KEY_SIZE] = {0};
	int duplCheck[RAND_SIZE] = {0};
	srand(time(NULL));	
	
	for(int i=0;i<KEY_SIZE;i++){
		while(1){
			int temp = rand()%RAND_SIZE;
			if(!duplCheck[temp]){
				key[i] = temp;
				duplCheck[temp]++;
				break;
			}
		}
	}
		
	for(int i=0;i<KEY_SIZE;i++){
		insertChain(key[i], hashTable1, 1);
		insertChain(key[i], hashTable2, 2);
		insertChain(key[i], hashTable3, 3);
	}
	
	printf("[Table a : h(k) = k mod 5]\n");
	printChain(hashTable1, 1);
	
	printf("[Table b : h(k) = k mod 7]\n");
	printChain(hashTable2, 2);
	
	printf("[Table c : h(k) = k mod 13]\n");
	printChain(hashTable3, 3);
	
	for(int i=0;i<TABLE_1_SIZE;i++){
		free(hashTable1[i]);
	}
	for(int i=0;i<TABLE_2_SIZE;i++){
		free(hashTable2[i]);
	}
	for(int i=0;i<TABLE_3_SIZE;i++){
		free(hashTable3[i]);
	}
	return 0;	
}
