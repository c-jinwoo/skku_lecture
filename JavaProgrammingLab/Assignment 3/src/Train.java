/**
 *	Subject		: Java Programming
 *	Task		: Assignment 3
 * 	Class		: Train
 *	Objective	: Sub Class of PublicTransport
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Train extends PublicTransport {
	Station station = null;			// later initialize station with Station class, which is 'has-a' relationship with Train
	
	/**
	 * Method	: Constructor
	 * Todo		: set model name, base fare by using super constructure
				  set fare per station, number of stations of the tranportation by using Station class
	 */
	public Train(String model, double baseFare, double farePerStation, int nStations){
		super(model, baseFare);
		this.station = new Station(farePerStation, nStations);
	}
	
	/**
	 * Method	: calculatePayment(Overrided)
	 * Todo		: calculate total fare by the given logic, store the value in totalFare variable in super class
	 */
	@Override
	public void calculatePayment(){
		if(this.station.getnStations() >= 5){
			super.setTotalFare(super.getBaseFare() + (this.station.getnStations() - 5) * this.station.getFarePerStation());
		}
		else{
			super.setTotalFare(super.getBaseFare());
		}
	}
}
