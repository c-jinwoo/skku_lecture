/**
 * Subject	: Java Programming
 * Task		: Assignment 2
 * Class	: Main
 * Objective: Main class of Apple products
 * SID		: 2020315798
 * Name		: Choi Jin Woo
 */

import java.util.Scanner;

public class Main {
	
	/**
	 * Fields	: constant values of the base price of each product
	 */
	private static final int BASE_PRICE_MACBOOK = 1000;
	private static final int BASE_PRICE_IPHONE = 800;
	private static final int BASE_PRICE_AIRPODS = 120;
	
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
		boolean repeatPurchase = true;																	// for controlling infinite purchase logic
		
		while(repeatPurchase){
			System.out.println("==== Welcome Apple Korea ====");
			System.out.println("What do you want to buy?");
			System.out.println("Choose 1 for MacBook");
			System.out.println("Choose 2 for iPhone");
			System.out.println("Choose 3 for AirPods");
			System.out.println("Choose 0 for Exit");
						
			System.out.print("Please enter your choice: ");
			int userChoice = (Integer)getUserInput("Integer");											// prompt user to choose items among above			
			System.out.println("=============================");
						
			Product product = null;																		// generate instance of Product class, later initialized upon user's product choice
						
			if(userChoice == 0) {																		// Case 0 : Escape				
				break;				
			}
			else if(userChoice == 1) {																	// Case 1 : MacBook
				System.out.println("Base cost of the MacBook: $" + BASE_PRICE_MACBOOK);		
				
				System.out.print("Which model do you want (Pro or Air): ");
				String model = (String)getUserInput("String");
				
				System.out.print("Size (13, 15): ");
				int size = (Integer)getUserInput("Integer");

				System.out.print("Color (base, Gold, Silver): ");
				String color = (String)getUserInput("String");
				
				System.out.print("Memory (128, 256, 512): ");
				int memory = (Integer)getUserInput("Integer");
				
				System.out.print("How many do you want to buy: ");
				int quantity = (Integer)getUserInput("Integer");				

				product = new MacBook(BASE_PRICE_MACBOOK, model, quantity, color, memory, size);		// create MacBook instance	
			}
			else if(userChoice == 2) {																	// Case 2 : iPhone
				System.out.println("Base cost of the iPhone: $" + BASE_PRICE_IPHONE);		
				
				System.out.print("Which model do you want (iPhone12 or iPhone13): ");
				String model = (String)getUserInput("String");

				System.out.print("Color (base, Gold, Silver): ");
				String color = (String)getUserInput("String");
				
				System.out.print("Memory (64, 128, 256, 512): ");
				int memory = (Integer)getUserInput("Integer");
				
				System.out.print("How many do you want to buy: ");
				int quantity = (Integer)getUserInput("Integer");		
				
				product = new iPhone(BASE_PRICE_IPHONE, model, quantity, color, memory);				// create iPhone instance
			}
			else if(userChoice == 3) {																	// Case 3 : AirPods
				System.out.println("Base cost of the iPhone: $" + BASE_PRICE_AIRPODS);	
				
				System.out.print("Which model do you want (AirPods3, AirPodsPro, AirPodsMax): ");
				String model = (String)getUserInput("String");

				System.out.print("How many do you want to buy: ");
				int quantity = (Integer)getUserInput("Integer");
				
				product = new AirPods(BASE_PRICE_AIRPODS, model, quantity);								// create AirPods instance
			}
			else {																						// Default cases : throw illegal error exception	
				throw new IllegalArgumentException("You chose an invalid option.");				
			}
			
			product.calculateCost();																	// calculate cost including extra options first, to compare base and total cost 
			product.getUserSelection();																	// list up user's extra choices
			
			String productName = product.getClass().getName();											// for storing product's name by using method
			double totalCost = product.getTotalCost();													// for storing total cost by caling getter method
			int quantity = product.getQuantity();														// for storing quantity by calling getter method

			System.out.println("");																		// just for the line change
			System.out.print("Total cost for one " + productName +": ");
			System.out.println("$" + String.format("%.2f", totalCost));									// print the value with .2 digit double type
			
			System.out.print("Total cost (quantity x " + quantity + "): ");
			System.out.println("$" + String.format("%.2f", totalCost * quantity));						// print the value with .2 digit double type
			
			System.out.print(" Do you want to purchase something else (Yes: 1, No: 0): ");
			int isBuyingMore = (Integer)getUserInput("Integer");										// for storing 0 or 1 to determine furthur purchase

			if(isBuyingMore == 0){
				repeatPurchase = false;																	// break the loop by false
				System.out.println("Thank you for your purchase!");										// end program with the statement
			}
			else if(isBuyingMore == 1){
				repeatPurchase = true;																	// keep the value true to repeat
				System.out.println("");																	// just for line change
			}
			else{
				throw new IllegalArgumentException("You should choose 0 or 1.");
			}
		}
	}
}