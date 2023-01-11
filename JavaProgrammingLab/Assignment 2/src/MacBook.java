/**
 * Subject	: Java Programming
 * Task		: Assignment 2
 * Class	: MacBook
 * Objective: Sub class of Apple products - MacBook
 * SID		: 2020315798
 * Name		: Choi Jin Woo
 */

public class MacBook extends Product {
	
	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int EXTRA_CHARGE_MODEL_AIR = 0;
	private static final int EXTRA_CHARGE_MODEL_PRO = 200;
	private static final int EXTRA_CHARGE_SIZE_13 = 0;
	private static final int EXTRA_CHARGE_SIZE_15 = 200;
	private static final int EXTRA_CHARGE_COLOR_BASE = 0;
	private static final int EXTRA_CHARGE_COLOR_SILVER = 10;
	private static final int EXTRA_CHARGE_COLOR_GOLD = 20;
	private static final int EXTRA_CHARGE_MEMORY_128 = 0;
	private static final int EXTRA_CHARGE_MEMORY_256 = 100;
	private static final int EXTRA_CHARGE_MEMORY_512 = 300;
	
	private int size = 0;										// for storing size option for MacBook(13,15)
	private String color = "";									// for storing color option for MacBook(base, Silver, Gold)
	private int memory = 0;										// for storing memory option for MacBook(128, 256, 512)
	
	/**
	 * Method	: Constructor
	 * Todo		: set base price, model name, quantity of the purchase by calling the super constructor
	 * 			  set size, color, memory by calling setter method
	 */	
	public MacBook(int basePrice, String model, int quantity, String color, int memory, int size) {
		super(basePrice, model, quantity);

		this.setSize(size);
		this.setColor(color);
		this.setMemory(memory);
	}
	
	/**
	 * Method	: getUserSelection(Overrided)
	 * Todo		: print out the list of every extra cost user made
	 */
	@Override
	public void getUserSelection() {
		if(super.getBasePrice() < super.getTotalCost()) {
			System.out.println("===== check =====");			// if user's total cost is bigger than base price, there's at least one extra option
		}
		
		if(super.getModel().equals("Pro")) {
			System.out.println("Model (" + super.getModel() + ") ----- +$" + EXTRA_CHARGE_MODEL_PRO);
		}		

		if(this.getSize() == 15) {
			System.out.println("Size (" + this.getSize() + ") ----- +$" + EXTRA_CHARGE_SIZE_15);
		}	
		
		if(this.getColor().equals("Silver")) {
			System.out.println("Color (" + this.getColor() + ") ----- +$" + EXTRA_CHARGE_COLOR_SILVER);
		}	
		else if(this.getColor().equals("Gold")) {
			System.out.println("Color (" + this.getColor() + ") ----- +$" + EXTRA_CHARGE_COLOR_GOLD);
		}	

		if(this.getMemory() == 256) {
			System.out.println("Memory (" + this.getMemory() + ") ----- +$" + EXTRA_CHARGE_MEMORY_256);
		}			
		else if(this.getMemory() == 512) {
			System.out.println("Memory (" + this.getMemory() + ") ----- +$" + EXTRA_CHARGE_MEMORY_512);
		}	
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total cost including extra cost user made, saving the value in 'totalCost'
	 */
	@Override
	public void calculateCost() {	
		if(super.getModel().equals("Air")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MODEL_AIR);
		}
		else if(super.getModel().equals("Pro")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MODEL_PRO);
		}	

		if(this.getSize() == 13) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_SIZE_13);
		}
		else if(this.getSize() == 15) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_SIZE_15);
		}	
		
		if(this.getColor().equals("base")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_COLOR_BASE);
		}
		else if(this.getColor().equals("Silver")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_COLOR_SILVER);
		}	
		else if(this.getColor().equals("Gold")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_COLOR_GOLD);
		}
		
		if(this.getMemory() == 128) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MEMORY_128);
		}
		else if(this.getMemory() == 256) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MEMORY_256);
		}
		else if(this.getMemory() == 512) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MEMORY_512);
		}
	}
	
	/**
	 * Method	: Getter
	 * Todo		: return each private field
	 */	
	public int getSize() {
		return size;
	}

	public String getColor() {
		return color;
	}

	public int getMemory() {
		return memory;
	}
	
	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	@Override
	public void setModel(String model) {
		if(model.equals("Air") || model.equals("Pro")) {
			super.setModel(model);
		} 
		else {
			throw new IllegalArgumentException("No such model is provided");
		}
	}

	public void setSize(int size) {
		if(size == 13 || size == 15) {
			this.size = size;
		} 
		else {
			throw new IllegalArgumentException("No such Size is provided");
		}
	}

	public void setColor(String color) {
		if(color.equals("base") || color.equals("Gold") || color.equals("Silver")) {
			this.color = color;
		} 
		else {
			throw new IllegalArgumentException("No such Color is provided");
		}
	}

	public void setMemory(int memory) {
		if(memory == 128 || memory == 256 || memory == 512) {
			this.memory = memory;
		} 
		else {
			throw new IllegalArgumentException("No such Memory is provided");
		}
	}
}
