let loginFinishAppendHTML = '<h2 class="ls-05">You are logged in.</h2>';
let signupFinishAppendHTML = '<h2 class="ls-05">You are signed up.</h2>';

let loginAppendHTML = "";
loginAppendHTML += '<h2 class="ls-05">Login</h2>';
loginAppendHTML += '<h4 class="ls-05 lg">Enter user name and password:</h4>';						
loginAppendHTML += '<div class="input_wrapper">';
loginAppendHTML += '	<label class="" for="email">Email</label>';
loginAppendHTML += '	<input class="i_text" type="text" id="email" name="email" placeholder="Email" data-label="email"/>';
loginAppendHTML += '	<div class="invalid-tooltip"></div>';
loginAppendHTML += '</div>';
loginAppendHTML += '<div class="input_wrapper">';
loginAppendHTML += '	<label class="" for="password">Password</label>';
loginAppendHTML += '	<input class="i_text" type="password" id="password" name="password" placeholder="Password" data-label="password"/>';
loginAppendHTML += '	<div class="invalid-tooltip"></div>';
loginAppendHTML += '</div>';
loginAppendHTML += '<button class="cp login_submit" onclick="login_validCheck();">Login</button>';

let signupAppendHTML = "";
signupAppendHTML += '<h2 class="ls-05">Sign up</h2>';
signupAppendHTML += '<h4 class="ls-05">Fill in the form:</h4>';
signupAppendHTML += '<div class="input_wrapper">';
signupAppendHTML += '	<label class="" for="su_fn">First name</label>';
signupAppendHTML += '	<input class="i_text" type="text" id="su_fn" name="su_fn" placeholder="First name" data-label="first name"/>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';
signupAppendHTML += '</div>';
signupAppendHTML += '<div class="input_wrapper">';
signupAppendHTML += '	<label class="" for="su_ln">Last name</label>';
signupAppendHTML += '	<input class="i_text" type="text" id="su_ln" name="su_ln" placeholder="Last name" data-label="last name"/>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';
signupAppendHTML += '</div>';
signupAppendHTML += '<div class="input_wrapper">';								
signupAppendHTML += '	<div class="su_gender_wrapper">';
signupAppendHTML += '		<input type="radio" value="1" id="su_male" name="su_gender"/>';
signupAppendHTML += '		<label for="su_male">Male</label>';
signupAppendHTML += '	</div>';
signupAppendHTML += '	<div class="su_gender_wrapper">';
signupAppendHTML += '		<input type="radio" value="2" id="su_female" name="su_gender"/>';
signupAppendHTML += '		<label for="su_female">Female</label>';
signupAppendHTML += '	</div>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';        
signupAppendHTML += '</div>';
signupAppendHTML += '<div class="input_wrapper">';
signupAppendHTML += '	<label class="" for="su_email">Email</label>';
signupAppendHTML += '	<input class="i_text" type="text" id="su_email" name="su_email" placeholder="Email" data-label="email"/>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';
signupAppendHTML += '</div>';
signupAppendHTML += '<div class="input_wrapper">';
signupAppendHTML += '	<label class="" for="su_pw">Password</label>';
signupAppendHTML += '	<input class="i_text" type="password" id="su_pw" name="su_pw" placeholder="Password" data-label="password"/>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';
signupAppendHTML += '</div>';
signupAppendHTML += '<div class="input_wrapper">';
signupAppendHTML += '	<label class="" for="su_cpw">Confirm Password</label>';
signupAppendHTML += '	<input class="i_text sp_cpw" type="password" id="su_cpw" name="su_cpw" placeholder="Confirm Password" data-label="password"/>';
signupAppendHTML += '	<div class="invalid-tooltip"></div>';
signupAppendHTML += '</div>';
signupAppendHTML += '<button class="cp su_submit" onclick="su_validCheck();">Sign Up</button>';

let userArr = new Array();

$(document).ready(function(){
	$(".form").html(loginAppendHTML);										// Initially insert DOM
	loginFormValidCheck();													// Add event listner

	$("section.head ul li").click(function(){
		if(!$(this).hasClass("active")){			
			$("section.head ul li").removeClass("active");					// Toggle button activation
			$(this).addClass("active");
			
			
			$(".form").fadeOut(250);										// Form fadeIn/Out
			if($(this).index() == 0){
				setTimeout(function(){
					$(".form").html(loginAppendHTML);
					loginFormValidCheck();
				}, 250);				
			}
			else{
				setTimeout(function(){
					$(".form").html(signupAppendHTML);					
					signupFormValidCheck();
				}, 250);
			}
			
			$(".form").fadeIn(500);											// Transition
		}
	});
});

function loginFormValidCheck(){
	// email validation check
	$("input[name=email]").on("keyup", function(e){
		let value = $(this).val();
		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
		}
		else{
			let regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
			if($(this).val().match(regExp) == null) {
				toggleTooltip($(this), true, "Your email address is invalid!");
			}
			else{
				toggleTooltip($(this), false, "");
			}
		}
	});

	// password validation check
	$("input[name=password]").on("keyup", function(e){
		let value = $(this).val();
		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
		}
		else{
			toggleTooltip($(this), false, "");
		}
	});
}

