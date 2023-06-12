/* Subject      : Programming Language
 * File         : ExprMain.cpp
 * Name         : Choi Jin Woo
 * SID          : 2020315798
 */

#include <iostream>
#include <stack>
#include <map>
#include <string.h>
#include "antlr4-cpp/ExprBaseListener.h"
#include "antlr4-cpp/ExprLexer.h"
#include "antlr4-cpp/ExprParser.h"
#define BUFFER 1024

using namespace std;
using namespace antlr4;
using namespace antlr4::tree;

class EvalListener : public ExprBaseListener {
	map<string, double> vars;		// <ID, value> Map
	stack<double> sEval;			// Number Stack
	stack<char> sOp;				// Operator Stack
	string cid = "";				// Current Variable Id
	string ceq = "";				// Current Equation in postfix order
	bool isAssign = false;			// Check if = is in the equation
	bool isOpenBracket = false;		// Check if ( is in the stack

public:
	// Function : Return calculated value upon op
	double calc(double num1, double num2, char op){
		double result = 0;

		switch(op){
			case '+':
				result = num2 + num1;
				break;
			case '-':
				result = num2 - num1;
				break;
			case '*':
				result = num2 * num1;
				break;
			case '/':
				result = num2 / num1;
				break;			
		}

		return result;
	}

	// Function : Concatenate op at the top of stack to ceq, and pop
	void add2ceq(){
		string stOp(1, sOp.top());
		ceq += " " + stOp;
		sOp.pop();
	}

	// Function : Reset
	void reset(){
		while(!sEval.empty()){
			sEval.pop();
		}
		while(!sOp.empty()){
			sOp.pop();
		}
		cid = "";
		ceq = "";
		isAssign = false;
		isOpenBracket = false;
	}		

	// Function : Implementation of visitTerminal
	virtual void visitTerminal(tree::TerminalNode *node) {
		int nodeType = node->getSymbol()->getType();
		string nodeText = node->getText();
	
		// Case 1 : ID(variable)
		if(nodeType == ExprLexer::ID){
			cid = nodeText;
			ceq += " " + cid;
		}
		// Case 2 : num(INT, FLOAT)
		else if(nodeType == ExprLexer::INT || nodeType == ExprLexer::FLOAT){
			ceq += " " + nodeText;
		}
		// Case 3 : operators
		else{
			char op = nodeText.c_str()[0];

			if(op == '+' || op == '-'){
				if(isOpenBracket){
					while(sOp.top() != '('){
						add2ceq();
					}					
				}
				else{
					while(!sOp.empty()){
						add2ceq();
					}
				}
				sOp.push(op);
			}
			else if(op == '*' || op == '/'){
				sOp.push(op);
			}
			else if(op == '('){
				isOpenBracket = true;
				sOp.push(op);
			}
			else if(op == ')'){
				isOpenBracket = false;
				while(sOp.top() != '('){
					add2ceq();
				}
				sOp.pop();
			}
			else if(op == '='){
				isAssign = true;
			}
			else if(op == ';'){
				while(!sOp.empty()){
					add2ceq();
				}

				char buf[BUFFER];
				strcpy(buf, ceq.c_str());

				char* ptr = strtok(buf, " ");
				
				while(ptr != NULL){
					if(atof(ptr) != 0 || !strcmp(ptr, "0")){
						sEval.push(atof(ptr));
					}
					// If it is not a number, depends on the map
					else{
						if(vars.count(ptr)){
							// If it is assignment, erase for reeassignment
							if(isAssign){
								vars.erase(ptr);
							}
							// If it is not, push the value of the id to stack
							else{
								sEval.push(vars[ptr]);
							}
						}
						else{
							double num1 = sEval.top();	sEval.pop();
							double num2 = sEval.top();	sEval.pop();
						
							sEval.push(calc(num1, num2, ptr[0]));
						}
					}
					ptr = strtok(NULL, " ");
				}

				if(isAssign){
					vars.insert(make_pair(cid, sEval.top()));
				}
				else{
					cout.setf(ios::fixed);
					cout.precision(1);
					cout << sEval.top() << endl;
				}
				reset();
			}
		}
	}
};

int main(int argc, const char* argv[]) {
	if (argc < 2) {
		cerr << "[Usage] " << argv[0] << "  <input-file>\n";
		exit(0);
	}
	std::ifstream stream;
	stream.open(argv[1]);
	if (stream.fail()) {
		cerr << argv[1] << " : file open fail\n";
		exit(0);
	}

	ANTLRInputStream input(stream);
	ExprLexer lexer(&input);
	CommonTokenStream tokens(&lexer);
	ExprParser parser(&tokens);	

	ParseTreeWalker walker;
	EvalListener listener;	

	walker.walk(&listener, parser.prog());
}
