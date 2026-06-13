
public class WhileNode extends StatementNode{
	Node condition;
	BlockNode block;
	
	public WhileNode(Node condition, BlockNode block){
		this.condition = condition;
		this.block = block;
	}
	
	public String toString() {
		return "While:" + condition + " " + block ;
	}

	public Node getCondition() {
		return condition;
	}

	public BlockNode getBlock() {
		return block;
	}

	
	
}
