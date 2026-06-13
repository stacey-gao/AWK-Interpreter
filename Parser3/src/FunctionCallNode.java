import java.util.LinkedList;

public class FunctionCallNode extends StatementNode{
	private Node name;
	private LinkedList<Node> parameters;
	
	public FunctionCallNode(Node name, LinkedList<Node> parameters ) {
		this.name = name;
		this.parameters = parameters;
	}
	
	public String toString() {
		return name +"(" + parameters + ")";
	}
	
	
}
