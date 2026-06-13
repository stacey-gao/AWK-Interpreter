import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
/**
 * @author Stacey Gao ICSI311- Lexer, University at Albany, Fall 2023
 */

public class Lexer {

	private int lineNumber;
	private int charPosition;
	private StringHandler handler;
	private HashMap <String, Token.TokenType> keywords = new HashMap <String, Token.TokenType>();
	private HashMap <String, Token.TokenType> oneCharSymbols = new HashMap <String, Token.TokenType>();
	private HashMap <String, Token.TokenType> twoCharSymbols = new HashMap <String, Token.TokenType>();
	
	
	// this is the “main” that will break the data from StringHandler into a linked list of tokens
	public Lexer(String content) {
		handler = new StringHandler(content);
		lineNumber = 1;
		charPosition = 0;
		keywords = createKeywordTokens();
		oneCharSymbols = createOneCharSymbols();
		twoCharSymbols = createTwoCharSymbols();
	}

	/**
	 * breaks content into tokens
	 * @return token list
	 * @throws Exception
	 */
	
	public LinkedList<Token> Lex() throws Exception{
		LinkedList<Token> tokens = new LinkedList<Token>();
		
		try {
			while(handler.IsDone()== false) {
				//skips line due to awk comment using #
				if (handler.Peek(0) == '#') {
					lineNumber++;
					charPosition= 0;
					int skipChar = 0;
					do {
						skipChar++;
					}while(handler.Peek(skipChar) != '\n');
					handler.Swallow(skipChar);
				}
				else if (handler.Peek(0) == '"') {
					tokens.add(HandleStringLiteral());
				}
				// creates separator token (new line)
				else if (handler.Peek(0) == '\n') {
					lineNumber++;
					charPosition= 0;
					handler.Swallow(1);
					tokens.add(new Token (Token.TokenType.SEPARATOR, lineNumber, charPosition));
				}
				// creates separator token (;)
				else if (handler.Peek(0) == ';') {
					charPosition++;
					handler.Swallow(1);
					tokens.add(new Token (Token.TokenType.SEPARATOR, lineNumber, charPosition));
				}
				
				//skips spaces and tabs
				else if (handler.Peek(0) == ' ' || handler.Peek(0) == '\t') {
					charPosition++;
					handler.Swallow(1);
				}
				
				//skips carriage return
				else if (handler.Peek(0) == '\r') {	}
				
				else if ((handler.Peek(0) >= 'a' && handler.Peek(0) <= 'z') ||
			        (handler.Peek(0) >= 'A' && handler.Peek(0) <= 'Z') || handler.Peek(0) == '_') {
					tokens.add(ProcessWord());
				}
				
				else if (handler.Peek(0) >= '0' && handler.Peek(0) <= '9' || handler.Peek(0) == '.') {
					tokens.add(ProcessNumber());
				}
				
				else if (handler.Peek(0) == '`') {
					tokens.add(HandlePattern());
				}
				
				else {
					Token symbol = ProcessSymbol();
					if(symbol != null) 
						tokens.add(symbol);
					else
						throw new IllegalArgumentException("Invalid character: " + handler.Peek(0));
				}
			
			}
		}catch (IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
			
		}
		return tokens;
	
	}
	
	

	/**
	 * accepts letters, digits and underscores (_) and make a token of them and 
	 * checks the hash map for known words and makes a token specific to
	 * the word with no value if the word is in the hash map, but WORD otherwise
	 * @return WORD token
	 */
	private Token ProcessWord() {
		String tokenValue = "";
		char next;
		
		do {
			tokenValue= tokenValue + handler.Peek(0);
			next= handler.getChar();
		} while (((next >= 'a' && next <= 'z') ||
	            (next >= 'A' && next <= 'Z') ||
	            (next >= '0' && next <= '9') ||
	            next == '_') && handler.IsDone()==false);
	
		if (keywords.containsKey(tokenValue) == true)
			return new Token(keywords.get(tokenValue), lineNumber, charPosition);
		else
			return new Token(Token.TokenType.WORD, tokenValue, lineNumber, charPosition);
		
	}

