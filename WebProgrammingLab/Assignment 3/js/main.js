let quiz = [
	["Which one is Not a legal variable</br>name?", "_myvar", "Myvar", "my_var", "my-var", 4],
	["What is a correct syntax to output</br>'Hello World' in Python?", "print('Hello World')", "p('Hello World')", "echo('Hello World');", "echo 'Hello World'", 1],
	["How do you insert COMMENTS in</br>Python Code?", "/*This is a comment*/", "#This is a comment", "//This is a comment", "None of the above", 2],
	["What is the correct syntax to output</br>the type of a variable or object in</br>Python?", "print(typeof(x))", "print(typeof x)", "print(typeOf(x))", "print(type(x))", 1],
	["What is the correct file extension for</br>Python files?", ".pt", ".pyt", ".pyth", ".py", 4],
	["A semaphore is an (  ) variable that, apart from initialization, is accessed only through two standard atomic operations, P() and V().", "float", "char", "bool", "integer", 4],
	["When a lock must be held for a longer period, (  ) or mutex locks are appropriate for use.", "semaphore", "spinlock", "ticketlock", "deadlock", 1],
	["The ( ) is a key way in which hardware devices interact with the operating system.", "system call", "interrupt", "exception", "preemption", 2]
]

const MAX_QUIZ_NUM = 4;															// max quiz number 4
let currentIdx = 0;																// store current quiz index
let correctCnt = 0;																// store correct count
let userClick = true;															// control user clickable

$(document).ready(function(){
	// Intro
	$("#intro button").click(function(){
		$("#intro").removeClass("active");										// hide intro page
		quizSet();
	});
	
	// quiz option click event
	$("#main .qOp").click(function(){
		if(userClick){
			let myAns = $(this).index()+1;
			let realAns = quiz[currentIdx-1][5];
			if(myAns == realAns){
				$(this).css("background-color", "#1d9441");						// change option bg color to green

				correctCnt++;													// correct count increment
				$(".qScore").html(correctCnt);									// correct count insertion
			}
			else{
				$(this).css("background-color", "#e94541");						// change option bg color to red
			}

			userClick = false;													// disable user mouse event
			setTimeout(function(){
				quizSet();														// load next question
			}, 1000);
		}
	});

	$("#outro button").click(function(){
		$("#outro").removeClass("active");
		currentIdx = 0;
		correctCnt = 0;
		quizSet();
	});	
});

function quizSet(){
	if(currentIdx == 0){
		quiz.sort(() => Math.random() - 0.5);									// shuffle array at the beginning
	}
	if(currentIdx < MAX_QUIZ_NUM){
		currentIdx++;															// set current Quiz index
		$("#main .qProgress_status").html("Question "+currentIdx+"/4");
		$(".qProgress").css("width", (100 * currentIdx / MAX_QUIZ_NUM)+"%");
		$(".qScore").html(correctCnt);											// correct count insertion
		$("#main .qTitle").html(quiz[currentIdx-1][0]);							// set current Quiz title
		$("#main .qOp").each(function(i){								
			$(this).children("span").html(quiz[currentIdx-1][i+1]);				// set current Quiz option
			$(this).css("background-color", "#FFF");							// reset color
		});
		$("#main").addClass("active");											// show quiz page
	}
	else{
		$("#main").removeClass("active");										// hide quiz page
		$("#outro h1").html("Total Score: "+ correctCnt);
		$("#outro").addClass("active");
	}

	userClick = true;															// enable mouse event
}
