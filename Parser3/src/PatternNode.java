/**
 * @author Stacey Gao ICSI311- Parser, University at Albany, Fall 2023
 */
public class PatternNode extends Node{
	private String value;
	
	public PatternNode(String value) {
		this.value= value;
	}
	
	public String toString() {
		return "Pattern: " + value + "\n";
		
	}
}
