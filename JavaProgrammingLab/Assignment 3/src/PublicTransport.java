/**
 *	Subject		: Java Programming
 *	Task		: Assignment 3
 * 	Class		: PublicTransport(Abstract)
 *	Objective	: Abstract Class of Payable
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public abstract class PublicTransport implements Payable{
	
	private String model;				// for storing model name of the transportation
	private double baseFare;			// for storing base fare of each transportation
	private double totalFare;			// for storing total fare of the tranportation usage
	
	/**
	 * Method	: Constructor
	 * Todo		: set model name, base fare of the tranportation
	 */	
	public PublicTransport(String model, double baseFare){
		this.setModel(model);
		this.setBaseFare(baseFare);
	}
	
	/**
	 * Method	: Abstract method
	 * Todo		: define abstract method in abstract class
	 */	
	public abstract void calculatePayment();
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public String getModel() {
		return model;
	}
	public double getBaseFare() {
		return baseFare;
	}
	public double getTotalFare() {
		return totalFare;
	}

	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setModel(String model) {
		if(model.length() > 0){
			this.model = model;
		}
		else{
			throw new IllegalArgumentException("Model name should be at least one character");
		}
	}
	public void setBaseFare(double baseFare) {
		if(baseFare >= 0){
			this.baseFare = baseFare;
		}
		else{
			throw new IllegalArgumentException("Base fare should be a positive value");
		}
	}
	public void setTotalFare(double totalFare) {
		if(totalFare >= 0){
			this.totalFare = totalFare;
		}
		else{
			throw new IllegalArgumentException("Total fare should be a positive value");
		}
	}
}
