/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
public class AssignmentNode extends StatementNode {
	
	private Node target;
	private Node expression;
	
	public AssignmentNode (Node target, Node expression){
		this.target = target;
		this.expression = expression;
	}
	
	public String toString() {
		return "Target: " + target + ", Expression:" + expression + "\n";
	}

	
	public void setTarget(Node target) {
		this.target = target;
	}

	//for interpreter
	public Node getTarget() {
		return target;
	}

	public Node getExpression() {
		return expression;
	}
	
}
