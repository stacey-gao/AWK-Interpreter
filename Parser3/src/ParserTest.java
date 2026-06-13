import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
class ParserTest {

	@Test
	public void ParseMultipleParamFunction() throws Exception {
		String content = "function name (parameter1, parameter2) {\n"
				+ "    # Function body\n"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    ProgramNode node = parser.Parse();
	    String actual = node.toString();
	    
        String expected= "BEGIN: []\n"
        		+ "END: []\n"
        		+ "OTHER: []\n"
        		+ "Functions: [Function Name: name, Parameters: parameter1, parameter2,  Statements: STATEMMENT PARSING NOT IMPLEMENTED YET-[]\n"
        		+ "]";
        
        assertEquals(actual, expected);

	}
	
	@Test
	public void ParseFunction() throws Exception {
		String content = "function name1 (param1) {\n"
				+ "    # Function body\n"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    ProgramNode node = parser.Parse();
	    String actual = node.toString();
	    
        String expected= "BEGIN: []\n"
        		+ "END: []\n"
        		+ "OTHER: []\n"
        		+ "Functions: [Function Name: name1, Parameters: param1,  Statements: STATEMMENT PARSING NOT IMPLEMENTED YET-[]\n"
        		+ "]";
        
        assertEquals(actual, expected);

	}
	
	@Test
	public void ParseAction() throws Exception {
		String content = "BEGIN{\n"
				+ "	#BEGIN BODY\n"
				+ "}\n"
				+ "\n"
				+ "END{\n"
				+ "	#END BODY\n"
				+ "}\n"
				+ "\n"
				+ "(a==5){\n"
				+ "	#CONDITION BLOCK BODY\n"
				+ "}\n"
				+ "\n"
				+ "{\n"
				+ "	#BLOCK BODY\n"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    ProgramNode node = parser.Parse();
	    String actual = node.toString();
	    
        String expected= "BEGIN: [No statements in this block]\n"
        		+ "END: [No statements in this block]\n"
        		+ "OTHER: [No statements in this block, No statements in this block]\n"
        		+ "Functions: []";
        
        assertEquals(actual, expected);

	}
	
	
	@Test
	public void OverallTest() throws Exception {
		String content = "\n"
				+ "function name (parameter1, parameter2) {\n"
				+ "    # Function body\n"
				+ "}\n"
				+ "function name1 (param1) {\n"
				+ "    # Function body\n"
				+ "}\n"
				+ "\n"
				+ "BEGIN{\n"
				+ "	#BEGIN BODY\n"
				+ "}\n"
				+ "\n"
				+ "END{\n"
				+ "	#END BODY\n"
				+ "}\n"
				+ "\n"
				+ "BEGIN{\n"
				+ "	#BEGIN BODY\n"
				+ "}\n"
				+ "\n"
				+ "END{\n"
				+ "	#END BODY\n"
				+ "}\n"
				+ "\n"
				+ "(a==5){\n"
				+ "	#CONDITION BLOCK BODY\n"
				+ "}\n"
				+ "\n"
				+ "{\n"
				+ "	#BLOCK BODY\n"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    ProgramNode node = parser.Parse();
	    String actual = node.toString();
	    
        String expected= "BEGIN: [No statements in this block, No statements in this block]\n"
        		+ "END: [No statements in this block, No statements in this block]\n"
        		+ "OTHER: [No statements in this block, No statements in this block]\n"
        		+ "Functions: [Function Name: name, Parameters: parameter1, parameter2,  Statements: STATEMMENT PARSING NOT IMPLEMENTED YET-[]\n"
        		+ ", Function Name: name1, Parameters: param1,  Statements: STATEMMENT PARSING NOT IMPLEMENTED YET-[]\n"
        		+ "]";
        
        assertEquals(actual, expected);

	}
	
	@Test
	public void UnknownCharacters() throws Exception {
		String content = "'foo*= +0";
		Lexer lexer = new Lexer(content);
		try {
		    LinkedList<Token> tokens = lexer.Lex();
		    Parser parser = new Parser(tokens);
		    ProgramNode node = parser.Parse();
		}
		catch (IllegalArgumentException e) {
	        
	    }
    }
	
	
	
