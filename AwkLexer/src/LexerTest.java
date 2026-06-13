import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Stacey Gao ICSI311- Lexer, University at Albany, Fall 2023
 */

public class LexerTest {

	private String getActualTokens(List<Token> tokens) {
		String actualTokens = "";
	    for (Token token : tokens) 
            actualTokens= actualTokens + token.toString();
		return actualTokens;
	}
	
	@Test
	public void NumberWordCombination() throws Exception {
		String content = "I have 4.5 pizzas"
				+ "\n I will eat 2";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "WORD(I) WORD(have) NUMBER(4.5) WORD(pizzas) SEPARATOR WORD(I) WORD(will) WORD(eat) NUMBER(2) ";
        
        assertEquals(actualTokens, expectedTokens);

	}
	
	@Test
	public void EmptyDocument() throws Exception {
		String content = "";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "";
        
        assertEquals(actualTokens, expectedTokens);
	}
	
	@Test
	public void NumberLexing() throws Exception {
		String content = "7544.76 87";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "NUMBER(7544.76) NUMBER(87) ";
        
        assertEquals(actualTokens, expectedTokens);
	}
	
	@Test
	public void WordLexing() throws Exception {
		String content = "ICSI311"
				+ "\n Project";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "WORD(ICSI311) SEPARATOR WORD(Project) ";
        
        assertEquals(actualTokens, expectedTokens);
	}

	 @Test
	public void ExtraDecimals() throws Exception {
		String content = "I have 4.5.8 pizzas";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = "";
	    for (Token token : tokens) 
	            actualTokens= actualTokens + token.toString();
		    
	    String expectedTokens= "WORD(I) WORD(have) NUMBER(4.5) NUMBER(.8) WORD(pizzas) ";
	        
	    assertEquals(actualTokens, expectedTokens);
	}
	
	@Test
	public void UnknownCharacters() throws Exception {
		String content = "'foo*= +0";
		Lexer lexer = new Lexer(content);
		try {
		    List<Token> tokens = lexer.Lex();
		    String actualTokens = getActualTokens(tokens);
		}
		catch (IllegalArgumentException e) {
	        assertEquals("Invalid character: ' ", e.getMessage());
	    }
    }
	
	//test from assignment description
	@Test
	public void TestFullLine1() throws Exception {
        Lexer lexer = new Lexer("$0 = tolower($0)");
        List<Token> tokens = lexer.Lex();
        assertEquals(8, tokens.size());
        assertEquals(Token.TokenType.DOLLAR, tokens.get(0).getTokenType());
        assertEquals(Token.TokenType.NUMBER, tokens.get(1).getTokenType());
        assertEquals(Token.TokenType.ASSIGN, tokens.get(2).getTokenType());
        assertEquals(Token.TokenType.WORD, tokens.get(3).getTokenType());
        assertEquals("tolower", tokens.get(3).getTokenValue());
        assertEquals(Token.TokenType.OPENPAREN, tokens.get(4).getTokenType());
        assertEquals(Token.TokenType.DOLLAR, tokens.get(5).getTokenType());
        assertEquals(Token.TokenType.NUMBER, tokens.get(6).getTokenType());
        assertEquals(Token.TokenType.CLOSEPAREN, tokens.get(7).getTokenType());
    }
	
