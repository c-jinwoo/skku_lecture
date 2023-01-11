/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Extra
 *	Objective	: Sub class of Food - Cheese
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Cheese extends Food {

	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int AMERICANCHEESE = 0;		// type 1
	private static final int SWISS = 100;				// type 2
	private static final int CHEDDAR = 0;				// type 3
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Cheese
	 */
	public Cheese(int type) {
		super(type);		
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total charge
	 */
	@Override
	public void calculateCost(){
		if(super.getType() == 1){
			super.setCost(super.getCost() + getAmericancheese());
		}
		else if(super.getType() == 2){
			super.setCost(super.getCost() + getSwiss());
		}
		else if(super.getType() == 3){
			super.setCost(super.getCost() + getCheddar());
		}
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public static int getAmericancheese() {
		return AMERICANCHEESE;
	}
	
	public static int getSwiss() {
		return SWISS;
	}
	
	public static int getCheddar() {
		return CHEDDAR;
	}
}
