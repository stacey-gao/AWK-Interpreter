import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
public class Parser {
	
	private TokenHandler handler;
	private ProgramNode programNode;
	
	/**
	 * constructor, accepts linked list of tokens from lexer
	 * @param token
	 */
	public Parser(LinkedList <Token> token) {
		handler = new TokenHandler(token);
		programNode = new ProgramNode();
	}
	
	
	/**
	 * @return true if it finds at least one separator token
	 */
	private boolean AcceptSeperators() {
		if(handler.MatchAndRemove(Token.TokenType.SEPARATOR).isPresent())
			return true;
		else
			return false;
		
	}
	
	/**
	 * main parse method 
	 * @return programNode
	 * throws exception if ParseFunction() and ParseAction() are both false
	 */
	public ProgramNode Parse() {
		
		while(handler.MoreTokens()) {
			while (AcceptSeperators()) {}
			if(ParseFunction(programNode)) {
				while (AcceptSeperators()) {}
			}
			else if(ParseAction(programNode)) {
				while (AcceptSeperators()) {}
			}
			else {
				throw new IllegalArgumentException("Unknown item:" + handler.Peek(0));
			}
		}
		return programNode;
		
	
		
	}
	

	/**
	 * creates and adds FunctionDefinitionNode and populate it with name 
	 * and parameters and add it to the ProgramNode’s function list
	 * @param programNode
	 * @return false if token type is not a function, or else returns true
	 */
	private boolean ParseFunction(ProgramNode node) {
		if(handler.Peek(0).get().getTokenType() != Token.TokenType.FUNCTION)
			return false;
		else {
			handler.MatchAndRemove(Token.TokenType.FUNCTION);
			
			//String functionName = handler.Peek(0).get().getTokenValue();
			String functionName = handler.MatchAndRemove(Token.TokenType.WORD).get().getTokenValue();
			
			if (handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent());
			else throw new IllegalArgumentException("Parameter Error: '(' not present");
			
			//reads parameters
			LinkedList <Token> parameterNames = new LinkedList<Token>();
			while(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isEmpty()) {
				if (handler.Peek(0).get().getTokenType() == Token.TokenType.WORD) {
					parameterNames.add(handler.Peek(0).get());
					handler.MatchAndRemove(Token.TokenType.WORD);
				}
				else if (handler.Peek(0).get().getTokenType() == Token.TokenType.COMMA)
					handler.MatchAndRemove(Token.TokenType.COMMA);
				else 
					throw new IllegalArgumentException("Parameter Error: Illegal parameter name or ')' not present");
			}
			
			//creation of FunctionDefinitionNode
			if (handler.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACE) 
				programNode.addFunctionDefinitionNode(new FunctionDefinitionNode(functionName, parameterNames, ParseBlock().getStatements()));
			else 
				throw new IllegalArgumentException("Function Error: block starter '{' not present");
	
			return true;
		}
		
	}
	
