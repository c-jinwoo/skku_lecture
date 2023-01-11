/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Extra
 *	Objective	: Sub class of Food - Ingredients
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Ingredients extends Food {

	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int LETTUCE = 0;			// type 1
	private static final int TOMATOES = 0;			// type 2
	private static final int CUCUMBERS = 50;		// type 3
	private static final int OLIVES = 50;			// type 4
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Ingredients
	 */
	public Ingredients(int type) {
		super(type);
	}

	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total charge
	 */
	@Override
	public void calculateCost(){
		if(super.getType() == 1){
			super.setCost(super.getCost() + getLettuce());
		}
		else if(super.getType() == 2){
			super.setCost(super.getCost() + getTomatoes());
		}
		else if(super.getType() == 3){
			super.setCost(super.getCost() + getCucumbers());
		}
		else if(super.getType() == 4){
			super.setCost(super.getCost() + getOlives());
		}
	}	
		
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */		
	public static int getLettuce() {
		return LETTUCE;
	}
	
	public static int getTomatoes() {
		return TOMATOES;
	}
	
	public static int getCucumbers() {
		return CUCUMBERS;
	}
	
	public static int getOlives() {
		return OLIVES;
	}
}
