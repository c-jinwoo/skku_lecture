/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: UserImpl
 *	Objective	: Execute Login(Service Implement)
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

package service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import service.User;
import util.SessionManager;

public class UserImpl implements User {
	
	@Override
	/* SELECT USER DB for login */
	public String executeLogin(String inputID, String inputPW, String Type){		
		ArrayList<Boolean> conditions = new ArrayList<Boolean>();
		String result = "";
		
		try {
			FileInputStream fileObject = new FileInputStream("./db/USER.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				conditions = new ArrayList<Boolean>();
				
				String infoStr = fileScanner.nextLine();				
				String[] info = infoStr.split(",");				
				String dbUserID = info[1].split(":")[1];									// split ID part from USER DB
				String dbUserPW = info[2].split(":")[1];									// split PW part from USER DB
				String dbUserType = info[5].split(":")[1];									// split TYPE part from USER DB				
				
				if(dbUserID.equals(inputID)){
					conditions.add(true);													// add true to ArrayList if ID matches
				}
				if(dbUserPW.equals(inputPW)){
					conditions.add(true);													// add true to ArrayList if PW matches			
				}
				
				if(Type.equals("user")){
					if(dbUserType.equals("user")){
						conditions.add(true);
					}
				}
				else if(Type.equals("admin")){
					if(dbUserType.equals("admin")){
						conditions.add(true);
					}
				}
				result = (conditions.size() < 3) ? "Login Fail" : "Login Success";			// only success if all 3 match
				
				if(conditions.size() >= 3){
					setSession(infoStr);
					break;
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	/* CREATE user to USER DB */
	public String executeSubmit(String inputID, String inputPW, String inputPWCK, String inputNM, String inputAGE){
		
		String result = "";
		
		try {
			FileOutputStream fileObject = new FileOutputStream("./db/USER.txt",true);
			PrintWriter fileWriter = new PrintWriter(fileObject);
			
			int userCnt = getUserCnt();														// get last user index
			
			String line = "";																// concatenate input line
			line += "IDX:"+userCnt+",";
			line += "ID:"+inputID+",";
			line += "PW:"+inputPW+",";
			line += "NAME:"+inputNM+",";
			line += "AGE:"+inputAGE+",";
			line += "TYPE:user";
				
			if(inputID.length() == 0){														// check if ID is empty
				result = "Please enter user ID";				
			}
			else if(inputPW.length() == 0){													// check if PW is empty
				result = "Please enter user PW";	
			}
			else if(inputPWCK.length() == 0){												// check if PW CK is empty
				result = "Please enter user PW again";					
			}
			else if(inputNM.length() == 0){													// check if Name is empty
				result = "Please enter user Name";					
			}
			else if(inputAGE.length() == 0){												// check if AGE is empty
				result = "Please enter user Age";					
			}
			else{		
				if(checkIfDupl(inputID)){
					result = "Same ID exists";												// check if ID duplicates		
				}
				else{
					if(inputPW.equals(inputPWCK)){											// register success if PW/PW CK equals
						fileWriter.write("\n");
						fileWriter.write(line);
						result = "Success";
					}
					else{
						result = "Please check a password";				
					}
				}
			}
			
			fileWriter.close();
			fileObject.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	/* SELECT user ID from USER DB for duplication */ 
	public Boolean checkIfDupl(String inputID){
		boolean isDupl = false;
		
		try {
			FileInputStream fileObject = new FileInputStream("./db/USER.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				if(fileScanner.nextLine().length() > 0){
					String[] info = fileScanner.nextLine().split(",");				
					String dbUserID = info[1].split(":")[1];								// split ID part from USER DB
					
					if(dbUserID.equals(inputID)){
						isDupl = true;
						break;
					}					
				}
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return isDupl;
	}
	
	@Override
	/* SELECT user count from USER DB */ 
	public Integer getUserCnt(){
		int count = 0;		
		try {
			FileInputStream fileObject = new FileInputStream("./db/USER.txt");
			Scanner fileScanner = new Scanner(fileObject);
			
			while(fileScanner.hasNext()){
				if(fileScanner.nextLine().length() > 0){
					count++;
				}
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	/* set current login user idx to session */
	public void setSession(String info){
		SessionManager.setSessionUserIdx(Integer.parseInt(info.split(",")[0].split(":")[1]));
		SessionManager.setSessionUserName(info.split(",")[3].split(":")[1]);
		SessionManager.setSessionUserAge(Integer.parseInt(info.split(",")[4].split(":")[1]));
	}
}
