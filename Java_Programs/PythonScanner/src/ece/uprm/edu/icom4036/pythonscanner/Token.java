package ece.uprm.edu.icom4036.pythonscanner;

/**
 * Token class used for lexical analysis
 * @author Luis de la Vega
 *
 */
public class Token {

	// Private fields
	private String text = "";
	private int startIndex, endIndex, type;

	public Token(){
		super();
		this.text = "";
		this.startIndex = -1;
		this.endIndex = -1;
		this.type = -1;
	}
	/**
	 * Creates a token that has a text value, the values its position in the string it was read from, and what type f token it is
	 * @param text
	 * @param startIndex
	 * @param endIndex
	 * @param type
	 */
	public Token(String text, int startIndex, int endIndex, int type) {
		super();
		this.text = text;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

}
