/////////////////////////////////////
//  Generated Initialization File  //
/////////////////////////////////////

#include "compiler_defs.h"
#include "C8051F040_defs.h"
#include "Font_Header.h"
#include <string.h>
sbit CS1 	= P2^0;
sbit CS2 	= P2^1;
sbit RS 	= P2^2;			
sbit RW 	= P2^3;
sbit E  	= P2^4;
sbit RST 	= P2^5;
sbit OW 	= P2^7;

char alphabet[][5] = {
{0x7e, 0x11, 0x11, 0x11, 0x7e} // 41 A
};

void Reset_Sources_Init(){
    WDTCN     = 0xDE;
    WDTCN     = 0xAD;
}

void Timer_Init(){
    SFRPAGE   = TIMER01_PAGE;
    TMOD      = 0x01;
    CKCON     = 0x01;
}

void Port_IO_Init(){
    SFRPAGE   = CONFIG_PAGE;
    P2MDOUT   = 0xFF;
    P3MDOUT   = 0xFF;
    P4MDOUT   = 0xFF;
    P5MDOUT   = 0xFF;
    P6MDOUT   = 0xFF;
    P7MDOUT   = 0xFF;
    XBR2      = 0x40;
}

void Oscillator_Init(){
    SFRPAGE   = CONFIG_PAGE;
    OSCICN    = 0x83;
}

void Init_Device(void){
    Reset_Sources_Init();    
	Timer_Init();
    Port_IO_Init();
	Oscillator_Init();
}

void delay(unsigned int us) {
	unsigned int us_md = 0xFFFF - (unsigned int)(us * 6.125);
    SFRPAGE = TIMER01_PAGE;
	TL0 = us_md & 0xFF;
	TH0 = (us_md >> 8);

    TR0 = 1;
    while (TF0 == 0);
	TF0 = 0;
    TR0 = 0;
}

unsigned char read_bit(void){
	unsigned char i;
	OW = 0; // pull OW low to start timeslot
	OW = 1; // then return high
	for (i=0; i<3; i++);
	return(OW); // return value of OW line
}

void write_bit(char bitval){
	OW = 0; // pull OW low to start timeslot
	if(bitval==1) OW =1; // return OW high if write 1
	delay(104); // hold value for remainder of timeslot
	OW = 1;
}

unsigned char read_byte(void){
	unsigned char i;
	unsigned char value = 0;
	for (i=0;i<8;i++)
	{
		if(read_bit()) value|=0x01<<i; // reads byte in, one byte at a time and then
		// shifts it left
		delay(104); // wait for rest of timeslot
	}
	return(value);
}

void write_byte(char val){
	unsigned char i;
	unsigned char temp;
	for (i=0; i<8; i++) // writes byte, one bit at a time
	{
		temp = val>>i; // shifts val right 'i' spaces
		temp &= 0x01; // copy that bit to temp
		write_bit(temp); // write bit in temp into
	}
	delay(104);
}

void GLCD_CMD(char cmd){
	SFRPAGE = CONFIG_PAGE;
	P4 = cmd;			
	RS = 0;						// RS low
	RW = 0;						// RW low

	E = 1;
	delay(1);
	E = 0;
	delay(1);
}

void GLCD_Data(char dat){
	SFRPAGE = CONFIG_PAGE;
	P4 = dat;	
	RS = 1;						// RS high
	RW = 0;						// RW low
	
	E = 1;
	delay(1);
	E = 0;
	delay(1);
}

void GLCD_Init(){	
	SFRPAGE = CONFIG_PAGE;
	P2 = 0xFF;					// Command Port
	P4 = 0xFF;					// Data Port

	RST = 0;	
	delay(1);	
	RST = 1;	
	delay(1);
		
	CS1 = 1;	CS2 = 1;
	CS1 = 0; 	CS2 = 0;

	RW = 0;						// RS low
	RS = 0;						// R/W low

	GLCD_CMD(0x3E);				// Send display off
	GLCD_CMD(0x40);				// Send Y addr
	GLCD_CMD(0xB8);				// Send X addr
	GLCD_CMD(0xC0);				// Send Z addr
	GLCD_CMD(0x3F);				// Send display on
}

