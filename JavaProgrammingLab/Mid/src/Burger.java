/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Extra
 *	Objective	: Sub class of Food - Burger
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public class Burger extends Food {

	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int EGGMAYO = 4000;			// type 1
	private static final int CHICKENTIKKA = 5000;		// type 2
	private static final int CHICKENHAM = 5000;			// type 3
	private static final int ROASTEDCHICKEN = 5500;		// type 4
	
	private static final int MAXSIZE_INGREDIENT = 4;	// max size of the ingredients(4)
		
	private Cheese chesseObject = null;
	private Ingredients ingredients[];
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Burger, cheese, ingredients
	 */
	public Burger(int burgerType, int cheeseType, int[] ingredientType) {
		super(burgerType);
		chesseObject = new Cheese(cheeseType);		
		ingredients = new Ingredients[MAXSIZE_INGREDIENT];
		
		for(int i=0;i<MAXSIZE_INGREDIENT;i++){
			if(ingredientType[i] != 0){
				ingredients[i] = new Ingredients(ingredientType[i]);
			}
		}
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total charge
	 */
	@Override
	public void calculateCost(){
		if(super.getType() == 1){
			super.setCost(super.getCost() + getEggmayo());
		}
		else if(super.getType() == 2){
			super.setCost(super.getCost() + getChickentikka());
		}
		else if(super.getType() == 3){
			super.setCost(super.getCost() + getChickenham());
		}
		else if(super.getType() == 4){
			super.setCost(super.getCost() + getRoastedchicken());
		}
		
		this.chesseObject.calculateCost();
		
		for(int i=0;i<MAXSIZE_INGREDIENT;i++){
			if(this.ingredients[i] != null){
				this.ingredients[i].calculateCost();
			}
		}
		
		// Sum up total charge for burger including cheese and ingredients
		int burgerTotal = 0;
		burgerTotal += super.getCost();
		burgerTotal += this.chesseObject.getCost();
		for(int i=0;i<MAXSIZE_INGREDIENT;i++){
			if(this.ingredients[i] != null){
				burgerTotal += this.ingredients[i].getCost();
			}
		}
		super.setCost(burgerTotal);
		
		System.out.println("BURGERTOTAL" + super.getCost());
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public static int getEggmayo() {
		return EGGMAYO;
	}
	
	public static int getChickentikka() {
		return CHICKENTIKKA;
	}
	
	public static int getChickenham() {
		return CHICKENHAM;
	}
	
	public static int getRoastedchicken() {
		return ROASTEDCHICKEN;
	}
}
