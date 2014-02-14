%{
#include "pythontest.h"
%}

%%
[(){}@,:.`=;]	return DELIMETER;
"["		return DELIMETER;
"]"		return DELIMETER;
"+="		return DELIMETER;
"-="		return DELIMETER;
"*="      	return DELIMETER;
"/="		return DELIMETER; 
"//="		return DELIMETER;
"%="		return DELIMETER;
"&="		return DELIMETER;
"|="		return DELIMETER;
"^="		return DELIMETER;
">>="		return DELIMETER;
"<<="		return DELIMETER;
"**="		return DELIMETER;
"and"		return KEYWORD;
"as" 		return KEYWORD;
"assert" 	return KEYWORD;
"break" 	return KEYWORD;
"class"		return KEYWORD;
"continue"	return KEYWORD;
"def"		return KEYWORD;
"del"		return KEYWORD;
"elif"		return KEYWORD;
"else"		return KEYWORD;
"except" 	return KEYWORD;
"exec"		return KEYWORD;
"finally"	return KEYWORD;
"for"		return KEYWORD;
"from"		return KEYWORD;
"global"	return KEYWORD;
"if"		return KEYWORD;
"import"	return KEYWORD; 
"in"		return KEYWORD;
"is"		return KEYWORD;
"lambda"	return KEYWORD;
"not" 		return KEYWORD;
"or"		return KEYWORD;
"pass"		return KEYWORD;
"print"		return KEYWORD;
"raise"		return KEYWORD;
"return" 	return KEYWORD;
"try" 		return KEYWORD;
"while"		return KEYWORD;
"with"		return KEYWORD;
"yield"		return KEYWORD;
"**"		return OPERATOR;
"//"		return OPERATOR;
"<<"		return OPERATOR;
">>"		return OPERATOR;
"=="		return OPERATOR;
">="		return OPERATOR;
"<="		return OPERATOR;
"!="		return OPERATOR;
"<>"		return OPERATOR;
[*+-/%&|~<>^]	return OPERATOR;

["][a-zA-Z0-9\t\n \[\].,/<>?;:'`!@#$%^&*(){}_+=|\\-]*["]	return LITERAL;
['][a-zA-Z0-9\t\n \[\].,/<>?;:'`!@#$%^&*(){}_+=|\\-]*[']    return LITERAL;
[0-9]+[lL]		return LITERAL;
[0][xX][0-9a-fA-F]*[lL]?	return LITERAL;
[0][oO][0-7]*[lL]?	return LITERAL;
[0][bB][0-1]*[lL]?      return LITERAL;
[0-9]*[.]?[0-9]*[jJ]?	return LITERAL;
[\n]		return NEWLINE;
[\t]		return INDENT;			
[_a-zA-Z][a-zA-Z_0-9]*	return IDENTIFIER;
[ ]		;
[ \t]*[#][ a-zA-Z0-9\t \[\].,/<>?;:'`!@#$%^&*(){}_+=|\\-]*	;
.			printf("unexpected character\n");

%%

int yywrap(void)
{
	return 1;
}
