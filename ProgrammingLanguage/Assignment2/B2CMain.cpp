// B2CMain.cpp
#include <iostream>
#include <map>
#include <stack>
#include "antlr4-runtime.h"
#include "antlr4-cpp/BBaseVisitor.h"
#include "antlr4-cpp/BLexer.h"
#include "antlr4-cpp/BParser.h"
#define MAX 1000
#define MAIN "main"

using namespace std;
using namespace antlr4;
using namespace antlr4::tree;

int errorCnt = 0;                                       // 에러 갯수 저장용
int paramCnt[MAX] = {0};                                // 파라미터 갯수 저장용
map<string, int> funcIdxMap;                            // (함수이름, 함수인덱스) 맵
map<string, string> funcTypeMap;                        // (함수이름, 함수타입) 맵
map<int, string> varIdxMap[MAX];                        // (변수인덱스, 변수이름) 맵 배열
map<string, string> varTypeMap[MAX];                    // (변수이름, 변수타입) 맵 배열

class TypeAugmentationVisitor : public BBaseVisitor {public:};  // 사용안함

class TypeAnalysisVisitor : public BBaseVisitor {
public:
    bool isTypeSet = false;                             // Expr 내 '=' 및 return 등 타입 관련
    int currentFuncIdx = 0;                             // 현재 함수 인덱스 저장용

    stack<string> typeStack;                            // 타입 저장 스택
    map<string, BParser::FuncdefContext*> funcDefs;     // (함수이름, ctx) 맵

    // typeStack pop()
    string getTypeStack(){
        if(typeStack.empty()) {
            cout << "Type stack empty" << endl;
            exit(-1);
        }

        string top = typeStack.top();
        typeStack.pop();

        return top;
    }

    // typeStack push()
    void setTypeStack(string type){
        typeStack.push(type);
    }

    void insertVarTypeMap(string name, string type){
        if(varTypeMap[currentFuncIdx][name].empty()){                               // 해당 함수에 변수가 없다면
            varTypeMap[currentFuncIdx][name] = type;                                // 변수 이름에 타입 맵핑
            varIdxMap[currentFuncIdx][varIdxMap[currentFuncIdx].size()] = name;     // 마지막 인덱스에 값 맵핑
        }
    }

    void updateVarTypeMap(string name, string type){
        int updateFuncIdx = 0;                                                      // 기본적으로 전역변수라 가정

        if(varTypeMap[currentFuncIdx][name].empty()){                               // 해당 함수에 변수가 없다면
            if(varTypeMap[0][name].empty()){
                errorCnt++;
                cout << "Error : Variable " << name << " not defined" << endl;    // 전역 변수도 아니면 에러 출력
            }
        }
        else{                                                                       // 해당 함수에 변수가 있으면 
            updateFuncIdx = currentFuncIdx;                                         // 업데이트 할 함수 인덱스 설정
        }

        string originalType = varTypeMap[updateFuncIdx][name];                      // 변수의 원래 타입 확인

        if(originalType != type){
            if(originalType == "undefined"){
                varTypeMap[updateFuncIdx][name] = type;                             // 정의되는 부분
            }
            else{
                errorCnt++;
                cout << "Error : Variable " << name << " type mismatch" << endl;  // 타입 에러나는 부분
            }
        }
    }
    
    any visitProgram(BParser::ProgramContext *ctx) override {
        string functionName = "global";                                             // 전역일 경우 함수 global로
        funcIdxMap[functionName] = 0;                                               // 전역인덱스 저장
        funcTypeMap[functionName] = "void";                                         // 전역타입 저장

    	for(int i = 0; i < ctx->children.size(); i++){
      	    visit(ctx->children[i]);                                                // 한줄씩 모두 방문
    	}

        visitFuncdef(funcDefs[MAIN]);                                               // 마지막에 main 함수 재방문

        // 함수 undefined 타입 확인
        for(const auto& pair : funcTypeMap){
            if(pair.second == "undefined"){
                errorCnt++;
                cout << "Error : Function " << pair.first << " type undefined" << endl;
            }
        }

        // 변수 undefined 타입 확인
        for(const auto& pair : funcIdxMap){
            for(int i = 0; i < varTypeMap[pair.second].size(); i++){     
                string varName = varIdxMap[pair.second][i];           
                if(varTypeMap[pair.second][varName] == "undefined"){
                    errorCnt++;
                    cout << "Error : Variable " << varName << " type undefined" << endl;
                }
            }
        }
                
        if(errorCnt > 0){
            exit(-1);
        }

    	return nullptr;
    }