	@Test
	public void RecongnizeKeywords() throws Exception {
		String content = "while if do for break continue else return BEGIN "
				+ "END print printf next in delete getline exit nextfile function";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.WHILE);
		assertEquals(tokens.get(1).getTokenType(), Token.TokenType.IF);
        assertEquals(tokens.get(2).getTokenType(), Token.TokenType.DO);
        assertEquals(tokens.get(3).getTokenType(), Token.TokenType.FOR);
        assertEquals(tokens.get(4).getTokenType(), Token.TokenType.BREAK);
        assertEquals(tokens.get(5).getTokenType(), Token.TokenType.CONTINUE);
        assertEquals(tokens.get(6).getTokenType(), Token.TokenType.ELSE);
        assertEquals(tokens.get(7).getTokenType(), Token.TokenType.RETURN);
        assertEquals(tokens.get(8).getTokenType(), Token.TokenType.BEGIN);
        assertEquals(tokens.get(9).getTokenType(), Token.TokenType.END);
        assertEquals(tokens.get(10).getTokenType(), Token.TokenType.PRINT);
        assertEquals(tokens.get(11).getTokenType(), Token.TokenType.PRINTF);
        assertEquals(tokens.get(12).getTokenType(), Token.TokenType.NEXT);
        assertEquals(tokens.get(13).getTokenType(), Token.TokenType.IN);
        assertEquals(tokens.get(14).getTokenType(), Token.TokenType.DELETE);
        assertEquals(tokens.get(15).getTokenType(), Token.TokenType.GETLINE);
        assertEquals(tokens.get(16).getTokenType(), Token.TokenType.EXIT);
        assertEquals(tokens.get(17).getTokenType(), Token.TokenType.NEXTFILE);
        assertEquals(tokens.get(18).getTokenType(), Token.TokenType.FUNCTION);
       
	}
	
	@Test
	public void SkipsComments() throws Exception {
		String content = "#I ate dumplings today haha"
				+ "\n LOL";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "SEPARATOR WORD(LOL) ";
        
        assertEquals(actualTokens, expectedTokens);
	}
	
	@Test
	public void StringLiteralTest() throws Exception {
		String content = "\"I sleep a lot\"";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "STRINGLITERAL(I sleep a lot) ";
        
        assertEquals(actualTokens, expectedTokens);
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.STRINGLITERAL);
        assertEquals(tokens.get(0).getTokenValue(), "I sleep a lot");
	}
	
	@Test
	public void EmptyStringLiteral() throws Exception {
		String content = "\"\"";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "STRINGLITERAL() ";
        
        assertEquals(actualTokens, expectedTokens);
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.STRINGLITERAL);
	}
	
	@Test
	public void EscapedStringLiteral() throws Exception {
		String content = "\"She said, \\\"hello there\\\" and then she left.\";";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "STRINGLITERAL(She said, \"hello there\" and then she left.) SEPARATOR ";
        
        assertEquals(actualTokens, expectedTokens);
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.STRINGLITERAL);
	}
	
	@Test
	public void UnclosedStringLiteral() throws Exception {
		String content = "\"hahaha skdk";
		Lexer lexer = new Lexer(content);
		try {
		    List<Token> tokens = lexer.Lex();
		    String actualTokens = getActualTokens(tokens);
		}
		catch (StringIndexOutOfBoundsException e) {
	        assertEquals("Error: missing charater to close STRINGLITERAL ", e.getMessage());
	    }
    }
	
	@Test
	public void UnclosedPattern() throws Exception {
		String content = "`Patt ern";
		Lexer lexer = new Lexer(content);
		try {
		    List<Token> tokens = lexer.Lex();
		    String actualTokens = getActualTokens(tokens);
		}
		catch (StringIndexOutOfBoundsException e) {
	        assertEquals("Error: missing charater to close PATTERN ", e.getMessage());
	    }
    }
	
	@Test
	public void Patterns() throws Exception {
		String content = "`this is a pattern`";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "PATTERN(this is a pattern) ";
        
        assertEquals(actualTokens, expectedTokens);
        assertEquals(actualTokens, expectedTokens);
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.PATTERN);
        assertEquals(tokens.get(0).getTokenValue(), "this is a pattern");
	}
	
	@Test
	public void TwoCharSymbols() throws Exception {
		String content = ">=  ++  --  <=  ==  !=  ^=  %=  *=  /=  +=  -=  !~   &&   >>   ||";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.GREATERTHAN_OREQUAL);
        assertEquals(tokens.get(1).getTokenType(), Token.TokenType.ADD_1);
        assertEquals(tokens.get(2).getTokenType(), Token.TokenType.SUBTRACT_1);
        assertEquals(tokens.get(3).getTokenType(), Token.TokenType.LESSTHAN_OREQUAL);
        assertEquals(tokens.get(4).getTokenType(), Token.TokenType.EQUALS_OPERATOR);
        assertEquals(tokens.get(5).getTokenType(), Token.TokenType.NOT_EQUAL);
        assertEquals(tokens.get(6).getTokenType(), Token.TokenType.EXPONENT_EQUALS);
        assertEquals(tokens.get(7).getTokenType(), Token.TokenType.MODULE_EQUALS);
        assertEquals(tokens.get(8).getTokenType(), Token.TokenType.MULTIPLY_EQUALS);
        assertEquals(tokens.get(9).getTokenType(), Token.TokenType.DIVIDE_EQUALS);
        assertEquals(tokens.get(10).getTokenType(), Token.TokenType.PLUS_EQUALS);
        assertEquals(tokens.get(11).getTokenType(), Token.TokenType.MINUS_EQUALS);
        assertEquals(tokens.get(12).getTokenType(), Token.TokenType.NOT_MATCH);
        assertEquals(tokens.get(13).getTokenType(), Token.TokenType.AND);
        assertEquals(tokens.get(14).getTokenType(), Token.TokenType.APPEND);
        assertEquals(tokens.get(15).getTokenType(), Token.TokenType.OR);
        
	}
	
	@Test
	public void OneCharSymbols() throws Exception {
		String content = "{ } [ ] ( ) $ ~ = < > !  + ^ - ?  : * / % ; \n | ,";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
        assertEquals(tokens.get(0).getTokenType(), Token.TokenType.OPENCURLYBRACE);
		assertEquals(tokens.get(1).getTokenType(), Token.TokenType.CLOSECURLYBRACE);
        assertEquals(tokens.get(2).getTokenType(), Token.TokenType.OPENBRACKET);
        assertEquals(tokens.get(3).getTokenType(), Token.TokenType.CLOSEBRACKET);
        assertEquals(tokens.get(4).getTokenType(), Token.TokenType.OPENPAREN);
        assertEquals(tokens.get(5).getTokenType(), Token.TokenType.CLOSEPAREN);
        assertEquals(tokens.get(6).getTokenType(), Token.TokenType.DOLLAR);
        assertEquals(tokens.get(7).getTokenType(), Token.TokenType.MATCH);
        assertEquals(tokens.get(8).getTokenType(), Token.TokenType.ASSIGN);
        assertEquals(tokens.get(9).getTokenType(), Token.TokenType.LESSTHAN);
        assertEquals(tokens.get(10).getTokenType(), Token.TokenType.GREATERTHAN);
        assertEquals(tokens.get(11).getTokenType(), Token.TokenType.NOT);
        assertEquals(tokens.get(12).getTokenType(), Token.TokenType.PLUS);
        assertEquals(tokens.get(13).getTokenType(), Token.TokenType.EXPONENT);
        assertEquals(tokens.get(14).getTokenType(), Token.TokenType.MINUS);
        assertEquals(tokens.get(15).getTokenType(), Token.TokenType.QUESTIONMARK);
        assertEquals(tokens.get(16).getTokenType(), Token.TokenType.COLON);
        assertEquals(tokens.get(17).getTokenType(), Token.TokenType.MULTIPLY);
        assertEquals(tokens.get(18).getTokenType(), Token.TokenType.DIVIDE);
        assertEquals(tokens.get(19).getTokenType(), Token.TokenType.MODULE);
        assertEquals(tokens.get(20).getTokenType(), Token.TokenType.SEPARATOR);
        assertEquals(tokens.get(21).getTokenType(), Token.TokenType.SEPARATOR);
        assertEquals(tokens.get(22).getTokenType(), Token.TokenType.PIPE);
        assertEquals(tokens.get(23).getTokenType(), Token.TokenType.COMMA);
	}
	
	@Test
	public void OverallSymbols() throws Exception {
		String content = "(apples + bananas)/ [grapes * 2] - 50 % oranges == {a bunch of fruits}";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "OPENPAREN WORD(apples) PLUS WORD(bananas) CLOSEPAREN DIVIDE OPENBRACKET "
        		+ "WORD(grapes) MULTIPLY NUMBER(2) CLOSEBRACKET MINUS NUMBER(50) MODULE WORD(oranges) "
        		+ "EQUALS_OPERATOR OPENCURLYBRACE WORD(a) WORD(bunch) WORD(of) WORD(fruits) CLOSECURLYBRACE ";
        
        assertEquals(actualTokens, expectedTokens);
	}
	
	//awk file from online
	@Test
	public void OverallTest() throws Exception {
		String content = "#!/bin/awk -f\n"
				+ "\n"
				+ "# This AWK script reads a CSV file and prints the values in the second column.\n"
				+ "\n"
				+ "BEGIN {\n"
				+ "    FS = \",\"  # Set the field separator to comma\n"
				+ "}\n"
				+ "\n"
				+ "{\n"
				+ "    print $2  # Print the second column\n"
				+ "}\n"
				+ "";
		Lexer lexer = new Lexer(content);
	    List<Token> tokens = lexer.Lex();
	    String actualTokens = getActualTokens(tokens);
	    
        String expectedTokens= "SEPARATOR SEPARATOR SEPARATOR SEPARATOR BEGIN OPENCURLYBRACE SEPARATOR"
        		+ " WORD(FS) ASSIGN STRINGLITERAL(,) SEPARATOR CLOSECURLYBRACE SEPARATOR SEPARATOR"
        		+ " OPENCURLYBRACE SEPARATOR PRINT DOLLAR NUMBER(2) SEPARATOR CLOSECURLYBRACE SEPARATOR ";
        
        assertEquals(actualTokens, expectedTokens);
	}

}
	
	