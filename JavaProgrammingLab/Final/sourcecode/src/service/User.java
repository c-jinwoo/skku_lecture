/**
 *	Subject		: Java Programming
 *	Task		: Final
 * 	Interface	: UserLogin
 *	Objective	: Execute Login(Service)
 *	SID			: 2020315798
 *	Name		: Choi Jin Woo
 */

package service;

public interface User {
	public String executeLogin(String inputID, String inputPW, String Type);
	
	public String executeSubmit(String inputID, String inputPW, String inputPWCK, String inputNM, String inputAGE);
	
	public Boolean checkIfDupl(String inputID);
	
	public Integer getUserCnt();
}
