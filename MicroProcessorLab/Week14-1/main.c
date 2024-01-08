#include "si_toolchain.h"
#include "compiler_defs.h"
#include <c8051f040.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#define SYSCLK       24500000          // Internal oscillator frequency in Hz
#define SAMPLE_RATE_DAC 100000L        // DAC sampling rate in Hz
#define PHASE_PRECISION 65536          // range of phase accumulator
#define FREQUENCY 1000                 // Frequency of output waveform in Hz

sfr16 RCAP4    = 0xCA;                 // Timer4 capture/reload
sfr16 TMR4     = 0xCC;                 // Timer4
sfr16 DAC0     = 0xD2;                 // DAC0 data
sfr16 DAC1     = 0xD2;                 // DAC1 data

double floatValue = 0.0;

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

void DAC_Init(){
    SFRPAGE   = DAC0_PAGE;
    DAC0CN    = 0x94;
    SFRPAGE   = DAC1_PAGE;
    DAC1CN    = 0x94;
}

void Voltage_Reference_Init(){
    SFRPAGE   = ADC0_PAGE;
    REF0CN    = 0x03;
}

void TIMER4_Init (int counts){
	SFRPAGE = TMR4_PAGE;   
	TMR4CN = 0x00;                      // Stop Timer4; Clear overflow flag (TF4); Set to Auto-Reload Mode
	TMR4CF = 0x08;                      // Configure Timer4 to increment; Timer4 counts SYSCLKs

	RCAP4 = -counts;                    // Set reload value
	TMR4 = RCAP4;                       // Initialzie Timer4 to reload value

	EIE2 |= 0x04;                       // Enable Timer4 interrupts
	TMR4CN |= 0x04;                     // Start Timer4

	EA = 1;
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
	DAC_Init();
	Voltage_Reference_Init();
	TIMER4_Init(SYSCLK/SAMPLE_RATE_DAC);
}

void Set_DACs(){
	char SFRPAGE_SAVE = SFRPAGE;        // Save Current SFR page

	SFRPAGE = DAC0_PAGE;

    DAC0 = (unsigned int)(floatValue / 2.4 * 65536);
	//DAC0 = (unsigned int)(floatValue * 2.4 / 4095);

	SFRPAGE = SFRPAGE_SAVE;             // Restore SFR page
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

void TIMER4_ISR (void) interrupt 16{
	TF4 = 0;
	Set_DACs();
}

void main(void) {
	unsigned char receiveVol[4];
    int i = 0, count = 0;

    Init_Device();
	
	while(1){
		receiveVol[count++] = RecieveChar();

		if(count >= 3){
			receiveVol[count] = '\0';
			break;
		}
    }
	
	for(i = 0;i < strlen(receiveVol);i++){
		TransmitChar(receiveVol[i]);
	}
	
	floatValue = atof(receiveVol);

	while(1);
}