    any visitFuncdef(BParser::FuncdefContext *ctx) override {
        string functionName = ctx->name(0)->getText();

		if(ctx->statement()->blockstmt()){
            // 처음 방문이 아닐 경우
            if(funcIdxMap.count(functionName)){
                if(functionName == MAIN){                                           // 최종 main 방문 시
                    currentFuncIdx = funcIdxMap[MAIN];                              // 현재 함수 인덱스 설정
                    for(int i = 1; i < ctx->name().size(); i++){
                        insertVarTypeMap(ctx->name(i)->getText(), "undefined");     // 변수 undefined로 임시 저장
                    }

                    visit(ctx->statement());
                    currentFuncIdx = 0;                                             // 실질적으로 마지막 행
                }
                else{
                    int cIdxTemp = currentFuncIdx;                                  // caller 인덱스 저장
                    currentFuncIdx = funcIdxMap[functionName];                      // callee 인덱스 세팅

                    visit(ctx->statement());                                        // callee statement 방문
                    currentFuncIdx = cIdxTemp;                                      // caller 인덱스 세팅
                }
            }
            // 처음 방문일 경우 = 선언일 경우
            else{            
                currentFuncIdx = funcIdxMap[functionName] = funcIdxMap.size();      // 인덱스 저장
                funcDefs[functionName] = ctx;                                       // ctx 저장
                
                if(functionName == MAIN){
                    funcTypeMap[functionName] = "int";                              // main 처음 방문때는 함수정보만 저장 후 리턴
                    return nullptr;
                }
                else{
                    funcTypeMap[functionName] = "undefined";                        // 이외는 undefined로 임시 저장
                }

                for(int i = 1; i < ctx->name().size(); i++){
                    insertVarTypeMap(ctx->name(i)->getText(), "undefined");         // 변수 undefined로 임시 저장
                }

                paramCnt[currentFuncIdx] = ctx->name().size() - 1;                  // 파라미터 갯수 저장용
                visit(ctx->statement());
                currentFuncIdx = 0;                                                 // 함수 선언 끝나면 전역 인덱스
            }
        }

        return nullptr;
    }

    any visitReturnstmt(BParser::ReturnstmtContext *ctx) override {
        string functionName;                                                        // callee 이름 저장용

        isTypeSet = true;                                                           // 타입 세팅 활성화

        for(const auto& pair : funcIdxMap){
            if(pair.second == currentFuncIdx){
                functionName = pair.first;                                          // 현재 함수명 저장
                break;
            }
        }

        if(ctx->expression()){
            visit(ctx->expression());                                               // return 뒤 expr의 타입 저장

            if(funcTypeMap[functionName] == "undefined"){                           // 아직 정의되지 않은 함수 타입일 경우
                funcTypeMap[functionName] = getTypeStack();                         // 현재 함수타입 = 리턴 변수 타입
            }
            else{
                if(funcTypeMap[functionName] != typeStack.top()){                   // 이미 정의된 함수 타입과 리턴 타입이 맞지 않은 경우
                    errorCnt++;
                    cout << "Error : Function " << functionName << " return type mismatch" << endl;    
                }
            }
        }

        isTypeSet = false;

        return nullptr;
    }

