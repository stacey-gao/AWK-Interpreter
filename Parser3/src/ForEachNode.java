import java.util.Optional;

public class ForEachNode extends StatementNode{

	private Optional <Node> array;
	private Optional <Node> iterator;
	private BlockNode block;
	
	public ForEachNode(Optional <Node> array, Optional <Node> iterator, BlockNode block){
		this.array = array;
		this.iterator = iterator; 
		this.block = block;
	}
	
	public String toString() {
		return "ForEach loop:"+ iterator + " in "+ array + block ;
	}

	public Optional<Node> getArray() {
		return array;
	}

	public Optional<Node> getIterator() {
		return iterator;
	}

	public BlockNode getBlock() {
		return block;
	}
}
