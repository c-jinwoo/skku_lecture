package service.Item;

public class Product {

	private int index;
	private String name;
	
	/* Constructor */
	public Product(int index, String name) {
		this.setIndex(index);
		this.setName(name);
	}

	/* Getter Methods */
	public int getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}

	/* Setter Methods */
	public void setIndex(int index) {
		this.index = index;
	}	
	public void setName(String name) {
		this.name = name;
	}	
}
