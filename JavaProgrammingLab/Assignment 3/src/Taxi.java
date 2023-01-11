/**
 *	Subject		: Java Programming
 *	Task		: Assignment 3
 * 	Class		: Taxi
 *	Objective	: Sub Class of PublicTransport
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Taxi extends PublicTransport {
	private double farePerKm;		// for storing fare per KM using Taxi
	private double distance;		// for storing distance using Taxi
	
	/**
	 * Method	: Constructor
	 * Todo		: set model name, base fare, farePerKm, distance of the tranportation
	 */	
	public Taxi(String model, double baseFare, double farePerKm, double distance){
		super(model, baseFare);
		
		this.setFarePerKm(farePerKm);
		this.setDistance(distance);
	}
	
	/**
	 * Method	: calculatePayment(Overrided)
	 * Todo		: calculate total fare by the given logic, store the value in totalFare variable in super class
	 */
	@Override
	public void calculatePayment(){
		super.setTotalFare(super.getBaseFare() + this.getDistance() * this.getFarePerKm());
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public double getFarePerKm() {
		return farePerKm;
	}
	public double getDistance() {
		return distance;
	}

	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setFarePerKm(double farePerKm) {
		if(farePerKm >= 0){
			this.farePerKm = farePerKm;
		}
		else{
			throw new IllegalArgumentException("Fare per KM should be a positive value");
		}
	}
	public void setDistance(double distance) {
		if(distance >= 0){
			this.distance = distance;
		}
		else{
			throw new IllegalArgumentException("Distance should be a positive value");
		}
	}
}