	/**
	 * accepts 0-9 and one “.”
	 * @return NUMBER token
	 */
	private Token ProcessNumber() {
		String tokenValue = "";
		boolean decimal = false;
		char next;
		
		do {
			tokenValue= tokenValue + handler.Peek(0);
			if(decimal== false && handler.Peek(0) == '.') 
				decimal = true;
			next= handler.getChar();
	
		} while (((next >= '0' && next <= '9') ||
	            (next == '.') && decimal==false) && handler.IsDone()== false);
		
		return new Token(Token.TokenType.NUMBER,tokenValue, lineNumber, charPosition);
		
	}
	
	/**
	 * @return token with correct type corresponding to the symbol
	 * or null, if that is an unknown symbol
	 */
	private Token ProcessSymbol() {
		Token symbol = null;
		try {
			if (twoCharSymbols.containsKey(handler.StringPeek(2)) == true) {
				symbol = new Token(twoCharSymbols.get(handler.StringPeek(2)), lineNumber, charPosition);
				handler.Swallow(2);//updates position
			}
		}
		catch (StringIndexOutOfBoundsException e) {}// catches, if a symbol is the last char of the document
		try {
			if (oneCharSymbols.containsKey(handler.StringPeek(1)) == true) {
				symbol = new Token(oneCharSymbols.get(handler.StringPeek(1)), lineNumber, charPosition);
				handler.Swallow(1);//updates position
			}
		}catch (StringIndexOutOfBoundsException e) {}// catches, if a symbol is the last char of the document
		try {
			if (handler.StringPeek(1)== ";")
				return new Token (Token.TokenType.SEPARATOR, lineNumber, charPosition);
		}catch (StringIndexOutOfBoundsException e) {}// catches, if a symbol is the last char of the document
			
		return symbol;
		
	}
	
	/**
	 * A Pattern is content between `` (backticks)
	 * @return new Pattern Token with contents between
	 */
	private Token HandlePattern() {
		String tokenValue = "";
		char next;

		next= handler.getChar();;// to skip ` char
		try {
			while(next != '`') {
				tokenValue= tokenValue + handler.Peek(0);
				next= handler.getChar();
			} 
		}catch (StringIndexOutOfBoundsException e) {
			System.out.print("Error: missing charater to close PATTERN \n");
			}// catches, if there is no ending '`'
		
		handler.Swallow(1);// to skip ` char
		return new Token (Token.TokenType.PATTERN,tokenValue, lineNumber, charPosition);
		
	}
	
	
	/**
	 * String literals is content between "", \" is an escaped in string literal
	 * @return STRINGLITERAL token with contents between
	 */
	private Token HandleStringLiteral() {
		String tokenValue = "";
		char next;
		
		next= handler.getChar();;// to skip " char
		try {
			while(next != '"') {
				tokenValue= tokenValue + handler.Peek(0);
				next= handler.getChar();
				if(next == '\\' ) 
					handler.Swallow(1);
			} 
		}catch (StringIndexOutOfBoundsException e) {
			System.out.print("Error: missing charater to close STRINGLITERAL \n");
		}// catches, if there is no ending '"'
		
		handler.Swallow(1);// to skip " char
		return new Token (Token.TokenType.STRINGLITERAL,tokenValue, lineNumber, charPosition);
	}
	
	
	/**
	 * @return hashmap of keywords in AWK
	 */
	private HashMap <String, Token.TokenType> createKeywordTokens() {
		HashMap <String, Token.TokenType> keywords = new HashMap <String, Token.TokenType>();
        keywords.put("while", Token.TokenType.WHILE);
		keywords.put("if", Token.TokenType.IF);
        keywords.put("do", Token.TokenType.DO);
        keywords.put("for", Token.TokenType.FOR);
        keywords.put("break", Token.TokenType.BREAK);
        keywords.put("continue", Token.TokenType.CONTINUE);
        keywords.put("else", Token.TokenType.ELSE);
        keywords.put("return", Token.TokenType.RETURN);
        keywords.put("BEGIN", Token.TokenType.BEGIN);
        keywords.put("END", Token.TokenType.END);
        keywords.put("print", Token.TokenType.PRINT);
        keywords.put("printf", Token.TokenType.PRINTF);
        keywords.put("next", Token.TokenType.NEXT);
        keywords.put("in", Token.TokenType.IN);
        keywords.put("delete", Token.TokenType.DELETE);
        keywords.put("getline", Token.TokenType.GETLINE);
        keywords.put("exit", Token.TokenType.EXIT);
        keywords.put("nextfile", Token.TokenType.NEXTFILE);
        keywords.put("function", Token.TokenType.FUNCTION);
       
		return keywords;
	}
	
