/**
 * Subject	: Java Programming
 * Task		: Assignment 2
 * Class	: AirPods
 * Objective: Sub class of Apple products - AirPods
 * SID		: 2020315798
 * Name		: Choi Jin Woo
 */

public class AirPods extends Product {
	
	/**
	 * Fields	: constant values of every extra costs
	 */
	private static final int EXTRA_CHARGE_MODEL_AIRPODS3 = 0;
	private static final int EXTRA_CHARGE_MODEL_AIRPODSPRO = 100;
	private static final int EXTRA_CHARGE_MODEL_AIRPODSMAX = 200;
	
	/**
	 * Method	: Constructor
	 * Todo		: set base price, model name, quantity of the purchase by calling the super constructor
	 */	
	public AirPods(int basePrice, String model, int quantity) {
		super(basePrice, model, quantity);
	}
	
	/**
	 * Method	: getUserSelection(Overrided)
	 * Todo		: print out the list of every extra cost user made
	 */
	@Override
	public void getUserSelection() {
		if(super.getBasePrice() < super.getTotalCost()) {
			System.out.println("===== check =====");						// if user's total cost is bigger than base price, there's at least one extra option
		}
		
		if(super.getModel().equals("AirPodsPro")) {
			System.out.println("Model (" + super.getModel() + ") ----- +$" + EXTRA_CHARGE_MODEL_AIRPODSPRO);
		}		
		else if(super.getModel().equals("AirPodsMax")) {
			System.out.println("Model (" + super.getModel() + ") ----- +$" + EXTRA_CHARGE_MODEL_AIRPODSMAX);
		}
	}
	
	/**
	 * Method	: calculateCost(Overrided)
	 * Todo		: calculate total cost including extra cost user made, saving the value in 'totalCost'
	 */
	@Override
	public void calculateCost() {		
		if(super.getModel().equals("AirPods3")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MODEL_AIRPODS3);
		}
		else if(super.getModel().equals("AirPodsPro")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MODEL_AIRPODSPRO);
		}
		else if(super.getModel().equals("AirPodsMax")) {
			super.setTotalCost(super.getTotalCost() + EXTRA_CHARGE_MODEL_AIRPODSMAX);
		}
	}
	
	/**
	 * Method	: Setter
	 * Todo		: set each private field with constraints 
	 */
	@Override
	public void setModel(String model) {
		if(model.equals("AirPods3") || model.equals("AirPodsPro") || model.equals("AirPodsMax")) {
			super.setModel(model);
		}
		else {
			throw new IllegalArgumentException("No such model is provided");
		}		
	}	
}