const MAXACCOUNTLENGTH = 16;
const MAXPINLENGTH = 4;
const ACCOUNTS = [
	// account number, pid
	["1234567890123450","1234"],
	["1234567890123451","5678"],
	["1234567890123452","0000"],
];

let currentAccount = -1;														// for storing index of loggined account
let pinAttempt = 4;																// for counting attempts of pid check
let depositTemp = 0;															// for storing temporary deposit amount
let withdrawTemp = 0;															// for storing temporary withdraw amount
let transferTemp = 0;															// for storing temporary transfer amount
let transferFromAccountTemp = 0;												// for storing temporary transfer from account
let transferToAccountTemp = 0;													// for storing temporary transfer to account
let accounts_hist = new Array();												// Date, Funds out, Funds in, Running balance
for(let i=0;i<ACCOUNTS.length;i++){
	accounts_hist.push(new Array(new Array(new Date(), 0, 0, 2000)));
}

document.addEventListener("DOMContentLoaded", function(){
	buttonSetting();	
});
function buttonSetting(){
	// Digit button
	let digit_btns = document.getElementsByClassName("num");
	[].forEach.call(digit_btns, function(_this){
		_this.addEventListener("click", function(){
			let inputObj = new Object();
			let curInputVal = "";
			
			if(isActive("login_page")){
				inputObj = document.getElementById("acnt_input");
				curInputVal = inputObj.value;

				if(curInputVal.length >= MAXACCOUNTLENGTH){
					return;														// exit when the length of the input value exceed 16
				}
			}
			else if(isActive("pin_page")){
				inputObj = document.getElementById("pin_input");
				curInputVal = inputObj.value;

				if(curInputVal.length >= MAXPINLENGTH){
					return;														// exit when the length of the input value exceed 4
				}
			}
			else if(isActive("deposit_page")){
				inputObj = document.getElementById("deposit_input");
				curInputVal = inputObj.value;				

				if(_this.classList.contains("nf")){
					if(curInputVal.length == 0){
						return;													// exit if the first letter is 0 or .
					}
				}
				if(_this.classList.contains("nm")){
					if(curInputVal.indexOf(".") != -1){
						return;													// exit if it already has .
					}
				}
			}
			else if(isActive("transfer_page")){
				inputObj = document.getElementById("transfer_input");
				curInputVal = inputObj.value;

				if(_this.classList.contains("nf")){
					if(curInputVal.length == 0){
						return;													// exit if the first letter is 0 or .
					}
				}
				if(_this.classList.contains("nm")){
					if(curInputVal.indexOf(".") != -1){
						return;													// exit if it already has .
					}
				}
			}
			
			inputObj.value = curInputVal + this.innerText;						// append currently clicked button's text to the new value
		});
	});

	// Enter button
	let enter_btn = document.getElementsByClassName("enter");
	[].forEach.call(enter_btn, function(_this){
		_this.addEventListener("click", function(){
			let inputObj = new Object();
			let curInputVal = "";
			let result = false;

			if(isActive("login_page")){
				inputObj = document.getElementById("acnt_input");
				curInputVal = inputObj.value;
				
				if(curInputVal.length == 16){
					for(let i=0;i<ACCOUNTS.length;i++){							// linear search through account info array
						if(ACCOUNTS[i][0] == curInputVal){
							result = true;
							currentAccount = i;									// to check currently login info
							break;
						}
					}
				}
				if(result){
					terminatePage("login_page");								// remove active class to hide display of acnt page
					executePage("pin_page");									// add active class to show display of pin page	
				}
				else{
					alert("That account number does not exist!");					
				}

				inputObj.value = "";											// reset input value for preventing bug
			}
			else if(isActive("pin_page")){
				inputObj = document.getElementById("pin_input");
				curInputVal = inputObj.value;
				
				if(curInputVal.length == 4){
					if(ACCOUNTS[currentAccount][1] == curInputVal){
						result = true;											// check if pin matches exact account
					}
				}
				if(result){
					terminatePage("pin_page");									// remove active class to hide display of pin page
					executePage("menu_page");									// add active class to show display of menu page	
				}
				else{
					if(pinAttempt > 0){
						alert("Incorrect PIN. Yoou have "+(pinAttempt--)+" attempts left");
					}
					else{		
						terminatePage("pin_page");
						executePage("login_page");
					}
					inputObj.value = "";
				}
			}
			else if(isActive("deposit_page")){
				terminatePage("deposit_page");
				executePage("deposit_confirm_page");				
			}
			else if(isActive("transfer_page")){
				if(checkException("transfer_page")){
					alert("Not enough information");
					return;
				}
				terminatePage("transfer_page");
				executePage("transfer_confirm_page");
			}
		});
	});
	
	// Del button
	let del_btn = document.getElementsByClassName("del");
	[].forEach.call(del_btn, function(_this){
		_this.addEventListener("click", function(){
			let inputObj = new Object();

			if(isActive("login_page")){
				inputObj = document.getElementById("acnt_input");
			}
			else if(isActive("pin_page")){
				inputObj = document.getElementById("pin_input");
			}
			else if(isActive("deposit_page")){
				inputObj = document.getElementById("deposit_input");
			}
			else if(isActive("transfer_page")){
				inputObj = document.getElementById("transfer_input");
			}

			let curInputVal = inputObj.value;
			let newInputVal = "";
			
			if(curInputVal.length > 0){
				for(let i=0;i<curInputVal.length-1;i++){
					newInputVal += curInputVal[i];								// copy first length-1 letters to new string
				}
			}

			inputObj.value = newInputVal;										// insert the string into input value
		});
	});
	
	// Enter button : Withdraw
	let wdenter_btn = document.getElementsByClassName("wdenter")[0];
	wdenter_btn.addEventListener("click", function(){
		withdrawTemp = parseInt(document.getElementById("withdraw_input").value);
		terminatePage("withdraw_page");
		executePage("withdraw_confirm_page");
	});

	// Arrow button : Withdraw
	let arrow_btns = document.getElementsByClassName("arrow");
	[].forEach.call(arrow_btns, function(_this, idx){
		_this.addEventListener("click", function(){
			let val = parseInt(document.getElementById("withdraw_input").value);

			if(idx == 0){
				val += 100;
			}
			else if(idx == 1){
				val += 20;
			}
			else if(idx == 2){
				val -= 20;
			}
			else if(idx == 3){
				val -= 100;
			}

			if(val < 0){
				val = 0;
			}

			document.getElementById("withdraw_input").value = val;
		});

		// mouse events
		"mouseenter mouseup".split(" ").forEach(function(e){
			_this.addEventListener(e, function(){
				if(idx == 0){
					_this.setAttribute("src", "./img/uparrow100copyhover.png");
				}
				else if(idx == 1){
					_this.setAttribute("src", "./img/uparrow20copyhover.png");
				}
				else if(idx == 2){
					_this.setAttribute("src", "./img/downarrow20copyhover.png");
				}
				else if(idx == 3){
					_this.setAttribute("src", "./img/downarrow100copyhover.png");
				}
			});
		});
		"mouseleave mousedown".split(" ").forEach(function(e){
			_this.addEventListener(e, function(){
				if(idx == 0){
					_this.setAttribute("src", "./img/uparrow100copy.png");
				}
				else if(idx == 1){
					_this.setAttribute("src", "./img/uparrow20copy.png");
				}
				else if(idx == 2){
					_this.setAttribute("src", "./img/downarrow20copy.png");
				}
				else if(idx == 3){
					_this.setAttribute("src", "./img/downarrow100copy.png");
				}
			});
		});
	});

	// Quick Link button
	let ql_btn = document.getElementsByClassName("ql");
	[].forEach.call(ql_btn, function(_this){
		_this.addEventListener("click", function(){
			withdrawTemp = parseInt(_this.innerText);
			terminatePage("withdraw_page");
			executePage("withdraw_confirm_page");
		});
	});

	// Menu button
	let menu_btn = document.getElementsByClassName("menu");
	[].forEach.call(menu_btn, function(_this, index){
		_this.addEventListener("click", function(){
			terminatePage("menu_page");

			if(index == 0){
				executePage("account_page");		
			}
			else if(index == 1){
				executePage("withdraw_page");	
			}
			else if(index == 2){
				executePage("deposit_page");	
			}
			else if(index == 3){
				executePage("transfer_page");	
			}
		});
	});

	// Yes button
	let yes_btn = document.getElementsByClassName("yes");
	[].forEach.call(yes_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("withdraw_confirm_page")){
				if(checkException("withdraw_confirm_page")){
					alert("Not enough money to withdraw");
					return;
				}
				terminatePage("withdraw_confirm_page");
				executePage("withdraw_result_page");
			}
			else if(isActive("deposit_confirm_page")){
				terminatePage("deposit_confirm_page");
				executePage("deposit_semi_result_page");
			}
			else if(isActive("transfer_confirm_page")){
				if(checkException("transfer_confirm_page")){
					alert("Not enough money to transfer");
					return;
				}
				terminatePage("transfer_confirm_page");
				executePage("transfer_result_page");
			}
		});
	});

	// No button
	let no_btn = document.getElementsByClassName("no");
	[].forEach.call(no_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("withdraw_confirm_page")){
				terminatePage("withdraw_confirm_page");
				executePage("withdraw_page");
			}
			else if(isActive("deposit_confirm_page")){
				terminatePage("deposit_confirm_page");
				executePage("deposit_page");
			}
			else if(isActive("transfer_confirm_page")){
				terminatePage("transfer_confirm_page");
				executePage("transfer_page");
			}
		});
	});

	// OK button
	let ok_btn = document.getElementsByClassName("ok");
	[].forEach.call(ok_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("deposit_semi_result_page")){
				terminatePage("deposit_semi_result_page");
				executePage("deposit_result_page");				
			}
		});
	});

	// Again button : Deposit, Withdraw, Transfer
	let again_btn = document.getElementsByClassName("again");
	[].forEach.call(again_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("withdraw_result_page")){
				terminatePage("withdraw_result_page");	
				executePage("withdraw_page");			
			}
			else if(isActive("deposit_result_page")){
				terminatePage("deposit_result_page");
				executePage("deposit_page");				
			}
			else if(isActive("transfer_result_page")){
				terminatePage("transfer_result_page");
				executePage("transfer_page");				
			}
		});
	});

	// Home button
	let home_btn = document.getElementsByClassName("home");
	[].forEach.call(home_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("withdraw_result_page")){
				terminatePage("withdraw_result_page");		
			}
			else if(isActive("deposit_result_page")){
				terminatePage("deposit_result_page");		
			}
			else if(isActive("transfer_result_page")){
				terminatePage("transfer_result_page");
			}
			executePage("menu_page");		
		});
	});

	// Return button
	let ret_btn = document.getElementsByClassName("return");
	[].forEach.call(ret_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("pin_page")){
				terminatePage("pin_page");
			}
			else if(isActive("menu_page")){
				terminatePage("menu_page");
			}
			else if(isActive("withdraw_result_page")){
				terminatePage("withdraw_result_page");		
			}
			else if(isActive("deposit_result_page")){
				terminatePage("deposit_result_page");
			}
			else if(isActive("transfer_result_page")){
				terminatePage("transfer_result_page");
			}
			executePage("login_page");
		});
	});

	// Back button
	let back_btn = document.getElementsByClassName("back");
	[].forEach.call(back_btn, function(_this){
		_this.addEventListener("click", function(){
			if(isActive("account_page")){
				terminatePage("account_page");
			}
			else if(isActive("withdraw_page")){
				terminatePage("withdraw_page");
			}
			else if(isActive("deposit_page")){
				terminatePage("deposit_page");
			}
			else if(isActive("transfer_page")){
				terminatePage("transfer_page");
			}
			executePage("menu_page");
		});
	});
}

