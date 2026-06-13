import java.util.LinkedList;
import java.util.Optional;

/**
 * @author Stacey Gao ICSI311- Parser1, University at Albany, Fall 2023
 */
public class BlockNode extends Node {
	private LinkedList <StatementNode> statements;
	private Optional<Node> condition; //tells when to run this block
	
	
	public BlockNode(LinkedList <StatementNode> statements, Optional<Node> condition) {
		this.statements = statements;
		this.condition = condition;
	}
	
	public String toString() {
		String string = "No statements in this block";
		for (StatementNode stateNode : statements) 
            string = stateNode + " ";
		if(condition.isPresent())
			string += "Condition: " + condition;
		return string;
	}
	
	//used in function def node
	public LinkedList<StatementNode> getStatements() {
		return statements;
	}

	
}
