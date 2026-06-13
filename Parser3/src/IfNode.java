import java.util.Optional;

public class IfNode extends StatementNode {
	
	private Node condition;
	private BlockNode block;
	private Optional<StatementNode> next;
	
	public IfNode(Node condition, BlockNode block) {
		this.block = block;
		this.condition = condition;
		this.next = Optional.empty();
		}
	
	public IfNode(Node condition, BlockNode block, Optional<StatementNode> next) {
		this.block = block;
		this.condition = condition;
		this.next= next;
	}
	
	public String toString() {
		return "If Statement:" + condition + block + next;
	}

	public Node getCondition() {
		return condition;
	}

	public BlockNode getBlock() {
		return block;
	}

	public Optional<StatementNode> getNext() {
		return next;
	}
}