function signupFormValidCheck(){
	// name validation check
	$("input[name=su_fn], input[name=su_ln]").on("keyup", function(e){
		let value = $(this).val();

		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");	
		}
		else{
			if(value[0] != value[0].toUpperCase()){
				toggleTooltip($(this), true, "First letter is not a capital letter!");						
				return;
			}

			for(let i=0;i<value.length;i++){
				if(parseInt(value[i]) >= 0 && parseInt(value[i]) <= 9){
					toggleTooltip($(this), true, "Name cannot contain numbers!");						
					return;
				}
			}

			let regExp = /[^\w\s¤¡-ÆR]|[\_]/g;
			if($(this).val().match(regExp) != null) {
				toggleTooltip($(this), true, "Name cannot contain special letter!");
				return;
			}

			toggleTooltip($(this), false, "");
		}
	});
	
	// check if gender selected
	$("input[name=su_gender]").change(function(){
		$(this).parent().parent().children(".invalid-tooltip").removeClass("active");
	});

	// email validation check
	$("input[name=su_email]").on("keyup", function(e){
		let value = $(this).val();
		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
		}
		else{
			let regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
			if($(this).val().match(regExp) == null) {
				toggleTooltip($(this), true, "Your email address is invalid!");
			}
			else{
				toggleTooltip($(this), false, "");
			}
		}
	});

	// password validation check
	$("input[name=su_pw]").on("keyup", function(e){
		let value = $(this).val();
		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
		}
		else{
			let regExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$/
			if($(this).val().match(regExp) == null) {
				toggleTooltip($(this), true, "Requirement: at least 6 characters, one capital letter,</br>one lowercase letter, at least one digit and one special</br>character!");
			}
			else{
				toggleTooltip($(this), false, "");
			}
		}
	});

	// confirm password validation check
	$("input[name=su_cpw]").on("keyup", function(e){
		let value = $(this).val();
		if(value == ""){
			toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
		}
		else{
			if(value == $("input[name=su_pw]").val()){
				toggleTooltip($(this), false, "");
			}
			else{
				toggleTooltip($(this), true, "Password does not match!");
			}
		}
	});
}

function login_validCheck(){
	let input_email = $("input[name=email]");
	if($(input_email).val() == ""){
		toggleTooltip($(input_email), true, "Please enter your "+ $(input_email).data("label")+"!");
	}
	else{
		if(!$(input_email).hasClass("invalid")){
			toggleTooltip($(input_email), false, "");
		}
	}

	let input_pw = $("input[name=password]");
	if($(input_pw).val() == ""){
		toggleTooltip($(input_pw), true, "Please enter your "+ $(input_pw).data("label")+"!");
	}
	else{
		if(!$(input_pw).hasClass("invalid")){
			toggleTooltip($(input_pw), false, "");
		}
	}
	
	// check if there is an invalid input
	if($(input_email).hasClass("invalid") || $(input_pw).hasClass("invalid")){
		return;
	}

	// find if the info matches
	let result = false;
	for(let i=0;i<userArr.length;i++){
		if(userArr[i][0] == $("input[name=email]").val() && userArr[i][1] == $("input[name=password]").val()){
			result = true;
			break;
		}
	}

	if(result){
		$("section.head").hide();
		$(".form").html(loginFinishAppendHTML);
	}
	else{
		$(".lg").html("Credential do not match");
		$(".lg").addClass("warn");
	}
}

function su_validCheck(){
	// input check
	$(".i_text").each(function(){
		if($(this).val() == ""){
			if($(this).hasClass("sp_cpw")){
				toggleTooltip($(this), true, "Please re-enter your "+ $(this).data("label")+"!");
			}
			else{
				toggleTooltip($(this), true, "Please enter your "+ $(this).data("label")+"!");
			}
		}
		else{
			if(!$(this).hasClass("invalid")){
				toggleTooltip($(this), false, "");
			}
		}
	});

	// gender radio check	
	let _genderInvalidToolTop = $("input[name=su_gender]").parent().parent().children(".invalid-tooltip");
	if($("input[name=su_gender]:checked").val() == null){
		_genderInvalidToolTop.html("Please select your gender!");
		_genderInvalidToolTop.addClass("active");
		return;
	}
	else{
		_genderInvalidToolTop.removeClass("active");
	}
	
	// check if there is an invalid input
	if($(".i_text").hasClass("invalid")){
		return;
	}

	userArr.push(new Array($("input[name=su_email]").val(),$("input[name=su_pw]").val()));
	$(".form").html(signupFinishAppendHTML);
}

function toggleTooltip(_this, bool, msg){
	if(bool){
		$(_this).parent().children(".invalid-tooltip").html(msg);
		$(_this).parent().children(".invalid-tooltip").addClass("active");
		$(_this).addClass("invalid");
		$(_this).removeClass("valid");
	}
	else{
		$(_this).parent().children(".invalid-tooltip").removeClass("active");
		$(_this).removeClass("invalid");
		$(_this).addClass("valid");
	}
}