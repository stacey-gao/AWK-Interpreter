import java.util.Optional;

/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
public class VariableReferenceNode extends Node{
	private String name;
	private Optional<Node> indexExpression;
	
	public VariableReferenceNode(String name){
		this.name =name;
		this.indexExpression = Optional.empty();
		}
	
	public VariableReferenceNode(String name, Optional<Node> indexExpression){
		this.name =name;
		this.indexExpression = indexExpression;
	}
	
	public String toString() {
		if (indexExpression.isPresent())
			return "VariableReferenceNode: " + name + ", index: " + indexExpression + "\n";
		else
			return "VariableReferenceNode: " + name + "\n";
	}

	public String getName() {
		return name;
	}


	public Optional<Node> getIndexExpression() {
		return indexExpression;
	}

	
	
	
	
}
