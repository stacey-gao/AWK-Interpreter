import java.util.LinkedList;
import java.util.List;

/**
 * @author Stacey Gao ICSI311- Parser1, University at Albany, Fall 2023
 */
public class ProgramNode extends Node{
	
	private List <BlockNode> BEGIN ;
	private List <BlockNode> END;
	private List <BlockNode> OTHER;
	private List <FunctionDefinitionNode> functions;
	
	public ProgramNode() {
		functions = new LinkedList <FunctionDefinitionNode> ();
		BEGIN = new LinkedList <BlockNode> ();
		END = new LinkedList <BlockNode> ();
		OTHER = new LinkedList <BlockNode> ();
	}

	//mutators used in Parser.ParseAction()
	public void addBEGINblock (BlockNode node) {
		BEGIN.add(node);
	}
	
	public void addENDblock (BlockNode node) {
		END.add(node);
	}
	
	public void addOTHERblock (BlockNode node) {
		OTHER.add(node);
	}
	
	public void addFunctionDefinitionNode (FunctionDefinitionNode node) {
		functions.add(node);
	}
	
	
	public String toString() {
		
		return "BEGIN: " + BEGIN + "\nEND: " + END + "\nOTHER: " + OTHER + "\nFunctions: " + functions;
	}
}
