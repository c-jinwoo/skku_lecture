
 #include <stdio.h>
 #define SIZE 10
 
 int main(void){
 	int a[10] = {2, 5, 8, 1, 4, 7, 3, 10, 6, 9};
	
	int *MaxPtr = NULL,
		*MinPtr = NULL,
		*CurPtr = NULL,				// pointer for searching 
		i = 0;
		
	MaxPtr = &a[0];
	MinPtr = &a[0];
	
	/* Finding Max */
	CurPtr = a;	
	for(i=0;i<SIZE-1;i++){
		CurPtr++;
		if(*MaxPtr < *CurPtr){
			MaxPtr = CurPtr;
		}
	}
	
	/* Finding Min */
	CurPtr = a;	
	for(i=0;i<SIZE-1;i++){
		CurPtr++;
		if(*MinPtr > *CurPtr){
			MinPtr = CurPtr;
		}
	}
	
 	printf("Value of the item MaxPtr pointing at: %d\n",*MaxPtr);
	printf("Value of the item MinPtr pointing at: %d\n",*MinPtr);
	
	return 0;
}
