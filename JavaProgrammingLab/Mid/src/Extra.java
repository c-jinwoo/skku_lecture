/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Extra
 *	Objective	: Sub class of Food - Extra
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Extra extends Food {

	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int COOKIES = 1500;			// type 1
	private static final int FRENCHFRIES = 1300;		// type 2
	private static final int CHIPS = 1700;				// type 3
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Extra menu
	 */
	public Extra(int type) {
		super(type);
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total charge
	 */
	@Override
	public void calculateCost(){
		if(super.getType() == 1){
			super.setCost(super.getCost() + getCookies());
		}
		else if(super.getType() == 2){
			super.setCost(super.getCost() + getFrenchfries());
		}
		else if(super.getType() == 3){
			super.setCost(super.getCost() + getChips());
		}		
		System.out.println("@@@ETRATOTAL" + super.getCost());
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public static int getCookies() {
		return COOKIES;
	}
	
	public static int getFrenchfries() {
		return FRENCHFRIES;
	}
	
	public static int getChips() {
		return CHIPS;
	}
}
