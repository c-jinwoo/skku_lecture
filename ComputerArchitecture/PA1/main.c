#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

int main(int argc, char* argv[]){
	FILE *fp;														// file pointer
	int ch;															// for storing byte info from bin
	int inst_idx = 0;												// instruction index
	int byte_idx = 0;												// byte index
	int num[4] = {0};												// for storing numbers per byte

	if((fp = fopen(argv[1], "rb")) == NULL){
		fputs("File Open Error", stderr);
		exit(1);
	}

	while((ch = fgetc(fp)) != EOF){
		num[byte_idx] = ch;											// store bin per byte
		byte_idx++;

		if(byte_idx % 4 == 0){										// for every 4bytes do
			char inst[33] = "";
			for(int i=3;i>=0;i--){
				int mask = 0x80;
				for(int j=0;j<8;j++){
					inst[(3-i)*8+j] = ((num[i] & mask) == mask) ? '1' : '0';
					mask >>= 1;
				}
			}

			// Instruction transform
			char inst_name[30] = "";
			char bit_opcode[8] = "";
			int inst_int = str2unsigned(inst);			
			for(int i=25;i<32;i++){
				bit_opcode[i-25] = inst[i];
			}

			if(!strcmp(bit_opcode, "0110011")){						// add, sub, sll, slt, sltu, xor, srl, sra, or, and

				char bit_funct3[4] = "";
				char bit_funct7[8] = "";
				char bit_rd[6] = "";
				char bit_rs1[6] = "";
				char bit_rs2[6] = "";

				for(int i=0;i<7;i++){
					bit_funct7[i] = inst[i];
				}
				for(int i=7;i<12;i++){
					bit_rs2[i-7] = inst[i];
				}
				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=17;i<20;i++){
					bit_funct3[i-17] = inst[i];
				}
				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}

				if(!strcmp(bit_funct7, "0000000") || !strcmp(bit_funct7, "0100000")){

					if(!strcmp(bit_funct3, "000")){
						strcmp(bit_funct7, "0000000") ? strcpy(inst_name, "sub") : strcpy(inst_name, "add");
					}
					else if(!strcmp(bit_funct3, "001")){			// sll
						strcpy(inst_name, "sll");
					}
					else if(!strcmp(bit_funct3, "010")){			// slt
						strcpy(inst_name, "slt");
					}
					else if(!strcmp(bit_funct3, "011")){			// sltu
						strcpy(inst_name, "sltu");
					}
					else if(!strcmp(bit_funct3, "100")){			// xor
						strcpy(inst_name, "xor");
					}
					else if(!strcmp(bit_funct3, "101")){
						strcmp(bit_funct7, "0000000") ? strcpy(inst_name, "sra") : strcpy(inst_name, "srl");
					}
					else if(!strcmp(bit_funct3, "110")){			// or
						strcpy(inst_name, "or");
					}
					else if(!strcmp(bit_funct3, "111")){			// and
						strcpy(inst_name, "and");
					}

					printf("inst %d: %.8x %s x%d, x%d, x%d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2unsigned(bit_rs1), str2unsigned(bit_rs2));
				}
				else{												// unknown
					strcpy(inst_name, "unknown instruction");
				}
			}
			else if(!strcmp(bit_opcode, "0010011")){				// addi, slli, slti, sltiu, xori, srli, srai, ori, andi

				char bit_funct3[4] = "";
				char bit_rd[6] = "";
				char bit_rs1[6] = "";

				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=17;i<20;i++){
					bit_funct3[i-17] = inst[i];
				}
				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}

				if(!strcmp(bit_funct3, "000") || !strcmp(bit_funct3, "010")	|| !strcmp(bit_funct3, "011")
					|| !strcmp(bit_funct3, "100") || !strcmp(bit_funct3, "110") || !strcmp(bit_funct3, "111")){

					char bit_imm[13] = "";
					for(int i=0;i<12;i++){
						bit_imm[i] = inst[i];
					}

					if(!strcmp(bit_funct3, "000")){					// addi
						strcpy(inst_name, "addi");					
					}
					else if(!strcmp(bit_funct3, "010")){			// slti
						strcpy(inst_name, "slti");
					}
					else if(!strcmp(bit_funct3, "011")){			// sltiu
						strcpy(inst_name, "sltiu");
					}
					else if(!strcmp(bit_funct3, "100")){			// xori
						strcpy(inst_name, "xori");
					}
					else if(!strcmp(bit_funct3, "110")){			// ori
						strcpy(inst_name, "ori");
					}
					else if(!strcmp(bit_funct3, "111")){			// andi
						strcpy(inst_name, "andi");
					}

					printf("inst %d: %.8x %s x%d, x%d, %d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2unsigned(bit_rs1), str2signed(bit_imm));
				}
				else{

					char bit_funct7[8] = "";	
					char bit_shamt[6] = "";	
					
					for(int i=0;i<7;i++){
						bit_funct7[i] = inst[i];
					}
					for(int i=7;i<12;i++){
						bit_shamt[i-7] = inst[i];
					}

					if(!strcmp(bit_funct3, "001")){					// slli
						strcpy(inst_name, "slli");
					}
					else if(!strcmp(bit_funct3, "101")){
						if(!strcmp(bit_funct7, "0000000")){			// srli
							strcpy(inst_name, "srli");
						}
						else if(!strcmp(bit_funct7, "0100000")){	// srai
							strcpy(inst_name, "srai");
						}
					}

					printf("inst %d: %.8x %s x%d, x%d, %d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2unsigned(bit_rs1), str2unsigned(bit_shamt));
				}			
			}
			else if(!strcmp(bit_opcode, "0000011")){				// lb, lh, lw, lbu, lhu

				char bit_funct3[4] = "";
				char bit_rd[6] = "";
				char bit_rs1[6] = "";
				char bit_imm[13] = "";

				for(int i=0;i<12;i++){
					bit_imm[i] = inst[i];
				}
				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=17;i<20;i++){
					bit_funct3[i-17] = inst[i];
				}
				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}

				if(!strcmp(bit_funct3, "000")){						// lb
					strcpy(inst_name, "lb");					
				}
				else if(!strcmp(bit_funct3, "001")){				// lh
					strcpy(inst_name, "lh");
				}
				else if(!strcmp(bit_funct3, "010")){				// lw
					strcpy(inst_name, "lw");
				}
				else if(!strcmp(bit_funct3, "100")){				// lbu
					strcpy(inst_name, "lbu");
				}
				else if(!strcmp(bit_funct3, "101")){				// lhu
					strcpy(inst_name, "lhu");
				}

				printf("inst %d: %.8x %s x%d, %d(x%d)\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2signed(bit_imm), str2unsigned(bit_rs1));
			}
			else if(!strcmp(bit_opcode, "0100011")){				// sb, sh, sw

				char bit_funct3[4] = "";
				char bit_rs1[6] = "";
				char bit_rs2[6] = "";
				char bit_imm[13] = "";

				for(int i=0;i<7;i++){
					bit_imm[i] = inst[i];
				}
				for(int i=7;i<12;i++){
					bit_rs2[i-7] = inst[i];
				}
				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=17;i<20;i++){
					bit_funct3[i-17] = inst[i];
				}
				for(int i=20;i<25;i++){
					bit_imm[i-13] = inst[i];
				}

				if(!strcmp(bit_funct3, "000")){						// sb
					strcpy(inst_name, "sb");					
				}
				else if(!strcmp(bit_funct3, "001")){				// sh
					strcpy(inst_name, "sh");
				}
				else if(!strcmp(bit_funct3, "010")){				// sw
					strcpy(inst_name, "sw");
				}

				printf("inst %d: %.8x %s x%d, %d(x%d)\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rs2), str2signed(bit_imm), str2unsigned(bit_rs1));
			}
			else if(!strcmp(bit_opcode, "1100011")){				// beq, bne, blt, bge, bltu, bgeu

				char bit_funct3[4] = "";
				char bit_rs1[6] = "";
				char bit_rs2[6] = "";
				char bit_imm[14] = "";

				bit_imm[0] = inst[0];
				bit_imm[1] = inst[24];
				bit_imm[12] = '0';

				for(int i=2;i<8;i++){
					bit_imm[i] = inst[i-1];
				}
				for(int i=8;i<12;i++){
					bit_imm[i] = inst[i+12];
				}
				for(int i=7;i<12;i++){
					bit_rs2[i-7] = inst[i];
				}
				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=17;i<20;i++){
					bit_funct3[i-17] = inst[i];
				}

				if(!strcmp(bit_funct3, "000")){						// beq
					strcpy(inst_name, "beq");					
				}
				else if(!strcmp(bit_funct3, "001")){				// bne
					strcpy(inst_name, "bne");
				}
				else if(!strcmp(bit_funct3, "100")){				// blt
					strcpy(inst_name, "blt");
				}
				else if(!strcmp(bit_funct3, "101")){				// bge
					strcpy(inst_name, "bge");
				}
				else if(!strcmp(bit_funct3, "110")){				// bltu
					strcpy(inst_name, "bltu");
				}
				else if(!strcmp(bit_funct3, "111")){				// bgeu
					strcpy(inst_name, "bgeu");
				}

				printf("inst %d: %.8x %s x%d, x%d, %d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rs1), str2unsigned(bit_rs2), str2signed(bit_imm));
			}
			else if(!strcmp(bit_opcode, "1101111")){				// jal
				
				char bit_rd[6] = "";
				char bit_imm[22] = "";
				
				bit_imm[0] = inst[0];
				bit_imm[9] = inst[11];
				bit_imm[20] = '0';

				for(int i=1;i<9;i++){
					bit_imm[i] = inst[i+11];
				}
				for(int i=10;i<20;i++){
					bit_imm[i] = inst[i-9];
				}

				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}

				strcpy(inst_name, "jal");

				printf("inst %d: %.8x %s x%d, %d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2signed(bit_imm));
			}
			else if(!strcmp(bit_opcode, "1100111")){				// jalr
				
				char bit_rd[6] = "";
				char bit_rs1[6] = "";
				char bit_imm[13] = "";
				
				for(int i=0;i<12;i++){
					bit_imm[i] = inst[i];
				}
				for(int i=12;i<17;i++){
					bit_rs1[i-12] = inst[i];
				}
				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}
					
				strcpy(inst_name, "jalr");

				printf("inst %d: %.8x %s x%d, %d(x%d)\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2signed(bit_imm), str2unsigned(bit_rs1));
			}
			else if(!strcmp(bit_opcode, "0110111") || !strcmp(bit_opcode, "0010111")){	// lui, auipc
				
				char bit_rd[6] = "";
				char bit_imm[33] = "";
				
				for(int i=0;i<20;i++){
					bit_imm[i] = inst[i];
				}
				for(int i=20;i<32;i++){
					bit_imm[i] = '0';
				}
				for(int i=20;i<25;i++){
					bit_rd[i-20] = inst[i];
				}

				if(!strcmp(bit_opcode, "0110111")){
					strcpy(inst_name, "lui");
				}
				else if(!strcmp(bit_opcode, "0010111")){
					strcpy(inst_name, "auipc");
				}

				printf("inst %d: %.8x %s x%d, %d\n", inst_idx, inst_int, inst_name, str2unsigned(bit_rd), str2signed(bit_imm));
			}
			byte_idx = 0;
			inst_idx++;
		}
	}
	fclose(fp);

	return 0;
}