void GLCD_Clear(void){
	int i,j;
	CS1 = 0; 
	CS2 = 0;

	for(i=0;i<8;i++){
		GLCD_CMD((0xB8)+i);

		for(j=0;j<64;j++){
			GLCD_Data(0);
		}
	}
}

void GLCD_Img_String(char *str){
	int column,page,page_add=0xB8,k=0;
	float page_inc=0.5;
	CS1 = 0; CS2 = 1;
	for(page=0;page<16;page++) 
	{
		for(column=0;column<64;column++)
		{
			GLCD_Data(str[column+k]);
		}
		CS1 = ~CS1; CS2 = ~CS2;
		GLCD_CMD((page_add+page_inc));
		page_inc=page_inc+0.5;
		k=k+64;
	}
	GLCD_CMD(0x40);
	GLCD_CMD(0xB8);
}

void GLCD_str(char *str, int line, int side, int offset){
	int i, j;
	 
	if(side){
		CS1 = 1; 		CS2 = 0;	// Use right part
	}
	else{
		CS1 = 0; 		CS2 = 1;	// Use left part
	}

	for(i = 0;i < strlen(str);i++){
		for(j = 0;j < 6; j++){
			GLCD_CMD(0xB8+0+line);	// Set X addr
			delay(1);
			GLCD_CMD(0x40+i*6+j+offset);   // Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(font[str[i] - 32][j]);
		}
	}
}

void main(void){
	int i;
	
	char eng_left[] = {0x38, 0x44, 0x44, 0x44};
	char eng_right[] = {0x38, 0x00, 0x38, 0x44, 0x44, 0x44, 0x38, 0x00};

	char dept_left[] = {0x7c, 0x08, 0x04, 0x04};
	char dept_right[] = {0x78, 0x00};

	char name[] = {0x92, 0x8A, 0xE7, 0x8A, 0x92, 0xFF, 0x00, 0xE9, 0x85, 0x83, 0x85, 0x89, 0xBF, 0x00, 0x26, 0x29, 0xE9, 0x29, 0x26};


	Init_Device();	
	GLCD_Init();

	while(1){
		GLCD_Clear();				// Clear GLCD

		// English Name
		GLCD_str("Choi Jin W", 0, 0, 0);

		CS1 = 0; 		CS2 = 1;	// Use left part
		for(i = 0;i < sizeof(eng_left); i++){
			GLCD_CMD(0xB8);			// Set X addr
			delay(1);
			GLCD_CMD(0x40+60+i);  	// Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(eng_left[i]);
		}	

		CS1 = 1; 		CS2 = 0;	// Use right part
		for(i = 0;i < sizeof(eng_right); i++){
			GLCD_CMD(0xB8);			// Set X addr
			delay(1);
			GLCD_CMD(0x40+i);  		// Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(eng_right[i]);
		}	

		// SID
		GLCD_str("2020315798", 2, 0, 0);

		// Department
		GLCD_str("Software E", 3, 0, 0);

		CS1 = 0; 		CS2 = 1;	// Use left part
		for(i = 0;i < sizeof(dept_left); i++){
			GLCD_CMD(0xB8+3);		// Set X addr
			delay(1);
			GLCD_CMD(0x40+60+i);  	// Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(dept_left[i]);
		}	

		CS1 = 1; 		CS2 = 0;	// Use right part
		for(i = 0;i < sizeof(dept_right); i++){
			GLCD_CMD(0xB8+3);		// Set X addr
			delay(1);
			GLCD_CMD(0x40+i);  		// Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(dept_right[i]);
		}	
		// Department
		GLCD_str("gineering", 3, 1, 2);
							
		// Korean Name
		CS1 = 0; 		CS2 = 1;
		for(i = 0;i < sizeof(name); i++){
			GLCD_CMD(0xB8+1);		// Set X addr
			delay(1);
			GLCD_CMD(0x40+i);  		 // Set Y addr
			delay(1);
			GLCD_CMD(0xC0);			// Set Z addr
			delay(1);
			
			GLCD_Data(name[i]);
		}

		for(i = 0;i < 50000; i++){
			delay(1);
		}
	}
}