    any visitFuncinvocation(BParser::FuncinvocationContext *ctx) override {
        string functionName = ctx->name()->getText();                               // 현재 함수명 저장
        
        if(paramCnt[funcIdxMap[functionName]] != ctx->expr().size()){
            errorCnt++;
            cout << "Error : Function " << functionName << " parameter count mismatch" << endl;
        }

        for(int i = 0; i < ctx->expr().size(); i++){                                // 각 파라미터 타입 저장
			visit(ctx->expr(i));
            varTypeMap[funcIdxMap[functionName]][varIdxMap[funcIdxMap[functionName]][i]] = getTypeStack();
        }

        visitFuncdef(funcDefs[functionName]);                                       // 해당 함수 (재)방문
        setTypeStack(funcTypeMap[functionName]);                                    // 해당 함수의 타입을 스택에 저장(돌아가서 변수에 저장됨)

        return nullptr;
    }

    any visitAutostmt(BParser::AutostmtContext *ctx) override {
        for(int i = 0, j = 0; i < ctx->name().size(); i++){
            int idx = 1 + i * 2 + j * 2 + 1;                                        // 변수 다음 인덱스
            string currentSymbol = ctx->name(i)->getText();                         // 현재 변수 명
            string nextSymbol = ctx->children[idx]->getText();                      // 현재 변수 다음 기호
            
            if(nextSymbol == "," || nextSymbol == ";"){
                insertVarTypeMap(currentSymbol, "undefined");                       // , ; 일경우 undefined로 일단 선언
            }
            else{                                                                   // = 일경우 문법에 맞게 선언
                if(ctx->constant(j)->BOOL()){
                    insertVarTypeMap(currentSymbol, "bool");
                }
                else if(ctx->constant(j)->INT()){
                    insertVarTypeMap(currentSymbol, "int");
                }
                else if(ctx->constant(j)->REAL()){
                    insertVarTypeMap(currentSymbol, "double");
                }
                else if(ctx->constant(j)->CHAR()){
                    insertVarTypeMap(currentSymbol, "char");
                }
                else{
                    insertVarTypeMap(currentSymbol, "string");
                }
                j++;
            }
        }
        return nullptr;
    }

    any visitExpression(BParser::ExpressionContext *ctx) override {
        if(ctx->ASSN()){
            isTypeSet = true;

            string varName = ctx->name()->getText();

            visit(ctx->expr());

            updateVarTypeMap(varName, getTypeStack());

            isTypeSet = false;
        }
        else{
            visit(ctx->expr());
        }
        return nullptr;
    }

    any visitExpr(BParser::ExprContext *ctx) override {
        if(isTypeSet){
            if(ctx->atom()){
                visit(ctx->atom());
            }
            else if(ctx->expr().size() == 2){
                visit(ctx->expr(0));
                visit(ctx->expr(1));
                
                string op = ctx->children[1]->getText();
                string rightType = getTypeStack();
                string leftType = getTypeStack();
                string resultType;

                if(leftType != rightType){
                    errorCnt++;
                    cout << "Error : Type mismatch" << ctx->children[0]->getText() << ctx->children[2]->getText() << endl;                    
                }

                if(op == "<" || op == "<=" || op == ">" || op == ">=" || op == "==" || op == "!=" || op == "&&" || op == "||"){
                    resultType = "bool";
                }
                else{
                    resultType = leftType;
                }

                setTypeStack(resultType);
            }
            else{
                visit(ctx->expr(0));
                visit(ctx->expr(1));
                visit(ctx->expr(2));

                string rightType = getTypeStack();
                string leftType = getTypeStack();
                string condType = getTypeStack();
                string resultType;
                
                if(leftType != rightType){
                    errorCnt++;
                    cout << "Error : Type mismatch" << ctx->children[2]->getText() << ctx->children[4]->getText() << endl;       
                    cout << "Error : Type mismatch" << endl;
                }

                setTypeStack(leftType);
            }
        }

        return nullptr;
    }

    any visitAtom(BParser::AtomContext *ctx) override {
        if(isTypeSet){
            if(ctx->name()){
                if(!varTypeMap[currentFuncIdx][ctx->name()->getText()].empty()){
                    setTypeStack(varTypeMap[currentFuncIdx][ctx->name()->getText()]);
                }
            }
            else if(ctx->constant()){
                if(ctx->constant()->BOOL()){
                    setTypeStack("bool");
                }
                else if(ctx->constant()->INT()){
                    setTypeStack("int");
                }
                else if(ctx->constant()->REAL()){
                    setTypeStack("double");
                }
                else if(ctx->constant()->CHAR()){
                    setTypeStack("char");
                }
                else{
                    setTypeStack("string");
                }
            }
            else if(ctx->expression()){
                visit(ctx->expression());
            }
            else{
                visit(ctx->funcinvocation());
            }
        }

        return nullptr;
    }
};

