/**
 *	Subject		: Java Programming
 *	Task		: Assignment 3
 * 	Class		: Station
 *	Objective	: Sub Class of Bus and Train
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Station {
	private double farePerStation;		// for storing fare per station using Bus or Train
	private int nStations;				// for storing number of stations using Bus or Train
	
	/**
	 * Method	: Constructor
	 * Todo		: set fare per station, number of stations of the tranportation
	 */
	public Station(double farePerStation, int nStations){
		this.setFarePerStation(farePerStation);
		this.setnStations(nStations);
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public double getFarePerStation() {
		return farePerStation;
	}
	public int getnStations() {
		return nStations;
	}

	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setFarePerStation(double farePerStation) {
		if(farePerStation >= 0){
			this.farePerStation = farePerStation;
		}
		else{
			throw new IllegalArgumentException("Fare per station should be a positive value");
		}
	}
	public void setnStations(int nStations) {
		if(nStations >= 0){
			this.nStations = nStations;
		}
		else{
			throw new IllegalArgumentException("Station number should be a positive value");
		}
	}
}
