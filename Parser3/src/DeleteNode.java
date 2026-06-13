import java.util.Optional;

public class DeleteNode extends StatementNode {
	private Node array;
	private Optional<Node> indices;
	
	public DeleteNode(Node array, Optional<Node> indices){
		this.array= array;
		this.indices = indices;
	}
	
	public String toString() {
		return "Delete Node" + array + "[" + indices + "]";
		
	}

	public Node getArray() {
		return array;
	}

	public Optional<Node> getIndices() {
		return indices;
	}
}
