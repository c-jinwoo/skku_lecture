//-----------------------------------------------------------------------------
// Program Description:
//
// This program measures the voltage on an external ADC input and prints the 
// result to a terminal window via the UART.
//
// The system is clocked using the internal 24.5MHz oscillator.  
// Results are printed to the UART from a loop with the rate set by a delay 
// based on Timer 2.  This loop periodically reads the ADC value from a global 
// variable, Result.
//
// The ADC makes repeated measurements at a rate determined by SAMPLE_RATE 
// using Timer 3. The end of each ADC conversion initiates an interrupt which 
// calls an averaging function.  
// <INT_DEC> samples are averaged then the Result value updated.
//
// For each power of 4 of <INT_DEC>, you gain 1 bit of effective resolution.
// For example, <INT_DEC> = 256 gain you 4 bits of resolution: 4^4 = 256.
// 
// The ADC input multiplexer is set for a single-ended input at AIN0.1.  
// The input amplifier is set for unity gain so a voltage range of 0 to Vref 
// (2.43V) may be measured.  Although voltages up to Vdd may be applied without
// damaging the device, only the range 0 to Vref may be measured by the ADC.  
//
// A 100kohm potentiometer may be connected as a voltage divider between 
// VREF and AGND as shown below: (minimum value = 12Kohms as the maximum 
// recommended current through the Vref is of 200uA @2.4V)
//
// ---------
//          |        
//       8 o| AGND ----| 
//         o| VREF ----|<-|
//         o| AIN0.1   |  |
//         o|    |        |
//         o|     -------- 
//         o|
//         o|
//       1 o|
//          |
//----------   
//
// How To Test:
//
// 1) Download code to a 'F04x device that is connected to a UART transceiver
// 2) Connect serial cable from the transceiver to a PC
// 3) On the PC, open HyperTerminal (or any other terminal program) and connect
//    to the COM port at <BAUDRATE> and 8-N-1
// 4) Connect a variable voltage source (between 0 and Vref) 
//    to AIN 0.1, or a potentiometer voltage divider as shown above.
// 5) HyperTerminal will print the voltage measured by the device if
//    everything is working properly
//
//-----------------------------------------------------------------------------
// Includes
//-----------------------------------------------------------------------------

#include <c8051F040.h>                 // SFR declarations
#include <stdio.h>                     

//-----------------------------------------------------------------------------
// 16-bit SFR Definitions for 'F04x
//-----------------------------------------------------------------------------

sfr16 ADC0     = 0xbe;                 // ADC0 data

//-----------------------------------------------------------------------------
// Global Constants
//-----------------------------------------------------------------------------

#define BAUDRATE     115200            // Baud rate of UART in bps
#define SYSCLK       24500000          // System Clock
#define SAMPLE_RATE  50000             // Sample frequency in Hz
#define INT_DEC      256               // Integrate and decimate ratio
#define SAR_CLK      2500000           // Desired SAR clock speed

#define SAMPLE_DELAY 50                // Delay in ms before taking sample

sbit LED = P1^6;                       // LED='1' means ON
sbit SW1 = P3^7;                       // SW1='0' means switch pressed

//-----------------------------------------------------------------------------
// Function Prototypes
//-----------------------------------------------------------------------------

void OSCILLATOR_Init (void);           
void PORT_Init (void);
void UART1_Init (void);
void ADC0_Init (void);
void ADC0_ISR (void);

//-----------------------------------------------------------------------------
// Global Variables
//-----------------------------------------------------------------------------
long Result;                           // ADC0 decimated value

void Reset_Sources_Init()
{
   WDTCN     = 0xDE;
   WDTCN     = 0xAD;
}

// 24.5MHz
void OSCILLATOR_Init (void)
{
   char SFRPAGE_SAVE = SFRPAGE;
   SFRPAGE = CONFIG_PAGE;
   OSCICN = 0x83;
   CLKSEL = 0x00;                      // Select the internal osc. as
                                       // the SYSCLK source
   SFRPAGE = SFRPAGE_SAVE;
}

