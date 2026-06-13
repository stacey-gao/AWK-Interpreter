import java.util.Optional;
/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */

public class OperationNode extends StatementNode {

	public enum Operation{
		EQUAL, NOT_EQUAL, LESSTHAN, LESSTHAN_EQUALTO,
		GREATHERTHAN, GREATHERTHAN_EQUALTO, AND, OR, NOT,
		MATCH, NOTMATCH, DOLLAR,
        PREINC,POSTINC,PREDEC, POSTDEC,UNARYPOS, UNARYNEG, IN,
        EXPONENT, ADD, SUBTRACT,MULTIPLY, DIVIDE,MODULO, CONCATENATION

	}
	
	private Node left; 
	private Optional <Node> right;
	private Operation operation;
	
	public OperationNode(Node left, Operation operation){
		this.left = left;
		this.operation = operation;
	}
	
	public OperationNode(Node left, Operation operation, Optional <Node> right){
		this.left = left;
		this.operation = operation;
		this.right= right;
	}
	
	public String toString() {
		return "Left: " +left + ", Operation: " + operation + ", Right:" + right + "\n";
	}

	public OperationNode.Operation getOperation() {
		return operation
				;
	}

	public Node getLeft() {
		return left;
	}

	public Optional<Node> getRight() {
		return right;
	}
	
	
}
