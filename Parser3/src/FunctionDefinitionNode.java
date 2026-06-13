import java.util.LinkedList;

/**
 * @author Stacey Gao ICSI311- Parser1, University at Albany, Fall 2023
 */
public class FunctionDefinitionNode extends Node {
	
	protected String functionName;
	private LinkedList <Token> parameterName;
	private LinkedList <StatementNode> statements;
	

	
	/**
	 * @param functionName
	 * @param parameterName
	 * @param statements
	 */
	public FunctionDefinitionNode(String functionName, LinkedList <Token> parameterName, LinkedList <StatementNode> statements) {
		this.functionName = functionName;
		this.parameterName = parameterName;
		this.statements = statements;
	}
	
	public String toString() {
		String parameters ="";
		for (Token token: parameterName)
			parameters += token.getTokenValue() + ", ";
		return "Function Name: " + functionName + ", Parameters: " + parameters + " Statements: STATEMMENT PARSING NOT IMPLEMENTED YET-" + statements +"\n" ;
	}
}
