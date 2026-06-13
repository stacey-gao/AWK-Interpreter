import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class Interpreter1Tests {

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
	
	@Test
	public void ParseFunctionCallExit() throws Exception {
		String content = "exit";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("exit"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	@Test
	public void ParseFunctionCallPrint() throws Exception {
		String content = "print";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("print"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	@Test
	public void ParseFunctionCallPrintf() throws Exception {
		String content = "printf";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("printf"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	@Test
	public void ParseFunctionCallNextFile() throws Exception {
		String content = "nextfile";
		Lexer lexer = new Lexer(content);
	    LinkedList<Token> tokens = lexer.Lex();
	    Parser parser = new Parser(tokens);
	    Optional<Node> actual = parser.ParseFunctionCall();
	    
	    LinkedList<Node> param  =new LinkedList<Node>();
	    FunctionCallNode expected= new FunctionCallNode(new ConstantNode("nextfile"), param);
        
        assertEquals(actual.get().toString(), expected.toString());


	}
	
	
	
	
}
