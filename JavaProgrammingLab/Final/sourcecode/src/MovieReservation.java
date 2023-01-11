public class MovieReservation{

	private String date;
	private String seat;
	private String type;
	
	/* Constructor */
	public MovieReservation(int price, String name) {
		//super(name);
	}

	/* Getter Method */
	public String getDate() {
		return this.date;
	}
	
	public String getSeat() {
		return this.seat;
	}
	
	public String getType() {
		return this.type;
	}

	/* Setter Method */
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setSeat(String seat) {
		this.seat = seat;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}
