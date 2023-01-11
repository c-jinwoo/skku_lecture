/**
 * Java Programming
 * Assignment 1-01
 * 2020315798
 * Choi Jin Woo
 */

public class Student {
	final int CURRENT_YEAR = 2022;			// for storing constant int for this year
	private String name;					// for storing student's name(private to prevent direct allocation)
	private int age;						// for storing student's age(private to prevent direct allocation)
	private float gpa;						// for storing student's gpa(private to prevent direct allocation)
	private String department;				// for storing student's department(private to prevent direct allocation)
	
	/**
	 * Constructor
	 * - uses setter method to initialize the student's info
	 */
	public Student(String name, int date, float gpa, String department){
		setStudentName(name);
		setAge(date);
		setGpa(gpa);
		setDepartment(department);
	}
	
	/**
	 * Setter methods
	 * - variables can be set either by the constructor method or these following methods
	 */
	public void setStudentName(String name){
		this.name = name;
	}
	public void setAge(int date){
		this.age = CURRENT_YEAR - date;		// age calculation by subtracting date of birth from current year
	}
	public void setGpa(float gpa){
		this.gpa = gpa;
	}
	public void setDepartment(String department){
		this.department = department;
	}

	/**
	 * Getter methods
	 * - variables can be called by these following methods
	 */
	public String getStudentName(){
		return this.name;
	}	
	public int getAge(){
		return this.age;
	}	
	public float getGpa(){
		return this.gpa;
	}	
	public String getDepartment(){
		return this.department;
	}
}