/*
 *  Subject : Algorithms
 *  Date    : 2022.09.25
 *  Todo    : Matrix Multiplication
 *  Author  : 2020315798 ChoiJinWoo
 */

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int addCnt;
int subCnt;
int mulCnt;

int* add(int* A, int* B, int size) {
	int* C = (int*)malloc(sizeof(int) * size * size);

	if (C != NULL) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				*(C + i * size + j) = *(A + i * size + j) + *(B + i * size + j);
				addCnt++;
			}
		}
	}
	return C;
}

int* sub(int* A, int* B, int size) {
	int* C = (int*)malloc(sizeof(int) * size * size);

	if (C != NULL) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				*(C + i * size + j) = *(A + i * size + j) - *(B + i * size + j);
				subCnt++;
			}
		}
	}
	return C;
}

int* mul(int* A, int* B, int size) {
	int* C = (int*)malloc(sizeof(int) * size * size);

	if (C != NULL) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int sum = 0;
				for (int k = 0; k < size; k++) {
					sum += *(A + i * size + k) * *(B + k * size + j);
					mulCnt++;
				}
				*(C + i * size + j) = sum;
			}
		}
	}
	return C;
}

int* StandardAlgorithm(int* A, int* B, int size, int op) {
	int* C = (int*)malloc(sizeof(int) * size * size);

	if (C != NULL) {
		if (op == 1) {
			C = add(A, B, size);
		}
		else if (op == 2) {
			C = sub(A, B, size);
		}
		else {
			C = mul(A, B, size);
		}
		return C;
	}
}

int* DCAlgorithm(int* A, int* B, int size, int op) {
	if (size <= 2) {
		if (op == 1) {
			return add(A, B, size);
		}
		else if (op == 2) {
			return sub(A, B, size);
		}
		else {
			return mul(A, B, size);
		}
	}
	else {
		int* A11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C = (int*)malloc(sizeof(int) * size * size);

		for (int i = 0; i < size / 2; i++) {
			for (int j = 0; j < size / 2; j++) {
				*(A11 + i * size / 2 + j) = *(A + i * size + j);
				*(B11 + i * size / 2 + j) = *(B + i * size + j);
			}
			for (int j = size / 2; j < size; j++) {
				*(A12 + i * size / 2 + (j - size / 2)) = *(A + i * size + j);
				*(B12 + i * size / 2 + (j - size / 2)) = *(B + i * size + j);
			}
		}
		for (int i = size / 2; i < size; i++) {
			for (int j = 0; j < size / 2; j++) {
				*(A21 + (i - size / 2) * size / 2 + j) = *(A + i * size + j);
				*(B21 + (i - size / 2) * size / 2 + j) = *(B + i * size + j);
			}
			for (int j = size / 2; j < size; j++) {
				*(A22 + (i - size / 2) * size / 2 + (j - size / 2)) = *(A + i * size + j);
				*(B22 + (i - size / 2) * size / 2 + (j - size / 2)) = *(B + i * size + j);
			}
		}

		if (op == 1 || op == 2) {
			C11 = DCAlgorithm(A11, B11, size / 2, op);
			C12 = DCAlgorithm(A12, B12, size / 2, op);
			C21 = DCAlgorithm(A21, B21, size / 2, op);
			C22 = DCAlgorithm(A22, B22, size / 2, op);

			for (int i = 0; i < size / 2; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C11 + i * size / 2 + j);
				}
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C12 + i * size / 2 + (j - size / 2));
				}
			}
			for (int i = size / 2; i < size; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C21 + (i - size / 2) * size / 2 + j);
				}
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C22 + (i - size / 2) * size / 2 + (j - size / 2));
				}
			}
		}
		else {
			C11 = add(DCAlgorithm(A11, B11, size / 2, op), DCAlgorithm(A12, B21, size / 2, op), size / 2);
			C12 = add(DCAlgorithm(A11, B12, size / 2, op), DCAlgorithm(A12, B22, size / 2, op), size / 2);
			C21 = add(DCAlgorithm(A21, B11, size / 2, op), DCAlgorithm(A22, B21, size / 2, op), size / 2);
			C22 = add(DCAlgorithm(A21, B12, size / 2, op), DCAlgorithm(A22, B22, size / 2, op), size / 2);

			printf("(Partial Matrix : %d * %d)\n", size / 2, size / 2);
			for (int i = 0; i < size / 2; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C11 + i * size / 2 + j);
					printf("%d\t", *(C11 + i * size / 2 + j));
				}
				printf("\t");
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C12 + i * size / 2 + (j - size / 2));
					printf("%d\t", *(C12 + i * size / 2 + (j - size / 2)));
				}
				printf("\n");
			}
			printf("\n");
			for (int i = size / 2; i < size; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C21 + (i - size / 2) * size / 2 + j);
					printf("%d\t", *(C21 + (i - size / 2) * size / 2 + j));
				}
				printf("\t");
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C22 + (i - size / 2) * size / 2 + (j - size / 2));
					printf("%d\t", *(C22 + (i - size / 2) * size / 2 + (j - size / 2)));
				}
				printf("\n");
			}
			printf("\n");
		}
		return C;
	}
}