/* Page Settings */
function setLoginPage(){
	document.getElementById("acnt_input").value = "";
}
function setPinPage(){
	pinAttempt = 4;
	document.getElementById("pin_input").value = "";
}
function setAccountPage(){
	let histArr = accounts_hist[currentAccount];
	let currentBalance = histArr[histArr.length-1][3];
	let appendHTML = "";
	appendHTML += "<h1 class='title'>Account Information</h1>";
	appendHTML += "<h3 class='subtitle'>Current Balance: $"+currentBalance+"</h3>";	
	document.getElementById("account_page").getElementsByClassName("header")[0].innerHTML = appendHTML;				// title append
		
	appendHTML = "";
	appendHTML += "<table>";
	appendHTML += "	<colgroup>";
	appendHTML += "		<col width='50%'>";
	appendHTML += "		<col width='15%'>";
	appendHTML += "		<col width='15%'>";
	appendHTML += "		<col width='20%'>";
	appendHTML += "	</colgroup>";
	appendHTML += "	<thead>";
	appendHTML += "		<th>Date</th><th>Funds Out</th><th>Funds In</th><th>Running Balance</th>";
	appendHTML += "	</thead>";
	appendHTML += "	<tbody>";
	for(let i=0;i<histArr.length;i++){
		appendHTML += "<tr>";
		appendHTML += "	<td>"+histArr[i][0]+"</td>";
		appendHTML += "	<td>"+histArr[i][1]+"</td>";
		appendHTML += "	<td>"+histArr[i][2]+"</td>";
		appendHTML += "	<td>"+histArr[i][3]+"</td>";
		appendHTML += "</tr>";
	}
	appendHTML += "	</tbody>";
	appendHTML += "</table>";
	document.getElementById("account_page").getElementsByClassName("table_wrapper")[0].innerHTML = appendHTML;		// append account history table
}
function setWithdrawPage(){
	document.getElementById("withdraw_input").value = 0;
}
function setWithdrawConfirmPage(){
	let appendHTML = "";
	appendHTML += "<h1 class='title'>Withdraw</h1>";
	appendHTML += "<h3 class='subtitle'>$"+withdrawTemp+"?</h3>";	
	document.getElementById("withdraw_confirm_page").getElementsByClassName("header")[0].innerHTML = appendHTML;	// append title
}
function setWithdrawResultPage(){
	let histArr = accounts_hist[currentAccount];
	let currentBalance = histArr[histArr.length-1][3];
	currentBalance -= withdrawTemp;
	accounts_hist[currentAccount].push(new Array(new Date(), withdrawTemp, 0, currentBalance));						// insert history into account array
	
	withdrawTemp = 0;	
}
function setDepositPage(){
	document.getElementById("deposit_input").value = "";
}
function setDepositConfirmPage(){	
	depositTemp = parseFloat(document.getElementById("deposit_input").value);										// save to global variable depositTemp
	
	let appendHTML = "";
	appendHTML += "<h1 class='title'>Are you sure you want to deposit the amount specified?</h1>";
	appendHTML += "<h3 class='subtitle'>$"+depositTemp+"?</h3>";	
	document.getElementById("deposit_confirm_page").getElementsByClassName("header")[0].innerHTML = appendHTML;		// append title
}
function setDepositResultPage(){	
	let histArr = accounts_hist[currentAccount];
	let currentBalance = histArr[histArr.length-1][3];
	currentBalance += depositTemp;
	accounts_hist[currentAccount].push(new Array(new Date(), 0, depositTemp, currentBalance));						// insert history into account array
	
	depositTemp = 0;																								// reset depositTemp
}
function setTransferPage(){
	document.getElementById("transfer_input").value = "";
		
	let from_select = document.getElementById("from_select");
	let to_select	= document.getElementById("to_select");
	let from_appendHTML = "<option hidden='' value='-1'>Choose an Account</option>";
	let to_appendHTML	= "<option hidden='' value='-1'>Choose an Account</option>";

	for(let i=0;i<ACCOUNTS.length;i++){																				// append account options to select tag
		if(i == currentAccount){
			from_appendHTML += "<option value='"+i+"'>"+ACCOUNTS[i][0]+"</option>";
		}
		else{
			to_appendHTML += "<option value='"+i+"'>"+ACCOUNTS[i][0]+"</option>";
		}
	}

	from_select.innerHTML = from_appendHTML;
	to_select.innerHTML = to_appendHTML;
}
function setTransferConfirmPage(){
	transferTemp = parseFloat(document.getElementById("transfer_input").value);										// save to global variable transferTemp
	transferFromAccountTemp = document.getElementById("from_select").value;											// save to global variable transferFromAccountTemp
	transferToAccountTemp = document.getElementById("to_select").value;												// save to global variable transferToAccountTemp

	let appendHTML = "";
	appendHTML += "<h1 class='title'>Transfer?</h1>";
	appendHTML += "<h3 class='subtitle'>$"+transferTemp+"</h3>";
	appendHTML += "<h3 class='subtitle'>From "+ACCOUNTS[transferFromAccountTemp][0]+"</h3>";
	appendHTML += "<h3 class='subtitle'>To "+ACCOUNTS[transferToAccountTemp][0]+"</h3>";	
	document.getElementById("transfer_confirm_page").getElementsByClassName("header")[0].innerHTML = appendHTML;	// append title
}
function setTransferResultPage(){
	let histArr = new Array();
	let currentBalance = 0;
	
	// from
	histArr = accounts_hist[transferFromAccountTemp];
	currentBalance = histArr[histArr.length-1][3];
	currentBalance -= transferTemp;
	accounts_hist[transferFromAccountTemp].push(new Array(new Date(), transferTemp, 0, currentBalance));			// insert history into from account array
	
	//to
	histArr = accounts_hist[transferToAccountTemp];
	currentBalance = histArr[histArr.length-1][3];
	currentBalance += transferTemp;
	accounts_hist[transferToAccountTemp].push(new Array(new Date(), 0, transferTemp, currentBalance));				// insert history into to account array

	transferTemp = 0;																								// reset transferTemp
	transferFromAccountTemp = 0;																					// reset transferFromAccountTemp
	transferToAccountTemp = 0;																						// reset transferToAccountTemp
}

