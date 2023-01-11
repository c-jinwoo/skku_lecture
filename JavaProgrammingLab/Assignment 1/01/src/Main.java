/**
 * Java Programming
 * Assignment 1-01
 * 2020315798
 * Choi Jin Woo
 */

import java.util.Scanner;

public class Main{	
	public static void main(String[] args){
		String studentName = "";							// for storing student's name
		int dateOfBirth = 0;								// for storing student's date of birth	
		float gpa = 0.0f;									// for storing student's gpa
		String department = "";								// for storing student's department
		
		Scanner userInput = new Scanner(System.in);			// to use Scanner object for input
		
		System.out.print("Enter your name: ");				
		studentName = userInput.nextLine();
		
		System.out.print("Enter your date of birth: ");
		dateOfBirth = userInput.nextInt();
		
		System.out.print("Enter your cumulative GPA: ");
		gpa = userInput.nextFloat();
		
		userInput = new Scanner(System.in);					// re-allocate Scanner object to save another string type variable
		System.out.print("Enter your department: ");
		department = userInput.nextLine(); 

		Student studentObject = new Student(studentName, dateOfBirth, gpa, department);		// making object using parameters from user inputs
		
		System.out.println("===================");
		System.out.println("Student name: " + studentObject.getStudentName());
		System.out.println("Student's age: " + studentObject.getAge());
		System.out.println("Student's CGPA: " + studentObject.getGpa());
		System.out.println("Student's department: " + studentObject.getDepartment());
	}
}