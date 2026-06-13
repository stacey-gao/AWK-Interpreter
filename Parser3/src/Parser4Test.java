import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class Parser4Test {

	// parser4 tests
	@Test
	public void ParseStatementBreak() throws Exception {
		String content = "break;";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    BreakNode expected= new BreakNode();
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementContinue() throws Exception {
		String content = "continue;";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    ContinueNode expected= new ContinueNode();
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementIf() throws Exception {
		String content = "if (1==8){"
				+ ""
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    IfNode expected= new IfNode(new OperationNode(new ConstantNode("1"), OperationNode.Operation.EQUAL, 
				Optional.of(new ConstantNode("8"))), new BlockNode(new LinkedList<StatementNode>(),Optional.empty()));
        
        assertEquals(actual.get().toString(), expected.toString());

	}
	
	@Test
	public void ParseStatementFor() throws Exception {
		String content = "for (i = 0 ; i == 0; ){"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    ForNode expected= new ForNode(Optional.of(new AssignmentNode(new VariableReferenceNode("i"), new ConstantNode("0"))),
	    		Optional.of(new OperationNode(new VariableReferenceNode("i"), OperationNode.Operation.EQUAL, 
	    				Optional.of(new ConstantNode("0")))),
	    		Optional.empty()
	    		, new BlockNode(new LinkedList<StatementNode>(),Optional.empty()));
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementDelete() throws Exception {
		String content = "delete array;";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    DeleteNode expected= new DeleteNode(new VariableReferenceNode("array"), Optional.empty());
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementWhile() throws Exception {
		String content = "while(1==8){"
				+ "}";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    WhileNode expected= new WhileNode(new OperationNode(new ConstantNode("1"), OperationNode.Operation.EQUAL, 
				Optional.of(new ConstantNode("8"))), new BlockNode(new LinkedList<StatementNode>(),Optional.empty()));
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementDoWhile() throws Exception {
		String content = "do {"
				+ "} while(1==8)";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    DoWhileNode expected= new DoWhileNode(new OperationNode(new ConstantNode("1"), OperationNode.Operation.EQUAL, 
				Optional.of(new ConstantNode("8"))), new BlockNode(new LinkedList<StatementNode>(),Optional.empty()));
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseStatementReturn() throws Exception {
		String content = "return 1;";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<StatementNode> actual = parser.ParseStatement();
	    
	    ReturnNode expected= new ReturnNode(new ConstantNode("1"));
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseFunctionCall() throws Exception {
		String content = "foo(boo)";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    param.add(new VariableReferenceNode("boo"));
	    FunctionCallNode expected= new FunctionCallNode(new VariableReferenceNode("foo"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	@Test
	public void ParseFunctionCallGetline() throws Exception {
		String content = "getline";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("getline"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	@Test
	public void ParseFunctionCallNext() throws Exception {
		String content = "next";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("next"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
}
