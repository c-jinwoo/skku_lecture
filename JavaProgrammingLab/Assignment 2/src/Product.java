/**
 * Subject	: Java Programming
 * Task		: Assignment 2
 * Class	: Product
 * Objective: Super class of Apple products
 * SID		: 2020315798
 * Name		: Choi Jin Woo
 */

public class Product {
	
	private int basePrice = 0;							// for storing basePrice of each product : common field for each product
	private int totalCost = 0;							// for storing totalCost of user's purchase : common field for each product
	private String model = "";							// for storing model name : common field for each product
	private int quantity = 0;							// for storing how many items user purchased : common field for each product
		
	/**
	 * Method	: Constructor
	 * Todo		: set base price, model name, quantity of the purchase
	 */	
	public Product(int basePrice, String model, int quantity) {
		this.setBasePrice(basePrice);
		this.setTotalCost(basePrice);					// since total cost includes base price, initialize with the base value
		this.setModel(model);
		this.setQuantity(quantity);		
	}
	
	/**
	 * Method	: getUserSelection
	 * Todo		: print out the list of every extra cost user made
	 *  		  later overrided and implemented since each product has different extra options
	 */
	public void getUserSelection() {		
	}
	
	/**
	 * Method	: calculateCost
	 * Todo		: calculate total cost including extra cost user made, saving the value in 'totalCost'
	 *  		  later overrided and implemented since each product has different extra options
	 */
	public void calculateCost() {		
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public int getBasePrice() {
		return basePrice;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public String getModel() {
		return model;
	}
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setBasePrice(int basePrice) {
		if(basePrice > 0){
			this.basePrice = basePrice;			
		}
		else{
			throw new IllegalArgumentException("Price must be a positive value.");
		}
	}
	public void setTotalCost(int totalCost) {
		if(totalCost > 0){
			this.totalCost = totalCost;			
		}
		else{
			throw new IllegalArgumentException("Total Cost must be a positive value.");
		}
	}
	public void setModel(String model) {
		if(model.length() > 0) {
			this.model = model;			
		}
		else{
			throw new IllegalArgumentException("Model must be at least one character");
		}
	}
	public void setQuantity(int quantity) {
		if(quantity > 0){
			this.quantity = quantity;			
		}
		else{
			throw new IllegalArgumentException("Quantity Cost must be a positive value.");
		}
	}
}