class PrintTreeVisitor : public BBaseVisitor {
public:
    string currentFuncName = "global";

    any visitProgram(BParser::ProgramContext *ctx) override {
    	for (int i=0; i< ctx->children.size(); i++) {
      	    visit(ctx->children[i]);
    	}
    	return nullptr;
    }

    any visitDefinition(BParser::DefinitionContext *ctx) override {
	    visit(ctx->children[0]);
        return nullptr;
    }

    any visitFuncdef(BParser::FuncdefContext *ctx) override {
		// Handle function definition
        string functionName = ctx->name(0)->getText();
        currentFuncName = functionName;

		cout << funcTypeMap[functionName] << " " << functionName << "(" ;

        // You can retrieve and visit the parameter list using ctx->name(i)
		for (int i=1; i < ctx->name().size(); i++) {
			if (i != 1) cout << ", ";

            int funcIdx = funcIdxMap[functionName];
            string varName = ctx->name(i)->getText();            

			cout << varTypeMap[funcIdx][varName] << " " << varName;		
		}
		cout << ")";

		// visit statement
		visit(ctx->statement());

        currentFuncName = "global";

        return nullptr;
    }

    any visitStatement(BParser::StatementContext *ctx) override {
		visit(ctx->children[0]);
        return nullptr;
    }

    any visitAutostmt(BParser::AutostmtContext *ctx) override {
    	// You can retrieve the variable names and constants using ctx->name(i) and ctx->constant(i)
		for (int i=0, j=0; i < ctx->name().size(); i++) {
            int funcIdx = funcIdxMap[currentFuncName];
            string varName = ctx->name(i)->getText();
            string typeName = varTypeMap[funcIdx][varName];

            cout << typeName << " ";
			cout << varName;

			int idx_assn = 1 + i*2 + j*2 + 1;  // auto name (= const)?, name (= const)?, ...
			if (ctx->children[idx_assn]->getText().compare("=") == 0) { 
				if (ctx->constant(j)) {
					cout << " = ";    
					visit(ctx->constant(j));
					j++;
				}
			}
		    cout << ";" << endl;
		}

    	return nullptr;
    }

    any visitDeclstmt(BParser::DeclstmtContext *ctx) override {
		// Handle function declaration
        string functionName = ctx->name()->getText();

        if(paramCnt[funcIdxMap[functionName]] != ctx->AUTO().size() - 1){
            cout << "Error : Function " << functionName << " parameter count mismatch" << endl;
            exit(-1);
        }

		cout << funcTypeMap[functionName] << " " << functionName << "(" ;
        
		// You can retrieve and visit the parameter type list
		for (int i=1; i < ctx->AUTO().size(); i++) {
			if (i != 1) cout << ", ";

            int funcIdx = funcIdxMap[functionName];
            string varName = varIdxMap[funcIdx][i - 1]; 

			cout << varTypeMap[funcIdx][varName] << " ";		
		}
		cout << ");" << endl;
        return nullptr;
    }

    any visitBlockstmt(BParser::BlockstmtContext *ctx) override {
    	// Perform some actions when visiting a block statement
		cout << "{" << endl;
    	for (auto stmt : ctx->statement()) {
      	    visit(stmt);
    	}
		cout << "}" << endl;
    	return nullptr;
    }

    any visitIfstmt(BParser::IfstmtContext *ctx) override {
		cout << "if (";
		visit(ctx->expr());
		cout << ") " ;

		visit(ctx->statement(0));
		if (ctx->ELSE()) {
	   		cout << endl << "else ";
	   		visit(ctx->statement(1));
		}
        return nullptr;
    }

