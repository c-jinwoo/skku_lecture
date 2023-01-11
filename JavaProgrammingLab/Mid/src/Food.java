/**
 *	Subject		: Java Programming
 *	Task		: Midterm
 * 	Class		: Food(Abstract)
 *	Objective	: Abstract Class of Payment
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

public abstract class Food implements Payment {
	
	private int type = 0;
	private int cost = 0;
	
	/**
	 * Method	: Constructor
	 * Todo		: set type of the Food
	 */
	public Food(int type) {
		this.setType(type);
	}
	
	/**
	 * Method	: Abstract method
	 * Todo		: define abstract method in abstract class
	 */	
	public abstract void calculateCost();
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public int getType() {
		return this.type;
	}	
	
	public int getCost() {
		return this.cost;
	}
	
	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	public void setType(int type) {
		if(type >= 0){
			this.type = type;
		}
		else{
			throw new IllegalArgumentException("Cost should be a positive number.");
		}
	}

	public void setCost(int cost) {
		if(cost >= 0){
			this.cost = cost;
		}
		else{
			throw new IllegalArgumentException("Cost should be a positive number.");
		}
	}
}
