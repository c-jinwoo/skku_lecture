#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define BYTE_SIZE 4
#define REG_SIZE 32
#define DATA_PER_32BIT 16384
#define DATA_SIZE 65536
#define MASK 0x80
#define ALLOC_ADDR 0x10000000
#define INST_MIN_RANGE 0x00000000
#define INST_MAX_RANGE 0x0000FFFF
#define DATA_MIN_RANGE 0x00000000
#define DATA_WORD_MAX_RANGE 0x0000FFFC
#define DATA_HALFWORD_MAX_RANGE 0x0000FFFE
#define DATA_BYTE_MAX_RANGE 0x0000FFFF
#define SPECIAL_DATA_ADDR 0x20000000

int power(int a, int b);
int str2signed(char* str);
int str2unsigned(char* str);

int main(int argc, char* argv[]){
	FILE *fp1, *fp2;											// p1: File pointer
	int inst_idx = 0;											// p1: Instruction index
	int byte_idx = 0;											// p1: Byte index
	int isDataUsed = 0;											// p3: Data usage T/f
	int data_idx = 0;											// p3: Data index	
	int data_offset = 0;										// p3: Data offset	
	char data[DATA_SIZE];										// p3: For storing data per byte
	int reg[REG_SIZE] = {0};									// p2: Register array
	int fp1_num[BYTE_SIZE] = {0};								// p1: For storing numbers per byte
	int fp2_num[BYTE_SIZE] = {0};								// p1: For storing numbers per byte
	int total_inst_idx = 0;										// p2: Total instruction index(input)
	char inst_hist[DATA_PER_32BIT][REG_SIZE+1];					// p3: Record instruction
	unsigned char lbu;

	for(int i=0;i<DATA_PER_32BIT;i++){
		for(int j=0;j<REG_SIZE+1;j++){
			if(j == REG_SIZE){
				inst_hist[i][j] = '\0';
			}
			else{
				inst_hist[i][j] = 0xFF;
			}
		}
	}
	for(int i=0;i<DATA_SIZE;i++){
		data[i] = 0xFF;
	}

	// p3: Argument allocation with an error check
	if(argc < 4){
		if((fp1 = fopen(argv[1], "rb")) == NULL){
			printf("File Open Error\n");
			exit(1);
		}
		else if(argv[2] == NULL){
			printf("Number of instructions empty\n");
			exit(1);
		}
		total_inst_idx = atoi(argv[2]);
	}
	else{
		if((fp1 = fopen(argv[1], "rb")) == NULL){
			printf("File Open Error\n");
			exit(1);
		}
		else if((fp2 = fopen(argv[2], "rb")) == NULL){
			printf("File Open Error\n");
			exit(1);
		}
		else if(argv[3] == NULL){
			printf("Number of instructions empty\n");
			exit(1);
		}
		isDataUsed = 1;
		total_inst_idx = atoi(argv[3]);
	}

	// Inst Fetch
	while(inst_idx < DATA_PER_32BIT){
		fp1_num[byte_idx] = fgetc(fp1);
		byte_idx++;

		if(byte_idx % BYTE_SIZE == 0){							// For every 4 bytes do
			char inst_str[REG_SIZE+1] = "";						// Save every 4 bytes of binary
			for(int i=3;i>=0;i--){
				int mask = MASK;
				for(int j=0;j<8;j++){
					inst_str[(3-i)*8+j] = ((fp1_num[i] & mask) == mask) ? '1' : '0';
					mask >>= 1;
				}
			}
			byte_idx = 0;
			strcpy(inst_hist[inst_idx], inst_str);				// Copy to instruction history array
			inst_idx++;
		}
	}

	// Data Fetch
	if(isDataUsed){
		byte_idx = 0;
		while(data_idx < DATA_PER_32BIT){
			fp2_num[byte_idx] = fgetc(fp2);
			byte_idx++;

			if(byte_idx % BYTE_SIZE == 0){						// For every 4 bytes do
				char data_str[REG_SIZE+1] = "";					// Save every 4 bytes of binary
				for(int i=3;i>=0;i--){
					int mask = MASK;
					for(int j=0;j<8;j++){
						data_str[(3-i)*8+j] = ((fp2_num[i] & mask) == mask) ? '1' : '0';
						mask >>= 1;
					}
					// Save every byte info with offset+i
					data[data_offset+i] = str2unsigned(data_str) & 0xFF;
				}
				data_offset += BYTE_SIZE;						// Increase offset by 4
				byte_idx = 0;
				data_idx++;
			}
		}
	}

	// Proceed actual instruction
	int target_inst_idx = 0;
	int target_address = 0;
	for(int t=0;t<total_inst_idx;t++){
		if(target_address < INST_MIN_RANGE || target_address > INST_MAX_RANGE){
			printf("Memory address error\n");
			break;
		}			

		int isValid = 1;										// Inst 유효성 체크
		char inst_name[10] = "";								// Inst 이름 저장
		char bit_opcode[8] = "";								// Inst Opcode 저장
		for(int i=25;i<32;i++){
			bit_opcode[i-25] = inst_hist[target_inst_idx][i];
		}

		if(!strcmp(bit_opcode, "0110011")){						// add, sub, sll, slt, sltu, xor, srl, sra, or, and
			char bit_funct3[4] = "";
			char bit_funct7[8] = "";
			char bit_rd[6] = "";
			char bit_rs1[6] = "";
			char bit_rs2[6] = "";

			for(int i=0;i<7;i++){
				bit_funct7[i] = inst_hist[target_inst_idx][i];
			}
			for(int i=7;i<12;i++){
				bit_rs2[i-7] = inst_hist[target_inst_idx][i];
			}
			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=17;i<20;i++){
				bit_funct3[i-17] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}

			if(!strcmp(bit_funct7, "0000000") || !strcmp(bit_funct7, "0100000")){
				if(!strcmp(bit_funct3, "000")){
					if(strcmp(bit_funct7, "0000000")){			// sub
						strcpy(inst_name, "sub");

						if(str2unsigned(bit_rd)){
							reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] - reg[str2unsigned(bit_rs2)];
						}
					}
					else{										// add
						strcpy(inst_name, "add");

						if(str2unsigned(bit_rd)){
							reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] + reg[str2unsigned(bit_rs2)];
						}
					}
				}
				else if(!strcmp(bit_funct3, "001")){			// sll
					strcpy(inst_name, "sll");

					if(str2unsigned(bit_rd)){
						int low5bits = reg[str2unsigned(bit_rs2)] & 0x1f;
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] << low5bits;
					}
				}
				else if(!strcmp(bit_funct3, "010")){			// slt
					strcpy(inst_name, "slt");
					
					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] < reg[str2unsigned(bit_rs2)];
					}
				}
				else if(!strcmp(bit_funct3, "011")){			// sltu
					strcpy(inst_name, "sltu");

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = (unsigned)reg[str2unsigned(bit_rs1)] < (unsigned)reg[str2unsigned(bit_rs2)];
					}
				}
				else if(!strcmp(bit_funct3, "100")){			// xor
					strcpy(inst_name, "xor");

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] ^ reg[str2unsigned(bit_rs2)];
					}
				}
				else if(!strcmp(bit_funct3, "101")){
					if(strcmp(bit_funct7, "0000000")){			// sra
						strcpy(inst_name, "sra");

						if(str2unsigned(bit_rd)){
							int low5bits = reg[str2unsigned(bit_rs2)] & 0x1f;
							
							if(reg[str2unsigned(bit_rs1)] < 0 && low5bits > 0){
								reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] >> low5bits | ~(~0U >> low5bits);
							}
							else{
								reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] >> low5bits;
							}
						}
					}
					else{										// srl
						strcpy(inst_name, "srl");

						if(str2unsigned(bit_rd)){
							int low5bits = reg[str2unsigned(bit_rs2)] & 0x1f;

							reg[str2unsigned(bit_rd)] = (unsigned)reg[str2unsigned(bit_rs1)] >> low5bits;
						}
					}
				}
				else if(!strcmp(bit_funct3, "110")){			// or
					strcpy(inst_name, "or");
					
					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] | reg[str2unsigned(bit_rs2)];
					}
				}
				else if(!strcmp(bit_funct3, "111")){			// and
					strcpy(inst_name, "and");

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] & reg[str2unsigned(bit_rs2)];
					}
				}
				else{
					isValid = 0;
				}
			}
			else{												// unknown : mul, div, rem
				isValid = 0;
			}
			target_inst_idx++;
		}
		else if(!strcmp(bit_opcode, "0010011")){				// addi, slli, slti, sltiu, xori, srli, srai, ori, andi
			char bit_funct3[4] = "";
			char bit_rd[6] = "";
			char bit_rs1[6] = "";

			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=17;i<20;i++){
				bit_funct3[i-17] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}

			if(!strcmp(bit_funct3, "000") || !strcmp(bit_funct3, "010")	|| !strcmp(bit_funct3, "011")
				|| !strcmp(bit_funct3, "100") || !strcmp(bit_funct3, "110") || !strcmp(bit_funct3, "111")){
				char bit_imm[13] = "";
				for(int i=0;i<12;i++){
					bit_imm[i] = inst_hist[target_inst_idx][i];
				}

				if(!strcmp(bit_funct3, "000")){					// addi
					strcpy(inst_name, "addi");	
					
					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] + str2signed(bit_imm);
					}
				}
				else if(!strcmp(bit_funct3, "010")){			// slti
					strcpy(inst_name, "slti");

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] < str2signed(bit_imm);
					}
				}
				else if(!strcmp(bit_funct3, "011")){			// sltiu
					strcpy(inst_name, "sltiu");

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = (unsigned)reg[str2unsigned(bit_rs1)] < (unsigned)str2signed(bit_imm);
					}
				}
				else if(!strcmp(bit_funct3, "100")){			// xori
					strcpy(inst_name, "xori");	

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] ^ str2signed(bit_imm);
					}
				}
				else if(!strcmp(bit_funct3, "110")){			// ori
					strcpy(inst_name, "ori");
								
					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] | str2signed(bit_imm);
					}
				}
				else if(!strcmp(bit_funct3, "111")){			// andi
					strcpy(inst_name, "andi");		

					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] & str2signed(bit_imm);
					}
				}
				else{
					isValid = 0;
				}
			}
			else{
				char bit_funct7[8] = "";	
				char bit_shamt[6] = "";	
				
				for(int i=0;i<7;i++){
					bit_funct7[i] = inst_hist[target_inst_idx][i];
				}
				for(int i=7;i<12;i++){
					bit_shamt[i-7] = inst_hist[target_inst_idx][i];
				}

				if(!strcmp(bit_funct3, "001")){					// slli
					strcpy(inst_name, "slli");
					if(str2unsigned(bit_rd)){
						reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] << str2unsigned(bit_shamt);
					}
				}
				else if(!strcmp(bit_funct3, "101")){
					if(!strcmp(bit_funct7, "0000000")){			// srli
						strcpy(inst_name, "srli");

						if(str2unsigned(bit_rd)){
							reg[str2unsigned(bit_rd)] = (unsigned)reg[str2unsigned(bit_rs1)] >> str2unsigned(bit_shamt);
						}
					}
					else if(!strcmp(bit_funct7, "0100000")){	// srai
						strcpy(inst_name, "srai");

						if(str2unsigned(bit_rd)){								
							if(reg[str2unsigned(bit_rs1)] < 0 && str2unsigned(bit_shamt) > 0){
								reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] >> str2unsigned(bit_shamt) | ~(~0U >> str2unsigned(bit_shamt));
							}
							else{
								reg[str2unsigned(bit_rd)] = reg[str2unsigned(bit_rs1)] >> str2unsigned(bit_shamt);
							}
						}
					}
					else{
						isValid = 0;
					}
				}
				else{
					isValid = 0;
				}
			}	
			target_inst_idx++;
		}
		else if(!strcmp(bit_opcode, "0000011")){				// lb, lh, lw, lbu, lhu
			char bit_funct3[4] = "";
			char bit_rd[6] = "";
			char bit_rs1[6] = "";
			char bit_imm[13] = "";

			for(int i=0;i<12;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i];
			}
			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=17;i<20;i++){
				bit_funct3[i-17] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}

			int relative_addr = str2signed(bit_imm) + reg[str2unsigned(bit_rs1)];
			int relative_idx = relative_addr - ALLOC_ADDR;
			if(relative_idx < DATA_MIN_RANGE){
				printf("Memory address error\n");
				target_inst_idx++;
				target_address = BYTE_SIZE*target_inst_idx;	
				break;
			}

			if(!strcmp(bit_funct3, "000")){						// lb
				strcpy(inst_name, "lb");

				if(str2unsigned(bit_rd)){
					if(relative_idx > DATA_BYTE_MAX_RANGE){
						printf("Memory address error\n");
						target_inst_idx++;
						target_address = BYTE_SIZE*target_inst_idx;	
						break;
					}
					reg[str2unsigned(bit_rd)] = 0;
					reg[str2unsigned(bit_rd)] = (data[relative_idx] & 0x8) ? 0xFFFFFF00 : 0;		// In case of sign extension
					reg[str2unsigned(bit_rd)] |= data[relative_idx] & 0x000000FF;
				}
			}
			else if(!strcmp(bit_funct3, "001")){				// lh
				strcpy(inst_name, "lh");

				if(str2unsigned(bit_rd)){
					if(relative_idx > DATA_HALFWORD_MAX_RANGE){
						printf("Memory address error\n");
						target_inst_idx++;
						target_address = BYTE_SIZE*target_inst_idx;	
						break;
					}
					reg[str2unsigned(bit_rd)] = 0;
					reg[str2unsigned(bit_rd)] = (data[relative_idx] & 0x80) ? 0xFFFF0000 : 0;		// In case of sign extension
					for(int i=0;i<2;i++){
						reg[str2unsigned(bit_rd)] |= (data[relative_idx+i] & 0x000000FF) << (i * 8);
					}
				}
			}
			else if(!strcmp(bit_funct3, "010")){				// lw
				strcpy(inst_name, "lw");

				if(str2unsigned(bit_rd)){
					if(relative_idx > DATA_WORD_MAX_RANGE){
						printf("Memory address error\n");
						target_inst_idx++;
						target_address = BYTE_SIZE*target_inst_idx;	
						break;
					}
					reg[str2unsigned(bit_rd)] = 0;
					for(int i=0;i<BYTE_SIZE;i++){
						reg[str2unsigned(bit_rd)] |= (data[relative_idx+i] & 0x000000FF) << (i * 8);
					}
				}
			}
			else if(!strcmp(bit_funct3, "100")){				// lbu
				strcpy(inst_name, "lbu");
				
				if(str2unsigned(bit_rd)){
					if(relative_addr == SPECIAL_DATA_ADDR){		// p3: Additional functionality for lbu
						scanf("%hhu", &lbu);
						reg[str2unsigned(bit_rd)] = lbu;
					}
					else{
						if(relative_idx > DATA_BYTE_MAX_RANGE){
							printf("Memory address error\n");
							target_inst_idx++;
							target_address = BYTE_SIZE*target_inst_idx;	
							break;
						}		
						reg[str2unsigned(bit_rd)] = 0;
						reg[str2unsigned(bit_rd)] |= data[relative_idx] & 0x000000FF;
					}
				}
			}
			else if(!strcmp(bit_funct3, "101")){				// lhu
				strcpy(inst_name, "lhu");

				if(str2unsigned(bit_rd)){
					if(relative_idx > DATA_HALFWORD_MAX_RANGE){
						printf("Memory address error\n");
						target_inst_idx++;
						target_address = BYTE_SIZE*target_inst_idx;	
						break;
					}
					reg[str2unsigned(bit_rd)] = 0;
					for(int i=0;i<2;i++){
						reg[str2unsigned(bit_rd)] |= (data[relative_idx+i] & 0x000000FF) << (i * 8);
					}
				}
			}
			else{
				isValid = 0;
			}
			target_inst_idx++;
		}
		else if(!strcmp(bit_opcode, "0100011")){				// sb, sh, sw
			char bit_funct3[4] = "";
			char bit_rs1[6] = "";
			char bit_rs2[6] = "";
			char bit_imm[13] = "";

			for(int i=0;i<7;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i];
			}
			for(int i=7;i<12;i++){
				bit_rs2[i-7] = inst_hist[target_inst_idx][i];
			}
			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=17;i<20;i++){
				bit_funct3[i-17] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<25;i++){
				bit_imm[i-13] = inst_hist[target_inst_idx][i];
			}

			int relative_addr = str2signed(bit_imm) + reg[str2unsigned(bit_rs1)];
			int relative_idx = relative_addr - ALLOC_ADDR;
			if(relative_idx < 0){
				printf("Memory address error\n");
				target_inst_idx++;
				target_address = BYTE_SIZE*target_inst_idx;	
				break;
			}

			if(!strcmp(bit_funct3, "000")){						// sb
				strcpy(inst_name, "sb");

				if(relative_addr == SPECIAL_DATA_ADDR){			// p3: Additional functionality for sb
					char sbu = reg[str2unsigned(bit_rs2)] & 0x000000FF;
					printf("%c", sbu);		
				}
				else{
					if(relative_idx > DATA_BYTE_MAX_RANGE){
						printf("Memory address error\n");
						target_inst_idx++;
						target_address = BYTE_SIZE*target_inst_idx;	
						break;
					}	
					data[relative_idx] = reg[str2unsigned(bit_rs2)] & 0x000000FF;
				}
			}
			else if(!strcmp(bit_funct3, "001")){				// sh
				strcpy(inst_name, "sh");

				if(relative_idx > DATA_HALFWORD_MAX_RANGE){
					printf("Memory address error\n");
					target_inst_idx++;
					target_address = BYTE_SIZE*target_inst_idx;	
					break;
				}		
				data[relative_idx] = reg[str2unsigned(bit_rs2)] & 0x000000FF;
				data[relative_idx+1] = ((reg[str2unsigned(bit_rs2)] & 0x0000FF00) >> 8) & 0x000000FF;
			}
			else if(!strcmp(bit_funct3, "010")){				// sw
				strcpy(inst_name, "sw");

				if(relative_idx > DATA_WORD_MAX_RANGE){
					printf("Memory address error\n");
					target_inst_idx++;
					target_address = BYTE_SIZE*target_inst_idx;	
					break;
				}
				data[relative_idx]	 = reg[str2unsigned(bit_rs2)] & 0x000000FF;
				data[relative_idx+1] = ((reg[str2unsigned(bit_rs2)] & 0x0000FF00) >> 8) & 0x000000FF;
				data[relative_idx+2] = ((reg[str2unsigned(bit_rs2)] & 0x00FF0000) >> 16) & 0x000000FF;
				data[relative_idx+3] = ((reg[str2unsigned(bit_rs2)] & 0xFF000000) >> 24) & 0x000000FF;
			}
			else{
				isValid = 0;
			}
			target_inst_idx++;
		}
		else if(!strcmp(bit_opcode, "1100011")){				// beq, bne, blt, bge, bltu, bgeu
			char bit_funct3[4] = "";
			char bit_rs1[6] = "";
			char bit_rs2[6] = "";
			char bit_imm[14] = "";
			int isCondition = 0;

			bit_imm[0] = inst_hist[target_inst_idx][0];
			bit_imm[1] = inst_hist[target_inst_idx][24];
			bit_imm[12] = '0';

			for(int i=2;i<8;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i-1];
			}
			for(int i=8;i<12;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i+12];
			}
			for(int i=7;i<12;i++){
				bit_rs2[i-7] = inst_hist[target_inst_idx][i];
			}
			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=17;i<20;i++){
				bit_funct3[i-17] = inst_hist[target_inst_idx][i];
			}

			if(!strcmp(bit_funct3, "000")){						// beq
				strcpy(inst_name, "beq");
				isCondition = (reg[str2unsigned(bit_rs1)] == reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else if(!strcmp(bit_funct3, "001")){				// bne
				strcpy(inst_name, "bne");
				isCondition = (reg[str2unsigned(bit_rs1)] != reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else if(!strcmp(bit_funct3, "100")){				// blt
				strcpy(inst_name, "blt");
				isCondition = (reg[str2unsigned(bit_rs1)] < reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else if(!strcmp(bit_funct3, "101")){				// bge
				strcpy(inst_name, "bge");
				isCondition = (reg[str2unsigned(bit_rs1)] >= reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else if(!strcmp(bit_funct3, "110")){				// bltu
				strcpy(inst_name, "bltu");
				isCondition = ((unsigned)reg[str2unsigned(bit_rs1)] < (unsigned)reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else if(!strcmp(bit_funct3, "111")){				// bgeu
				strcpy(inst_name, "bgeu");
				isCondition = ((unsigned)reg[str2unsigned(bit_rs1)] >= (unsigned)reg[str2unsigned(bit_rs2)]) ? 1 : 0;
			}
			else{
				isValid = 0;
			}
			if(isValid){
				target_inst_idx += (isCondition ? (str2signed(bit_imm) / BYTE_SIZE) : 1);
			}
			else{
				target_inst_idx++;
			}
		}
		else if(!strcmp(bit_opcode, "1101111")){				// jal				
			char bit_rd[6] = "";
			char bit_imm[22] = "";			
			bit_imm[0] = inst_hist[target_inst_idx][0];
			bit_imm[9] = inst_hist[target_inst_idx][11];
			bit_imm[20] = '0';

			for(int i=1;i<9;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i+11];
			}
			for(int i=10;i<20;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i-9];
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}
			strcpy(inst_name, "jal");

			int target_inst_idx_copy = target_inst_idx;
			target_inst_idx += (str2signed(bit_imm) / BYTE_SIZE);				// 이동 할 주소
			if(str2unsigned(bit_rd)){
				reg[str2unsigned(bit_rd)] = BYTE_SIZE*(target_inst_idx_copy+1);		// rd가 x0아닐 경우 PC+1 저장
			}
		}
		else if(!strcmp(bit_opcode, "1100111")){				// jalr				
			char bit_rd[6] = "";
			char bit_rs1[6] = "";
			char bit_imm[13] = "";
			
			for(int i=0;i<12;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i];
			}
			for(int i=12;i<17;i++){
				bit_rs1[i-12] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}				
			strcpy(inst_name, "jalr");

			int target_inst_idx_copy = target_inst_idx;
			target_inst_idx = (str2signed(bit_imm) + reg[str2unsigned(bit_rs1)]) / BYTE_SIZE;
			if(str2unsigned(bit_rd)){
				reg[str2unsigned(bit_rd)] = BYTE_SIZE*(target_inst_idx_copy+1);		// rd가 x0아닐 경우 PC+1 저장
			}
		}
		else if(!strcmp(bit_opcode, "0110111") || !strcmp(bit_opcode, "0010111")){	// lui, auipc				
			char bit_rd[6] = "";
			char bit_imm[33] = "";
			
			for(int i=0;i<20;i++){
				bit_imm[i] = inst_hist[target_inst_idx][i];
			}
			for(int i=20;i<32;i++){
				bit_imm[i] = '0';
			}
			for(int i=20;i<25;i++){
				bit_rd[i-20] = inst_hist[target_inst_idx][i];
			}

			if(!strcmp(bit_opcode, "0110111")){					// lui
				strcpy(inst_name, "lui");
									
				if(str2unsigned(bit_rd)){
					reg[str2unsigned(bit_rd)] = str2signed(bit_imm);
				}
			}
			else{												// auipc
				strcpy(inst_name, "auipc");

				if(str2unsigned(bit_rd)){
					reg[str2unsigned(bit_rd)] = BYTE_SIZE*target_inst_idx + str2signed(bit_imm);
				}
			}							
			target_inst_idx++;
		}
		else{
			isValid = 0;
			target_inst_idx++;
		}

		target_address = BYTE_SIZE*target_inst_idx;				// 다음 실행 주소 저장

		if(!isValid){
			printf("unknown instruction\n");					// unknown inst일 경우 출력 후 Loop종료
			break;
		}
	}
	for(int i=0;i<REG_SIZE;i++){								// p2 : print current reg values
		printf("x%d: 0x%.8x\n", i, reg[i]);
	}	
	printf("PC: 0x%.8x\n", target_address);						// p3 : print current PC value

	fclose(fp1);
	if(isDataUsed){
		fclose(fp2);
	}	
	return 0;
}
int power(int a, int b){
	switch(b){
		case 0:
			return 1;
			break;
		case 1:
			return a;
			break;
		default:
			return a * power(a, b-1);
			break;
	}
}
int str2signed(char* str){
	int len = strlen(str);
	int result = 0;
	for(int i=0;i<len;i++){
		int coeff = (str[i] == '1') ? 1 : 0;
		int digit = coeff * power(2, len-i-1);
		
		result += ((i == 0) ? -1*digit : digit);
	}
	return result;
}
int str2unsigned(char* str){
	int num = 0;
	for(int i=0;i<strlen(str);i++){
		num = (num << 1) + str[i] - '0';
	}
	return num;
}