    any visitWhilestmt(BParser::WhilestmtContext *ctx) override {
        cout << "while (";
        visit(ctx->expr());
        cout << ") ";
        visit(ctx->statement());
        return nullptr;
    }

    any visitExpressionstmt(BParser::ExpressionstmtContext *ctx) override {
		visit(ctx->expression());
		cout << ";" << endl;
        return nullptr;
    }

    any visitReturnstmt(BParser::ReturnstmtContext *ctx) override {
		cout << "return";
		if (ctx->expression()) {
			cout << " (";
			visit(ctx->expression());
			cout << ")";
		}
		cout << ";" << endl;
        return nullptr;
    }

    any visitNullstmt(BParser::NullstmtContext *ctx) override {
		cout << ";" << endl;
        return nullptr;
    }

    any visitExpr(BParser::ExprContext *ctx) override {
		// unary operator
        if(ctx->atom()) {
            if (ctx->PLUS()) cout << "+";
            else if (ctx->MINUS()) cout << "-";
	    	else if (ctx->NOT()) cout << "!";
	    	visit(ctx->atom()); 
        }
		// binary operator
		else if (ctx->MUL() || ctx->DIV() || ctx->PLUS() || ctx->MINUS() || 
		 		ctx->GT() || ctx->GTE() || ctx->LT() || ctx->LTE() || ctx->EQ() || ctx->NEQ() ||
		 		ctx->AND() || ctx->OR() ) {
	    	visit(ctx->expr(0));
	    	cout << " " << ctx->children[1]->getText() << " "; // print binary operator
	    	visit(ctx->expr(1));
		}
		// ternary operator
		else if (ctx->QUEST()) {
			visit(ctx->expr(0));
			cout << " ? ";
			visit(ctx->expr(1));
			cout << " : ";
			visit(ctx->expr(2));
		}
		else {
			int lineNum = ctx->getStart()->getLine();
			cerr << endl << "[ERROR] visitExpr: unrecognized ops in Line " << lineNum << " --" << ctx->children[1]->getText() << endl;
			exit(-1); // error
        }	
        return nullptr;
    }
   
    any visitAtom(BParser::AtomContext *ctx) override {
		if (ctx->expression()) { // ( expression )
			cout << "(";
			visit(ctx->expression());
			cout << ")";
		}
		else	// name | constant | funcinvocation
			visit(ctx->children[0]);
        return nullptr;
    }
    
    any visitExpression(BParser::ExpressionContext *ctx) override {
        if (ctx->ASSN()) { // assignment
	   		visit(ctx->name());
	  		cout << " = ";
		}
		visit(ctx->expr());
        return nullptr;
    }

    any visitFuncinvocation(BParser::FuncinvocationContext *ctx) override {
		cout << ctx->name()->getText() << "(";
		for (int i=0; i < ctx->expr().size(); i++) {
			if (i != 0) cout << ", ";
			visit(ctx->expr(i));
		}
		cout << ")";
        return nullptr;
    }
    
    any visitConstant(BParser::ConstantContext *ctx) override {
        cout << ctx->children[0]->getText();
        return nullptr;
    }
    
    any visitName(BParser::NameContext *ctx) override {
		cout << ctx->NAME()->getText();
        return nullptr;
    }
    
    any visitDirective(BParser::DirectiveContext *ctx) override {
		cout << ctx->SHARP_DIRECTIVE()->getText();
		cout << endl;
        return nullptr;
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

    ANTLRInputStream inputStream(stream);
    BLexer lexer(&inputStream);
    CommonTokenStream tokenStream(&lexer);
    BParser parser(&tokenStream);
    ParseTree* tree = parser.program();

    // STEP 1. visit parse tree and perform type inference for expressions, function calls
    TypeAnalysisVisitor AnalyzeTree;
    AnalyzeTree.visit(tree);

    // STEP 2. visit parse tree and augment types for 'auto' typed variables --- you may skip this by keeping the class empty 
    TypeAugmentationVisitor AugmentTree;
    AugmentTree.visit(tree);

    // STEP 3. visit parse tree and print out C code with augmented types
    PrintTreeVisitor PrintTree;
    PrintTree.visit(tree);


    return 0;
}