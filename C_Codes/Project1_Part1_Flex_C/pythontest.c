#include <stdio.h>
#include <string.h>
#include "pythontest.h"
#include <time.h>

extern int yylex();
extern int yylineno;
extern char *yytext;

int main (void)
{

	int ntoken, lastToken = 0, currentIndents = 0, expectedIndents = 0, expectedDedents = 0;
	double one, dos;
	one=time(0);
	
	ntoken = yylex();
	while(ntoken)
	{

		if(lastToken != ntoken && lastToken == INDENT)
		{
			expectedIndents = currentIndents;
			currentIndents = 0;
		}
		else if(lastToken == NEWLINE && expectedIndents > 0 && ntoken != INDENT && ntoken != NEWLINE)
		{
			expectedIndents = 0;
		}

		if(expectedIndents > expectedDedents)
		{
			expectedDedents = expectedIndents;
		}
		else if(expectedIndents < expectedDedents)
		{
			while(expectedDedents > expectedIndents)
			{
				printf("DEDENT\n");
				expectedDedents--;
			}
		}
		// If the last token was a newline and the current token is also
		// a newline, do nothing. Python counts consecutive newlines
		// as one newline token.
		if(lastToken == ntoken && ntoken == NEWLINE)
		{
			// Do nothing
		}
		else
		{
			switch(ntoken)
			{
				case KEYWORD:
					printf("KEYWORD(%s)\n", yytext);
					break;
				case LITERAL:
					printf("LITERAL(%s)\n", yytext);
					break;
       		                 case OPERATOR:
       	       		                printf("OPERATOR(%s)\n", yytext);
       	                	        break;
       	                	 case DELIMETER:
       	                        	printf("DELIMETER(%s)\n", yytext);
       	                         	break;
       	                 	case IDENTIFIER:
       	                         	printf("IDENTIFIER(%s)\n", yytext);
       	                         	break;
				case NEWLINE:
					printf("NEWLINE\n");
					break;
				case INDENT:
					currentIndents++;
					if(currentIndents <= expectedIndents)
               				{
						// Do nothing
					}
					else
					{
                                		printf("INDENT\n");
					}
                        	        break;
				default:
					printf("Syntax error in line %d\n", yylineno);
			}
		}
		lastToken = ntoken;
		ntoken = yylex();
	}

	while(expectedDedents > 0)
		{
			printf("DEDENT\n");
			expectedDedents--;
		}

	dos=time(0);
	printf("%f\n",dos-one);
	
	return 0;

}
