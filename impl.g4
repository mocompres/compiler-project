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
	;
	
expr	: e1=expr '+' e2=expr # Addition
	| e1=expr '-' e2=expr # Subtraction
	| e1=expr '*' e2=expr # Multiplication
	| e1=expr '/' e2=expr # Division
	| c=FLOAT     	      # Constant
	| x=ID		      # Variable
	| '(' e=expr ')'      # Parenthesis
	;

condition : e1=expr '!=' e2=expr # Unequal
	  // ... extend me 
	| e1=expr '==' e2=expr # Equal
	| e1=expr '<' e2=expr # SmallerThan
	| e1=expr '>' e2=expr # BiggerThan
	| e1=expr '<=' e2=expr # BiggerEqualThan
	| e1=expr '>=' e2=expr # SmallerEqualThan
	| e1=condition '&&' e2=condition # And
	| e1=condition '||' e2=condition # Or
	;  

ID    : ALPHA (ALPHA|NUM)* ;
FLOAT : '-'? NUM+ ('.' NUM+)? ;

ALPHA : [a-zA-Z_ÆØÅæøå] ;
NUM   : [0-9] ;

WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'~[\n]*  -> skip;
COMMENT2   : '/*' (~[*] | '*'~[/]  )*   '*/'  -> skip;