	//parser2 tests
	@Test
	public void ParseBottomLevelStringLiteral() throws Exception {
		String content = "\"string\"";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
        ConstantNode expected= new ConstantNode("string");
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	@Test
	public void ParseBottomLevelNumber() throws Exception {
		String content = "400";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
        ConstantNode expected= new ConstantNode("400");
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	@Test
	public void ParseBottomLevelPattern() throws Exception {
		String content = "`pattern`";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
        PatternNode expected= new PatternNode("pattern");
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	@Test
	public void ParseBottomLevelOpenParen() throws Exception {
		String content = "(8)";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
		Node expected= new ConstantNode("8");
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	@Test
	public void ParseBottomLevelMissingParen() throws Exception {
		String content = "(8";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    try {
	    	Optional<Node> actual = parser.ParseBottomLevel();
		}
		catch (IllegalArgumentException e) {     }

	}
	
	@Test
	public void ParseBottomLevelNot() throws Exception {
		String content = "!1";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
 		Node expected= new OperationNode(new ConstantNode ("8"), OperationNode.Operation.NOT);
        
        assertEquals(actual.get().toString(), expected.toString());
        
	}
	
	@Test
	public void ParseBottomLevelMinus() throws Exception {
		String content = "-5";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
 		Node expected= new OperationNode(new ConstantNode ("5"), OperationNode.Operation.SUBTRACT);
        
        assertEquals(actual.get().toString(), expected.toString());
	}
	
	@Test
	public void ParseBottomLevelPlus() throws Exception {
		String content = "8 + a";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
 		Node expected= new OperationNode(new ConstantNode ("8"), OperationNode.Operation.ADD, 
 				Optional.of( new VariableReferenceNode("a")));
        
        assertEquals(actual.get().toString(), expected.toString());
	}
	
	@Test
	public void ParseBottomLevelIncrement() throws Exception {
		String content = "++5";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
 		Node expected= new OperationNode(new ConstantNode ("5"), OperationNode.Operation.PREINC);
        
        assertEquals(actual.get().toString(), expected.toString());
	}
	
	@Test
	public void ParseBottomLevelDeIncrement() throws Exception {
		String content = "--5";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseBottomLevel();
 		Node expected= new OperationNode(new ConstantNode ("5"), OperationNode.Operation.PREDEC);
        
        assertEquals(actual.get().toString(), expected.toString());
	}
	
	@Test
	public void ParseLValueDollar() throws Exception {
		String content = "$7";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseLValue();
        OperationNode expected= new OperationNode(new ConstantNode("7"), OperationNode.Operation.DOLLAR);
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	
	@Test
	public void ParseLValueArray() throws Exception {
		String content = "array[]";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseLValue();
	    VariableReferenceNode expected= new VariableReferenceNode("array");
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseLValueArrayError() throws Exception {
		String content = "a[3";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	   
	    try {
	    	Optional<Node> actual = parser.ParseBottomLevel();
		}
	    catch (IllegalArgumentException e) {     }
	}
	
	
	// parser3 tests
	@Test
	public void ParseOperationPostInc() throws Exception {
		String content = "a++";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new VariableReferenceNode("a"), OperationNode.Operation.POSTINC);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationPostDec() throws Exception {
		String content = "a--";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new VariableReferenceNode("a"), OperationNode.Operation.POSTDEC);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationConcatencation() throws Exception {
		String content = "1 8";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new ConstantNode("1"), OperationNode.Operation.CONCATENATION, 
	    														Optional.of(new ConstantNode("8")));
        
        assertEquals(actual.toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationBooleanCompare() throws Exception {
		String content = "1==8";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new ConstantNode("1"), OperationNode.Operation.EQUAL, 
				Optional.of(new ConstantNode("8")));
        assertEquals(actual.get().toString(), expected.toString());


	}
	@Test
	public void ParseOperationMatch() throws Exception {
		String content = "1 ~ 1";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new ConstantNode("1"), OperationNode.Operation.MATCH, 
				Optional.of(new ConstantNode("1")));
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationAnd() throws Exception {
		String content = "1 && 2";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new ConstantNode("1"), OperationNode.Operation.AND, 
				Optional.of(new ConstantNode("2")));
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationOR() throws Exception {
		String content = "1 || 2";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    OperationNode expected= new OperationNode(new ConstantNode("1"), OperationNode.Operation.OR, 
				Optional.of(new ConstantNode("2")));
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationTernary() throws Exception {
		String content = "1==8 ? a++ : a--";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    TernaryNode expected= new TernaryNode(new OperationNode(new ConstantNode("1"), OperationNode.Operation.EQUAL, 
				Optional.of(new ConstantNode("8"))),new OperationNode(new VariableReferenceNode("a"), OperationNode.Operation.POSTINC),
	    		new OperationNode(new VariableReferenceNode("a"), OperationNode.Operation.POSTDEC) );
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseOperationAssignment() throws Exception {
		String content = "a=8";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseOperation();
	    
	    AssignmentNode expected= new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("8"));
        
        assertEquals(actual.get().toString(), expected.toString());


	}


}