	/**
	 * parses actions BEGIN, END, {}, (Condition){}
	 * conditional actions not implemented yet
	 * @param node
	 * @return true, if parsing action was successful; false, if not a action
	 */
	private boolean ParseAction(ProgramNode node) {
		//BEGIN { /* do something at the beginning of the program*/
		if(handler.MatchAndRemove(Token.TokenType.BEGIN).isPresent()) {
			if (handler.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACE) 
				programNode.addBEGINblock(ParseBlock()); 
			else 
				throw new IllegalArgumentException("Error: block starter '{' not present");
			return true;
		}
		
		//END { /* do something  at the end of the program*/}
		else if(handler.MatchAndRemove(Token.TokenType.END).isPresent()) {
			if (handler.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACE) 
				programNode.addENDblock(ParseBlock());
			else 
				throw new IllegalArgumentException("Error: block starter '{' not present");
			return true;
		}
		
		//{ /* do something for every input line */}
		else if(handler.MoreTokens() && handler.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACE) {
			programNode.addOTHERblock(ParseBlock());
			return true;
		}
		
		//(a==5) { /* do something for every input line if a = 5 */}
		else if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {
			String condition = handler.Peek(0).get().getTokenValue() + 
					handler.Peek(1).get().getTokenType()+ handler.Peek(2).get().getTokenValue();
			//first variable of comparison
			if(handler.MatchAndRemove(Token.TokenType.WORD).isPresent()||
					handler.MatchAndRemove(Token.TokenType.NUMBER).isPresent()){
				
			}
			//comparison operator
			if(handler.MatchAndRemove(Token.TokenType.EQUALS_OPERATOR).isPresent()||
					handler.MatchAndRemove(Token.TokenType.GREATERTHAN).isPresent()||
					handler.MatchAndRemove(Token.TokenType.LESSTHAN).isPresent()||
					handler.MatchAndRemove(Token.TokenType.LESSTHAN_OREQUAL).isPresent()||
					handler.MatchAndRemove(Token.TokenType.GREATERTHAN_OREQUAL).isPresent()){
				
			}
			// second variable of comparison
			if(handler.MatchAndRemove(Token.TokenType.WORD).isPresent()||
					handler.MatchAndRemove(Token.TokenType.NUMBER).isPresent()){
				
			}
			handler.MatchAndRemove(Token.TokenType.CLOSEPAREN);
			
			if (handler.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACE) {
				BlockNode conditionBlock = ParseBlock();
				programNode.addOTHERblock(conditionBlock);
				
			}
			else 
				throw new IllegalArgumentException("Error: block starter '{' not present");
			return true;
		}
		
		
		
		// conditional parsing, not yet implemented
		else if(handler.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
			ParseOperation();
			return true;
		}
		else if(handler.MatchAndRemove(Token.TokenType.IF).isPresent()) {
			ParseOperation();
			return true;
		}
		else if(handler.MatchAndRemove(Token.TokenType.ELSE).isPresent()) {
			ParseOperation();
			return true;
		}
		else if(handler.MatchAndRemove(Token.TokenType.FOR).isPresent()) {
			ParseOperation();
			return true;
		}
		else if(handler.MatchAndRemove(Token.TokenType.DO).isPresent()) {
			ParseOperation();
			return true;
		}
		else 
			return false;
		
	}
	
	/**
	 * called when token OPENCURLYBRACE is recognized
	 * parses a block in curly braces {} and its statements
	 * @return a BlockNode with all statements
	 */
	private BlockNode ParseBlock() {
		
		LinkedList <StatementNode> statements = new LinkedList <StatementNode>();
		
		if(handler.MatchAndRemove(Token.TokenType.OPENCURLYBRACE).isPresent()) {}
		else
			throw new IllegalArgumentException("ParseBlock: Missing '{'");
		
		while (handler.Peek(0).get().getTokenType() != Token.TokenType.CLOSECURLYBRACE) {
			while (AcceptSeperators()) {}
			
			Optional<Node> current = ParseOperation();
			if(current.isEmpty())
				statements.add((StatementNode) current.get());
			else
				throw new IllegalArgumentException("Invalid Statement: Please fix statement or close block with '}' ");
			
			while (AcceptSeperators()) {}
	
		}
		handler.MatchAndRemove(Token.TokenType.CLOSECURLYBRACE);
		Optional<Node> condition = ParseBottomLevel() ;
		
		return new BlockNode(statements, condition);
	}
	
	/**
	 * Parses break, continue, if, for, delete, while, do, return statments
	 * left public for unit test testing for now
	 * @return StatementNode and Optional.empty if not a statement listed
	 */
	public Optional<StatementNode> ParseStatement() {
		
		if(handler.MatchAndRemove(Token.TokenType.BREAK).isPresent()) {
			return ParseBreak();
		}
		if(handler.MatchAndRemove(Token.TokenType.CONTINUE).isPresent()) {
			return ParseContinue();
		}
		if(handler.MatchAndRemove(Token.TokenType.IF).isPresent()) {
			return ParseIf();
		}
		if(handler.MatchAndRemove(Token.TokenType.FOR).isPresent()) {
			return ParseFor();
		}
		if(handler.MatchAndRemove(Token.TokenType.DELETE).isPresent()) {
			return ParseDelete();
		}
		if(handler.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
			return ParseWhile();
		}
		if(handler.MatchAndRemove(Token.TokenType.DO).isPresent()) {
			return ParseDo();
		}
		if(handler.MatchAndRemove(Token.TokenType.RETURN).isPresent()) {
			return ParseReturn();
		}
		return Optional.empty();
		
	}
	
