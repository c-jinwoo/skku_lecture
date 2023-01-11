/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Class		: SessionManager
 *	Objective	: Store session info
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

package util;

public class SessionManager {
	
	private static int sessionUserIdx;
	private static String sessionUserName;
	private static int sessionUserAge;
	
	/* Getter Method */
	public static int getSessionUserIdx(){
		return sessionUserIdx;
	}
	public static String getSessionUserName(){
		return sessionUserName;
	}
	public static int getSessionUserAge(){
		return sessionUserAge;
	}
	
	/* Setter Method */
	public static void setSessionUserIdx(int idx){
		sessionUserIdx = idx;
	}
	public static void setSessionUserName(String Name){
		sessionUserName = Name;
	}
	public static void setSessionUserAge(int age){
		sessionUserAge = age;
	}
}