	/**
	 * @return hashmap of one character symbols in AWK
	 */
	private HashMap <String, Token.TokenType> createOneCharSymbols() {
		HashMap <String, Token.TokenType> symbols = new HashMap <String, Token.TokenType>();
        symbols.put("{", Token.TokenType.OPENCURLYBRACE);
		symbols.put("}", Token.TokenType.CLOSECURLYBRACE);
        symbols.put("[", Token.TokenType.OPENBRACKET);
        symbols.put("]", Token.TokenType.CLOSEBRACKET);
        symbols.put("(", Token.TokenType.OPENPAREN);
        symbols.put(")", Token.TokenType.CLOSEPAREN);
        symbols.put("$", Token.TokenType.DOLLAR);
        symbols.put("~", Token.TokenType.MATCH);
        symbols.put("=", Token.TokenType.ASSIGN);
        symbols.put("<", Token.TokenType.LESSTHAN);
        symbols.put(">", Token.TokenType.GREATERTHAN);
        symbols.put("!", Token.TokenType.NOT);
        symbols.put("+", Token.TokenType.PLUS);
        symbols.put("^", Token.TokenType.EXPONENT);
        symbols.put("-", Token.TokenType.MINUS);
        symbols.put("?", Token.TokenType.QUESTIONMARK);
        symbols.put(":", Token.TokenType.COLON);
        symbols.put("*", Token.TokenType.MULTIPLY);
        symbols.put("/", Token.TokenType.DIVIDE);
        symbols.put("%", Token.TokenType.MODULE);
        symbols.put("|", Token.TokenType.PIPE);
        symbols.put(",", Token.TokenType.COMMA);
       
		return symbols;
	}

	/**
	 * @return hashmap of two character symbols in AWK
	 */
	private HashMap <String, Token.TokenType> createTwoCharSymbols() {
		HashMap <String, Token.TokenType> symbols = new HashMap <String, Token.TokenType>();
        symbols.put(">=", Token.TokenType.GREATERTHAN_OREQUAL);
		symbols.put("++", Token.TokenType.ADD_1);
        symbols.put("--", Token.TokenType.SUBTRACT_1);
        symbols.put("<=", Token.TokenType.LESSTHAN_OREQUAL);
        symbols.put("==", Token.TokenType.EQUALS_OPERATOR);
        symbols.put("!=", Token.TokenType.NOT_EQUAL);
        symbols.put("^=", Token.TokenType.EXPONENT_EQUALS);
        symbols.put("%=", Token.TokenType.MODULE_EQUALS);
        symbols.put("*=", Token.TokenType.MULTIPLY_EQUALS);
        symbols.put("/=", Token.TokenType.DIVIDE_EQUALS);
        symbols.put("+=", Token.TokenType.PLUS_EQUALS);
        symbols.put("-=", Token.TokenType.MINUS_EQUALS);
        symbols.put("!~", Token.TokenType.NOT_MATCH);
        symbols.put("&&", Token.TokenType.AND);
        symbols.put(">>", Token.TokenType.APPEND);
        symbols.put("||", Token.TokenType.OR);
       
		return symbols;
	}
}