	/**
	 * Parses break
	 * @return BreakNode
	 */
	private Optional<StatementNode> ParseBreak() {
		if(handler.Peek(0).get().getTokenType() != Token.TokenType.SEPARATOR)
			throw new IllegalArgumentException("Missing ';' : Please add ';' after your break statement ");
		else
			return Optional.of(new BreakNode());
	}

	/**
	 * Parses continue
	 * @return ContinueNode
	 */
	private Optional<StatementNode> ParseContinue() {
		if(handler.Peek(0).get().getTokenType() != Token.TokenType.SEPARATOR)
			throw new IllegalArgumentException("Missing ';' : Please add ';' after your continue statement ");
		else
			return Optional.of(new ContinueNode());
	}

	/**
	 * Parses If
	 * @return IfNode
	 */
	private Optional<StatementNode> ParseIf() {
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing '(' in if condition");
		
		Optional<Node> condition = ParseOperation();
		
		if(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing ')' in if condition");
		
		while (AcceptSeperators()) {}
		if (handler.Peek(0).get().getTokenType() == Token.TokenType.ELSE && handler.Peek(1).get().getTokenType() == Token.TokenType.IF) {
			handler.MatchAndRemove(Token.TokenType.ELSE);
			handler.MatchAndRemove(Token.TokenType.IF);
			return Optional.of(new IfNode(condition.get(), ParseBlock(), ParseIf()));
		}
			
		return Optional.of(new IfNode(condition.get(), ParseBlock()));
	}
	
	/**
	 * Parses for
	 * @return ForNode
	 */
	private Optional<StatementNode> ParseFor() {
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing '(' in if condition");
		
		Optional<Node> initializer = ParseOperation();
		if(handler.MatchAndRemove(Token.TokenType.SEPARATOR).isEmpty())
			throw new IllegalArgumentException("Missing ';' in for statement ");
		Optional<Node> condition = ParseOperation();
		if(handler.MatchAndRemove(Token.TokenType.SEPARATOR).isEmpty())
			throw new IllegalArgumentException("Missing ';' in for statement ");
		Optional<Node> iterator = ParseOperation();
		
		if(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing ')' in if condition");
		
		return Optional.of(new ForNode(initializer, condition, iterator, ParseBlock()));
	}
	
	
	private Optional<StatementNode> ParseDelete() {
		VariableReferenceNode array = (VariableReferenceNode)ParseLValue().get();// will return variableref node 
		return Optional.of(new DeleteNode(array, array.getIndexExpression()));
	}
	
	private Optional<StatementNode> ParseWhile() {
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing '(' in if condition");
		
		Optional<Node> condition = ParseOperation();
		
		if(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing ')' in if condition");
		
		return Optional.of(new WhileNode(condition.get(), ParseBlock()));
	}
	
	private Optional<StatementNode> ParseDo() {
		BlockNode block = ParseBlock();
		if(handler.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing while keyword in do while loop");
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing '(' in if condition");
		
		Optional<Node> condition = ParseOperation();
		
		if(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing ')' in if condition");
		
		return Optional.of(new DoWhileNode(condition.get(), block));
	}
	
	
	private Optional<StatementNode> ParseReturn() {
		Optional<Node> statement = ParseOperation();
		if(statement.isEmpty()) {
			throw new IllegalArgumentException("Please add a return value after your return statement ");
		}
		if(handler.Peek(0).get().getTokenType() != Token.TokenType.SEPARATOR)
			throw new IllegalArgumentException("Missing ';' : Please add ';' after your return statement ");
		else
			return Optional.of(new ReturnNode(statement.get()));
	}
	
	//temporary public for testing 
	/**
	 * parses function calls and getline, print, printf, exit, nextfile, next
	 * @return
	 */
	public Optional<Node> ParseFunctionCall(){
		
		Optional<Node> name = ParseLValue();
		
		if (handler.MatchAndRemove(Token.TokenType.GETLINE).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("getline"), new LinkedList<Node>()));
		}
		
		else if (handler.MatchAndRemove(Token.TokenType.PRINT).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("print"), new LinkedList<Node>()));
		}
		
		else if (handler.MatchAndRemove(Token.TokenType.PRINTF).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("printf"), new LinkedList<Node>()));
		}
		else if (handler.MatchAndRemove(Token.TokenType.EXIT).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("exit"), new LinkedList<Node>()));
		}
		else if (handler.MatchAndRemove(Token.TokenType.NEXTFILE).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("nextfile"), new LinkedList<Node>()));
		}
		else if (handler.MatchAndRemove(Token.TokenType.NEXT).isPresent()) {
			return Optional.of(new FunctionCallNode(new ConstantNode("next"), new LinkedList<Node>()));
		}
				
		
	
		else if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing '(' in function call");
		LinkedList<Node> parameters= new LinkedList<Node>();
		Optional<Node> param;
		do {
			param = ParseLValue();
			if(param.isPresent()) {
				parameters.add(param.get());
			}
		}while (handler.Peek(0).get().getTokenType() == Token.TokenType.COMMA);
		
		if(handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isPresent()) {}
		else
			throw new IllegalArgumentException("Missing ')' in function call");
		
		return Optional.of(new FunctionCallNode(name.get(), parameters));
	}

	/**
	 * Deals with Post-increment, Post-decrement
	 * Exponentiation
	 * Multiplication, Division, Modulus
	 * Addition, Subtraction
	 * String concatenation
	 * Less than, Less than or equal to, Not equal to, Equal to, Greater than, Greater than or equal to
	 * ERE match, ERE non-match
	 * Array membership
	 * Logical AND
	 * Logical OR
	 * Conditional expression
	 * Exponentiation assignment, Modulus assignment, Multiplication assignment,
	 * Division assignment, Addition assignment, Subtraction assignment, Assignment
	 * 
	 * @return AssignmentNode, TernaryNode, or Operation Node
	 */
			
	public Optional<Node> ParseOperation(){
		
		//if(handler.Peek(1).isPresent()) {
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.ADD_1)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.SUBTRACT_1))
			return ParsePostIncDec();;
		
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.EXPONENT))
			return ParseExponent();
		
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.EQUALS_OPERATOR)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.LESSTHAN)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.LESSTHAN_OREQUAL)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.GREATERTHAN)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.GREATERTHAN_OREQUAL)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.NOT_EQUAL))
			return ParseBooleanCompare();
		
		
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.MATCH)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.NOT_MATCH))
			return ParseMatch();
		
		//if (handler.Peek(1).get().getTokenType()==(Token.TokenType.))
			//return ParseArrayMembership();
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.AND))
			return ParseAnd();
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.OR))
			return ParseOr();
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.QUESTIONMARK))
			return ParseTernary();
		
		if (handler.Peek(1).get().getTokenType()==(Token.TokenType.ASSIGN)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.EXPONENT_EQUALS)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.DIVIDE_EQUALS)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.MODULE_EQUALS)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.MULTIPLY_EQUALS)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.MINUS_EQUALS)||
				handler.Peek(1).get().getTokenType()==(Token.TokenType.PLUS_EQUALS))
			return ParseAssignment();
		//}
		return ParseBottomLevel();
	}

	/**
	 * 
	 * @return Optional<Node>
	 */
	private Optional<Node> ParseFactor() {
		Optional<Node> expression = null;
		Optional<Node> num = ParseBottomLevel();
		
		if(num.isPresent())
			return num;
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isPresent())
			expression = ParseExpression();
		if(expression == null)
			throw new IllegalArgumentException("Parse Factor: Expression not initalized");
		if(handler.MatchAndRemove(Token.TokenType.OPENPAREN).isEmpty())
			throw new IllegalArgumentException("Parse Factor: Missing )");

		return Optional.empty();
	}
	
	/**
	 * parses multiply, divide and module, calls Parse Factor
	 * @return OperationNode
	 */
	private Optional<Node> ParseTerm() {
		Optional<Node> leftNode = ParseFactor();
		Optional<Token> operation;
		OperationNode.Operation op = null;
		Optional<Node> rightNode;
		do {
			operation = handler.MatchAndRemove(Token.TokenType.MULTIPLY);
			if (operation.isPresent())
				op = OperationNode.Operation.MULTIPLY;
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.DIVIDE);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.DIVIDE)
					op = OperationNode.Operation.DIVIDE;
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.MODULE)
					op = OperationNode.Operation.MODULO;
			if (operation.isEmpty())
				return leftNode;
			rightNode= ParseFactor();
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		}while(true);
	}
	
	/**
	 * Parses plus and minus, calls ParseTerm
	 * @return OperationNode
	 */
	private Optional<Node> ParseExpression() {

		Optional<Node> leftNode = ParseTerm();
		Optional<Token> operation;
		OperationNode.Operation op = null;
		Optional<Node> rightNode ;
		do {
			operation = handler.MatchAndRemove(Token.TokenType.PLUS);
			if (operation.isPresent())
				op = OperationNode.Operation.ADD;
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.MINUS);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.MINUS)
					op = OperationNode.Operation.SUBTRACT;
			if (operation.isEmpty())
				return leftNode;
			rightNode= ParseTerm();
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		}while(true);
	}
	
	/**
	 * Parses expr expr
	 * @return OperationNode
	 */
	private Optional<Node> ParseConcatenation() {
		Optional<Node> leftNode = ParseExpression();
		OperationNode.Operation op = OperationNode.Operation.CONCATENATION;
		Optional<Node> rightNode ;
		do {
			rightNode= ParseExpression();
			if (rightNode.isEmpty())
				return leftNode;
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
		
	}
	/**
	 * Parses ==, !=, <, >, <=, >=
	 * @return OperationNode
	 */
	private Optional<Node> ParseBooleanCompare() {
		Optional<Node> leftNode = ParseExpression();
		Optional<Token> operation;
		OperationNode.Operation op = null;
		Optional<Node> rightNode;
		
			operation = handler.MatchAndRemove(Token.TokenType.EQUALS_OPERATOR);
			if (operation.isPresent())
				op = OperationNode.Operation.EQUAL;
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.NOT_EQUAL);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.NOT_EQUAL)
					op =  OperationNode.Operation.NOT_EQUAL;
			
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.LESSTHAN);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.LESSTHAN)
					op =  OperationNode.Operation.LESSTHAN;
			
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.GREATERTHAN);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.GREATERTHAN)
					op =  OperationNode.Operation.GREATHERTHAN;
			
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.LESSTHAN_OREQUAL);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.LESSTHAN_OREQUAL)
					op =  OperationNode.Operation.LESSTHAN_EQUALTO;
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.GREATERTHAN_OREQUAL);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.GREATERTHAN_OREQUAL)
					op =  OperationNode.Operation.GREATHERTHAN_EQUALTO;
			rightNode= ParseExpression();
			return Optional.of(new OperationNode (leftNode.get(), op, rightNode));
	}
	
	/**
	 * Parses match ~ and not match !~
	 * @return OperationNode
	 */
	private Optional<Node> ParseMatch() {
		Optional<Node> leftNode = ParseExpression();
		Optional<Token> operation;
		OperationNode.Operation op = null;
		Optional<Node> rightNode;
		do {
			operation = handler.MatchAndRemove(Token.TokenType.MATCH);
			if (operation.isPresent())
				op = OperationNode.Operation.MATCH;
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.NOT_MATCH);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.NOT_MATCH)
					op =  OperationNode.Operation.NOTMATCH;
			if (operation.isEmpty())
				return leftNode;
			rightNode= ParseExpression();
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
	}
	
	/**
	 * @return OperationNode
	 */
	private Optional<Node> ParseArrayMembership() {
		Optional<Node> leftNode = ParseExpression();
		OperationNode.Operation op = OperationNode.Operation.IN;
		Optional<Node> rightNode ;
		do {
			rightNode= ParseExpression();
			if (rightNode.isEmpty())
				return leftNode;
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
	}
	
	/**
	 * Parses &&
	 * @return OperationNode
	 */
	private Optional<Node> ParseAnd() {
		Optional<Node> leftNode = ParseExpression();
		OperationNode.Operation op = OperationNode.Operation.AND;
		Optional<Node> rightNode ;
		do {
			handler.MatchAndRemove(Token.TokenType.AND);
			rightNode= ParseExpression();
			if (rightNode.isEmpty())
				return leftNode;
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
	}
	/**
	 * Parses ||
	 * @return OperationNode
	 */
	private Optional<Node> ParseOr() {
		Optional<Node> leftNode = ParseExpression();
		OperationNode.Operation op = OperationNode.Operation.OR;
		Optional<Node> rightNode ;
		do {
			handler.MatchAndRemove(Token.TokenType.OR);
			rightNode= ParseExpression();
			if (rightNode.isEmpty())
				return leftNode;
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
	}
	
	/**
	 * Parses =, +=, -=, %=, /= *=
	 * @return  AssignmentNode
	 */
	private Optional<Node> ParseAssignment() {
		Optional<Node> leftNode = ParseExpression();
		Optional<Token> operation;
		OperationNode.Operation op = null;
		Optional<Node> rightNode;
			// =
			operation = handler.MatchAndRemove(Token.TokenType.ASSIGN);
			if (operation.isPresent()) {
				op = OperationNode.Operation.EQUAL;
				rightNode= ParseExpression();
				return Optional.of(new AssignmentNode (leftNode.get(), rightNode.get()));
			}
			//+=
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.PLUS_EQUALS);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.PLUS_EQUALS) {
					op =  OperationNode.Operation.ADD;
					rightNode= ParseExpression();
					return Optional.of(new AssignmentNode (leftNode.get(), new OperationNode(leftNode.get(), op, rightNode)));
				}
			//-=
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.MINUS_EQUALS);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.MINUS_EQUALS){
					op =  OperationNode.Operation.SUBTRACT;
					rightNode= ParseExpression();
					return Optional.of(new AssignmentNode (leftNode.get(), new OperationNode(leftNode.get(), op, rightNode)));
				}
			//%=
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.MODULE_EQUALS);
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.MODULE_EQUALS){
					op =  OperationNode.Operation.MODULO;
					rightNode= ParseExpression();
					return Optional.of(new AssignmentNode (leftNode.get(), new OperationNode(leftNode.get(), op, rightNode)));
				}
			// /=
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.DIVIDE_EQUALS);
			
			if (operation.isPresent())
				if(operation.get().getTokenType() == Token.TokenType.DIVIDE_EQUALS){
					op =  OperationNode.Operation.DIVIDE;
					rightNode= ParseExpression();
					return Optional.of(new AssignmentNode (leftNode.get(), new OperationNode(leftNode.get(), op, rightNode)));
				}
			//*=
			if (operation.isEmpty())
				operation = handler.MatchAndRemove(Token.TokenType.MULTIPLY_EQUALS);
			
			if (operation.isPresent()) {
				if(operation.get().getTokenType() == Token.TokenType.MULTIPLY_EQUALS){
					op =  OperationNode.Operation.MULTIPLY;
					rightNode= ParseExpression();
					return Optional.of(new AssignmentNode (leftNode.get(), new OperationNode(leftNode.get(), op, rightNode)));
				}
			}
			throw new IllegalArgumentException("Assignment Error");
	
	}
	
	/**
	 * Parses exponent
	 * @return OperationNode
	 */
	private Optional<Node> ParseExponent() {
		Optional<Node> leftNode = ParseExpression();
		OperationNode.Operation op = OperationNode.Operation.EXPONENT;
		Optional<Node> rightNode ;
		do {
			handler.MatchAndRemove(Token.TokenType.EXPONENT);
			rightNode= ParseExpression();
			if (rightNode.isEmpty())
				return leftNode;
			leftNode = Optional.of(new OperationNode (leftNode.get(), op, rightNode));
		} while (true);
	}
	
	/**
	 * Parses Post Increment and Post DeIncrement
	 * @return 
	 */
	private Optional<Node> ParsePostIncDec() {
		Optional<Node> previousNode;
		
			previousNode= ParseBottomLevel();
			//ParseBottomLevel() INC -> Operation(result of ParseBottomLevel, POSTINC)
			if (handler.Peek(0).get().getTokenType()==(Token.TokenType.ADD_1)) {
				handler.MatchAndRemove(Token.TokenType.ADD_1);
				return Optional.of(new OperationNode(previousNode.get(), OperationNode.Operation.POSTINC));
			}
			//ParseBottomLevel() DEC -> Operation(result of ParseBottomLevel, POSTDEC)
			else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.SUBTRACT_1)) {
				handler.MatchAndRemove(Token.TokenType.SUBTRACT_1);
				return Optional.of(new OperationNode(previousNode.get(), OperationNode.Operation.POSTDEC));
			}
			throw new IllegalArgumentException("ParsePostIncDec(): Error in Incrementing");
	}
	
	/**
	 * parses (condition ? ifTrue : ifFalse)
	 * @return TernaryNode
	 */
	private Optional<Node> ParseTernary() {
		Optional<Node> condition, ifTrue, ifFalse;
		condition = ParseBottomLevel();
		if (handler.Peek(0).get().getTokenType()==(Token.TokenType.QUESTIONMARK)) 
			handler.MatchAndRemove(Token.TokenType.QUESTIONMARK);
		else
			throw new IllegalArgumentException ("Missing ? in Teranary Expression");
		
		ifTrue = ParseOperation();
		
		if (handler.Peek(0).get().getTokenType()==(Token.TokenType.COLON)) 
			handler.MatchAndRemove(Token.TokenType.COLON);
		else
			throw new IllegalArgumentException ("Missing : in Teranary Expression");
		
		ifFalse = ParseOperation();
		return Optional.of(new TernaryNode(condition.get(), ifTrue.get(), ifFalse.get()));
		
	}
	/**
	 * Handles:
	 * STRINGLITERAL -> ConstantNode(value)
	 * NUMBER -> ConstantNode (value)
	 * PATTERN -> PatternNode(value)
	 * LPAREN ParseOperation() RPAREN -> result of ParseOperation
	 * NOT ParseOperation() -> Operation(result of ParseOperation, NOT)
	 * MINUS ParseOperation() -> Operation(result of ParseOperation, UNARYNEG)
	 * PLUS ParseOperation() -> Operation(result of ParseOperation, UNARYPOS)
	 * INCREMENT ParseOperation() -> Operation(result of ParseOperation, PREINC)
	 * DECREMENT ParseOperation() -> Operation(result of ParseOperation, PREDEC)
	 * Else return ParseLValue()
	 * 
	 * @return Optional<Node>
	 */
	

	public Optional<Node> ParseBottomLevel() {
		
		Optional<Token> currentToken;
		Optional<Node> currentNode;
		if(handler.MoreTokens()) {
		//STRINGLITERAL -> ConstantNode(value)
		if (handler.Peek(0).get().getTokenType()==(Token.TokenType.STRINGLITERAL)) {
			currentToken= handler.Peek(0);
			handler.MatchAndRemove(Token.TokenType.STRINGLITERAL);
			return Optional.of(new ConstantNode(currentToken.get().getTokenValue()));
		}
		//NUMBER -> ConstantNode (value)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.NUMBER)) {
			currentToken= handler.Peek(0);
			handler.MatchAndRemove(Token.TokenType.NUMBER);
			return Optional.of(new ConstantNode(currentToken.get().getTokenValue()));
		}
		//PATTERN -> PatternNode(value)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.PATTERN)) {
			currentToken= handler.Peek(0);
			handler.MatchAndRemove(Token.TokenType.PATTERN);
			return Optional.of(new PatternNode(currentToken.get().getTokenValue()));
		}
		//LPAREN ParseOperation() RPAREN -> result of ParseOperation
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.OPENPAREN)) {
			handler.MatchAndRemove(Token.TokenType.OPENPAREN);
			currentNode = ParseOperation();
			if (handler.MatchAndRemove(Token.TokenType.CLOSEPAREN).isEmpty())
				throw new IllegalArgumentException("Error: ')' not present");
			return currentNode;
		}
		//NOT ParseOperation() -> Operation(result of ParseOperation, NOT)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.NOT)) {
			handler.MatchAndRemove(Token.TokenType.NOT);
			currentNode = ParseOperation();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.NOT));
			
		}
		
		//MINUS ParseOperation() -> Operation(result of ParseOperation, UNARYNEG)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.MINUS)) {
			handler.MatchAndRemove(Token.TokenType.MINUS);
			currentNode = ParseOperation();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.UNARYNEG));
			
		}
		//PLUS ParseOperation() -> Operation(result of ParseOperation, UNARYPOS)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.PLUS)) {
			handler.MatchAndRemove(Token.TokenType.PLUS);
			currentNode = ParseOperation();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.UNARYPOS));
			
		}
		//INCREMENT ParseOperation() -> Operation(result of ParseOperation, PREINC)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.ADD_1)) {
			handler.MatchAndRemove(Token.TokenType.ADD_1);
			currentNode = ParseOperation();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.PREINC));
		}
		//DECREMENT ParseOperation() -> Operation(result of ParseOperation, PREDEC)
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.SUBTRACT_1)) {
			handler.MatchAndRemove(Token.TokenType.SUBTRACT_1);
			currentNode = ParseOperation();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.PREDEC));
		}
		}
		
		
		return ParseLValue();
		

	}
	
	/**
	 * Handles:
	 * DOLLAR + ParseBottomLevel()-> OperationNode(value, DOLLAR)
	 * WORD + OPENARRAY + ParseOperation() + CLOSEARRAY -> VariableReferenceNode(name, index)
	 * WORD (and no OPENARRAY) -> VariableReferenceNode(name)
	 * @return Optional<Node>
	 */
	public Optional<Node> ParseLValue() {
		
		Optional<Token> currentToken;
		Optional<Node> currentNode;
		
		if(handler.MoreTokens()) {
		//DOLLAR + ParseBottomLevel()-> OperationNode(value, DOLLAR)
		if (handler.MatchAndRemove(Token.TokenType.DOLLAR).isPresent()) {
			currentNode= ParseBottomLevel();
			return Optional.of(new OperationNode(currentNode.get(), OperationNode.Operation.DOLLAR));
		}
		else if (handler.Peek(0).get().getTokenType()==(Token.TokenType.WORD)) {
			currentToken= handler.MatchAndRemove(Token.TokenType.WORD);
			//WORD + OPENARRAY + ParseOperation() + CLOSEARRAY -> VariableReferenceNode(name, index)
			if(handler.MoreTokens() && handler.Peek(0).get().getTokenType()==(Token.TokenType.OPENBRACKET)) {
				handler.MatchAndRemove(Token.TokenType.OPENBRACKET);
				currentNode = ParseOperation();
				if (handler.MatchAndRemove(Token.TokenType.CLOSEBRACKET).isEmpty())
					throw new IllegalArgumentException("Error: ']' not present");
				return Optional.of(new VariableReferenceNode(currentToken.get().getTokenValue(),currentNode)) ;
			}
			//WORD (and no OPENARRAY) -> VariableReferenceNode(name)
			else if (handler.MoreTokens()) {
				return Optional.of(new VariableReferenceNode(currentToken.get().getTokenValue())) ;
			}
		}
		}
		
		return Optional.empty();
		
		
	}
	

	
}
