/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Extra
 *	Objective	: Sub class of Food - Beverage
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Beverage extends Food {

	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int COLA = 1000;				// type 1
	private static final int FANTA = 1100;				// type 2
	private static final int CHILSUNGCIDER = 900;		// type 3
	private static final int ZEROCOLA = 1200;			// type 4
	private static final int COFFEE = 2000;				// type 5

	private boolean ice = true;
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Beverage
	 */
	public Beverage(int type, boolean ice) {
		super(type);
		this.setIce(ice);
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total charge
	 */
	@Override
	public void calculateCost(){
		if(super.getType() == 1){
			super.setCost(super.getCost() + getCola());
		}
		else if(super.getType() == 2){
			super.setCost(super.getCost() + getFanta());
		}
		else if(super.getType() == 3){
			super.setCost(super.getCost() + getChilsungcider());
		}
		else if(super.getType() == 4){
			super.setCost(super.getCost() + getZerocola());
		}
		else if(super.getType() == 5){
			super.setCost(super.getCost() + getCoffee());
		}
		System.out.println("@@@BEVATOTAL" + super.getCost());
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public boolean isIce() {
		return ice;
	}
	
	public static int getCola() {
		return COLA;
	}
	
	public static int getFanta() {
		return FANTA;
	}
	
	public static int getChilsungcider() {
		return CHILSUNGCIDER;
	}
	
	public static int getZerocola() {
		return ZEROCOLA;
	}
	
	public static int getCoffee() {
		return COFFEE;
	}
	
	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setIce(boolean ice) {
		this.ice = ice;
	}
}
