#include "si_toolchain.h"
#include "compiler_defs.h"
#include "C8051F040_defs.h"
#include <string.h>

void Reset_Sources_Init(){		// Disable Watchdog Timer
    WDTCN     = 0xDE;
    WDTCN     = 0xAD;
}

void Timer_Init(){				// Set Timer 1 to 8-bit auto-reload mode with SYSCLK / 1 
    SFRPAGE   = TIMER01_PAGE;
    TMOD      = 0x20;
    CKCON     = 0x10;
    TH1       = 0x60;			// Calculated with Baud Rate of 4800
	TR1		  = 1;
}

void UART_Init(){				// Mode 1, 8-bit UART, enable receiver
    SFRPAGE   = UART0_PAGE;
    SCON0     = 0x50;			    
}

void Oscillator_Init(){			// Oscillator 24.5 MHz
    SFRPAGE   = CONFIG_PAGE;
    OSCICN    = 0x83;
}

void Port_IO_Init(){			// P0.0, P0.1  -  TX0 (UART0), RX0 (UART0) Open-Drain, Digital
    SFRPAGE   = CONFIG_PAGE;
	P0MDOUT	  = 0x01;
    XBR0      = 0x04;
    XBR2      = 0x40;
}

void Init_Device(){
	Reset_Sources_Init();
	Timer_Init();
	UART_Init();
	Port_IO_Init();
	Oscillator_Init();
}

void TransmitChar(unsigned char ch){	
	SFRPAGE = UART0_PAGE;
	SBUF0 = ch;    				// Transmit to buffer
	while (!TI0);				// Wait until the transmission
	TI0 = 0;					// Clear
}

unsigned char RecieveChar(void){
	unsigned char receivedData;

    SFRPAGE = UART0_PAGE;  
	while (!RI0);				// Wait until the transmission
    receivedData = SBUF0;		// Receive from buffer
	RI0 = 0;					// Clear

    return receivedData;
}

void main(void) {
    unsigned char name[] = "2020315798 Choi Jin Woo";
	unsigned char data_recv;
    int count = 0, i = 0;

    Init_Device();

	/* 
	 *	Print out my name and student ID as output 1 time.
	 *  After that, receive the data from the computer,
	 *  and send it back by adding 1 to each byte.
	 */
	while(1){
		if(count == 0){
			for(i = 0; i < strlen(name); i++){
				TransmitChar(name[i]);
			}
			TransmitChar('\n');			// New line for Question 2.
			count++;					// To exit the branch
		}
		else{
			data_recv = RecieveChar();	// Receive char input
			TransmitChar(data_recv+1);	// Return with adding 1
			TransmitChar('\n');			// New line for every input
		}
    }
}



