package service.Item;

public class MovieItem extends Product {
	
	private int age;
	private int duration;
	
	public MovieItem(int index, String name, int age, int duration) {
		super(index, name);
		this.setAge(age);
		this.setDuration(duration);
	}
	
	/* Getter Method */
	public int getAge() {
		return age;
	}
	public int getDuration() {
		return duration;
	}

	/* Setter Method */
	public void setAge(int age) {
		this.age = age;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