/* Common Utils */
function isActive(obj){																								// function : return whether the page is active
	return document.getElementById(obj).classList.contains("active");
}
function executePage(obj){																							// function : execute page with page setting func
	if(obj == "login_page"){
		setLoginPage();
	}
	else if(obj == "pin_page"){
		setPinPage();
	}
	else if(obj == "account_page"){
		setAccountPage();
	}
	else if(obj == "withdraw_page"){
		setWithdrawPage();
	}
	else if(obj == "withdraw_confirm_page"){
		setWithdrawConfirmPage();
	}
	else if(obj == "withdraw_result_page"){
		setWithdrawResultPage();
	}
	else if(obj == "deposit_page"){
		setDepositPage();
	}
	else if(obj == "deposit_confirm_page"){
		setDepositConfirmPage();
	}
	else if(obj == "deposit_result_page"){
		setDepositResultPage();
	}
	else if(obj == "transfer_page"){
		setTransferPage();
	}
	else if(obj == "transfer_confirm_page"){
		setTransferConfirmPage();
	}
	else if(obj == "transfer_result_page"){
		setTransferResultPage();
	}
	document.getElementById(obj).classList.add("active");
}
function terminatePage(obj){																						// function : deactivate page
	document.getElementById(obj).classList.remove("active");
}
function checkException(obj){																						// function : return whether there is an exception with withdrawal, transfer
	let result = true;

	if(obj == "withdraw_confirm_page"){
		let histArr = accounts_hist[currentAccount];
		let currentBalance = histArr[histArr.length-1][3];
		if(currentBalance >= withdrawTemp){
			result = false;
		}
	}
	else if(obj == "transfer_page"){
		if(document.getElementById("from_select").value != -1 && document.getElementById("to_select").value != -1	// empty account info
			&& document.getElementById("transfer_input").value.length > 0){											// empty input	
			result = false;
		}
	}
	else if(obj == "transfer_confirm_page"){
		let histArr = accounts_hist[transferFromAccountTemp];
		let currentBalance = histArr[histArr.length-1][3];
		if(currentBalance >= transferTemp){
			result = false;
		}
	}
	
	return result;
}