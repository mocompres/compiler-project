grammar impl;

/* A small imperative language */

start   :  cs+=command* EOF ;

program : c=command                      # SingleCommand
	| '{' cs+=command* '}'           # MultipleCommands
	;
	
command : x=ID '=' e=expr ';'	         # Assignment
	| 'output' e=expr ';'            # Output
    | 'while' '('c=condition')' p=program  # WhileLoop
	| 'if' '('c=condition')' p=program # IfStatement
	| 'for' '('x=ID '=' e=expr  '..' e2=expr ')' p=program # ForLoop
	| x=ID '[' e=expr ']' '=' e1=expr ';' # Array
	;
	 
expr :  e1=expr s=GangeDividerOperator e2=expr # GangeDividerOperator 
	| e1=expr s=PlusMinusOperator e2=expr # PlusMinusOperator
	| c=FLOAT     	      # Constant
	| x=ID		     	 # Variable
	| '(' e=expr ')'      # Parenthesis
	| a=ID '[' e=expr ']' # ExprArray
	;

PlusMinusOperator: ('+'|'-');
GangeDividerOperator: ('*'|'/'); 

condition : e1=expr '!=' e2=expr # Unequal
	  // ... extend me 
	| e1=expr '==' e2=expr # Equal
	| e1=expr '<' e2=expr # SmallerThan
	| e1=expr '>' e2=expr # BiggerThan
	| e1=expr '<=' e2=expr # BiggerEqualThan
	| e1=expr '>=' e2=expr # SmallerEqualThan
	| e1=condition '&&' e2=condition # And
	| e1=condition '||' e2=condition # Or
	| '!' c=condition #Not
	| '(' c=condition ')' # ParenthesisCondition
	;

ID    : ALPHA (ALPHA|NUM)* ;
FLOAT : '-'? NUM+ ('.' NUM+)? ;

ALPHA : [a-zA-Z_ÆØÅæøå] ;
NUM   : [0-9] ;

WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'~[\n]*  -> skip;
COMMENT2   : '/*' (~[*] | '*'~[/]  )*   '*/'  -> skip;
