/*
 *	Basic and Practice in Programming
 *	Lab Assignment 2
 *	2020315798 Choi Jin Woo(ÃÖÁø¿ì) 
 *
 */
 
 #include <stdio.h>
 
 int main(void){
 	int date = 0,
	 	month = 0,
		day = 0,
		year = 0;
	char *monthStr[12] = {	"January","Febuary","March","April",
							"May","June","July","August",
							"September","October","November","December"};				// initialize month strings using pointer variable then call by month value
		
 	printf("Enter a date in numerical form (MMDDYYYY): ");
 	scanf("%d", &date);																	// prompt user to enter whole date
 	
 	month = date / 1000000;																// extract first 2 digit by dividing 1000000 to get month
 	day = (date / 10000) % 100;															// extract middle 2 digit by dividing 10000 and then modulus 100 to get day
 	year = date % 10000;																// extract last 4 digit by modulus 10000 to get year
 	
 	/**
 	 *	condition statement : put 'st,nd,rd,th' after day upon its first digit value, calculating by day modulus 10
 	 */
 	if(day == 1 || day == 21 || day == 31){
 		printf("The date you entered is: %dst %s %d", day, monthStr[month-1], year);
 	}
 	else if(day == 2 || day == 22){
 		printf("The date you entered is: %dnd %s %d", day, monthStr[month-1], year);
 	}
 	else if(day == 3 || day == 23){
 		printf("The date you entered is: %drd %s %d", day, monthStr[month-1], year);
 	}
 	else{
 		printf("The date you entered is: %dth %s %d", day, monthStr[month-1], year);
 	} 	
 }
