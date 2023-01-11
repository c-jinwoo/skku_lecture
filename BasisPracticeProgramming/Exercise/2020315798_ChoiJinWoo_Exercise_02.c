/*
 *	Basic and Practice in Programming
 *	Lab Exercise 2
 *	2020315798 Choi Jin Woo(ÃÖÁø¿ì) 
 *
 */
 
 #include <stdio.h>
 #include <math.h>
 #define _USE_MATH_DEFINES										// define constant to use Math.PI to get the area of circle
 
 int main(void){
 	int shape = 0; 	
 			
 	printf("(1) Circle\n(2) Rectangle\n(3) Triangle\n");
 	printf("Enter the shape: ");
 	scanf("%d", &shape);										// prompt user to enter value for the kind of shape
 	
 	
 	if(shape == 1){												// case 1 : circle
 		int radius = 0;
 		float area = 0;
		
		printf("Enter the radius of the circle: ");
		scanf("%d", &radius);
		
		area = M_PI * radius * radius;							// calculate the area of the circle by the formula
		printf("The area of the circle is %.0f.\n\n", area);	// print the result by rounding up the float point number
 	}
 	else if(shape == 2){										// case 2 : rectangle
 		int width = 0,
 			height = 0,
			area = 0;											// unlike circle and triangle, the area cannot be float point number
 			
 		printf("Enter the width of the rectangle: ");
 		scanf("%d", &width);
 		printf("Enter the width of the rectangle: ");
 		scanf("%d", &height);
 		
 		area = width * height;
 		printf("The area of the rectangle is %d.\n\n", area);
 	}
 	else if(shape == 3){										// case 3 : triangle
 		int width = 0,
 			height = 0;
		float area = 0;											// since we multiply 0.5 for triangle area, it can be a float point number
 			
 		printf("Enter the width of the triangle: ");
 		scanf("%d", &width);
 		printf("Enter the width of the triangle: ");
 		scanf("%d", &height);
 		
 		area = width * height * 0.5;
 		printf("The area of the rectangle is %.1f.\n\n", area);	// round up at .1 as the lecture note
 	}
 	else{														// other cases : exception
 		printf("You should select 1 ~ 3.");	 		
 	}	
 }
