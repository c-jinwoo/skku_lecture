/**
 *	Subject		: Java Programming
 *	Task		: Assignment 3
 * 	Class		: Main
 *	Objective	: Calculate fare of user's transportation
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

import java.util.Scanner;

public class Main {
	
	/**
	 * Method	: getUserInput
	 * Todo		: Common function for the whole user prompt inputs
	 * @return	: Object (Later casted into each type, determined by parameter 'type')		   
	 */	
	public static Object getUserInput(String type) {
		Scanner input = new Scanner(System.in);
		Object result = new Object();
		
		if(type.equals("String")){
			result = input.next();
		}
		else if(type.equals("Integer")){
			result = input.nextInt();
		}
		else if(type.equals("Double")){
			result = input.nextDouble();
		}
		else{
			throw new IllegalArgumentException("Unknown type");
		}
		
		return result;
	}	
	
	public static void main(String[] args) {	
		
		int nTransfer = 0;																		// for storing number of transfers by user input
		double totalTripFare = 0;																// for storing total trip fare for each tranportation usage
		
		/**
		 *	Prologue : Start program with greetings, prompt user to enter number of transfer
		 */
		System.out.println("======= Welcome to Trip Planner =======");
		System.out.print("Number of transfers: ");
		nTransfer = (Integer)getUserInput("Integer");

		if(nTransfer <= 0){
			throw new IllegalArgumentException("Number of transfers should be greater than 0.");
		}		
		
		PublicTransport[] transport = new PublicTransport[nTransfer];							// create array for PublicTransport type whose size is based on user input
		
		/**
		 *	For-Loop : Store each tranportation's information
		 */
		for(int i=0;i<transport.length;i++){
			System.out.println("===============================");
			System.out.println("Choose transport " + (i+1) + ":");
			System.out.println("1. Taxi  (Enter 1)");
			System.out.println("2. Bus   (Enter 2)");
			System.out.println("3. Train (Enter 3)");
			System.out.println("0. For canceling trip (Enter 0)");			
			System.out.print("Enter your choice: ");
			
			int userChoice = (Integer)getUserInput("Integer");			
			
			if(userChoice == 0){																// Case 0 : break
				System.out.println("Trip is canceled");
				break;
			}		
			else if(userChoice == 1){															// Case 1 : Taxi
				System.out.print("Enter transport name: ");
				String model = (String)getUserInput("String");
				
				System.out.print("Enter base fare: ");
				double baseFare = (Double)getUserInput("Double");				

				System.out.print("Enter fare per km: ");
				double farePerKm = (Double)getUserInput("Double");
				
				System.out.print("Enter distance (in km): ");
				double distance = (Double)getUserInput("Double");
				
				transport[i] = new Taxi(model, baseFare, farePerKm, distance);					// create Taxi object in the PublicTransport array, index of i
				
			}		
			else if(userChoice == 2){															// Case 2 : Bus
				System.out.print("Enter transport name: ");
				String model = (String)getUserInput("String");
				
				System.out.print("Enter base fare: ");
				double baseFare = (Double)getUserInput("Double");				

				System.out.print("Enter fare per station(for extra stations): ");
				double farePerStation = (Double)getUserInput("Double");
				
				System.out.print("Enter number of stations: ");
				int nStations = (Integer)getUserInput("Integer");
				
				transport[i] = new Bus(model, baseFare, farePerStation, nStations);				// create Taxi object in the PublicTransport array, index of i
			}		
			else if(userChoice == 3){															// Case 3 : Train
				System.out.print("Enter transport name: ");
				String model = (String)getUserInput("String");
				
				System.out.print("Enter base fare: ");
				double baseFare = (Double)getUserInput("Double");				

				System.out.print("Enter fare per station(for extra stations): ");
				double farePerStation = (Double)getUserInput("Double");
				
				System.out.print("Enter number of stations: ");
				int nStations = (Integer)getUserInput("Integer");
				
				transport[i] = new Train(model, baseFare, farePerStation, nStations);			// create Taxi object in the PublicTransport array, index of i
			}		
			else {																				// Default : Exception
				throw new IllegalArgumentException("You have chosen the wrong option.");
			}
			
			transport[i].calculatePayment();													// calculate the total fare of each tranportation
			totalTripFare += transport[i].getTotalFare();										// sum up the each total fare
		}	
		
		
		/**
		 * 	Extra-functionality : Special gift
		 * 	Objective			: Trip manager will offer the user a random gift as a special thanks.
		 * 						  The user can save 10% of total fare as a mileage.
		 * 						  The gift will be chosen based on the mileage.
		 * 						  TripManager Class is implemented at the bottom of this page.
		 */
		TripManager tripManager = new TripManager(totalTripFare * 0.1);
		tripManager.selectRandomGift();

		System.out.println("");
		System.out.println("====== Speical Gift!!! ======");
		System.out.println("You have got \"" + tripManager.getGift() + "\"");
		
		if(tripManager.getGift().equals("JAVA Lab Assignment") || tripManager.getGift().equals("Nothing")){
			System.out.println("LOL");	// just for teasing 
		}
		System.out.println("");
		
		
		/**
		 *	Epilogue : Print out fare of each transport, total trip fare, and info
		 */
		System.out.println("====== Trip Plan ======");
		
		for(int i=0;i<transport.length;i++){
			System.out.println(String.format("Transport %d: %s", i+1, transport[i].getModel()));
			System.out.println(String.format("Fare: %.2f", transport[i].getTotalFare()));
		}

		System.out.println("===============================");
		System.out.println(String.format("Total trip Fare: %.2f", totalTripFare));
		System.out.println("===============================");
		System.out.println("Info : if the number of stations are more than 5, there will be an extra charge for each extra stations.");
		System.out.println("===== Have a wonderful trip! =====");
	}
	
	public static class TripManager{
		private double mileage = 0;																			// for storing a mileage based on total fare
		private String gift = "";																			// for storing a random gift
		private static final String[][] giftList = {
			{"Candy", "Chocolate", "JAVA Lab Assignment"},													// Mileage range : 0 ~ 999
			{"Coffee", "Kimchi", "Spam sausage"},															// Mileage range : 1000 ~ 1999
			{"Tumbler", "Phone case", "Midium-rare Steak"},													// Mileage range : 2000 ~ 2999
			{"iPad", "Samsung Z-Flip3", "LG Styler", "Coffee Machine"},										// Mileage range : 3000 ~ 3999
			{"Nothing", "Mercedes Maybach s650", "Audi R8", "Rolex Submariner", "Greubel Frosey GMT"},		// Mileage range : 4000 ~		
		};	
		
		/**
		 * Method	: Constructor
		 * Todo		: set mileage of the total fare
		 */	
		public TripManager(double mileage){
			this.setMileage(mileage);
		}
		
		/**
		 * Method	: selectRandomGift
		 * Todo		: set random gift based on the mileage and store it at gift variable
		 */	
		public void selectRandomGift(){
			
			int mileageRange = (int)this.getMileage() / 1000;												// calculate 1000s digit of the mileage to use it in array
			if(mileageRange > 4){
				mileageRange = 4;																			// array range is max 4
			}
			
			int randomIdx = (int)(Math.random()*100) % this.giftList[mileageRange].length;					// get random index of selected gift range, based on the length of array

			this.setGift(this.giftList[mileageRange][randomIdx]);											// store the gift name in the gift variable
		}
		
		/**
		 * Method	: Getter
		 * Todo		: return each private field
		 */	
		public double getMileage() {
			return mileage;
		}
		public String getGift() {
			return gift;
		}
		
		/**
		 * Method	: Setter
		 * Todo		: set each private field with constraints 
		 */
		public void setMileage(double mileage) {
			this.mileage = mileage;
		}	
		public void setGift(String gift) {
			this.gift = gift;
		}		
	}
}
