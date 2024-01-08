#include "compiler_defs.h"
#include "C8051F040_defs.h"

void Reset_Sources_Init(){
    WDTCN     = 0xDE;
    WDTCN     = 0xAD;
}

void Port_IO_Init(){

    SFRPAGE   = CONFIG_PAGE;
    P4MDOUT   = 0xFF;
    P5MDOUT   = 0xFF;
    P6MDOUT   = 0xFF;
    P7MDOUT   = 0xFF;
    XBR2      = 0x40;
}
void Init_Device(void){
    Reset_Sources_Init();
    Port_IO_Init();
}
void main(void)
{
	int I0, I1;
	Init_Device();
	while(1)
	{
		XBR2 = 0x00; 		// change port to input mode
		I0 = P0; 			// read port and save to I0 variable
		I1 = P1; 			// read port and save to I0 variable
		XBR2 = 0x40; 		// convert port to output mode again
	
		if(I0==1)			// 0
		{
			P4 = 0X7E;
			P5 = 0X81;
			P6 = 0X81;
			P7 = 0X7E;
		}
		else if(I0==2)		// 1
		{
			P4 = 0X00;
			P5 = 0X82;
			P6 = 0XFF;
			P7 = 0X80;
		}
		else if(I0==4)		// 2
		{
			P4 = 0XC2;
			P5 = 0XA1;
			P6 = 0X91;
			P7 = 0X8E;
		}
		else if(I0==8)		// 3
		{
			P4 = 0X81;
			P5 = 0X89;
			P6 = 0X99;
			P7 = 0X66;
		}
		else if(I0==16)		// 4
		{
			P4 = 0X0F;
			P5 = 0X08;
			P6 = 0XFF;
			P7 = 0X08;
		}
		else if(I0==32)		// 5
		{
			P4 = 0X9F;
			P5 = 0X91;
			P6 = 0X91;
			P7 = 0XF1;
		}
		else if(I0==64)		// 6
		{
			P4 = 0XFF;
			P5 = 0X91;
			P6 = 0X91;
			P7 = 0XF1;
		}
		else if(I0==128)	// 7
		{
			P4 = 0X01;
			P5 = 0X01;
			P6 = 0X01;
			P7 = 0XFF;
		}
		else if(I1==1)		// 8
		{
			P4 = 0XFF;
			P5 = 0X89;
			P6 = 0X89;
			P7 = 0XFF;
		}
		else if(I1==2)		// 9
		{
			P4 = 0X8F;
			P5 = 0X89;
			P6 = 0X89;
			P7 = 0XFF;
		}
		else
		{
			P4 = 0;
			P5 = 0;
			P6 = 0;
			P7 = 0;
		}
	}
}