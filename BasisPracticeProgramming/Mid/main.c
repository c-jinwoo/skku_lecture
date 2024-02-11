
#include <stdio.h>

const int MAX_COMPARTMENT = 4;													// Number of data types : 4 (S,E,I,R : uses for array index)
 
/**
 *	Function	: getTotalPopulation()
 *	Todo		: return each day's total population
 */	
double getTotalPopulation(double *seir){
	int i = 0;
	double sum = 0;
	
	for(i = 0;i < MAX_COMPARTMENT;i++){
		sum += seir[i];
	}	
	
	return sum;
}

/**
 *	Function	: setSeir()
 *	Todo		: calculate seir data
 */	
void setSeir(double *seir, double *rate){	
	double beta = rate[0];
	double sigma = rate[1];
	double gamma = rate[2];
	
	double sus_old = seir[0];
	double exp_old = seir[1];
	double inf_old = seir[2];
	double rec_old = seir[3];
	
	double sus_new = 0;
	double exp_new = 0;
	double inf_new = 0;
	double rec_new = 0;
	
	double susToEx = beta * sus_old * inf_old / getTotalPopulation(seir);
	double exToInf = sigma * exp_old;
	double infToRec = gamma * inf_old;
	
	// calculate each data type with negative exception
	if(sus_old >= susToEx){
		sus_new = sus_old - susToEx;
		exp_new = exp_old + susToEx;
	}
	else{
		exp_new = exp_old + sus_old;
		sus_new = 0;
	}
	
	if(exp_new >= exToInf){
		exp_new = exp_new - exToInf;
		inf_new = inf_old + exToInf;		
	}
	else{
		inf_new = inf_old + exp_new;
		exp_new = 0;			
	}
	
	if(inf_new >= infToRec){
		inf_new = inf_new - infToRec;
		rec_new = rec_old + infToRec;		
	}
	else{
		rec_new = rec_old + inf_new;	
		inf_new = 0;		
	}
	
	seir[0] = sus_new;
	seir[1] = exp_new;
	seir[2] = inf_new;
	seir[3] = rec_new;
}
 
int main(void){		
	const double POPULATION = 51700000;																					// init population of Susceptible in day 0	
	double rate[3] = {0};																								// 0 : beta, 1 : sigma, 2 : gamma
	double seir[4] = {
		POPULATION, 0																									// init condition of seir(day 0), infected people will be assigned later
	};																				
	int day = 0,																										// days to run simulation
		i = 0,
		j = 0,
		k = 0;
	
	while(1){	
		// initialize seir array	
		seir[0] = POPULATION;																							
		seir[1] = 0;
		seir[2] = 0;
		seir[3] = 0;
				
		printf("Enter the number of infected people in Day 0: ");
		scanf("%lf", &seir[2]);
		printf("Enter the value of Beta (Transmission Rate): ");
		scanf("%lf", &rate[0]);		
		printf("Enter the value of Sigma (Incubation Rate): ");
		scanf("%lf", &rate[1]);
		printf("Enter the value of Gamma (Recovery Rate): ");
		scanf("%lf", &rate[2]);
		printf("How many days do you want to run the simulation: ");
		scanf("%d", &day);			
		printf("Day\tS\t\tE\t\tI\t\tR\n");
		printf("=============================================================================\n");
		
		double result[day+1][MAX_COMPARTMENT];																			// create 2D array to save the result
		
		/*
		 *	Section 	: Calculation
		 *	Objective 	: Calculate and print out the amount of people for each data
		 */	
		for(i = 0;i <= day; i++){
			if(i > 0){
				setSeir(seir, rate);
			}				
			for(j = 0;j < MAX_COMPARTMENT; j++){
				result[i][j] = seir[j];																					// copy to result array
			}
			printf("Day %d:\t%lf\t%lf\t%lf\t%lf\n", i, result[i][0], result[i][1], result[i][2], result[i][3]);
		}
		printf("\n");	
		
		/*
		 *	Section 	: Plot
		 *	Objective 	: Print out percentage of data
		 */	
		while(1){
			char isPlot = "";
			char dataType = "";
			
			printf("Do you want to plot the result [y/n]? ");
			scanf(" %c", &isPlot);
			
			if(isPlot == 'y'){
				while(1){
					printf("Which data would you like to plot [s/e/i/r]? ");
					scanf(" %c", &dataType);
					
					if(dataType == 's' || dataType == 'e' || dataType == 'i' || dataType == 'r'){
						int dataNum = 0;
						
						if(dataType == 's'){
							dataNum = 0;
						}
						else if(dataType == 'e'){
							dataNum = 1;
						}
						else if(dataType == 'i'){
							dataNum = 2;
						}
						else if(dataType == 'r'){
							dataNum = 3;
						}
						
						for(i = 0;i <= day;i++){
							int percentage = (result[i][dataNum] * 100) / getTotalPopulation(seir);
							printf("Day %d\t(%d%%)\t| ", i, percentage);
							for(j = 0;j < percentage;j++){
								printf("*");								
							}
							printf("\n");							
						}
						printf("\n");									
						break;
					}				
					else{
						printf("Input Error! Please enter [s/e/i/r].\n");
					}
				}
			}
			else if(isPlot == 'n'){
				break;
			}
			else{
				printf("Input Error! Please enter [y/n].\n");
			}
		}
		
		/*
		 *	Section 	: Outro
		 *	Obejctive 	: Prompt user whether to finish
		 */		
		char isAgain = "";
		int isLoop = 0;														// 1 : run again the simulation, 0 : exit
		
		while(1){
			printf("Do you want to run the simulation again [y/n]? ");
			scanf(" %c", &isAgain);
			if(isAgain == 'y'){
				isLoop = 1;
				break;
			}
			else if(isAgain == 'n'){
				isLoop = 0;
				break;
			}
			else{
				printf("Input Error! Please enter [y/n].\n");			
			}
		}
		
		if(isLoop == 1){
			printf("\n");
		}
		else{
			break;
		}	
	}	
}
