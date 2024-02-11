
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Information {
	int PMID;
	char articleTitle[300];
	char idNum[50];
	char date[20];
	char bookTitle[300];
	char type[30];
	char auth[30][40];
	char lang[15];
};

int main(void){
	struct Information node[1000];																// struct object with size 1000 array
	char *userFile = (char *)malloc(100);														// for storing user file name input
	int userOp = 0;																				// for storing user menu option input
	int nodeIdx = 0;
	int authIdx = 0;
	int i = 0;
	
	while(1){
		printf("Enter an input file (or q to quit) : ");	
		scanf("%s", userFile);
		
		if(!strcmp(userFile, "q")){
			break;																				// break if user enters q
		}
		else{
			FILE *fPtr = fopen(userFile, "r");													// to check if there's file
			
			if(fPtr == NULL){
				printf("Input file not found.\n");
				printf("Please check the file name or the location of your input file.\n");		
			}
			else{			
				/* Initializing from File to Struct nodes */	
				nodeIdx = 0;
				char *line = (char *)malloc(1000);
				while(fgets(line, _msize(line), fPtr)){
				    char *line_PMID = strstr(line, "PMID");
				    char *line_TI = strstr(line, "TI");
				    char *line_LID = strstr(line, "LID");
				    char *line_DP = strstr(line, "DP");
				    char *line_AU = strstr(line, "AU");		    
				    char *line_JT = strstr(line, "JT");	    
				    char *line_LA = strstr(line, "LA");	    
				    char *line_PT = strstr(line, "PT");
				    /* 	
					 *	Assign to Struct
				     *	1. Move pointer next to '-' for every sentence
				     *	2. Reduce new line character '\n'
				     *	3. If there was new line character, replace with null('\0')
				     */
					if(line_PMID != NULL){
						line_PMID += 6;
						if(line_PMID[strlen(line_PMID) - 1] == '\n'){
							line_PMID[strlen(line_PMID) - 1] = '\0';
						}
						node[nodeIdx].PMID = atoi(line_PMID);
					}
					else if(line_TI != NULL){
						line_TI += 6;
						if(line_TI[strlen(line_TI) - 1] == '\n'){
							line_TI[strlen(line_TI) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].articleTitle, line_TI);				
					}
					else if(line_LID != NULL){
						line_LID += 6;
						if(line_LID[strlen(line_LID) - 1] == '\n'){
							line_LID[strlen(line_LID) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].idNum, line_LID);				
					}
					else if(line_DP != NULL){
						line_DP += 6;
						if(line_DP[strlen(line_DP) - 1] == '\n'){
							line_DP[strlen(line_DP) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].date, line_DP);					
					}
					else if(line_AU != NULL){
						line_AU += 6;
						if(line_AU[strlen(line_AU) - 1] == '\n'){
							line_AU[strlen(line_AU) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].auth[authIdx], line_AU);
				    	authIdx++;
					}
					else if(line_JT != NULL){
						line_JT += 6;
						if(line_JT[strlen(line_JT) - 1] == '\n'){
							line_JT[strlen(line_JT) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].bookTitle, line_JT);
					}
					else if(line_LA != NULL){
						line_LA += 6;
						if(line_LA[strlen(line_LA) - 1] == '\n'){
							line_LA[strlen(line_LA) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].lang, line_LA);
					}
					else if(line_PT != NULL){
						line_PT += 6;
						if(line_PT[strlen(line_PT) - 1] == '\n'){
							line_PT[strlen(line_PT) - 1] = '\0';
						}
				    	strcpy(node[nodeIdx].type, line_PT);
				    	
				    	// Set Idx 
						nodeIdx++;
						authIdx = 0;				
					}		
				}
				free(line);
				fclose(fPtr);																	// close file pointer once set

				while(1){
					printf("1. Display summary\n");
					printf("2. Search by Entry Number (PMID).\n");
					printf("3. Search by Book Title (JT).\n");
					printf("4. Add a record.\n");
					printf("5. Export the data in csv format.\n");
					printf("6. Quit.\n");		
					printf("Enter: ");			scanf("%d", &userOp);							// Prompt user to enter task
					
					if(userOp == 1){
						int numEngArticle = 0;
						int numJournalAricle = 0;
						int numEngJournalArticle = 0;
						
						for(i=0;i<nodeIdx;i++){
							if(strcmp(node[i].lang, "eng") == 0){
								numEngArticle++;
								
								if(strcmp(node[i].type, "Journal Article") == 0){
									numEngJournalArticle++;
								}
							}
							if(strcmp(node[i].type, "Journal Article") == 0){
								numJournalAricle++;
							}
						}
						
						printf("=====*****======\n");
						printf("Input file: %s\n", userFile);
						printf("Number of entry = %d\n", nodeIdx);
						printf("Number of English articles = %d\n", numEngArticle);
						printf("Number of Journal articles = %d\n", numJournalAricle);
						printf("Number of English Journal articles = %d\n", numEngJournalArticle);
						printf("=====*****======\n");
					}
					else if(userOp == 2){														// Search by Entry Number
						int userPMID = 0;
						int searchCnt = 0;
						
						printf("Enter the PMID: ");
						scanf("%d", &userPMID);							
												
						/* Result */
						printf("=====*****======\n");	
						printf("Input file: %s\n", userFile);
						
						for(i=0;i<nodeIdx;i++){
							if(userPMID == node[i].PMID){
								if(i > 0){
									printf("\n");
								}
								printf("PMID: %d\n", node[i].PMID);
								printf("Title = %s\n", node[i].articleTitle);
								printf("Book Title = %s\n", node[i].bookTitle);
								printf("Type = %s\n", node[i].type);
								printf("Language = %s\n", node[i].lang);
								searchCnt++;
							}
						}	
						
						/* Not Found */											
						if(searchCnt == 0){
							printf("PMID: %d\n", userPMID);
							printf("Entry not found in the input file.\n");
						}
						
						printf("=====*****======\n");			
					}
					else if(userOp == 3){
						char *userBookTitle = (char *)malloc(sizeof(char)*1000);
						int searchCnt = 0;
						
						printf("Enter the Book Title: ");
						scanf(" %[^\n]", userBookTitle);
												
						/* Result */
						printf("=====*****======\n");	
						printf("Input file: %s\n", userFile);
						
						for(i=0;i<nodeIdx;i++){
							if(strcmp(node[i].bookTitle, userBookTitle) == 0){
								if(i > 0){
									printf("\n");
								}
								printf("PMID: %d\n", node[i].PMID);
								printf("Title = %s\n", node[i].articleTitle);
								printf("Book Title = %s\n", node[i].bookTitle);
								printf("Type = %s\n", node[i].type);
								printf("Language = %s\n", node[i].lang);
								searchCnt++;
							}
						}	
						
						/* Not Found */											
						if(searchCnt == 0){
							printf("JT: %s\n", userBookTitle);
							printf("Entry not found in the input file.\n");
						}
						
						printf("=====*****======\n");
						free(userBookTitle);
					}
					else if(userOp == 4){
						FILE *fPtrAppend = fopen(userFile, "a");							// new File pointer for append
						int PMID = 0;
						char userArticleTitle[100] = "";
						char userBookTitle[100] = "";
						char userType[100] = "";
						char userLang[100] = "";						
						
						printf("=====*****======\n");
						printf("Add new entry to the input file: %s\n", userFile);
						
						printf("PMID: ");		scanf("%d", &PMID);						
						printf("Title: ");		scanf(" %[^\n]", userArticleTitle);	
						printf("Book Title: ");	scanf(" %[^\n]", userBookTitle);	
						printf("Type: ");		scanf(" %[^\n]", userType);	
						printf("Language: ");	scanf(" %[^\n]", userLang);
						printf("Entry is written to the input file\n");
						printf("=====*****======\n");
						
						/* Add to Struct node */
						node[nodeIdx].PMID = PMID;
						strcpy(node[nodeIdx].articleTitle, userArticleTitle);
						strcpy(node[nodeIdx].bookTitle, userBookTitle);
						strcpy(node[nodeIdx].type, userType);
						strcpy(node[nodeIdx].lang, userLang);
						nodeIdx++;
						
						/* Append to file */												
						fprintf(fPtrAppend,"\n\nPMID- %d\nTI  - %s\nJT  - %s\nPT  - %s\nLA  - %s", PMID, userArticleTitle, userBookTitle, userType, userLang);
						fclose(fPtrAppend);
					}
					else if(userOp == 5){
						char userExportFIle[100] = "";
										
						printf("Enter the export filename: ");
						scanf("%s", userExportFIle);
						
						FILE *fPtrCSV = fopen(userExportFIle, "w+");
						
						for(i=0;i<nodeIdx;i++){
							fprintf(fPtrCSV, "%d,%s,%s,%s,%s\n", node[i].PMID, node[i].articleTitle, node[i].bookTitle, node[i].type, node[i].lang);
						}
						printf("=====*****======\n");	
						printf("Input file: %s\n", userFile);
						printf("Output file: %s\n", userExportFIle);
						printf("%d entries were exported.\n", nodeIdx);
						printf("=====*****======\n");
						
						fclose(fPtrCSV);				
					}
					else if(userOp == 6){
						break;																	// just quit to go back to enter file name phase
					}
					else{
						printf("Invalid Option.\n");
					}	
				}
			}
		}
	}		
	free(userFile);
	
	return 0;	
}	
