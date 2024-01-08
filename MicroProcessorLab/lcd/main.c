/////////////////////////////////////
//  Generated Initialization File  //
/////////////////////////////////////

#include "compiler_defs.h"
#include "C8051F040_defs.h"

sbit OW = P2^7;
sbit RS = P3^0;
sbit EN = P3^1;

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

void LCD_CMD(char cmd){
	SFRPAGE = CONFIG_PAGE;
	P3 = 0x00;
	RS = 0;
	P3 = P3 | ((cmd & 0xF0) >> 2);
	EN = 1; 
	delay(1000);
	EN = 0;
	delay(2000);
	
	P3 = 0x00;
	RS = 0;
	P3 = P3 | ((cmd & 0x0F) << 2);
	EN = 1; 
	delay(1000);
	EN = 0;
	delay(5000);
}

void DATA_write(char char_data){
	SFRPAGE = CONFIG_PAGE;
	P3 = 0x00;
	RS = 1;
	P3 = P3 | ((char_data & 0xF0) >> 2);
	EN = 1; 
	delay(1000);
	EN = 0;
	delay(2000);
	
	P3 = 0x00;
	RS = 1;
	P3 = P3 | ((char_data & 0x0F) << 2);
	EN = 1; 
	delay(10);
	EN = 0;
	delay(10);
}

void LCD_Str(char *str){
	int i;
	for(i = 0;str[i] != 0;i++){
		DATA_write(str[i]);
	}
}

void LCD_Init(void){
	delay(20000);
	LCD_CMD(0x02);
	LCD_CMD(0x28);
	LCD_CMD(0x0C);
	LCD_CMD(0x06);
	LCD_CMD(0x01);
	LCD_CMD(0x06);
	LCD_CMD(0x80);
}

void LCD_Clear(void){
	LCD_CMD(0x80);
	LCD_Str("               ");
	LCD_CMD(0xC0);
	LCD_Str("               ");
}

void LCD_Name(void){
	LCD_CMD(0x80);
	LCD_Str("Choi Jin Woo");
}

void LCD_ID(void){
	LCD_CMD(0xC0);
	LCD_Str("2020315798");
}

void LCD_Port0(int val){
	LCD_CMD(0x80);

	switch(val){
		case 0:
			LCD_Str("Monday       ");
			break;
		case 1:
			LCD_Str("Tuesday      ");
			break;
		case 2:
			LCD_Str("Wednesday    ");
			break;
		case 3:
			LCD_Str("Thursday     ");
			break;
		case 4:
			LCD_Str("Friday       ");
			break;
		case 5:
			LCD_Str("Saturday     ");
			break;
		case 6:
			LCD_Str("Sunday       ");
			break;
		default:
			LCD_Str("Press Port 0");
			break;
	}
}

void LCD_Port1(int val) {
    char buffer[20];
    int i = 0, j = 0;
    int temp = val;
    int remainder;

    if(temp == 0){
        buffer[i++] = '0';
    } 
	else{
        while (temp != 0){
            remainder = temp % 16;
            buffer[i++] = (remainder < 10) ? remainder + '0' : remainder + 'A' - 10;
            temp /= 16;
        }
    }
    buffer[i++] = 'x';
    buffer[i++] = '0';
    buffer[i] = '\0';

    LCD_CMD(0xC0);
    for(j = i - 1; j >= 0; j--){
        DATA_write(buffer[j]);
    }
}


void LCD_Temp(int temp, char frac){
    char buffer[7];
    int position = 10;
    int i = 0, j = 0;

    if(temp == 0){
        buffer[i++] = '0';
    } 
	else{
        while(temp != 0){
            buffer[i++] = temp % 10 + '0';
            temp /= 10;
        }
    }
    buffer[i] = '\0';

    LCD_CMD(0xC0 + position);
    for(j = i - 1; j >= 0; j--){
        DATA_write(buffer[j]);
    }

	if(frac){
		LCD_Str(".5");
	}
	else{
		LCD_Str("  ");
	}
}

void main(void){
	int i, I0, I1;
	char temp_byte[9];
	char frac = 0;
    char temp;
	Init_Device();
	LCD_Init();

	// Show Name and ID
	LCD_Name();
	LCD_ID();

	// Delay for 1 second
	for(i = 0; i < 1000; i++){
		delay(1000);
	}

	// Clear the screen for Q2 ~ 4
	LCD_Clear();
	
	while(1){		
		XBR2 = 0x00;
		I0 = P0;
		I1 = P1;
		XBR2 = 0x40;

		// Q2 : Port 0
		if(I0 == 1){
			LCD_Port0(0);
		}
		else if(I0 == 2){
			LCD_Port0(1);
		}
		else if(I0 == 4){
			LCD_Port0(2);
		}
		else if(I0 == 8){
			LCD_Port0(3);
		}
		else if(I0 == 16){
			LCD_Port0(4);
		}
		else if(I0 == 32){
			LCD_Port0(5);
		}
		else if(I0 == 64){
			LCD_Port0(6);
		}
		else{
			LCD_Port0(7);
		}

		// Q3 : Port 1
		LCD_Port1(I1);

		// Q4 : Temperature
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

		// Print temperature
		LCD_Temp(temp, frac);
	}
}