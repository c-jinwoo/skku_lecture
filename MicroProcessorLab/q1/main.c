#include <c8051f040.h>

sbit LED1 = P1^6;
sbit SW1 = P3^7;
sbit INPUT1 = P4^0;
sbit OUTPUT1 = P4^1;

void OSCILLATOR_Init(){
	char SFRPAGE_SAVE = SFRPAGE;
	SFRPAGE = CONFIG_PAGE;
	OSCICN |= 0x03;
	SFRPAGE = SFRPAGE_SAVE;
}

void PORT_Init(){
	char SFRPAGE_SAVE = SFRPAGE;
	SFRPAGE = CONFIG_PAGE;

	P1MDIN |= 0x40;
	P1MDOUT = 0x40;			// P1^6 Push & Pull

	P3MDOUT = 0x00;
	P3 |= 0x80;

	P4MDOUT = 0x02;
	P4 = 0x01;

	XBR2 = 0x40;
	SFRPAGE = SFRPAGE_SAVE;
}

void main(void){
	WDTCN = 0xde;
	WDTCN = 0xad;
	PORT_Init();
	OSCILLATOR_Init();

	while(1){
		if(SW1 == 0){
			LED1 = 1;
		}
		else{
			LED1 = 0;
		}

		SFRPAGE = CONFIG_PAGE;
	
		if(INPUT1 == 0){
			OUTPUT1 = 1;
		}
		else{
			OUTPUT1 = 0;
		}
	}
}