int* StrassenAlgorithm(int* A, int* B, int size, int op) {
	if (size <= 2) {
		if (op == 1) {
			return add(A, B, size);
		}
		else if (op == 2) {
			return sub(A, B, size);
		}
		else {
			return mul(A, B, size);
		}
	}
	else {
		int* A11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* A22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* B22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C11 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C12 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C21 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C22 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P1 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P2 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P3 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P4 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P5 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P6 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* P7 = (int*)malloc(sizeof(int) * (size / 2) * (size / 2));
		int* C = (int*)malloc(sizeof(int) * size * size);

		for (int i = 0; i < size / 2; i++) {
			for (int j = 0; j < size / 2; j++) {
				*(A11 + i * size / 2 + j) = *(A + i * size + j);
				*(B11 + i * size / 2 + j) = *(B + i * size + j);
			}
			for (int j = size / 2; j < size; j++) {
				*(A12 + i * size / 2 + (j - size / 2)) = *(A + i * size + j);
				*(B12 + i * size / 2 + (j - size / 2)) = *(B + i * size + j);
			}
		}
		for (int i = size / 2; i < size; i++) {
			for (int j = 0; j < size / 2; j++) {
				*(A21 + (i - size / 2) * size / 2 + j) = *(A + i * size + j);
				*(B21 + (i - size / 2) * size / 2 + j) = *(B + i * size + j);
			}
			for (int j = size / 2; j < size; j++) {
				*(A22 + (i - size / 2) * size / 2 + (j - size / 2)) = *(A + i * size + j);
				*(B22 + (i - size / 2) * size / 2 + (j - size / 2)) = *(B + i * size + j);
			}
		}

		if (op == 3) {
			P1 = StrassenAlgorithm(A11, sub(B12, B22, size / 2), size / 2, op);
			P2 = StrassenAlgorithm(add(A11, A12, size / 2), B22, size / 2, op);
			P3 = StrassenAlgorithm(add(A21, A22, size / 2), B11, size / 2, op);
			P4 = StrassenAlgorithm(A22, sub(B21, B11, size / 2), size / 2, op);
			P5 = StrassenAlgorithm(add(A11, A22, size / 2), add(B11, B22, size / 2), size / 2, op);
			P6 = StrassenAlgorithm(sub(A12, A22, size / 2), add(B21, B22, size / 2), size / 2, op);
			P7 = StrassenAlgorithm(sub(A11, A21, size / 2), add(B11, B12, size / 2), size / 2, op);

			C11 = add(P5, add(P6, sub(P4, P2, size / 2), size / 2), size / 2);
			C12 = add(P1, P2, size / 2);
			C21 = add(P3, P4, size / 2);
			C22 = add(P5, sub(P1, add(P3, P7, size /2), size /2 ), size / 2);

			printf("(Partial Matrix : %d * %d)\n", size / 2, size / 2);
			for (int i = 0; i < size / 2; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C11 + i * size / 2 + j);
					printf("%d\t", *(C11 + i * size / 2 + j));
				}
				printf("\t");
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C12 + i * size / 2 + (j - size / 2));
					printf("%d\t", *(C12 + i * size / 2 + (j - size / 2)));
				}
				printf("\n");
			}
			printf("\n");
			for (int i = size / 2; i < size; i++) {
				for (int j = 0; j < size / 2; j++) {
					*(C + i * size + j) = *(C21 + (i - size / 2) * size / 2 + j);
					printf("%d\t", *(C21 + (i - size / 2) * size / 2 + j));
				}
				printf("\t");
				for (int j = size / 2; j < size; j++) {
					*(C + i * size + j) = *(C22 + (i - size / 2) * size / 2 + (j - size / 2));
					printf("%d\t", *(C22 + (i - size / 2) * size / 2 + (j - size / 2)));
				}
				printf("\n");
			}
			printf("\n");
		}
		return C;
	}
}


