#include <c8051F040.h>                 // SFR declarations
#include <stdio.h>
#include <string.h>
#define BAUDRATE     115200            // Baud rate of UART in bps
#define SYSCLK       24500000          // System Clock
#define SAMPLE_RATE  50000             // Sample frequency in Hz
#define INT_DEC      256               // Integrate and decimate ratio
#define SAR_CLK      2500000           // Desired SAR clock speed
#define SAMPLE_DELAY 50                // Delay in ms before taking sample

sbit LED = P1^6;                       // LED='1' means ON
sbit SW1 = P3^7;                       // SW1='0' means switch pressed
sfr16 ADC0     = 0xbe;                 // ADC0 data
long Result;                           // ADC0 decimated value

void Reset_Sources_Init(){
	WDTCN     = 0xDE;
	WDTCN     = 0xAD;
}

void Timer_Init(){						// Set Timer 1 to 8-bit auto-reload mode with SYSCLK / 1 
    SFRPAGE   = TIMER01_PAGE;
    TMOD      = 0x20;
    CKCON     = 0x10;
    TH1       = 0x60;					// Calculated with Baud Rate of 4800
	TR1		  = 1;
}

void OSCILLATOR_Init(void){
	char SFRPAGE_SAVE = SFRPAGE;
	SFRPAGE = CONFIG_PAGE;
	OSCICN = 0x83;
	CLKSEL = 0x00;
	SFRPAGE = SFRPAGE_SAVE;
}

void PORT_Init(void){
	char SFRPAGE_SAVE = SFRPAGE; 
	SFRPAGE = CONFIG_PAGE;

	XBR0     = 0x04;
	XBR1     = 0x00;
	XBR2     = 0x44;
	P0MDOUT |= 0x01;                    // Set TX1 pin to push-pull
	P1MDOUT |= 0x40;                    // Set P1.6(LED) to push-pull
	P3MDOUT |= 0xFF; 
	P4MDOUT |= 0xFF; 
	P5MDOUT |= 0xFF;
	SFRPAGE = SFRPAGE_SAVE;
}

void UART_Init(){				// Mode 1, 8-bit UART, enable receiver
    SFRPAGE   = UART0_PAGE;
    SCON0     = 0x50;			    
}

void UART1_Init(void){
	char SFRPAGE_SAVE = SFRPAGE;

	SFRPAGE = UART1_PAGE;
	SCON1   = 0x10;                     // SCON1: mode 0, 8-bit UART, enable RX

	SFRPAGE = TIMER01_PAGE;
	TMOD   &= ~0xF0;
	TMOD   |=  0x20;                    // TMOD: timer 1, mode 2, 8-bit reload

	if (SYSCLK/BAUDRATE/2/256 < 1) {
		TH1 = -(SYSCLK/BAUDRATE/2);
		CKCON |= 0x10;                   // T1M = 1; SCA1:0 = xx
	} 
	else if (SYSCLK/BAUDRATE/2/256 < 4) {
		TH1 = -(SYSCLK/BAUDRATE/2/4);
		CKCON &= ~0x13;                  // Clear all T1 related bits
		CKCON |=  0x01;                  // T1M = 0; SCA1:0 = 01
	} 
	else if (SYSCLK/BAUDRATE/2/256 < 12) {
		TH1 = -(SYSCLK/BAUDRATE/2/12);
		CKCON &= ~0x13;                  // T1M = 0; SCA1:0 = 00
	} 
	else {
		TH1 = -(SYSCLK/BAUDRATE/2/48);
		CKCON &= ~0x13;                  // Clear all T1 related bits
		CKCON |=  0x02;                  // T1M = 0; SCA1:0 = 10
	}

	TL1 = TH1;                          // Initialize Timer1
	TR1 = 1;                            // Start Timer1

	SFRPAGE = UART1_PAGE;
	TI1 = 1;                            // Indicate TX1 ready

	SFRPAGE = SFRPAGE_SAVE;
}

void ADC0_Init(void){
    char SFRPAGE_SAVE = SFRPAGE;
    SFRPAGE = ADC0_PAGE;
    ADC0CN = 0x00;                      // ADC0 disabled; normal tracking mode;
                                        // Conversion intiated on AD0BUSY=1
                                        // ADC0 data is right-justified
    REF0CN = 0x03;                      // Enable on-chip VREF,
                                        // and VREF output buffer
    AMX0CF = 0x00;                      // AIN inputs are single-ended (default)
    AMX0SL = 0x01;                      // Select AIN0.1 pin as ADC mux input

    ADC0CF = (SYSCLK/SAR_CLK) << 3;     // ADC conversion clock = 2.5MHz, Gain=1

    EIE2 |= 0x02;                       // enable ADC interrupts
    SFRPAGE = SFRPAGE_SAVE;
}

void Init_Device(void){
	Reset_Sources_Init();
	Timer_Init();
	OSCILLATOR_Init();
	PORT_Init();
	UART_Init();
	UART1_Init();
	ADC0_Init();
}

void longdelay(void){
	int i; 
	int j; 

	for (i=0; i<1000; i++){
		for (j=0; j<100; j++){
		}
	}
}

void TransmitChar(unsigned char ch){	
	SFRPAGE = UART0_PAGE;
	SBUF0 = ch;    				// Transmit to buffer
	while (!TI0);				// Wait until the transmission
	TI0 = 0;					// Clear
}


void main (void){
	long measurement;                   // Measured voltage in mV
	char buffer[20];
	int i = 0;

	Init_Device(); 
	LED = 1; 
	while (1){      

		SFRPAGE = ADC0_PAGE;
		AD0EN = 1;
		AD0INT = 0; 
		AD0BUSY = 1;
		while(AD0INT==0);

		if(ADC0 > 0){
			Result = ADC0; 
			measurement =  Result * 2430 / 4095;
			sprintf(buffer, "%ld", measurement);
			for(i = 0; i < strlen(buffer); i++){
				TransmitChar(buffer[i]);
			}
			
			SFRPAGE = UART1_PAGE;
			SFRPAGE = CONFIG_PAGE;
			LED = ~LED;                      // LED reflects state of switch

			P4 = (Result)&(0x0F); 
			P5 = (Result)>>8; 
			
			longdelay(); 
		}
	}
}