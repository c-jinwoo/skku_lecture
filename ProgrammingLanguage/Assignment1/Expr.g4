/* Subject	: Programming Language
 * File		: Expr.g4
 * Name		: Choi Jin Woo
 * SID		: 2020315798
 */

grammar Expr;

// parser rules
prog	:	(assn ';' NEWLINE? | expr ';' NEWLINE?)* ;
expr	:	expr ('*'|'/') expr
		|	expr ('+'|'-') expr
		|	num
		|	ID 
		|	'(' expr ')';
assn	:	ID '=' num ;
num		:	INT
		|	FLOAT ;

// lexer rules
NEWLINE	:	[\r\n]+ ;
INT		:	('+'|'-')?[0-9]+ ;
FLOAT	:	('+'|'-')?[0-9]+'.'[0-9]* ;
ID		:	[a-zA-Z_][a-zA-Z0-9_]* ;
WS		:	[ \t\r\n]+ -> skip ; 