int main(void) {
	while (1) {
		int type = 1;
		int size = 0;
		printf("Choose a size of the matrix(0:break, 1:4*4 matrix, 2:8*8 matrix): ");
		scanf("%d", &type);

		if (type == 0) {
			break;
		}
		else if (type == 1 || type == 2) {
			srand(time(NULL));
			size = 4 * type;

			int* A = (int*)malloc(sizeof(int) * size * size);
			int* B = (int*)malloc(sizeof(int) * size * size);
			int* C = (int*)malloc(sizeof(int) * size * size);

			if (A != NULL && B != NULL) {
				printf("\n[A]");
				for (int i = 0; i < size; i++) {
					printf("\t");
				}
				printf("[B]\n");
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						*(A + i * size + j) = rand() % 1000;
						printf("%d\t", *(A + i * size + j));
					}
					for (int j = 0; j < size; j++) {
						*(B + i * size + j) = rand() % 1000;
						printf("%d\t", *(B + i * size + j));
					}
					printf("\n");
				}
			}

			if (C != NULL) {
				/*********** Standard Algorithm ***********/
				printf("\n\n[Standard Algorithm]\n");
				addCnt = 0;
				subCnt = 0;
				mulCnt = 0;

				printf("\n(C : Addition)\n");
				C = StandardAlgorithm(A, B, size, 1);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", addCnt);

				printf("\n(C : Subtraction)\n");
				C = StandardAlgorithm(A, B, size, 2);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", subCnt);

				printf("\n(C : Multiplication)\n");
				C = StandardAlgorithm(A, B, size, 3);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", mulCnt);

				/*********** Divide-and-Conquer Algorithm ***********/
				printf("\n\n[Divide-and-Conquer Algorithm]\n");
				addCnt = 0;
				subCnt = 0;
				mulCnt = 0;

				printf("\n(C : Addition)\n");
				C = DCAlgorithm(A, B, size, 1);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", addCnt);

				printf("\n(C : Subtraction)\n");
				C = DCAlgorithm(A, B, size, 2);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", subCnt);

				printf("\n(C : Multiplication)\n");
				C = DCAlgorithm(A, B, size, 3);

				printf("(Result : %d * %d)\n", size, size);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", mulCnt);

				/*********** Strassen Algorithm ***********/
				printf("\n\n[Strassen Algorithm]\n");
				addCnt = 0;
				subCnt = 0;
				mulCnt = 0;

				printf("\n(C : Multiplication)\n");
				C = StrassenAlgorithm(A, B, size, 3);

				printf("(Result : %d * %d)\n", size, size);
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						printf("%d\t", *(C + i * size + j));
					}
					printf("\n");
				}
				printf("-> Number of Computations : %d\n", mulCnt);
			}
		}
		else {
			printf("Select proper option.\n");
		}
		printf("\n");
	}
	return 0;
}
