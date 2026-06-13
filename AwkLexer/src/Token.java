/**
 * @author Stacey Gao ICSI311- Lexer, University at Albany, Fall 2023
 */


public class Token {
	public enum TokenType {WORD, NUMBER, SEPARATOR, 
		//Keywords
		WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN, END,
		PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, EXIT, NEXTFILE, FUNCTION,
		
		STRINGLITERAL, PATTERN,
		
		//Symbols
		OPENCURLYBRACE, CLOSECURLYBRACE, OPENBRACKET, CLOSEBRACKET, OPENPAREN, CLOSEPAREN,
		//{ } [ ] ( )
		DOLLAR, MATCH, ASSIGN, LESSTHAN, GREATERTHAN, NOT, PLUS, EXPONENT, MINUS,
		//$ ~ = < > !  + ^ - 
		QUESTIONMARK, COLON, MULTIPLY, DIVIDE, MODULE, PIPE, COMMA,
		//?  : * / % | ,
		GREATERTHAN_OREQUAL, ADD_1, SUBTRACT_1, LESSTHAN_OREQUAL, EQUALS_OPERATOR,
		//>=  ++  --  <=  ==  
		NOT_EQUAL, EXPONENT_EQUALS, MODULE_EQUALS, MULTIPLY_EQUALS, DIVIDE_EQUALS,
		//!=  ^=  %= *=  /=  
		PLUS_EQUALS, MINUS_EQUALS, NOT_MATCH, AND, APPEND, OR
		//+=  -=  !~   &&   >>   ||
		};
		
		//'\n' and ';' will be a separator token
		
	private TokenType type;
	private String tokenValue;
	private int lineNumber;
	private int charPosition;
	
	/**
	 * constructor for words and number with value
	 * @param tokenValue
	 * @param lineNumber
	 * @param charPosition
	 */
	public Token(TokenType type, String tokenValue, int lineNumber, int charPosition) {
		this.type = type;
		this.tokenValue = tokenValue;
		this.lineNumber = lineNumber;
		this.charPosition = charPosition;
	}
	
	/**
	 * constructor for separator with no value
	 * @param lineNumber
	 * @param charPosition
	 */
	public Token(TokenType type, int lineNumber, int charPosition) {
		this.type = type;
		this.lineNumber = lineNumber;
		this.charPosition = charPosition;
		
	}

	@Override
	public String toString(){
		if (tokenValue == null)// type!= TokenType.WORD && type!= TokenType.NUMBER && type!= TokenType.STRINGLITERAL
			return type + " ";
		return type + "(" + tokenValue + ") ";
	}
	
	//accessors
	public TokenType getTokenType() {
		return type;
	}
	public String getTokenValue() {
		return tokenValue;
	}

}

