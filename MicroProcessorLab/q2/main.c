#include "compiler_defs.h"
#include "C8051F040_defs.h"

void Reset_Sources_Init(){
    WDTCN     = 0xDE;
    WDTCN     = 0xAD;
}
void Port_IO_Init(){
    SFRPAGE   = CONFIG_PAGE;
    P0MDOUT   = 0x01;
    XBR1      = 0x80;
    XBR2      = 0x40;
}
void Oscillator_Init(){
    SFRPAGE   = CONFIG_PAGE;
    OSCICN    = 0x83;
}
void Init_Device(void){
    Reset_Sources_Init();
    Port_IO_Init();
    Oscillator_Init();
}
void main(){
	Init_Device();
}