// Port Init -------------------------------------------------------------------
// P0.4   digital   push-pull     UART TX
// P0.5   digital   open-drain    UART RX
// P1.6   digital   push-pull     LED
// AIN0.1 analog                  Analog input 
//-----------------------------------------------------------------------------
void PORT_Init (void)
{
   char SFRPAGE_SAVE = SFRPAGE; 
   SFRPAGE = CONFIG_PAGE;

   XBR0     = 0x00;
   XBR1     = 0x00;
   XBR2     = 0x44;                    // Enable crossbar and weak pull-up
                                       // Enable UART1
   P0MDOUT |= 0x01;                    // Set TX1 pin to push-pull
   P1MDOUT |= 0x40;                    // Set P1.6(LED) to push-pull
   P3MDOUT |= 0xFF; 
   P4MDOUT |= 0xFF; 
   P5MDOUT |= 0xFF;
   SFRPAGE = SFRPAGE_SAVE;
}

//-----------------------------------------------------------------------------
// UART1_Init
//-----------------------------------------------------------------------------
// Configure the UART1 using Timer1, for <baudrate> and 8-N-1.
//-----------------------------------------------------------------------------
void UART1_Init (void)
{
   char SFRPAGE_SAVE = SFRPAGE;

   SFRPAGE = UART1_PAGE;
   SCON1   = 0x10;                     // SCON1: mode 0, 8-bit UART, enable RX

   SFRPAGE = TIMER01_PAGE;
   TMOD   &= ~0xF0;
   TMOD   |=  0x20;                    // TMOD: timer 1, mode 2, 8-bit reload

   if (SYSCLK/BAUDRATE/2/256 < 1) {
      TH1 = -(SYSCLK/BAUDRATE/2);
      CKCON |= 0x10;                   // T1M = 1; SCA1:0 = xx
   } else if (SYSCLK/BAUDRATE/2/256 < 4) {
      TH1 = -(SYSCLK/BAUDRATE/2/4);
      CKCON &= ~0x13;                  // Clear all T1 related bits
      CKCON |=  0x01;                  // T1M = 0; SCA1:0 = 01
   } else if (SYSCLK/BAUDRATE/2/256 < 12) {
      TH1 = -(SYSCLK/BAUDRATE/2/12);
      CKCON &= ~0x13;                  // T1M = 0; SCA1:0 = 00
   } else {
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

void Init_Device(void)
{
   Reset_Sources_Init();
   OSCILLATOR_Init ();
   PORT_Init ();
   UART1_Init ();                      // Initialize UART1
}

//-----------------------------------------------------------------------------
// ADC0_Init
//-----------------------------------------------------------------------------
void ADC0_Init (void)
{
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

void longdelay(void)
{
  int i; 
  int j; 

  for (i=0; i<1000; i++){
    for (j=0; j<100; j++){
    }
  }
}
//-----------------------------------------------------------------------------
// main() Routine
//-----------------------------------------------------------------------------
void main (void)
{
  long measurement;                   // Measured voltage in mV

  Init_Device(); 
  ADC0_Init ();                       // Init ADC

  // SFRPAGE = ADC0_PAGE;
  // AD2EN = 1;                          // Enable ADC
   LED = 1; 
  while (1)
  {      

     SFRPAGE = ADC0_PAGE;
     AD0EN = 1;
     AD0INT = 0; 
     AD0BUSY = 1;
     while(AD0INT==0);

      // The 10-bit ADC0 value is averaged across INT_DEC measurements.  
      // The result is then stored in Result, and is right-justified 
      // The measured voltage applied to AIN 0.1 is then:
      //
      //                           Vref (mV)
      //   measurement (mV) =   --------------- * Result (bits) 
      //                       (2^12)-1 (bits)
      
     if (ADC0>0)
     
     {
     Result = ADC0; 
      measurement =  Result * 2430 / 4095;

      SFRPAGE = UART1_PAGE;
      printf("AIN0.1 voltage: %ld mV\n",measurement);

      SFRPAGE = CONFIG_PAGE;
      LED = ~LED;                      // LED reflects state of switch
      
      P4 = (Result)&(0x0F); 
      P5 = (Result)>>8; 

      longdelay(); 
     }
  }
}