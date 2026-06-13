/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */

public class ConstantNode extends Node{
	private String value;
	
	public ConstantNode(String value) {
		this.value= value;
	}
	
	public String toString() {
		return "Constant: " + value + "\n";
		
	}
	
	public String getValue() {
		return value;
		
	}
}
