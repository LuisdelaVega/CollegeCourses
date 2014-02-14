package ece.uprm.edu.icom4036.pythonscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Python lexical analyzer
 * @author Luis de la Vega
 *
 */
public class PythonTest {

	// Count the amount of lexemes found in the file
	private static long lexemeCount = 0;
	
	// Types of tokens
	private static final int COMMENT = 0;
	private static final int INDENT = 1;
	private static final int LITERAL = 2;
	private static final int DELIMETER0 = 3;
	private static final int OPERATOR = 4;
	private static final int DELIMETER1 = 5;
	private static final int KEYWORD = 6;
	private static final int IDENTIFIER = 7;
	private static final int NEWLINE = 8;

	// Indents in Python are like opening curly braces in Java and other languages
	// Dedents are like the closing ones. We must keep track of the amount of indents
	private static int currentIndents, expectedIndents, expectedDedents;
	
	// Also keep track of the last token seen
	private static Token lastToken = new Token();

	// An array of the regular expressions needed to find the tokens
	private static final String[] REGEX = {
/*0 - COMMENT*/		"[ \t\\s]*[#][ a-zA-Z0-9\t .,/<>?;:'`\"'!@#$%\\^&(){}_=|\\-\\*\\+]*[\\n]*",
/*1 - INDENT*/		"\t",
/*2 - LITERAL*/		"\"[a-zA-Z0-9\t\n \\.,/<>\\?;:'`!@#$%\\^&(){}_=|\\-\\[\\]\\*\\+]*\""+"|['][a-zA-Z0-9\\t\\n .,/<>\\?;:'`!@#$%\\^&(){}_=|\\-\\[\\]\\*\\+]*[']"+"|[0-9]+[lL]"+"|[0][xX][0-9a-fA-F]*[lL]?"+"|[0][oO][0-7]*[lL]?"+"|[0][bB][0-1]*[lL]?"+"|[0-9]+[.]?[0-9]*[j|J]?",
/*3 - DELIMETER0*/	">>="+"|<<="+"|\\^="+"|\\["+"|\\]"+"|\\+="+"|-="+"|\\*="+"|/="+"|//="+"|%="+"|&="+"|\\|="+"|\\*\\*=",
/*4 - OPERATOR*/	"\\*\\*"+"|//"+"|>="+"|<="+"|<<"+"|>>"+"|=="+"|!="+"|<>"+"|[-/%&\\|~<>\\^\\*\\+]",
/*5 - DELIMETER1*/	"[(){}@,:.`=;]",
/*6 - KEYWORD*/		"\\sand\\s"+"|\\sas\\s"+"|\\sassert\\s"+"|\\sbreak\\s"+"|\\sclass\\s"+"|\\scontinue\\s"+"|\\sdef\\s"+"|\\sdel\\s"+"|\\selif\\s"+"|\\selse\\s"+"|\\sexcept\\s"+"|\\sexec\\s"+"|\\sfinally\\s"+"|\\sfor\\s"+"|\\sfrom\\s"+"|\\sglobal\\s"+"|\\sif\\s"+"|\\simport\\s"+"|\\sin\\s"+"|\\sis\\s"+"|\\slambda\\s"+"|\\snot\\s"+"|\\sor\\s"+"|\\spass\\s"+"|\\sprint\\s"+"|\\sraise\\s"+"|\\sreturn\\s"+"|\\stry\\s"+"|\\swhile\\s"+"|\\swith\\s"+"|\\syield\\s",
/*7 - IDENTIFIER*/	"[_a-zA-Z][a-zA-Z_0-9]*",
/*8 - NEWLINE*/		"[\n]",
	};

