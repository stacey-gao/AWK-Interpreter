import java.util.Optional;

public class ForNode extends StatementNode {

	private Optional <Node> initializer;
	private Optional <Node> condition;
	private Optional <Node> iterator;
	private BlockNode block;
	
	public ForNode(Optional <Node> initializer, Optional <Node> condition, Optional <Node> iterator, BlockNode block){
		this.initializer = initializer;
		this.condition = condition;
		this.iterator = iterator; 
		this.block = block;
	}
	
	public String toString() {
		return "For loop:"+ initializer + ";"+ condition + ";" + iterator + block ;
	}

	public Optional<Node> getInitializer() {
		return initializer;
	}

	public Optional<Node> getCondition() {
		return condition;
	}

	public Optional<Node> getIterator() {
		return iterator;
	}

	public BlockNode getBlock() {
		return block;
	}
}
