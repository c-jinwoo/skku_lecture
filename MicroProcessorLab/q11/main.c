/////////////////////////////////////
//  Generated Initialization File  //
/////////////////////////////////////

#include "compiler_defs.h"
#include "C8051F040_defs.h"

sbit OW = P2^7;

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
    P0MDOUT   = 0xFF;
    P1MDOUT   = 0xFF;
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

void reset(){
    SFRPAGE   = CONFIG_PAGE;
	OW = 0;
	delay(480);
    SFRPAGE   = CONFIG_PAGE;
	OW = 1;
    P2MDOUT   = 0x7F;
	delay(70);
	while(OW);
	delay(450);
    SFRPAGE   = CONFIG_PAGE;
	OW = 1;
    P2MDOUT   = 0xFF;
}

void main(void){
	char temp_byte[9];
	char frac = 0;
    char temp;
	int i;

	Init_Device();

	while(1){
		// Reset
		reset();		

		// Skip ROM command
		write_byte(0xCC);

		// Covert T command
        write_byte(0x44);

		// delay 104us
		delay(104);

		// Reset
		reset();	

		// Skip ROM command
		write_byte(0xCC);

		// Read Scratchpad command
        write_byte(0xBE);

	    // Read 9 bytes of temperature data
		for(i = 0;i < 9;i++){
			temp_byte[i] = read_byte();
		}
				
		// Convert 2 bytes to frac and temperature
		temp = (temp_byte[1] << 7) | (temp_byte[0] >> 1);
		frac = temp_byte[0] & 0x01;
		
		// For negative temperature
		if(temp & 0x8000) {
	        temp = -((temp ^ 0x7FFF) + 1);
	    }

		// Show in LED
    	SFRPAGE = CONFIG_PAGE;
		if(temp / 10 == 0){			// 0
			P0 = 0X3E;
			P1 = 0X41;
			P2 = 0X41;
			P3 = 0X3E;
		}
		else if(temp / 10 == 1){	// 1
			P0 = 0X00;
			P1 = 0X42;
			P2 = 0X7F;
			P3 = 0X40;
		}
		else if(temp / 10 == 2){	// 2
			P0 = 0X62;
			P1 = 0X51;
			P2 = 0X49;
			P3 = 0X46;
		}
		else if(temp / 10 == 3){	// 3
			P0 = 0X41;
			P1 = 0X49;
			P2 = 0X49;
			P3 = 0X36;
		}
		else if(temp / 10 == 4){	// 4
			P0 = 0X0F;
			P1 = 0X08;
			P2 = 0X7F;
			P3 = 0X08;
		}
		else if(temp / 10 == 5){	// 5
			P0 = 0X4F;
			P1 = 0X49;
			P2 = 0X49;
			P3 = 0X79;
		}
		else if(temp / 10 == 6){	// 6
			P0 = 0X7F;
			P1 = 0x49;
			P2 = 0X49;
			P3 = 0X79;
		}
		else if(temp / 10 == 7){	// 7
			P0 = 0X01;
			P1 = 0X01;
			P2 = 0X01;
			P3 = 0X7F;
		}
		else if(temp / 10 == 8){	// 8
			P0 = 0X7F;
			P1 = 0X49;
			P2 = 0X49;
			P3 = 0X7F;
		}
		else if(temp / 10 == 9){	// 9
			P0 = 0X4F;
			P1 = 0X49;
			P2 = 0X49;
			P3 = 0X7F;
		}
		else{
			P0 = 0;
			P1 = 0;
			P2 = 0;
			P3 = 0;
		}
	
		if(temp % 10 == 0){			// 0
			P4 = 0X3E;
			P5 = 0X41;
			P6 = 0X41;
			P7 = 0X3E;
		}
		else if(temp % 10 == 1){	// 1
			P4 = 0X00;
			P5 = 0X42;
			P6 = 0X7F;
			P7 = 0X40;
		}
		else if(temp % 10 == 2){	// 2
			P4 = 0X62;
			P5 = 0X51;
			P6 = 0X49;
			P7 = 0X46;
		}
		else if(temp % 10 == 3){	// 3
			P4 = 0X41;
			P5 = 0X49;
			P6 = 0X49;
			P7 = 0X36;
		}
		else if(temp % 10 == 4){	// 4
			P4 = 0X0F;
			P5 = 0X08;
			P6 = 0X7F;
			P7 = 0X08;
		}
		else if(temp % 10 == 5){	// 5
			P4 = 0X4F;
			P5 = 0X49;
			P6 = 0X49;
			P7 = 0X79;
		}
		else if(temp % 10 == 6){	// 6
			P4 = 0X7F;
			P5 = 0X49;
			P6 = 0X49;
			P7 = 0X79;
		}
		else if(temp % 10 == 7){	// 7
			P4 = 0X01;
			P5 = 0X01;
			P6 = 0X01;
			P7 = 0X7F;
		}
		else if(temp % 10 == 8){	// 8
			P4 = 0X7F;
			P5 = 0X49;
			P6 = 0X49;
			P7 = 0X7F;
		}
		else if(temp % 10 == 9){	// 9
			P4 = 0X4F;
			P5 = 0X49;
			P6 = 0X49;
			P7 = 0X7F;
		}
		else{
			P4 = 0;
			P5 = 0;
			P6 = 0;
			P7 = 0;
		}
		
		// For frac, use MSB from port4 ~ 7 to express .5
		if(frac){
			P4 |= 0x80;
			P5 |= 0x80;
			P6 |= 0x80;
			P7 |= 0x80;
		}
		else{
			P4 |= 0x00;
			P5 |= 0x00;
			P6 |= 0x00;
			P7 |= 0x00;
		}
		
	}
}