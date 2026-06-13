/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
// condition ? ifTrue : ifFalse
public class TernaryNode extends Node {
	boolean status;
	private Node condition, ifTrue, ifFalse;
	
	public TernaryNode(Node condition, Node ifTrue, Node ifFalse) {
		this.condition = condition;
		this.ifTrue = ifTrue;
		this.ifFalse = ifFalse;
		
	}
	
	public String toString() {
		return "Status: " + status + ", Condition: " + condition + ", if True:"
				+ ifTrue + ", if False:" + ifFalse + "\n";
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Node getCondition() {
		return condition;
	}

	public void setCondition(Node condition) {
		this.condition = condition;
	}

	public Node getIfTrue() {
		return ifTrue;
	}

	public void setIfTrue(Node ifTrue) {
		this.ifTrue = ifTrue;
	}

	public Node getIfFalse() {
		return ifFalse;
	}

	public void setIfFalse(Node ifFalse) {
		this.ifFalse = ifFalse;
	}
}
