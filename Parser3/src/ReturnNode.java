
public class ReturnNode extends StatementNode {
	private Node value;
	
	public ReturnNode(Node value){
		this.value = value;
	}
	
	public String toString() {
		return "Return value:" +  value ;
	}

	public Node getValue() {
		return value;
	}
	
}