	public static void main(String[] args) {

		// Create the a scanner and look for a file to read from
		Scanner in = null;

		File testfile = new File(args[0]);

		try {
			in = new Scanner(testfile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		long startTime = System.currentTimeMillis();
		
		// While not at EOF, continue reading
		while(in.hasNextLine()){
			regexChecker(in.nextLine());
		}

		// Print out the closing dedents
		while(expectedDedents > 0)
		{
			System.out.println("DEDENT");
			expectedDedents--;
		}
		
		System.out.println(System.currentTimeMillis() - startTime);
		System.out.println(lexemeCount);

		// Close the file to prevent errors
		in.close();
		
	}

	/**
	 * Checks matches the given string with the regular expressions. Prints out the found tokens.
	 * @param line
	 */
	private static void regexChecker(String line) {

		ArrayList<Token> tokens = new ArrayList<Token>();

		// Go over the array of regular expressions and store the found tokens in the tokens array
		for(int i = 0; i < REGEX.length; i++){
			Matcher matcher = Pattern.compile(REGEX[i]).matcher(line);
			
			while(matcher.find()){
				if(matcher.group().length() != 0){
					lexemeCount++;
					// Don't store the comments in the array!
					if(i == COMMENT){
						// Adjust the string so that it stays the same length after removing the found token
						String adjustment = "";
						for(int j = 0; j < matcher.end() - matcher.start(); adjustment += " ", j++);
						line = line.substring(0, matcher.start()) + adjustment + line.substring(matcher.end(), line.length());
						// After adjusting, reset the matcher with the new string
						matcher.reset(line);
					}
					// If the matcher finds anything but a comment, create a token and store it
					else{
						tokens.add(new Token(matcher.group().trim(), matcher.start(), matcher.end(), i));
						// Adjust the string so that it stays the same length after removing the found token
						String adjustment = "";
						for(int j = 0; j < matcher.end() - matcher.start(); adjustment += " ", j++);
						line = line.substring(0, matcher.start()) + adjustment + line.substring(matcher.end(), line.length());
						// After adjusting, reset the matcher with the new string
						matcher.reset(line);
					}
				}
			}
		}


		// Sort the tokens so they can be printed out in the order that they appear in the file
		for(int i = 0; i < tokens.size(); i++){
			Token temp = tokens.get(i);

			for(int j = i+1; j < tokens.size(); j++){
				if(temp.getStartIndex() > tokens.get(j).getStartIndex()){
					tokens.set(i, tokens.get(j));
					tokens.set(j, temp);
					temp = tokens.get(i);
				}
			}
		}

		// Add a new line token at the end. Do this since the scanner doesn't give you a newline string (\n)
		tokens.add(new Token("", -1, -1, NEWLINE));

		// Print the tokens
		for(Token token : tokens){
			printToken(token);
		}

	}

	/**
	 * Method that takes care of printing the tokens as they appear in the given file
	 * @param token
	 */
	private static void printToken(Token token) {

		// If done counting the indents at the beginning of the line, store the number of expected indents
		if(lastToken.getType() != token.getType() && lastToken.getType() == INDENT)
		{
			expectedIndents = currentIndents;
			currentIndents = 0;
		}
		// Else if we expected that a line would be indexed by some amount but it wasn't, the indentation is over.
		// Update the expected indexes
		else if(lastToken.getType() == NEWLINE 
				&& expectedIndents > 0 
				&& token.getType() != INDENT 
				&& token.getType() != NEWLINE)
		{
			expectedIndents = 0;
		}

		// After reading a line, if the expected indents are more than the expected dedents, update the expected dedents 
		if(expectedIndents > expectedDedents)
		{
			expectedDedents = expectedIndents;
		}
		// Else if the expected dedents are more than the expected indents, print out the amount of dedents
		else if(expectedIndents < expectedDedents)
		{
			while(expectedDedents > expectedIndents)
			{
				System.out.println("DEDENT");
				expectedDedents--;
			}
		}

		// Multiple newlines in python are interpreted as one newline token
		// If the last token was a newline and the current token is also a newline, then do nothing
		if(lastToken.getType() == token.getType() && token.getType() == NEWLINE)
		{
			// Do nothing
		}
		else{
			switch(token.getType()){

			case COMMENT:
				break;
			case LITERAL:
				System.out.println("LITERAL("+token.getText()+")");
				break;
			case DELIMETER0:
				System.out.println("DELIMETER("+token.getText()+")");
				break;
			case OPERATOR:
				System.out.println("OPERATOR("+token.getText()+")");
				break;
			case DELIMETER1:
				System.out.println("DELIMETER("+token.getText()+")");
				break;
			case KEYWORD:
				System.out.println("KEYWORD("+token.getText()+")");
				break;
			case IDENTIFIER:
				System.out.println("IDENTIFIER("+token.getText()+")");
				break;
			case NEWLINE:
				System.out.println("NEWLINE");
				break;
			case INDENT:
				currentIndents++;
				if(currentIndents <= expectedIndents){
					// Do nothing
				}
				else
					System.out.println("INDENT");
				break;
			default:
				System.out.println("Syntax error");

			}
		}
		
		// Update the last token
		lastToken = token;

	}

}
