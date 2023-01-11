import java.util.Scanner;

public class Main {
	
	public static final int MAXSIZE_INGREDIENT = 4; 

	/**
	 * Method	: getUserInput
	 * Todo		: Common function for the whole user prompt inputs
	 * @return	: Object (Later casted into each type, determined by parameter 'type')		   
	 */	
	public static Object getUserInput(String type) {
		Scanner input = new Scanner(System.in);
		Object result = new Object();
		
		if(type.equals("String")){
			result = input.nextLine();
		}
		else if(type.equals("Integer")){
			result = input.nextInt();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
		Food burger = null;
		Food beverage = null;
		Food extra = null;

		int[] ingredientTypeArr = new int[MAXSIZE_INGREDIENT];		// for storing type of ingredient from user input
		int burgerType = 0;											// for storing type of burger from user input
		int cheeseType = 0;											// for storing type of cheese from user input
		int beverageType = 0;										// for storing type of beverage from user input
		int isIceInt = 0;											// for storing is ice required from user input		
		int extraType = 0;											// for storing type of extra from user input
		boolean isIce = true;										// for storing boolean value of ice needed
				
		// Intro
		System.out.println("=======================");
		System.out.println("Welcome to SKKU-SUBWAY!");
		
		// Burger Section
		System.out.println("Choose burgers (sandwiches):");
		System.out.println(String.format("1. Egg Mayo - %,3d won", Burger.getEggmayo()));
		System.out.println(String.format("2. Chicken Tikka - %,3d won", Burger.getChickentikka()));
		System.out.println(String.format("3. Chicken Ham - %,3d won", Burger.getChickenham()));
		System.out.println(String.format("4. Roasted Chicken - %,3d won", Burger.getRoastedchicken()));
		System.out.println("0. Cancel Order");
		System.out.print("Select Burger (1-5): ");
		burgerType = (Integer)getUserInput("Integer");	

		if(burgerType != 0){
			// Cheese Section
			System.out.println("=======================");
			System.out.println("Select Cheese Type:");
			System.out.println(String.format("1. American Cheese - (+%,3d won)", Cheese.getAmericancheese()));
			System.out.println(String.format("2. Swiss - (+%,3d won)", Cheese.getSwiss()));
			System.out.println(String.format("3. Cheddar - (+%,3d won)", Cheese.getCheddar()));
			System.out.print("Select Cheese (1-3): ");		
			cheeseType = (Integer)getUserInput("Integer");

			// Ingredients Section
			for(int i=0;i<MAXSIZE_INGREDIENT;i++){
				System.out.println("=======================");
				System.out.println("Select Ingredients:");
				System.out.println(String.format("1. Lettuce - (+%,3d won)", Ingredients.getLettuce()));
				System.out.println(String.format("2. Tomatoes - (+%,3d won)", Ingredients.getTomatoes()));
				System.out.println(String.format("3. Cucumbers - (+%,3d won)", Ingredients.getCucumbers()));
				System.out.println(String.format("4. Olives - (+%,3d won)", Ingredients.getOlives()));
				System.out.println("0. Skip");
				System.out.print("Select Ingredients (1-4): ");
				
				int ingredientType = (Integer)getUserInput("Integer");
	
				if(ingredientType == 0){
					break;
				}
				else{
					ingredientTypeArr[i] = ingredientType;
				}
				
				for(int j=0;j<i;j++){
					if(ingredientTypeArr[j] == ingredientType){
						throw new IllegalArgumentException("You chose same ingredient twice.");
					}
				}				
			}

			burger = new Burger(burgerType, cheeseType, ingredientTypeArr);								// assign object by polymorphism
			burger.calculateCost();																		// immediately calculate cost of burger including cheese & ingredients
		}		
		
		// Beverage Section	
		System.out.println("=======================");
		System.out.println("Select Beverages:");
		System.out.println(String.format("1. Cola - %,3d won", Beverage.getCola()));
		System.out.println(String.format("2. Fanta - %,3d won", Beverage.getFanta()));
		System.out.println(String.format("3. Chilsung Cider - %,3d won", Beverage.getChilsungcider()));
		System.out.println(String.format("4. Zero Cola - %,3d won", Beverage.getZerocola()));
		System.out.println(String.format("5. Coffee - %,3d won", Beverage.getCoffee()));
		System.out.println("0. Skip");
		System.out.print("Select Beverage (1-4): ");
		beverageType = (Integer)getUserInput("Integer");	
		
		if(beverageType != 0){
			System.out.println("=======================");
			System.out.print("With ice? (0 [no] or 1 [yes]): ");	
			
			isIceInt = (Integer)getUserInput("Integer");
			if(isIceInt == 0){
				isIce = false;
			}
			else if(isIceInt == 1){
				isIce = true;
			}
			else{
				throw new IllegalArgumentException("You should enter 1 or 0.");
			}
			
			beverage = new Beverage(beverageType, isIce);												// assign object by polymorphism
			beverage.calculateCost();																	// immediately calculate cost of beverage
		}
		
		// Extra Section	
		System.out.println("=======================");
		System.out.println("Select Extras:");
		System.out.println(String.format("1. Cookies - %,3d won", Extra.getCookies()));
		System.out.println(String.format("2. French Fries - %,3d won", Extra.getFrenchfries()));
		System.out.println(String.format("3. Chips - %,3d won", Extra.getChips()));
		System.out.println("0. Skip");
		System.out.print("Select Extra (1-3): ");
		extraType = (Integer)getUserInput("Integer");

		if(extraType != 0){
			extra = new Extra(extraType);																// assign object by polymorphism
			extra.calculateCost();																		// immediately calculate cost of extra
		}
		
		// Outro		
		double burgerPrice = 0.0;
		double beveragePrice = 0.0;
		double extraPrice = 0.0;
		
		System.out.println("=======================");
		if(burger != null){
			burgerPrice = (double)burger.getCost();
			
			System.out.println(String.format("| Burger (with cheese and ingredients): %.1f", burgerPrice));			
		}
		if(beverage != null){
			beveragePrice = (double)beverage.getCost();
			
			if(isIce){
				System.out.println(String.format("| Beverage (without ice): %.1f", beveragePrice));
			}
			else{
				System.out.println(String.format("| Beverage (without ice): %.1f", beveragePrice));
			}
		}
		if(extra != null){
			extraPrice = (double)extra.getCost();
			
			System.out.println(String.format("| Extra : %.1f", extraPrice));		
		}
		System.out.println(String.format("| TOTAL : %.1f", burgerPrice + beveragePrice + extraPrice));		
	}
}
